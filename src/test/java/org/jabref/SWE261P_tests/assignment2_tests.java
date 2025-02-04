package org.jabref.SWE261P_tests;

import java.util.Arrays;
import java.util.List;

import org.jabref.model.database.BibDatabase;
import org.jabref.model.entry.BibEntry;
import org.jabref.model.entry.types.StandardEntryType;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class assignment2_tests {

    private BibDatabase database;

    @BeforeEach
    void setUp() {
        database = new BibDatabase();
    }

    @Test
    void checkFsmS1toS1RemoveEntriesNotInLib() {
        BibEntry entry1 = new BibEntry(StandardEntryType.Book);
        BibEntry entry2 = new BibEntry(StandardEntryType.Book);
        BibEntry entry3 = new BibEntry(StandardEntryType.Book);

        List<BibEntry> entriesToRemove = Arrays.asList(entry1, entry2, entry3);

        isStateS1();
        database.removeEntries(entriesToRemove);
        isStateS1();
    }

    @Test
    void checkFsmS1toS1RemoveEntryNotInLib() {
        BibEntry entry1 = new BibEntry(StandardEntryType.Book);

        isStateS1();
        database.removeEntry(entry1);
        isStateS1();
    }

    @Test
    void checkFsmS1toS2AddEntry() {
        BibEntry entry1 = new BibEntry(StandardEntryType.Book);

        isStateS1();
        database.insertEntry(entry1);
        isStateS2();
    }

    @Test
    void checkFsmS1toS3AddEntries() {
        BibEntry entry1 = new BibEntry(StandardEntryType.Book);
        BibEntry entry2 = new BibEntry(StandardEntryType.Book);
        BibEntry entry3 = new BibEntry(StandardEntryType.Book);

        List<BibEntry> entriesToAdd = Arrays.asList(entry1, entry2, entry3);

        isStateS1();
        database.insertEntries(entriesToAdd);
        isStateS3(3);
    }

    @Test
    void checkFsmS2toS1RemoveEntry() {
        BibEntry entry1 = new BibEntry(StandardEntryType.Book);
        database.insertEntry(entry1);

        isStateS2();
        database.removeEntry(entry1);
        isStateS1();
    }

    @Test
    void checkFsmS2toS1RemoveEntries() {
        BibEntry entry1 = new BibEntry(StandardEntryType.Book);
        database.insertEntry(entry1);

        List<BibEntry> entriesToRemove = Arrays.asList(entry1);

        isStateS2();
        database.removeEntries(entriesToRemove);
        isStateS1();
    }

    @Test
    void checkFsmS2toS2RemoveEntryNotInLib() {
        BibEntry entry1 = new BibEntry(StandardEntryType.Book);
        BibEntry entry2 = new BibEntry(StandardEntryType.Book);
        database.insertEntry(entry1);

        isStateS2();
        database.removeEntry(entry2);
        isStateS2();
    }

    @Test
    void checkFsmS2toS2RemoveEntriesNotInLib() {
        BibEntry entry1 = new BibEntry(StandardEntryType.Book);
        BibEntry entry2 = new BibEntry(StandardEntryType.Book);
        BibEntry entry3 = new BibEntry(StandardEntryType.Book);
        database.insertEntry(entry1);

        List<BibEntry> entriesToRemove = Arrays.asList(entry2, entry3);

        isStateS2();
        database.removeEntries(entriesToRemove);
        isStateS2();
    }

    @Test
    void checkFsmS2toS3AddEntry() {
        BibEntry entry1 = new BibEntry(StandardEntryType.Book);
        BibEntry entry2 = new BibEntry(StandardEntryType.Book);
        database.insertEntry(entry1);

        isStateS2();
        database.insertEntry(entry2);
        isStateS3(2);
    }

    @Test
    void checkFsmS2toS3AddEntries() {
        BibEntry entry1 = new BibEntry(StandardEntryType.Book);
        BibEntry entry2 = new BibEntry(StandardEntryType.Book);
        BibEntry entry3 = new BibEntry(StandardEntryType.Book);
        BibEntry entry4 = new BibEntry(StandardEntryType.Book);
        database.insertEntries(entry1);

        List<BibEntry> entriesToAdd = Arrays.asList(entry2, entry3, entry4);

        isStateS2();
        database.insertEntries(entriesToAdd);
        isStateS3(4);
    }

    @Test
    void checkFsmS3toS1RemoveEntries() {
        BibEntry entry1 = new BibEntry(StandardEntryType.Book);
        BibEntry entry2 = new BibEntry(StandardEntryType.Book);
        BibEntry entry3 = new BibEntry(StandardEntryType.Book);
        BibEntry entry4 = new BibEntry(StandardEntryType.Book);
        List<BibEntry> entriesToAdd = Arrays.asList(entry1, entry2, entry3, entry4);
        database.insertEntries(entriesToAdd);

        List<BibEntry> entriesToRemove = Arrays.asList(entry1, entry2, entry3, entry4);

        isStateS3(4);
        database.removeEntries(entriesToRemove);
        isStateS1();
    }

    @Test
    void checkFsmS3toS2RemoveEntry() {
        BibEntry entry1 = new BibEntry(StandardEntryType.Book);
        BibEntry entry2 = new BibEntry(StandardEntryType.Book);
        List<BibEntry> entriesToAdd = Arrays.asList(entry1, entry2);
        database.insertEntries(entriesToAdd);

        isStateS3(2);
        database.removeEntry(entry2);
        isStateS2();
    }

    @Test
    void checkFsmS3toS2RemoveEntries() {
        BibEntry entry1 = new BibEntry(StandardEntryType.Book);
        BibEntry entry2 = new BibEntry(StandardEntryType.Book);
        BibEntry entry3 = new BibEntry(StandardEntryType.Book);
        BibEntry entry4 = new BibEntry(StandardEntryType.Book);
        List<BibEntry> entriesToAdd = Arrays.asList(entry1, entry2, entry3, entry4);
        database.insertEntries(entriesToAdd);

        List<BibEntry> entriesToRemove = Arrays.asList(entry1, entry3, entry4);

        isStateS3(4);
        database.removeEntries(entriesToRemove);
        isStateS2();
    }

    @Test
    void checkFsmS3toS3RemoveEntry() {
        BibEntry entry1 = new BibEntry(StandardEntryType.Book);
        BibEntry entry2 = new BibEntry(StandardEntryType.Book);
        BibEntry entry3 = new BibEntry(StandardEntryType.Book);
        BibEntry entry4 = new BibEntry(StandardEntryType.Book);
        List<BibEntry> entriesToAdd = Arrays.asList(entry1, entry2, entry3, entry4);
        database.insertEntries(entriesToAdd);

        isStateS3(4);
        database.removeEntry(entry1);
        isStateS3(3);
    }

    @Test
    void checkFsmS3toS3RemoveEntries() {
        BibEntry entry1 = new BibEntry(StandardEntryType.Book);
        BibEntry entry2 = new BibEntry(StandardEntryType.Book);
        BibEntry entry3 = new BibEntry(StandardEntryType.Book);
        BibEntry entry4 = new BibEntry(StandardEntryType.Book);
        List<BibEntry> entriesToAdd = Arrays.asList(entry1, entry2, entry3, entry4);
        database.insertEntries(entriesToAdd);

        List<BibEntry> entriesToRemove = Arrays.asList(entry1, entry4);

        isStateS3(4);
        database.removeEntries(entriesToRemove);
        isStateS3(2);
    }

    @Test
    void checkFsmS3toS3RemoveEntryNotInLib() {
        BibEntry entry1 = new BibEntry(StandardEntryType.Book);
        BibEntry entry2 = new BibEntry(StandardEntryType.Book);
        BibEntry entry3 = new BibEntry(StandardEntryType.Book);
        BibEntry entry4 = new BibEntry(StandardEntryType.Book);
        BibEntry entry5 = new BibEntry(StandardEntryType.Book);
        BibEntry entry6 = new BibEntry(StandardEntryType.Book);
        List<BibEntry> entriesToAdd = Arrays.asList(entry1, entry2, entry3, entry4, entry5);
        database.insertEntries(entriesToAdd);

        isStateS3(5);
        database.removeEntry(entry6);
        isStateS3(5);
    }

    @Test
    void checkFsmS3toS3RemoveEntriesNotInLib() {
        BibEntry entry1 = new BibEntry(StandardEntryType.Book);
        BibEntry entry2 = new BibEntry(StandardEntryType.Book);
        BibEntry entry3 = new BibEntry(StandardEntryType.Book);
        BibEntry entry4 = new BibEntry(StandardEntryType.Book);
        BibEntry entry5 = new BibEntry(StandardEntryType.Book);
        BibEntry entry6 = new BibEntry(StandardEntryType.Book);
        List<BibEntry> entriesToAdd = Arrays.asList(entry1, entry3, entry5);
        database.insertEntries(entriesToAdd);

        List<BibEntry> entriesToRemove = Arrays.asList(entry2, entry4, entry6);

        isStateS3(3);
        database.removeEntries(entriesToRemove);
        isStateS3(3);
    }

    @Test
    void checkFsmS3toS3AddEntry() {
        BibEntry entry1 = new BibEntry(StandardEntryType.Book);
        BibEntry entry2 = new BibEntry(StandardEntryType.Book);
        BibEntry entry3 = new BibEntry(StandardEntryType.Book);
        BibEntry entry4 = new BibEntry(StandardEntryType.Book);
        List<BibEntry> entriesToAdd = Arrays.asList(entry1, entry2, entry3);
        database.insertEntries(entriesToAdd);

        isStateS3(3);
        database.insertEntry(entry4);
        isStateS3(4);
    }

    @Test
    void checkFsmS3toS3AddEntries() {
        BibEntry entry1 = new BibEntry(StandardEntryType.Book);
        BibEntry entry2 = new BibEntry(StandardEntryType.Book);
        BibEntry entry3 = new BibEntry(StandardEntryType.Book);
        BibEntry entry4 = new BibEntry(StandardEntryType.Book);
        BibEntry entry5 = new BibEntry(StandardEntryType.Book);
        BibEntry entry6 = new BibEntry(StandardEntryType.Book);
        List<BibEntry> entriesToAdd = Arrays.asList(entry1, entry3, entry5);
        database.insertEntries(entriesToAdd);

        entriesToAdd = Arrays.asList(entry2, entry4, entry6);

        isStateS3(3);
        database.insertEntries(entriesToAdd);
        isStateS3(6);
    }

    void isStateS1() {
        assertEquals(0, database.getEntryCount());
    }

    void isStateS2() {
        assertEquals(1, database.getEntryCount());
    }

    void isStateS3(int expected_n) {
        assertTrue(1 < database.getEntryCount());
        assertEquals(expected_n, database.getEntryCount());
    }
}
