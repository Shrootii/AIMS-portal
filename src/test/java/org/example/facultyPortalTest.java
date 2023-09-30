package org.example;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;

import static org.junit.jupiter.api.Assertions.*;

class facultyPortalTest {
    static facultyPortal fp;
    @BeforeAll
    public static void init() {fp = new facultyPortal();}

    @Test
    void portal() {
        String input = "6\n";
        ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        fp.portal("shruti27@iitrpr.ac.in");

    }
}