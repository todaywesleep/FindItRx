package pro.papaya.canyo.finditrx.utils;

import java.util.Date;

public class TimeUtils {
  public static long getTimestampForFullQuests() {
    long currentTime = new Date().getTime();
    long timeToFullQuests = secsToMillis(minsToSecs(Constants.TIME_TO_QUEST_MINS)) * Constants.USER_MAX_QUESTS;
    return currentTime - timeToFullQuests;
  }

  public static long minsToSecs(long mins) {
    return mins * 60;
  }

  public static long secsToMillis(long secs) {
    return secs * 1000;
  }

  public static long minsToMillis(long mins) {
    return secsToMillis(minsToSecs(mins));
  }

  public static long millisToSecs(long millis) {
    return millis / 1000;
  }
}
