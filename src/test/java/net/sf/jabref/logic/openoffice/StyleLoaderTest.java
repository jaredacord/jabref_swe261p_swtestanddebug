package net.sf.jabref.logic.openoffice;

import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sf.jabref.Globals;
import net.sf.jabref.JabRefMain;
import net.sf.jabref.logic.journals.JournalAbbreviationLoader;
import net.sf.jabref.logic.layout.LayoutFormatterPreferences;
import net.sf.jabref.preferences.JabRefPreferences;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;

public class StyleLoaderTest {

    private JabRefPreferences backup;
    private static int numberOfInternalStyles = 2;
    private StyleLoader loader;

    private OpenOfficePreferences preferences;
    @Before
    public void setUp() {
        backup = JabRefPreferences.getInstance();
        if (Globals.prefs == null) {
            Globals.prefs = JabRefPreferences.getInstance();
        }
        if (Globals.journalAbbreviationLoader == null) {
            Globals.journalAbbreviationLoader = mock(JournalAbbreviationLoader.class);
        }
        preferences = new OpenOfficePreferences(Globals.prefs);
    }

    @After
    public void tearDown() throws Exception {
        Globals.prefs.overwritePreferences(backup);
    }

    @Test(expected = NullPointerException.class)
    public void throwNPEWithNullPreferences() {
        loader = new StyleLoader(null,
                LayoutFormatterPreferences.fromPreferences(Globals.prefs, mock(JournalAbbreviationLoader.class)),
                mock(Charset.class));
        fail();
    }

    @Test(expected = NullPointerException.class)
    public void throwNPEWithNullRepository() {
        loader = new StyleLoader(mock(OpenOfficePreferences.class),
                LayoutFormatterPreferences.fromPreferences(Globals.prefs, null), mock(Charset.class));
        fail();
    }

    @Test(expected = NullPointerException.class)
    public void throwNPEWithNullCharset() {
        loader = new StyleLoader(mock(OpenOfficePreferences.class),
                LayoutFormatterPreferences.fromPreferences(Globals.prefs, mock(JournalAbbreviationLoader.class)), null);
        fail();
    }

    @Test
    public void testGetStylesWithEmptyExternal() {
        preferences.setExternalStyles(Collections.emptyList());
        loader = new StyleLoader(preferences,
                LayoutFormatterPreferences.fromPreferences(Globals.prefs, mock(JournalAbbreviationLoader.class)),
                Globals.prefs.getDefaultEncoding());

        assertEquals(2, loader.getStyles().size());
    }

    @Test
    public void testAddStyleLeadsToOneMoreStyle() throws URISyntaxException {
        preferences.setExternalStyles(Collections.emptyList());
        loader = new StyleLoader(preferences,
                LayoutFormatterPreferences.fromPreferences(Globals.prefs, mock(JournalAbbreviationLoader.class)),
                Globals.prefs.getDefaultEncoding());

        String filename = Paths.get(JabRefMain.class.getResource(StyleLoader.DEFAULT_AUTHORYEAR_STYLE_PATH).toURI())
                .toFile().getPath();
        loader.addStyleIfValid(filename);
        assertEquals(numberOfInternalStyles + 1, loader.getStyles().size());
    }

    @Test
    public void testAddInvalidStyleLeadsToNoMoreStyle() {
        preferences.setExternalStyles(Collections.emptyList());
        Globals.prefs.putStringList(JabRefPreferences.OO_EXTERNAL_STYLE_FILES, Collections.emptyList());
        loader = new StyleLoader(preferences,
                LayoutFormatterPreferences.fromPreferences(Globals.prefs, mock(JournalAbbreviationLoader.class)),
                Globals.prefs.getDefaultEncoding());
        int beforeAdding = loader.getStyles().size();
        loader.addStyleIfValid("DefinitelyNotAValidFileNameOrWeAreExtremelyUnlucky");
        assertEquals(beforeAdding, loader.getStyles().size());
    }

    @Test
    public void testInitalizeWithOneExternalFile() throws URISyntaxException {
        String filename = Paths.get(JabRefMain.class.getResource(StyleLoader.DEFAULT_AUTHORYEAR_STYLE_PATH).toURI())
                .toFile().getPath();
        preferences.setExternalStyles(Collections.singletonList(filename));
        loader = new StyleLoader(preferences,
                LayoutFormatterPreferences.fromPreferences(Globals.prefs, mock(JournalAbbreviationLoader.class)),
                Globals.prefs.getDefaultEncoding());
        assertEquals(numberOfInternalStyles + 1, loader.getStyles().size());
    }

    @Test
    public void testInitalizeWithIncorrectExternalFile() {
        preferences.setExternalStyles(Collections.singletonList("DefinitelyNotAValidFileNameOrWeAreExtremelyUnlucky"));

        loader = new StyleLoader(new OpenOfficePreferences(Globals.prefs),
                LayoutFormatterPreferences.fromPreferences(Globals.prefs, mock(JournalAbbreviationLoader.class)),
                Globals.prefs.getDefaultEncoding());
        assertEquals(numberOfInternalStyles, loader.getStyles().size());
    }

    @Test
    public void testInitalizeWithOneExternalFileRemoveStyle() throws URISyntaxException {
        String filename = Paths.get(JabRefMain.class.getResource(StyleLoader.DEFAULT_AUTHORYEAR_STYLE_PATH).toURI())
                .toFile().getPath();
        preferences.setExternalStyles(Collections.singletonList(filename));

        loader = new StyleLoader(new OpenOfficePreferences(Globals.prefs),
                LayoutFormatterPreferences.fromPreferences(Globals.prefs, mock(JournalAbbreviationLoader.class)),
                Globals.prefs.getDefaultEncoding());
        List<OOBibStyle> toremove = new ArrayList<>();
        int beforeRemoving = loader.getStyles().size();
        for (OOBibStyle style : loader.getStyles()) {
            if (!style.isFromResource()) {
                toremove.add(style);
            }
        }

        for (OOBibStyle style : toremove) {
            assertTrue(loader.removeStyle(style));
        }
        assertEquals(beforeRemoving - 1, loader.getStyles().size());
    }

