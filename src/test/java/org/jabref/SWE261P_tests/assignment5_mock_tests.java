package org.jabref.SWE261P_tests;

import java.util.Random;

import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;

import org.jabref.gui.LibraryTab;
import org.jabref.gui.undo.CountingUndoManager;
import org.jabref.model.database.BibDatabaseContext;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

class assignment5_mock_tests {

    private LibraryTab libraryTabMock;
    private CountingUndoManager undoCountingManagerMock;
    private BibDatabaseContext bibDatabaseContextMock;

    @BeforeEach
    void setUp() {
        libraryTabMock = mock(LibraryTab.class);
        undoCountingManagerMock = spy(new CountingUndoManager());
        bibDatabaseContextMock = mock(BibDatabaseContext.class);
    }

    @Test
    public void testUndoRedoFunctionalityDoesNotWriteToDisk() {
        when(libraryTabMock.getUndoManager()).thenReturn(undoCountingManagerMock);

        Random rand = new Random();
        int numUndoRedoCommands = 3 + rand.nextInt(8);
        int numUndoCommands = 0;
        int numRedoCommands = 0;

        for (int i = 0; i < numUndoRedoCommands; i++) {
            if (rand.nextBoolean()) {
                try {
                    numUndoCommands++;
                    libraryTabMock.getUndoManager().undo();
                } catch (CannotUndoException e) { }
            } else {
                try {
                    numRedoCommands++;
                    libraryTabMock.getUndoManager().redo();
                } catch (CannotRedoException e) { }
            }
        }

        verify(undoCountingManagerMock, times(numUndoCommands)).undo();
        verify(undoCountingManagerMock, times(numRedoCommands)).redo();

        verifyNoInteractions(bibDatabaseContextMock);
    }
}
