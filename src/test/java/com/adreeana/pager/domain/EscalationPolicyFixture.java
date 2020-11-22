package com.adreeana.pager.domain;

public class EscalationPolicyFixture {

  final static public Target smsFirstTarget = new SmsTarget("0601");
  final static public Target emailFirstTarget = new EmailTarget("email02");
  final static public Target smsSecondTarget = new SmsTarget("0602");
  final static public Target emailSecondTarget = new EmailTarget("email02");

  final static public MonitoredService monitoredService = new MonitoredService("1");

  static public EscalationPolicy twoLevels(MonitoredService monitoredService) {
    Levels levels = new Levels();

    Level firstLevel = new Level();
    firstLevel.addTarget(smsFirstTarget);
    firstLevel.addTarget(emailFirstTarget);
    levels.addLevel(firstLevel);

    Level secondLevel = new Level();
    secondLevel.addTarget(smsSecondTarget);
    secondLevel.addTarget(emailSecondTarget);
    levels.addLevel(secondLevel);

    return new EscalationPolicy(monitoredService, levels);
  }
}
