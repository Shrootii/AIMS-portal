package org.example;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;

import static org.junit.jupiter.api.Assertions.*;

class deregisterCourseTest {

    static deregisterCourse dc;
    @BeforeAll
    public static void init() {dc = new deregisterCourse();}
    @Test
    void viewEnrollments() {
        assertEquals(dc.viewEnrollments("2020csb1128@iitrpr.ac.in"), true);
    }

    @Test
    void deregister() {
        String input = "cs305\n";
        ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        dc.deregister("2020csb1128@iitrpr.ac.in");
    }
}