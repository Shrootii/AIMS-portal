package org.example;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;

import static org.junit.jupiter.api.Assertions.*;

class editRecordTest {

    static editRecord er;
    @BeforeAll
    public static void init() {er = new editRecord();}

    @Test
    void edit() {
        String input = "2\nabc\n";
        ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        er.edit("shruti27@iitrpr.ac.in",2);
    }
}