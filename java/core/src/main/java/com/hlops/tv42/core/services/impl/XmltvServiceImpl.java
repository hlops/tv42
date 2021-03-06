package com.hlops.tv42.core.services.impl;

import com.hlops.tv42.core.bean.Source;
import com.hlops.tv42.core.bean.TvShowChannel;
import com.hlops.tv42.core.bean.TvShowItem;
import com.hlops.tv42.core.services.DbService;
import com.hlops.tv42.core.services.SourceService;
import com.hlops.tv42.core.services.XmltvService;
import com.hlops.tv42.core.utils.TimeFormatter;
import com.sun.xml.internal.stream.events.EndElementEvent;
import com.sun.xml.internal.stream.events.StartElementEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.xml.namespace.QName;
import javax.xml.stream.*;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.XMLEvent;
import java.io.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.zip.GZIPInputStream;

/**
 * Created by tom on 1/31/16.
 */
@Service
public class XmltvServiceImpl extends GenericServiceImpl<TvShowChannel> implements XmltvService {

    @Value("${tv42-tv_item-max_age:259200000}")
    private long MAX_AGE;

    private static Logger log = LogManager.getLogger(XmltvServiceImpl.class);

    @Autowired
    private SourceService sourceService;

    @Override
    protected DbService.Entity getEntity() {
        return DbService.Entity.tvShowChannels;
    }

    @Override
    public List<TvShowItem> findItems(TvShowChannel channel, long start, long stop, @Nullable String preferableSource) {
        List<String> sources = new ArrayList<>();
        if (preferableSource != null) {
            sources.add(preferableSource);
        } else {
            sources.addAll(sourceService.getOrderedSources(Source.SourceType.xmltv).stream().map(Source::getId).collect(Collectors.toList()));
        }
        for (String source : sources) {
            if (channel.getSources().containsKey(source)) {
                List<TvShowItem> items = channel.getItems(source).stream().filter(tvShowItem ->
                        tvShowItem.getStart() <= stop && tvShowItem.getStop() >= start
                ).collect(Collectors.toList());
                if (!items.isEmpty()) {
                    return items;
                }
            }
        }
        return Collections.emptyList();
    }

    @Override
    public TvShowChannel getChannelById(String id) {
        return get(id);
    }

    @Override
    public Collection<TvShowChannel> getChannels() {
        return values();
    }

    @Override
    public void actualize(@NotNull Collection<TvShowChannel> channels) {
        super.actualize(channels);
        deleteObsoleteItems();
    }

    private void deleteObsoleteItems() {
        if (MAX_AGE == 0) {
            return;
        }
        Set<TvShowChannel> modifiedChannels = new HashSet<>();
        long minimalTime = System.currentTimeMillis() - MAX_AGE;
        for (TvShowChannel channel : getChannels()) {
            for (String source : channel.getSources().keySet()) {
                if (channel.getItems(source).removeAll(
                        channel.getItems(source).stream().filter(i -> i.getStart() < minimalTime).collect(Collectors.toList()))) {
                    modifiedChannels.add(channel);
                }
            }
        }
        super.actualize(modifiedChannels);
    }

    private boolean isGZipStream(PushbackInputStream pushbackInputStream) throws IOException {
        byte[] signature = new byte[4];
        int read = pushbackInputStream.read(signature, 0, 2);
        assert (read == 2);
        pushbackInputStream.unread(signature, 0, read);
        return GZIPInputStream.GZIP_MAGIC == ByteBuffer.wrap(signature).order(ByteOrder.LITTLE_ENDIAN).getInt();
    }

