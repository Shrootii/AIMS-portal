package org.example;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;

import static org.junit.jupiter.api.Assertions.*;

class facultyRDTest {

    static facultyRD frd;
    @BeforeAll
    public static void init() {frd = new facultyRD();}

    @Test
    void deregisterOfferings() {
        String input = "cs123\n";
        ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        frd.deregisterOfferings();
    }

    @Test
    void registerOffering() {
        String input = "zxc\ncs123\n2\n3-1-0-1-3\nnone\n6\nelective\n";
        ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        frd.registerOffering();

    }
}