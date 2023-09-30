package org.example;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;

import static org.junit.jupiter.api.Assertions.*;

class studentPortalTest {

    static studentPortal sp;
    @BeforeAll
    public static void init() {sp = new studentPortal();}

    @Test
    void cgpaUpdate() {
        assertEquals(sp.cgpaUpdate("2020csb1128@iitrpr.ac.in"), true);
    }

    @Test
    void updateSem() {
        assertEquals(sp.updateSem("2020csb1128@iitrpr.ac.in"), true);
    }

    @Test
    void gradcheck() {
        assertEquals(sp.gradcheck("2020csb1128@iitrpr.ac.in"), true);
    }

    @Test
    void portal() {
        String input = "7\n";
        ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        sp.portal("2020csb1128@iitrpr.ac.in");
    }
}