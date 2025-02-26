package org.jabref.gui.journals;

import javax.swing.undo.CompoundEdit;

import org.jabref.gui.undo.UndoableFieldChange;
import org.jabref.logic.journals.Abbreviation;
import org.jabref.logic.journals.JournalAbbreviationLoader;
import org.jabref.logic.journals.JournalAbbreviationRepository;
import org.jabref.model.database.BibDatabase;
import org.jabref.model.entry.BibEntry;
import org.jabref.model.entry.field.AMSField;
import org.jabref.model.entry.field.Field;
import org.jabref.model.entry.field.StandardField;

// Undo redo stuff
public class assignment5_UndoableAbbreviator {

    private JournalAbbreviationRepository journalAbbreviationRepository;
    private boolean useFJournalField;

    public assignment5_UndoableAbbreviator(JournalAbbreviationRepository journalAbbreviationRepository, boolean useFJournalField) {
        this.journalAbbreviationRepository = journalAbbreviationRepository;
        this.useFJournalField = useFJournalField;
    }

    public assignment5_UndoableAbbreviator() {
        this.journalAbbreviationRepository = JournalAbbreviationLoader.loadBuiltInRepository();
        this.useFJournalField = true;
    }

    public JournalAbbreviationRepository getRepository() {
        return this.journalAbbreviationRepository;
    }

    /**
     * Abbreviate the journal name of the given entry.
     *
     * @param database  The database the entry belongs to, or null if no database.
     * @param entry     The entry to be treated.
     * @param fieldName The field name (e.g. "journal")
     * @param ce        If the entry is changed, add an edit to this compound.
     * @return true if the entry was changed, false otherwise.
     */
    public boolean abbreviate(AbbreviationType abbType, BibDatabase database, BibEntry entry, Field fieldName, CompoundEdit ce) {
        if (!entry.hasField(fieldName)) {
            return false;
        }

        String text = entry.getField(fieldName).get();
        String origText = text;
        if (database != null) {
            text = database.resolveForStrings(text);
        }

        if (!journalAbbreviationRepository.isKnownName(text)) {
            return false; // Unknown, cannot abbreviate anything.
        }

        Abbreviation abbreviation = journalAbbreviationRepository.get(text).get();
        String newText = getAbbreviatedName(abbreviation, abbType);

        if (newText.equals(origText)) {
            return false;
        }

        // Store full name into fjournal but only if it exists
        if (useFJournalField && (StandardField.JOURNAL == fieldName || StandardField.JOURNALTITLE == fieldName)) {
            entry.setField(AMSField.FJOURNAL, abbreviation.getName());
            ce.addEdit(new UndoableFieldChange(entry, AMSField.FJOURNAL, null, abbreviation.getName()));
        }

        entry.setField(fieldName, newText);
        ce.addEdit(new UndoableFieldChange(entry, fieldName, origText, newText));
        return true;
    }

    public String getAbbreviatedName(Abbreviation text, AbbreviationType abbType) {
        return switch (abbType) {
            case DEFAULT -> text.getAbbreviation();
            case DOTLESS -> text.getDotlessAbbreviation();
            case SHORTEST_UNIQUE -> text.getShortestUniqueAbbreviation();
            default -> throw new IllegalStateException("Unexpected value: %s".formatted(abbType));
        };
    }
}
