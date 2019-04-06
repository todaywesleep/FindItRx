package pro.papaya.canyo.finditrx.utils;

import java.util.regex.Pattern;

public final class Constants {
  //DATABASE
  public static final String STOCK_NICKNAME = "Newbie#";

  //GLOBAL
  public static final int USER_MAX_QUESTS = 5;
  public static final int TIME_TO_QUEST_MINS = 3;
  public static final String EMPTY_STRING = "";
  public static final String TIME_UNIT_SEPARATOR = ":";
  public static final String TABULATION = "\t";

  //REWARD SECTION
  public static final int BOTTOM_BORDER_QUEST_REWARD = 20;
  public static final int UPPER_BORDER_QUEST_REWARD = 200;
  public static final int BOTTOM_BORDER_QUEST_EXP_REWARD = 25;
  public static final int UPPER_BORDER_QUEST_EXP_REWARD = 50;
  public static final int BOTTOM_BORDER_NEW_QUEST_REWARD = 10;
  public static final int UPPER_BORDER_NEW_QUEST_REWARD = 100;
  public static final int BOTTOM_BORDER_NEW_QUEST_EXP_REWARD = 15;
  public static final int UPPER_BORDER_NEW_QUEST_REXP_EWARD = 30;

  //LOG SECTION
  public static final String LOG_OPENED_BRACKET = "[";
  public static final String LOG_CLOSED_BRACKET = "]";
  public static final String LOG_TAG_DIVIDER = ": ";

  //LOGIN SCREEN
  public static final int MIN_PASSWORD_LENGTH = 6;
  public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
      Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
}
