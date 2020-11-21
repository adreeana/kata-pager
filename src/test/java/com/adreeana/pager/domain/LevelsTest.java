package com.adreeana.pager.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
        Target smsFirstTarget = new SmsTarget("0601");
        firstLevel.addTarget(smsFirstTarget);
        Target emailFirstTarget = new EmailTarget("email01");
        firstLevel.addTarget(emailFirstTarget);
        levels.addLevel(firstLevel);

        Level secondLevel = new Level();
        Target smsSecondTarget = new SmsTarget("0602");
        secondLevel.addTarget(smsSecondTarget);
        Target emailSecondTarget = new EmailTarget("email02");
        secondLevel.addTarget(emailSecondTarget);
        levels.addLevel(secondLevel);

        Level first = levels.first();

        assertEquals(firstLevel, first);
      }
    }

    @Nested
    class UnhappyPath {
      @Test
      void noSuchElement() {
        assertThrows(IndexOutOfBoundsException.class, () ->
          levels.first()
        );
      }
    }
  }
}