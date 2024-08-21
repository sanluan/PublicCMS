/*
 * Copyright 2010 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.typography.font.sfntly;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PushbackInputStream;
import java.util.List;

import com.google.typography.font.sfntly.Font.Builder;
import com.google.typography.font.sfntly.data.FontData;
import com.google.typography.font.sfntly.data.ReadableFontData;
import com.google.typography.font.sfntly.data.WritableFontData;

/**
 * The font factory. This is the root class for the creation and loading of
 * fonts.
 *
 * @author Stuart Gill
 */
public final class FontFactory {
    private static final int LOOKAHEAD_SIZE = 4;

    /**
     * Offsets to specific elements in the underlying data. These offsets are
     * relative to the start of the table or the start of sub-blocks within the
     * table.
     */
    private enum Offset {
        // Offsets within the main directory
        TTCTag(0), Version(4), numFonts(8), OffsetTable(12),

        // TTC Version 2.0 extensions
        // offsets from end of OffsetTable
        ulDsigTag(0), ulDsigLength(4), ulDsigOffset(8);

        private final int offset;

        private Offset(int offset) {
            this.offset = offset;
        }
    }

    /**
     * Constructor.
     */
    private FontFactory() {
        // Prevent construction.
    }

    // input stream font loading

    /**
     * Load the font(s) from the input stream. The current settings on the
     * factory are used during the loading process. One or more fonts are
     * returned if the stream contains valid font data. Some font container
     * formats may have more than one font and in this case multiple font
     * objects will be returned. If the data in the stream cannot be parsed or
     * is invalid an array of size zero will be returned.
     *
     * @param is
     *            the input stream font data
     * @return one or more fonts
     * @throws IOException
     */
    public static Font[] loadFonts(InputStream is) throws IOException {
        PushbackInputStream pbis = new PushbackInputStream(new BufferedInputStream(is), FontFactory.LOOKAHEAD_SIZE);
        if (isCollection(pbis)) {
            return loadCollection(pbis);
        }
        return new Font[] { loadSingleOTF(pbis) };
    }

    /**
     * Load the font(s) from the input stream into font builders. The current
     * settings on the factory are used during the loading process. One or more
     * font builders are returned if the stream contains valid font data. Some
     * font container formats may have more than one font and in this case
     * multiple font builder objects will be returned. If the data in the stream
     * cannot be parsed or is invalid an array of size zero will be returned.
     *
     * @param is
     *            the input stream font data
     * @return one or more font builders
     * @throws IOException
     */
    public static Builder[] loadFontsForBuilding(InputStream is) throws IOException {
        PushbackInputStream pbis = new PushbackInputStream(new BufferedInputStream(is), FontFactory.LOOKAHEAD_SIZE);
        if (isCollection(pbis)) {
            return loadCollectionForBuilding(pbis);
        }
        return new Builder[] { loadSingleOTFForBuilding(pbis) };
    }

    private static Font loadSingleOTF(InputStream is) throws IOException {
        return loadSingleOTFForBuilding(is).build();
    }

    private static Font[] loadCollection(InputStream is) throws IOException {
        Font.Builder[] builders = loadCollectionForBuilding(is);
        Font[] fonts = new Font[builders.length];
        for (int i = 0; i < fonts.length; i++) {
            fonts[i] = builders[i].build();
        }
        return fonts;
    }

    private static Font.Builder loadSingleOTFForBuilding(InputStream is) throws IOException {
        return Builder.getOTFBuilder(is);
    }

    private static Font.Builder[] loadCollectionForBuilding(InputStream is) throws IOException {
        WritableFontData wfd = WritableFontData.createWritableFontData(is.available());
        wfd.copyFrom(is);
        // TOOD(stuartg): add collection loading from a stream
        return loadCollectionForBuilding(wfd);
    }

    private static boolean isCollection(PushbackInputStream pbis) throws IOException {
        byte[] tag = new byte[4];
        pbis.read(tag);
        pbis.unread(tag);
        return Tag.ttcf == Tag.intValue(tag);
    }

    // ByteArray font loading
    /**
     * Load the font(s) from the byte array. The current settings on the factory
     * are used during the loading process. One or more fonts are returned if
     * the stream contains valid font data. Some font container formats may have
     * more than one font and in this case multiple font objects will be
     * returned. If the data in the stream cannot be parsed or is invalid an
     * array of size zero will be returned.
     *
     * @param b
     *            the font data
     * @return one or more fonts
     * @throws IOException
     */
    public static Font[] loadFonts(byte[] b) throws IOException {
        // TODO(stuartg): make a ReadableFontData when block loading moved to
        // FontFactory
        WritableFontData rfd = WritableFontData.createWritableFontData(b);
        if (isCollection(rfd)) {
            return loadCollection(rfd);
        }
        return new Font[] { loadSingleOTF(rfd) };
    }

    private static Font loadSingleOTF(WritableFontData wfd) throws IOException {
        return loadSingleOTFForBuilding(wfd, 0).build();
    }

    private static Font[] loadCollection(WritableFontData wfd) throws IOException {
        Font.Builder[] builders = loadCollectionForBuilding(wfd);
        Font[] fonts = new Font[builders.length];
        for (int i = 0; i < fonts.length; i++) {
            fonts[i] = builders[i].build();
        }
        return fonts;
    }

    private static Font.Builder loadSingleOTFForBuilding(WritableFontData wfd, int offsetToOffsetTable) throws IOException {
        return Font.Builder.getOTFBuilder(wfd, offsetToOffsetTable);
    }

    private static Font.Builder[] loadCollectionForBuilding(WritableFontData wfd) throws IOException {
        int numFonts = wfd.readULongAsInt(Offset.numFonts.offset);
        Font.Builder[] builders = new Font.Builder[numFonts];
        int offsetTableOffset = Offset.OffsetTable.offset;
        for (int fontNumber = 0; fontNumber < numFonts; fontNumber++, offsetTableOffset += FontData.DataSize.ULONG.size()) {
            int offset = wfd.readULongAsInt(offsetTableOffset);
            builders[fontNumber] = loadSingleOTFForBuilding(wfd, offset);
        }
        return builders;
    }

    private static boolean isCollection(ReadableFontData rfd) {
        byte[] tag = new byte[4];
        rfd.readBytes(0, tag, 0, tag.length);
        return Tag.ttcf == Tag.intValue(tag);
    }

    // font serialization
    /**
     * Serialize the font to the output stream.
     *
     * @param font
     *            the font to serialize
     * @param os
     *            the destination stream for the font
     * @throws IOException
     */
    public static void serializeFont(Font font, OutputStream os) throws IOException {
        // TODO(stuartg) should have serialization options somewhere
        font.serialize(os, null);
    }

    /**
     * Serialize the font to the output stream.
     *
     * @param font
     *            the font to serialize
     * @param os
     *            the destination stream for the font
     * @param tableOrdering
     * @throws IOException
     */
    public static void serializeFont(Font font, OutputStream os, List<Integer> tableOrdering) throws IOException {
        // TODO(stuartg) should have serialization options somewhere
        font.serialize(os, tableOrdering);
    }

    // new fonts

    /**
     * Get an empty font builder for creating a new font from scratch.
     *
     * @return an empty font builder
     */
    public static Builder newFontBuilder() {
        return Font.Builder.getOTFBuilder();
    }
}
