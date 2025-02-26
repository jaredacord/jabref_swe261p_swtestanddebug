package org.jabref.SWE261P_tests;

import org.jabref.gui.journals.AbbreviationType;
import org.jabref.gui.journals.assignment5_UndoableAbbreviator;
import org.jabref.logic.journals.Abbreviation;
import org.jabref.logic.journals.JournalAbbreviationRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class assignment_5_testable_design_tests {
    private JournalAbbreviationRepository repository;
    private assignment5_UndoableAbbreviator undoableAbbreviator;

    @BeforeEach
    void setUp() {
        undoableAbbreviator = new assignment5_UndoableAbbreviator();
        repository = undoableAbbreviator.getRepository();
    }

    @ParameterizedTest
    @CsvSource({
            "American Journal of Public Health, DEFAULT, Am. J. Public Health",
            "American Journal of Public Health, DOTLESS, Am J Public Health",
            "American Journal of Public Health, SHORTEST_UNIQUE, Am. J. Public Health",
            "Anales de Medicina, DEFAULT, An. Med. (Lima)",
            "Anales de Medicina, DOTLESS, An Med (Lima)",
            "Anales de Medicina, SHORTEST_UNIQUE, An. Med. (Lima)",
            "Annali Sclavo, DEFAULT, Ann. Sclavo",
            "Annali Sclavo, DOTLESS, Ann Sclavo",
            "Annali Sclavo, SHORTEST_UNIQUE, Ann. Sclavo"
    })
    void testGetAbbreviatedName(String text, AbbreviationType abbType, String expectedAbbr) {
        Abbreviation abbreviation = repository.get(text).get();
        String abbr = undoableAbbreviator.getAbbreviatedName(abbreviation, abbType);

        assertEquals(abbr, expectedAbbr);
    }
}
