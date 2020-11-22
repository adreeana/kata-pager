package com.adreeana.pager.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestInstance(TestInstance.Lifecycle.PER_METHOD)
class LevelsTest {
  Levels levels;

  @BeforeEach
  void setUp() {
    levels = new Levels();
  }

  @Nested
  class First {
    @Nested
    class HappyPath {
      @Test
      void first() {
        Level firstLevel = new Level();
        Level secondLevel = new Level();

        levels.addLevel(firstLevel);
        levels.addLevel(secondLevel);

        assertEquals(firstLevel, levels.first());
      }
    }
  }

  @Nested
  class Next {
    @Nested
    class HappyPath {
      @Test
      void next() {
        Level firstLevel = new Level();
        Level secondLevel = new Level();
        Level thirdLevel = new Level();

        levels.addLevel(firstLevel);
        levels.addLevel(secondLevel);
        levels.addLevel(thirdLevel);

        Optional<Level> next = levels.next(secondLevel);
        assertTrue(next.isPresent());
        assertEquals(thirdLevel, next.get());
      }

      @Test
      void noNext() {
        Level firstLevel = new Level();

        levels.addLevel(firstLevel);

        Optional<Level> next = levels.next(firstLevel);
        assertTrue(next.isEmpty());
      }

      @Test
      void notFound() {
        Level firstLevel = new Level();
        Level secondLevel = new Level();

        levels.addLevel(secondLevel);

        Optional<Level> next = levels.next(firstLevel);
        assertTrue(next.isEmpty());
      }
    }
  }
}