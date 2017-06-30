package me.martinrichards.wikipedia;

import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class WikipediaDataParser {
    private final String file;

    public WikipediaDataParser(final String file) {
        this.file = file;
    }

    public void parse() throws IOException {
        try(final InputStream stream = this.getClass().getResourceAsStream(file)) {
            try(final ZipInputStream zip = new ZipInputStream(stream)) {
                final ZipEntry entry = zip.getNextEntry();
            }
        }
    }
}
