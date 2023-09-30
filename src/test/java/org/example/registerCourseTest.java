package org.example;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;

import static org.junit.jupiter.api.Assertions.*;

class registerCourseTest {

    static registerCourse rc;
    @BeforeAll
    public static void init() {rc = new registerCourse();}

    @Test
    void viewCatalog() {
        assertEquals(rc.viewCatalog(), true);

    }

    @Test
    void register() {
        String input = "cs305\n";
        ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        rc.register("2020csb1128@iitrpr.ac.in");
    }
}