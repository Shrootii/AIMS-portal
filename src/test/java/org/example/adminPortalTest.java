package org.example;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;

import static org.junit.jupiter.api.Assertions.*;

class adminPortalTest {

    static adminPortal ap;

    @BeforeAll
    public static void init() {
        ap = new adminPortal();
    }

    @Test
    void semUpdate() {
        String input = "2021\nA\n";
        ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        ap.semUpdate();
    }

    @Test
    void grad() {
        String input = "3\n4\n5\n";
        ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        ap.grad();

    }

    @Test
    void genScript() {
        String input = "2020csb1128@iitrpr.ac.in\n";
        ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        ap.genScript();
    }

    @Test
    void portal() {
        String input = "7\n";
        ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        ap.portal("admin1@admin");
    }
}
