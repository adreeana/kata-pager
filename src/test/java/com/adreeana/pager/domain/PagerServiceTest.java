package com.adreeana.pager.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.ArgumentCaptor;

import java.util.List;
import java.util.Optional;

import static com.adreeana.pager.domain.EscalationPolicyFixture.emailFirstTarget;
import static com.adreeana.pager.domain.EscalationPolicyFixture.emailSecondTarget;
import static com.adreeana.pager.domain.EscalationPolicyFixture.monitoredService;
import static com.adreeana.pager.domain.EscalationPolicyFixture.smsFirstTarget;
import static com.adreeana.pager.domain.EscalationPolicyFixture.smsSecondTarget;
import static com.adreeana.pager.domain.EscalationPolicyFixture.twoLevels;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
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
        //Given a Monitored Service in an Unhealthy State,
        //the corresponding Alert is not Acknowledged
        //and the last level has not been notified
        alert = new Alert(monitoredService, "Oups!");
        alert.getMonitoredService().unhealthy();

        escalationPolicy = twoLevels(alert.getMonitoredService());
        alert.setLevel(escalationPolicy.getLevels().first());
      }

      @Test
      void receiveAcknowledgementTimeout() {
        when(escalationPolicyService.findEscalationPolicy(alert.getMonitoredService())).thenReturn(escalationPolicy);

        Level lastNotifiedLevel = alert.getLevel();

        //when the Pager receives the Acknowledgement Timeout,
        pagerService.receiveAcknowledgementTimeout(alert);

        //then the Pager notifies all targets of the next level of the escalation policy
        //and sets a 15-minutes acknowledgement delay.
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
  }
}