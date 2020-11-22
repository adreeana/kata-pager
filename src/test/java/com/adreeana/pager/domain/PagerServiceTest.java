package com.adreeana.pager.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.ArgumentCaptor;

import java.util.Optional;

import static com.adreeana.pager.domain.EscalationPolicyFixture.monitoredService;
import static com.adreeana.pager.domain.EscalationPolicyFixture.twoLevels;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@TestInstance(TestInstance.Lifecycle.PER_METHOD)
class PagerServiceTest {
  private PagerService pagerService;

  private EscalationPolicyService escalationPolicyService = mock(EscalationPolicyService.class);

  private Alerts alerts = mock(Alerts.class);
  private AlertService alertService = mock(AlertService.class);

  private TimerService timerService = mock(TimerService.class);

  @BeforeEach
  void setUp() {
    pagerService = new PagerService(alerts, alertService, escalationPolicyService, timerService);
  }

  @Nested
  class ReceiveAlert {
    @Nested
    class HappyPath {
      Alert alert;
      EscalationPolicy escalationPolicy;

      @BeforeEach
      void setUp() {
        //Given a Monitored Service in a Healthy State,
        alert = new Alert(monitoredService, "Oups!");
        escalationPolicy = twoLevels(alert.getMonitoredService());
      }

      @Test
      void receiveAlert() {
        when(escalationPolicyService.findEscalationPolicy(alert.getMonitoredService())).thenReturn(escalationPolicy);

        //when the Pager receives an Alert related to this Monitored Service,
        pagerService.receiveAlert(alert);

        //then the Monitored Service becomes Unhealthy,
        //the Pager notifies all targets of the first level of the escalation policy,
        //  and sets a 15-minutes acknowledgement delay
        assertFalse(alert.getMonitoredService().isHealthy());

        ArgumentCaptor<Alert> alertCaptor = ArgumentCaptor.forClass(Alert.class);
        verify(alerts).save(alertCaptor.capture());

        Alert savedAlert = alertCaptor.getValue();
        assertEquals(alert, savedAlert);

        assertEquals(alert.getLevel(), escalationPolicy.getLevels().first());

        verify(alertService).notifyLevel(alert);

        ArgumentCaptor<Alert> startTimerCaptor = ArgumentCaptor.forClass(Alert.class);
        verify(timerService).startTimer(startTimerCaptor.capture());

        assertEquals(alert, startTimerCaptor.getValue());
      }
    }
  }

  @Nested
  class AcknowledgementTimeout {
    @Nested
    class HappyPath {
      Alert alert;
      EscalationPolicy escalationPolicy;

      @BeforeEach
      void setUp() {
        alert = new Alert(monitoredService, "Oups!");
        alert.getMonitoredService().unhealthy();

        escalationPolicy = twoLevels(alert.getMonitoredService());
        alert.setLevel(escalationPolicy.getLevels().first());
      }

      @Nested
      class AlertIsNotAcknowledged {
        @Nested
        class LevelIsNotAcknowledged {
          @Test
          void receiveAcknowledgementTimeout() {
            when(escalationPolicyService.findEscalationPolicy(alert.getMonitoredService())).thenReturn(
              escalationPolicy);

            assertFalse(alert.getMonitoredService().isHealthy());
            assertFalse(alert.isAcknowledged());

            Level lastNotifiedLevel = alert.getLevel();
            assertNotNull(lastNotifiedLevel);

            pagerService.receiveAcknowledgementTimeout(alert);

            ArgumentCaptor<Alert> alertCaptor = ArgumentCaptor.forClass(Alert.class);
            verify(alerts).save(alertCaptor.capture());

            Alert savedAlert = alertCaptor.getValue();
            assertEquals(alert, savedAlert);

            Optional<Level> nextLevel = escalationPolicy.getLevels().next(lastNotifiedLevel);
            assertTrue(nextLevel.isPresent());
            assertEquals(nextLevel.get(), alert.getLevel());
            verify(alertService).notifyLevel(alert);

            ArgumentCaptor<Alert> startTimerCaptor = ArgumentCaptor.forClass(Alert.class);
            verify(timerService).startTimer(startTimerCaptor.capture());

            assertEquals(alert, startTimerCaptor.getValue());
          }
        }

        @Nested
        class LevelIsAcknowledged {
          @BeforeEach
          void setUp() {
            alert.acknowledgeLevel();
          }

          @Test
          void receiveAcknowledgementTimeout() {
            assertFalse(alert.getMonitoredService().isHealthy());
            assertFalse(alert.isAcknowledged());

            Level lastNotifiedLevel = alert.getLevel();
            assertNotNull(lastNotifiedLevel);

            pagerService.receiveAcknowledgementTimeout(alert);

            assertEquals(lastNotifiedLevel, alert.getLevel());

            verifyNoInteractions(escalationPolicyService);
            verifyNoInteractions(alerts);
            verifyNoInteractions(alertService);
            verifyNoInteractions(timerService);
          }
        }

        @Nested
        class AlertIsAcknowledged {
          @BeforeEach
          void setUp() {
            alert.acknowledge();
          }

          @Test
          void receiveAcknowledgementTimeout() {
            assertFalse(alert.getMonitoredService().isHealthy());
            assertTrue(alert.isAcknowledged());

            pagerService.receiveAcknowledgementTimeout(alert);

            verifyNoInteractions(escalationPolicyService);
            verifyNoInteractions(alerts);
            verifyNoInteractions(alertService);
            verifyNoInteractions(timerService);
          }
        }
      }
    }
  }

  @Nested
  class ReceiveLevelAcknowledgement {
    @Nested
    class HappyPath {
      Alert alert;
      EscalationPolicy escalationPolicy;

      @BeforeEach
      void setUp() {
        alert = new Alert(monitoredService, "Oups!");
        alert.getMonitoredService().unhealthy();

        escalationPolicy = twoLevels(alert.getMonitoredService());
        alert.setLevel(escalationPolicy.getLevels().first());
      }

      @Test
      void receiveLevelAcknowledgement() {
        when(escalationPolicyService.findEscalationPolicy(alert.getMonitoredService())).thenReturn(escalationPolicy);

        assertFalse(alert.getMonitoredService().isHealthy());
        assertFalse(alert.isAcknowledged());

        Level lastNotifiedLevel = alert.getLevel();
        assertNotNull(lastNotifiedLevel);

        pagerService.receiveLevelAcknowledgement(alert);

        ArgumentCaptor<Alert> alertCaptor = ArgumentCaptor.forClass(Alert.class);
        verify(alerts).save(alertCaptor.capture());

        Alert savedAlert = alertCaptor.getValue();
        assertEquals(alert, savedAlert);

        assertEquals(lastNotifiedLevel, alert.getLevel());
        assertTrue(alert.getLevel().isAcknowledged());
      }
    }
  }
}