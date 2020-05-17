package tech.beryllium.services;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AsciiServiceShould {

    @Test
    void getAsciiByProgression() {
        var asciiService = new AsciiService();
        var ascii = asciiService.getAsciiByProgression(5);
        assertEquals(5, ascii.getProgress());
    }
}