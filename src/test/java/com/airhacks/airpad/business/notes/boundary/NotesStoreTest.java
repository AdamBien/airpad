package com.airhacks.airpad.business.notes.boundary;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

/**
 *
 * @author adam-bien.com
 */
public class NotesStoreTest {

    @Test
    public void stripEnding() {
        NotesStore notesStore = new NotesStore();
        String actual = notesStore.stripEnding("hello.note");
        String expected = "hello";
        assertThat(actual, is(expected));
    }
}
