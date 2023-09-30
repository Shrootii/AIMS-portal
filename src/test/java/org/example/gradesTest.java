package org.example;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class gradesTest {

    static grades gd;
    @BeforeAll
    public static void init() {gd = new grades();}

    @Test
    void viewGrades() {
        assertEquals(gd.viewGrades("2020csb1128@iitrpr.ac.in", 1,"2020A"), true);
    }

    @Test
    void calculateCg() {
        assertEquals(gd.calculateCg("2020csb1128@iitrpr.ac.in"), 8.0);
    }

}