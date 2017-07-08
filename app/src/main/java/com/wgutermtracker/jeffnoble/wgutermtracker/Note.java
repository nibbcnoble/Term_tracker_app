package com.wgutermtracker.jeffnoble.wgutermtracker;

import java.util.ArrayList;

/**
 * Created by nibbc_000 on 6/3/2017.
 */

public class Note {

    private String note_string;
    private ArrayList<String> photos;
    public Note(String note_string) {
        this.note_string = note_string;
        this.photos = new ArrayList<>();
    }

    public void add_photo(String photo) {
        this.photos.add(photo);
    }

    public ArrayList<String> get_photos() {
        return this.photos;
    }

    public void set_photos(ArrayList<String> photos) {
        this.photos = photos;
    }

    public String get_note_string() {
        return this.note_string;
    }

}
