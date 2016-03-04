package com.hlops.tv42.core.utils;

import org.jetbrains.annotations.NotNull;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

/**
 * Created by IntelliJ IDEA.
 * User: akarnachuk
 * Date: 3/1/16
 * Time: 4:03 PM
 */
public class WellFormedXmlInputStream extends FilterInputStream {
    /**
     * Creates a <code>FilterInputStream</code>
     * by assigning the  argument <code>in</code>
     * to the field <code>this.in</code> so as
     * to remember it for later use.
     *
     * @param in the underlying input stream, or <code>null</code> if
     *           this instance is to be created without an underlying stream.
     */
    protected WellFormedXmlInputStream(InputStream in) {
        super(in);
    }

    private ByteBuffer wrapBuffer;

    @Override
    public int read(@NotNull byte[] buffer) throws IOException {
        if (wrapBuffer != null && wrapBuffer.hasRemaining()) {
            int i = 0;
            while (i < wrapBuffer.remaining()) {
                if (isNakedAmp(wrapBuffer, i)) {

                    if (i > 0) {
                        int n = Math.min(i, buffer.length);
                        wrapBuffer.get(buffer, 0, n);
                        return n;
                    }

                    if (wrapBuffer.remaining() - i < AMP.length) {
                        int read = super.read(buffer);
                        if (read >= 0) {
                            ByteBuffer newBuffer = ByteBuffer.allocate(wrapBuffer.remaining() + read);
                            newBuffer.put(wrapBuffer);
                            newBuffer.put(buffer, 0, read);
                            newBuffer.rewind();
                            wrapBuffer = newBuffer;
                            continue;
                        }
                    }

                    if (isNakedAmp(wrapBuffer, i)) {
                        System.arraycopy(AMP, 0, buffer, 0, AMP.length);
                        wrapBuffer.get();
                        return AMP.length;
                    }
                }
                i++;
            }
            int remaining = Math.min(wrapBuffer.remaining(), buffer.length);
            wrapBuffer.get(buffer, 0, remaining);
            return remaining;
        }
        int read = super.read(buffer);
        if (read >= 0) {
            for (int i = 0; i < read; i++) {
                if (isNakedAmp(buffer, i, read)) {
                    wrapBuffer = ByteBuffer.allocate(read - i).put(buffer, i, read - i);
                    wrapBuffer.rewind();
                    return i;
                }
            }
        }
        return read;
    }

    private boolean isNakedAmp(byte[] buffer, int position, int read) {
        if (buffer[position] != AMP[0]) {
            return false;
        }
        if (read - position < AMP.length) {
            return true;
        }
        for (int i = 1; i < AMP.length; i++) {
            if (buffer[position + i] != AMP[i]) {
                return true;
            }
        }
        return false;
    }

    private boolean isNakedAmp(ByteBuffer wrapBuffer, int position) {
        if (wrapBuffer.get(wrapBuffer.position() + position) != AMP[0]) {
            return false;
        }
        if (wrapBuffer.remaining() - position < AMP.length) {
            return true;
        }
        for (int i = 1; i < AMP.length; i++) {
            if (wrapBuffer.get(wrapBuffer.position() + position + i) != AMP[i]) {
                return true;
            }
        }
        return false;
    }

    public static byte[] AMP = "&amp;".getBytes();

}
