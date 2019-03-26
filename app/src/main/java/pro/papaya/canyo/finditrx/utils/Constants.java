package pro.papaya.canyo.finditrx.utils;

import java.util.regex.Pattern;

public final class Constants {
  //GLOBAL
  public static final String EMPTY_STRING = "";

  //LOG SECTION
  public static final String LOG_OPENED_BRACKET = "[";
  public static final String LOG_CLOSED_BRACKET = "]";
  public static final String LOG_TAG_DIVIDER = ": ";

  //LOGIN SCREEN
  public static final int MIN_PASSWORD_LENGTH = 6;
  public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
      Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
}