    @Test
    public void testInitalizeWithOneExternalFileRemoveStyleUpdatesPreferences() throws URISyntaxException {
        String filename = Paths.get(JabRefMain.class.getResource(StyleLoader.DEFAULT_AUTHORYEAR_STYLE_PATH).toURI())
                .toFile().getPath();
        preferences.setExternalStyles(Collections.singletonList(filename));

        loader = new StyleLoader(preferences,
                LayoutFormatterPreferences.fromPreferences(Globals.prefs, mock(JournalAbbreviationLoader.class)),
                Globals.prefs.getDefaultEncoding());
        List<OOBibStyle> toremove = new ArrayList<>();
        for (OOBibStyle style : loader.getStyles()) {
            if (!style.isFromResource()) {
                toremove.add(style);
            }
        }

        for (OOBibStyle style : toremove) {
            assertTrue(loader.removeStyle(style));
        }
        assertTrue(preferences.getExternalStyles().isEmpty());
    }

    @Test
    public void testAddSameStyleTwiceLeadsToOneMoreStyle() throws URISyntaxException {
        preferences.setExternalStyles(Collections.emptyList());
        loader = new StyleLoader(new OpenOfficePreferences(Globals.prefs),
                LayoutFormatterPreferences.fromPreferences(Globals.prefs, mock(JournalAbbreviationLoader.class)),
                Globals.prefs.getDefaultEncoding());
        int beforeAdding = loader.getStyles().size();
        String filename = Paths.get(JabRefMain.class.getResource(StyleLoader.DEFAULT_AUTHORYEAR_STYLE_PATH).toURI())
                .toFile().getPath();
        loader.addStyleIfValid(filename);
        loader.addStyleIfValid(filename);
        assertEquals(beforeAdding + 1, loader.getStyles().size());
    }

    @Test(expected = NullPointerException.class)
    public void testAddNullStyleThrowsNPE() {
        loader = new StyleLoader(new OpenOfficePreferences(Globals.prefs),
                LayoutFormatterPreferences.fromPreferences(Globals.prefs, mock(JournalAbbreviationLoader.class)),
                Globals.prefs.getDefaultEncoding());
        loader.addStyleIfValid(null);
        fail();
    }


    @Test
    public void testGetDefaultUsedStyleWhenEmpty() {
        Globals.prefs.remove(JabRefPreferences.OO_BIBLIOGRAPHY_STYLE_FILE);
        loader = new StyleLoader(new OpenOfficePreferences(Globals.prefs),
                LayoutFormatterPreferences.fromPreferences(Globals.prefs, mock(JournalAbbreviationLoader.class)),
                Globals.prefs.getDefaultEncoding());
        OOBibStyle style = loader.getUsedStyle();
        assertTrue(style.isValid());
        assertEquals(StyleLoader.DEFAULT_AUTHORYEAR_STYLE_PATH, style.getPath());
        assertEquals(StyleLoader.DEFAULT_AUTHORYEAR_STYLE_PATH, preferences.getCurrentStyle());
    }

    @Test
    public void testGetStoredUsedStyle() {
        preferences.setCurrentStyle(StyleLoader.DEFAULT_NUMERICAL_STYLE_PATH);
        loader = new StyleLoader(new OpenOfficePreferences(Globals.prefs),
                LayoutFormatterPreferences.fromPreferences(Globals.prefs, mock(JournalAbbreviationLoader.class)),
                Globals.prefs.getDefaultEncoding());
        OOBibStyle style = loader.getUsedStyle();
        assertTrue(style.isValid());
        assertEquals(StyleLoader.DEFAULT_NUMERICAL_STYLE_PATH, style.getPath());
        assertEquals(StyleLoader.DEFAULT_NUMERICAL_STYLE_PATH, preferences.getCurrentStyle());
    }

    @Test
    public void testGtDefaultUsedStyleWhenIncorrect() {
        preferences.setCurrentStyle("ljlkjlkjnljnvdlsjniuhwelfhuewfhlkuewhfuwhelu");
        loader = new StyleLoader(new OpenOfficePreferences(Globals.prefs),
                LayoutFormatterPreferences.fromPreferences(Globals.prefs, mock(JournalAbbreviationLoader.class)),
                Globals.prefs.getDefaultEncoding());
        OOBibStyle style = loader.getUsedStyle();
        assertTrue(style.isValid());
        assertEquals(StyleLoader.DEFAULT_AUTHORYEAR_STYLE_PATH, style.getPath());
        assertEquals(StyleLoader.DEFAULT_AUTHORYEAR_STYLE_PATH, preferences.getCurrentStyle());
    }

    @Test
    public void testRemoveInternalStyleReturnsFalseAndDoNotRemove() {
        preferences.setExternalStyles(Collections.emptyList());

        loader = new StyleLoader(preferences,
                LayoutFormatterPreferences.fromPreferences(Globals.prefs, mock(JournalAbbreviationLoader.class)),
                Globals.prefs.getDefaultEncoding());
        List<OOBibStyle> toremove = new ArrayList<>();
        for (OOBibStyle style : loader.getStyles()) {
            if (style.isFromResource()) {
                toremove.add(style);
            }
        }

        assertFalse(loader.removeStyle(toremove.get(0)));
        assertEquals(numberOfInternalStyles, loader.getStyles().size());
    }

}
