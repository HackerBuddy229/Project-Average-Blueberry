package tech.beryllium.services;

import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class GameEntityServiceShould {

    @Test
    void getGameEntityByDifficulty() {
        var service = new GameEntityService(new Random());
        var word = service.getGameEntityByDifficulty(0);
        assertEquals(4, word.length());
    }
}