    @Override
    public Collection<TvShowChannel> load(@NotNull String source, @NotNull InputStream originalStream) throws IOException {
        PushbackInputStream pushbackInputStream = new PushbackInputStream(originalStream, 2);
        InputStream stream;
        if (isGZipStream(pushbackInputStream)) {
            stream = new FilteredStream(new GZIPInputStream(pushbackInputStream));
        } else {
            stream = new FilteredStream(pushbackInputStream);
        }
        Map<String, TvShowChannel> channels = new LinkedHashMap<>();
        XMLInputFactory inputFactory = XMLInputFactory.newInstance();
        inputFactory.setProperty("javax.xml.stream.isCoalescing", true);
        try {
            XMLStreamReader xmlReader = inputFactory.createXMLStreamReader(new InputStreamReader(stream, "UTF-8"));
            try {
                long minimalTime = MAX_AGE == 0 ? 0 : System.currentTimeMillis() - MAX_AGE;
                XMLEventReader eventReader = inputFactory.createXMLEventReader(xmlReader);

                QName qNameId = new QName("id");
                QName qNameStart = new QName("start");
                QName qNameStop = new QName("stop");
                QName qNameChannel = new QName("channel");
                QName qNameSrc = new QName("src");

                TvShowChannel channel = null;
                TvShowItem tvItem = null;
                String elName = null;

                while (eventReader.hasNext()) {
                    final XMLEvent event = eventReader.nextEvent();
                    if (event.getEventType() == XMLStreamConstants.CHARACTERS) {
                        if (channel != null) {
                            if ("display-name".equals(elName)) {
                                channel.setName(event.asCharacters().getData());
                            }
                        } else if (tvItem != null) {
                            if ("title".equals(elName)) {
                                tvItem.setTitle(event.asCharacters().getData());
                            } else if ("desc".equals(elName)) {
                                tvItem.setDescription(event.asCharacters().getData());
                            } else if ("category".equals(elName)) {
                                tvItem.setCategory(event.asCharacters().getData());
                            }
                        }
                    } else if (event.getEventType() == XMLStreamConstants.START_ELEMENT) {
                        StartElementEvent el = ((StartElementEvent) event);
                        elName = el.nameAsString();

                        if ("channel".equals(elName)) {
                            channel = new TvShowChannel(source, el.getAttributeByName(qNameId).getValue());
                            channels.put(channel.getChannelId(), channel);
                        } else if ("programme".equals(elName)) {
                            try {
                                tvItem = new TvShowItem(source,
                                        parseTime(el.getAttributeByName(qNameStart)),
                                        parseTime(el.getAttributeByName(qNameStop)));
                                if (tvItem.getStart() >= minimalTime) {
                                    channels.get(el.getAttributeByName(qNameChannel).getValue()).getItems(source).add(tvItem);
                                }
                            } catch (ParseException e) {
                                log.error(e.getMessage(), e);
                            }
                        } else {
                            if (channel != null) {
                                if ("icon".equals(elName)) {
                                    channel.setIcon(el.getAttributeByName(qNameSrc).getValue());
                                }
                            }
                        }
                    } else if (event.getEventType() == XMLStreamConstants.END_ELEMENT) {
                        EndElementEvent el = ((EndElementEvent) event);
                        elName = el.nameAsString();

                        if ("channel".equals(elName)) {
                            channel = null;
                        } else if ("programme".equals(elName)) {
                            tvItem = null;
                        }
                        elName = null;
                    }
                }
            } finally {
                xmlReader.close();
            }
        } catch (XMLStreamException e) {
            e.printStackTrace();
        }

        return channels.values();
    }

    private long parseTime(Attribute attr) throws ParseException {
        return TimeFormatter.parse(attr.getValue());
    }

    private String peelChannelName(String name) {
        return name.toLowerCase().replaceAll("(канал)|(тв)|(\\(.*\\))|([\\s-\\+])", "");
    }

    @Override
    @Nullable
    public TvShowChannel matchByName(@NotNull String name) {
        String peelName = peelChannelName(name);
        for (TvShowChannel channel : getChannels()) {
            if (peelName.equals(peelChannelName(channel.getName()))) {
                return channel;
            }
        }
        return null;
    }

    class FilteredStream extends FilterInputStream {

        protected FilteredStream(InputStream in) {
            super(in);
        }

        @Override
        public int read(@NotNull byte[] b, int off, int len) throws IOException {
            return super.read(b, off, len);
        }

    }

    public TvShowChannel combine(TvShowChannel value, TvShowChannel oldValue) throws CloneNotSupportedException {
        for (String source : oldValue.getSources().keySet()) {
            List<TvShowItem> items = value.getSources().computeIfAbsent(source, s -> new ArrayList<>());
            items.addAll(oldValue.getItems(source));
        }
        return value.clone();
    }

    public void save(Collection<TvShowChannel> channels, @NotNull String source, OutputStream outputStream) throws IOException {
        XMLOutputFactory factory = XMLOutputFactory.newInstance();
        try {
            XMLStreamWriter writer = factory.createXMLStreamWriter(outputStream);
            writer.writeStartDocument();
            writer.writeDTD("<!DOCTYPE tv SYSTEM \"http://programtv.ru/xmltv.dtd\">");
            writer.writeCharacters("\n");
            writer.writeStartElement("tv");
            writer.writeCharacters("\n");

            for (TvShowChannel channel : channels) {
                writer.writeStartElement("channel");
                writer.writeAttribute("id", channel.getChannelId());

                writer.writeStartElement("display-name");
                writer.writeCharacters(channel.getName());
                writer.writeEndElement();

                writer.writeEndElement();
                writer.writeCharacters("\n");
            }

            for (TvShowChannel channel : channels) {
                for (TvShowItem item : channel.getItems(source)) {
                    writer.writeStartElement("programme");
                    writer.writeAttribute("start", TimeFormatter.format(item.getStart()));
                    writer.writeAttribute("stop", TimeFormatter.format(item.getStop()));
                    writer.writeAttribute("channel", channel.getChannelId());

                    writer.writeStartElement("title");
                    writer.writeCharacters(item.getTitle());
                    writer.writeEndElement();

                    if (item.getDescription() != null) {
                        writer.writeStartElement("desc");
                        writer.writeCharacters(item.getDescription());
                        writer.writeEndElement();
                    }

                    if (item.getCategory() != null) {
                        writer.writeStartElement("category");
                        writer.writeCharacters(item.getCategory());
                        writer.writeEndElement();
                    }

                    writer.writeEndElement();
                    writer.writeCharacters("\n");
                }
            }

            writer.writeEndElement();
        } catch (XMLStreamException e) {
            e.printStackTrace();
        }

        outputStream.close();
    }
}
