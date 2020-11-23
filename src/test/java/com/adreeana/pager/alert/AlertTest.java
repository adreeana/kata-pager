package com.adreeana.pager.alert;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AlertTest {
  Alert alert;

  @BeforeEach
  void setUp() {
    alert = new Alert(new MonitoredService("1"), "Oups!");
  }

  @Nested
  class AcknowledgeLevel {
    @Test
    void withAlertLevel() {
      Level level = new Level();
      assertFalse(level.isAcknowledged());

      alert.setLevel(level);

      alert.acknowledgeLevel();

      assertTrue(level.isAcknowledged());
    }

    @Test
    void withoutAlertLevel() {
      assertNull(alert.getLevel());
      assertDoesNotThrow(() -> alert.acknowledgeLevel());
    }
  }

  @Nested
  class IsLevelAcknowledged {
    @Test
    void withAlertLevel() {
      Level level = new Level();
      level.acknowledge();

      assertTrue(level.isAcknowledged());

      alert.setLevel(level);

      assertTrue(alert.isLevelAcknowledged());
    }

    @Test
    void withoutAlertLevel() {
      assertNull(alert.getLevel());
      assertFalse(() -> alert.isLevelAcknowledged());
    }
  }
}