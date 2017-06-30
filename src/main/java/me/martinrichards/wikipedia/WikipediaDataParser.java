package me.martinrichards.wikipedia;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.zip.ZipInputStream;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

public class WikipediaDataParser {
    private static final String ELEMENT_PAGE = "page";
    private static final String ELEMENT_NAME = "name";
    private static final String ELEMENT_ID = "id";

    private final String file;
    private final XMLInputFactory factory = XMLInputFactory.newInstance();
    private final List<WikipediaPage> pages;

    public WikipediaDataParser(final String file, final List<WikipediaPage> pages) {
        this.file = file;
        this.pages = pages;
    }

    public void parse() throws IOException, XMLStreamException {
        try(final InputStream stream = this.getClass().getResourceAsStream(file)) {
            try(final ZipInputStream zip = new ZipInputStream(stream)) {
                final XMLEventReader reader = factory.createXMLEventReader(zip);
                while (reader.hasNext()) {
                    final XMLEvent event = reader.nextEvent();
                    if (event.isStartElement() && event.asStartElement().getName()
                            .getLocalPart().equals(ELEMENT_PAGE)) {
                        parsePage(reader);
                    }
                }
            }
        }
    }

    private void parsePage(final XMLEventReader reader) throws XMLStreamException {
        String name = null;
        String id = null;
        while (reader.hasNext()) {
            final XMLEvent event = reader.nextEvent();
            if (event.isEndElement() && event.asEndElement().getName().getLocalPart().equals(ELEMENT_PAGE)) {
                return;
            }
            if (event.isStartElement()) {
                final StartElement element = event.asStartElement();
                final String elementName = element.getName().getLocalPart();
                switch (elementName) {
                    case ELEMENT_NAME:
                        name = reader.getElementText();
                        break;
                    case ELEMENT_ID:
                        id = reader.getElementText();
                        break;
                }
            }
        }
        final WikipediaPage page = new WikipediaPage(id, name);
        pages.add(page);
    }
}
