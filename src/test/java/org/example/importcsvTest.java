package org.example;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;

import static org.junit.jupiter.api.Assertions.*;

class importcsvTest {

    static importcsv ic;
    @BeforeAll
    public static void init() {ic = new importcsv();}

    @Test
    void copycsv() {
        String input = "cs305\n";
        ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        ic.copycsv();
    }

    @Test
    void uploadcsv() {
        String input = "C:\\Users\\shrut\\IdeaProjects\\AIMS\\grades.csv\n";
        ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        ic.uploadcsv();
    }
}