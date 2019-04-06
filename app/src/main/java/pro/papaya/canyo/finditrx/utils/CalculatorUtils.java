package pro.papaya.canyo.finditrx.utils;

public final class CalculatorUtils {
  public static int getRestExperience(int currentLevel, int currentExp) {
    int currentLevelNatural = currentLevel + 1;
    int experienceToLevel = currentLevelNatural * 2 * 25;

    return experienceToLevel - currentExp;
  }
}
