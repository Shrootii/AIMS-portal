package org.example;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;

import static org.junit.jupiter.api.Assertions.*;

class adminCCTest {

    static adminCC acc;
    @BeforeAll
    public static void init() {acc = new adminCC();}

    @Test
    void registerC() {
        String input = "asd\nge111\n2\n2-1-0-1-2\nge101\n";
        ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        acc.registerC();
    }

    @Test
    void deregisterC() {
        String input = "ch111\n";
        ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        acc.deregisterC();
    }

    @Test
    void updateC() {
        String input = "hs111\n1\nxcv\n";
        ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        acc.updateC();
    }

    @Test
    void editCatalog() {
        String input = "4\n";
        ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        acc.editCatalog();

    }
}