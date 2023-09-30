package org.example;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;

import static org.junit.jupiter.api.Assertions.*;
class loginpgTest {

    static loginpg log;
    @BeforeAll
    public static void init() {log = new loginpg();}
    @Test
    void askCreds() {
        String input = "2020csb1128@iitrpr.ac.in\n2020csb1128\n";
        ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        log.askCreds();
    }

    @Test
    void checkCreds() {
        assertEquals(log.checkCreds("2020csb1128@iitrpr.ac.in", "2020csb1128",1), 1);
    }

    @Test
    void loop() {
        String input = "2020csb1128@iitrpr.ac.in\n2020csb1128\n7\n";
        ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        log.loop(1);
    }

    @Test
    void login() {
        String input = "2\n\n";
        ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        log.login();
    }

    @Test
    void main() {

    }
}