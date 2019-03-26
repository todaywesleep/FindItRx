package pro.papaya.canyo.finditrx.utils;

import java.util.regex.Matcher;

public final class StringUtils {
  public static boolean isEmailValid(String email) {
    Matcher matcher = Constants.VALID_EMAIL_ADDRESS_REGEX.matcher(email);
    return matcher.find();
  }
}
