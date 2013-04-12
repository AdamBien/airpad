package com.airhacks.airpad.business.notes.boundary;

import javax.annotation.PostConstruct;

/**
 *
 * @author adam-bien.com
 */
public class NotesStore {

    @PostConstruct
    public void init() {
        System.out.println("Initializing");
    }

    public void findOrCreate(String text) {
        System.out.println("Notes store-- " + text);
    }
}
