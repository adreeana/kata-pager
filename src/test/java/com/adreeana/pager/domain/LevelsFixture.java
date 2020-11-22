package com.adreeana.pager.domain;

public class LevelsFixture {
  final static public Target smsFirstTarget = new SmsTarget("0601");
  final static public Target emailFirstTarget = new EmailTarget("email02");
  final static public Target smsSecondTarget = new SmsTarget("0602");
  final static public Target emailSecondTarget = new EmailTarget("email02");

  final static public Levels oneLevels() {
    Level firstLevel = new Level();
    firstLevel.addTarget(LevelsFixture.smsFirstTarget);
    firstLevel.addTarget(LevelsFixture.emailFirstTarget);

    Levels levels = new Levels();
    levels.addLevel(firstLevel);

    return levels;
  }

  final static public Levels twoLevels() {
    Level firstLevel = new Level();
    firstLevel.addTarget(LevelsFixture.smsFirstTarget);
    firstLevel.addTarget(LevelsFixture.emailFirstTarget);

    Level secondLevel = new Level();
    secondLevel.addTarget(LevelsFixture.smsSecondTarget);
    secondLevel.addTarget(LevelsFixture.emailSecondTarget);

    Levels levels = new Levels();
    levels.addLevel(firstLevel);
    levels.addLevel(secondLevel);

    return levels;
  }
}
