package com.wgutermtracker.jeffnoble.wgutermtracker;

import java.util.ArrayList;

/**
 * Created by nibbc_000 on 6/3/2017.
 */

public class CourseMentor {

    private String name;
    private String phone_numbers;
    private String email_addresses;

    public CourseMentor (String name, String phone, String email) {
        this.name =name;
        this.phone_numbers = phone;
        this.email_addresses = email;
    }

    public String get_name() {
        return this.name;
    }

    public String get_phone_number() {
        return this.phone_numbers;
    }

    public String get_email_address() {
        return this.email_addresses;
    }
}
