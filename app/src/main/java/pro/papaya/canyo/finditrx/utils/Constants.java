package pro.papaya.canyo.finditrx.utils;

import java.util.regex.Pattern;

public final class Constants {
  //DATABASE
  public static final String STOCK_NICKNAME = "Newbie#";

  //GLOBAL
  public static final int USER_MAX_QUESTS = 5;
  public static final int TIME_TO_QUEST_MINS = 1;
  public static final String EMPTY_STRING = "";
  public static final String TIME_UNIT_SEPARATOR = ":";
  public static final String TABULATION = "\t";

  //LOG SECTION
  public static final String LOG_OPENED_BRACKET = "[";
  public static final String LOG_CLOSED_BRACKET = "]";
  public static final String LOG_TAG_DIVIDER = ": ";

  //LOGIN SCREEN
  public static final int MIN_PASSWORD_LENGTH = 6;
  public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
      Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
}
