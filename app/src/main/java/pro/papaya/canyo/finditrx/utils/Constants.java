package pro.papaya.canyo.finditrx.utils;

import java.util.regex.Pattern;

public final class Constants {
  //DATABASE
  public static final int LEADER_BOARD_LIMIT = 100;
  public static final String STOCK_NICKNAME = "Newbie#";

  //GLOBAL
  public static final int USER_MAX_QUESTS = 5;
  public static final int TIME_TO_QUEST_MINS = 1;
  public static final String EMPTY_STRING = "";

  //TEXT SECTION
  public static final String OPENED_BOLD_TAG = "<b>";
  public static final String CLOSED_BOLD_TAG = "</b>";

  //REWARD SECTION
  public static final int BOTTOM_BORDER_QUEST_REWARD = 20;
  public static final int UPPER_BORDER_QUEST_REWARD = 200;
  public static final int BOTTOM_BORDER_QUEST_EXP_REWARD = 10;
  public static final int UPPER_BORDER_QUEST_EXP_REWARD = 50;
  public static final int BOTTOM_BORDER_NEW_QUEST_REWARD = 10;
  public static final int UPPER_BORDER_NEW_QUEST_REWARD = 100;
  public static final int BOTTOM_BORDER_NEW_QUEST_EXP_REWARD = 15;
  public static final int UPPER_BORDER_NEW_QUEST_EXP_REWARD = 30;

  public static final int QUEST_RARITY_STEPS = 5;
  public static final int RARITY_PRICE_STEP = (UPPER_BORDER_QUEST_REWARD - BOTTOM_BORDER_QUEST_REWARD) / QUEST_RARITY_STEPS;
  public static final int BOTTOM_RARITY_BORDER = BOTTOM_BORDER_QUEST_REWARD;

  //LOG SECTION
  public static final String LOG_OPENED_BRACKET = "[";
  public static final String LOG_CLOSED_BRACKET = "]";
  public static final String LOG_TAG_DIVIDER = ": ";

  //LOGIN SCREEN
  public static final int MIN_PASSWORD_LENGTH = 6;
  public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
      Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
}
