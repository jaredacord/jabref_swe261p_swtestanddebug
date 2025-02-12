package org.jabref.SWE261P_tests;

import java.util.List;
import java.util.Optional;

import org.jabref.logic.importer.FetcherException;
import org.jabref.logic.importer.plaincitation.RuleBasedPlainCitationParser;
import org.jabref.logic.importer.plaincitation.SeveralPlainCitationParser;
import org.jabref.model.entry.BibEntry;
import org.jabref.model.entry.field.StandardField;
import org.jabref.model.entry.types.StandardEntryType;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class assignment3_tests {
    private SeveralPlainCitationParser parser;

    @BeforeEach
    void setUp() {
        parser = new SeveralPlainCitationParser(new RuleBasedPlainCitationParser());
    }

    @Test
    void testSingleCitationArticle() throws FetcherException {
        String citation = "Li Y., Zhang D. Traditional varieties of cacao in Madagascar: their origin and dispersal revealed by SNP markers. 2021.";

        List<BibEntry> entries = parser.parseSeveralPlainCitations(citation);
        BibEntry entry = entries.get(0);

        assertEquals(StandardEntryType.Article, entry.getType());
        assertEquals(Optional.of("Traditional varieties of cacao in Madagascar: their origin and dispersal revealed by SNP markers"), entry.getFieldOrAlias(StandardField.TITLE));
        assertEquals(Optional.of("Li, Y.  and Zhang, D. "), entry.getFieldOrAlias(StandardField.AUTHOR));
        assertEquals(Optional.of("2021"), entry.getFieldOrAlias(StandardField.YEAR));
    }

    @Test
    void testSingleCitationBook() throws FetcherException {
        String citation = "Verne, J. Journey to the Center of the Earth. 1864. https://dn790009.ca.archive.org/0/items/cu31924090934815/cu31924090934815.pdf";

        List<BibEntry> entries = parser.parseSeveralPlainCitations(citation);
        BibEntry entry = entries.get(0);

        assertEquals(StandardEntryType.Book, entry.getType());
        assertEquals(Optional.of("Journey to the Center of the Earth"), entry.getFieldOrAlias(StandardField.TITLE));
        assertEquals(Optional.of("Verne, J. "), entry.getFieldOrAlias(StandardField.AUTHOR));
        assertEquals(Optional.of("1864"), entry.getFieldOrAlias(StandardField.YEAR));
        assertEquals(Optional.of("https://dn790009.ca.archive.org/0/items/cu31924090934815/cu31924090934815.pdf"), entry.getFieldOrAlias(StandardField.URL));
    }

    @Test
    void testSingleCitationArticleInsufficientInfo() throws FetcherException {
        String citation = "Li Y.";

        List<BibEntry> entries = parser.parseSeveralPlainCitations(citation);
        BibEntry entry = entries.get(0);

        assertEquals(StandardEntryType.Article, entry.getType());
        assertEquals(Optional.empty(), entry.getFieldOrAlias(StandardField.TITLE));
        assertEquals(Optional.empty(), entry.getFieldOrAlias(StandardField.AUTHOR));
    }

    @Test
    void testSingleCitationBookInvalidYear() throws FetcherException {
        String citation = "Verne, J. Some Really Old Book. 1600. https://dn790009.ca.archive.org/0/items/cu31924090934815/cu31924090934815.pdf";

        List<BibEntry> entries = parser.parseSeveralPlainCitations(citation);
        BibEntry entry = entries.get(0);

        assertEquals(StandardEntryType.Book, entry.getType());
        assertEquals(Optional.of("Some Really Old Book"), entry.getFieldOrAlias(StandardField.TITLE));
        assertEquals(Optional.of("Verne, J. "), entry.getFieldOrAlias(StandardField.AUTHOR));
        assertEquals(Optional.empty(), entry.getFieldOrAlias(StandardField.YEAR));
        assertEquals(Optional.of("https://dn790009.ca.archive.org/0/items/cu31924090934815/cu31924090934815.pdf"), entry.getFieldOrAlias(StandardField.URL));
    }
}
