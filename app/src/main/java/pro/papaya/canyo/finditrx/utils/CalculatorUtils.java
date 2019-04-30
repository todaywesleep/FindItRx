package pro.papaya.canyo.finditrx.utils;

public final class CalculatorUtils {
  public static int getRestExperience(int currentLevel, int currentExp) {
    return getTotalLevelExperience(currentLevel) - currentExp;
  }

  public static int getTotalLevelExperience(int currentLevel) {
    int currentLevelNatural = currentLevel + 1;
    int experienceToLevel = currentLevelNatural * 2 * 25;

    return experienceToLevel;
  }

  public static int getAllExperienceFromLevel(int level, int currentExp) {
    int fullExp = 0;

    for (int i = 1; i < level + 1; i++) {
      fullExp += level * 2 * 25;
    }

    return fullExp + currentExp;
  }
}
