package com.hlops.tv42.core.services.impl;

import com.hlops.tv42.core.bean.Identifiable;
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
public class XmltvServiceImpl implements XmltvService {

    private static final long MAX_AGE = 1000 * 60 * 60 * 24 * 3;

    private static Logger log = LogManager.getLogger(XmltvServiceImpl.class);

    @Autowired
    private DbService dbService;

    @Autowired
    private SourceService sourceService;

    @Override
    public List<TvShowItem> findItems(TvShowChannel channel, long start, long stop, @Nullable String preferableSource) {
        List<String> sources = new ArrayList<>();
        if (preferableSource != null) {
            sources.add(preferableSource);
        } else {
            sources.addAll(sourceService.getOrderedSources(Source.SourceType.xmltv).stream().map(Source::getId).collect(Collectors.toList()));
        }
        for (String source : sources) {
            if (channel.getSources().contains(source)) {
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
        return (TvShowChannel) dbService.get(DbService.Entity.tvShowChannels).get(id);
    }

    @Override
    public Collection<TvShowChannel> getChannels() {
        //noinspection unchecked
        return (Collection<TvShowChannel>) dbService.get(DbService.Entity.tvShowChannels).values();
    }

    @Override
    public void actualize(@NotNull Collection<TvShowChannel> channels) {
        dbService.update(DbService.Entity.tvShowChannels, channels);
        deleteObsoleteItems();
    }

    private void deleteObsoleteItems() {
        Set<TvShowChannel> modifiedChannels = new HashSet<>();
        long minimalTime = System.currentTimeMillis() - MAX_AGE;
        for (TvShowChannel channel : getChannels()) {
            for (String source : channel.getSources()) {
                if (channel.getItems(source).removeAll(
                        channel.getItems(source).stream().filter(i -> i.getStart() < minimalTime).collect(Collectors.toList()))) {
                    modifiedChannels.add(channel);
                }
            }
        }
        dbService.update(DbService.Entity.tvShowChannels, modifiedChannels);
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
                long minimalTime = System.currentTimeMillis() - MAX_AGE;
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

    @Override
    @Nullable
    public TvShowChannel matchByName(@NotNull String name) {
        for (TvShowChannel channel : getChannels()) {
            String channelName = channel.getName().toLowerCase().trim();

            if (name.equalsIgnoreCase(channelName)) {
                return channel;
            }

            if (name.equalsIgnoreCase(channelName.replaceAll("\\s+канал", ""))) {
                return channel;
            }

            if (name.equalsIgnoreCase(channelName.replaceAll("\\s+тв", ""))) {
                return channel;
            }

            if (name.replaceAll("[\\s-]", "").equalsIgnoreCase(channelName.replaceAll("[\\s-]", ""))) {
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
        public int read(byte[] b, int off, int len) throws IOException {
            return super.read(b, off, len);
        }
    }

}
