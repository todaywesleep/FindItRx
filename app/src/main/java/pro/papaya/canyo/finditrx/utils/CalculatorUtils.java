package pro.papaya.canyo.finditrx.utils;

public final class CalculatorUtils {
  private static final int EXP_PER_LEVEL = 100;

  public static int getRestExperience(int currentLevel, int currentExp) {
    return getTotalLevelExperience(currentLevel) - currentExp;
  }

  public static int getTotalLevelExperience(int currentLevel) {
    int currentLevelNatural = currentLevel + 1;
    int experienceToLevel = currentLevelNatural * EXP_PER_LEVEL;

    return experienceToLevel;
  }

  public static int getAllExperienceFromLevel(int level, int currentExp) {
    int fullExp = 0;

    for (int i = 1; i < level + 1; i++) {
      fullExp += i * EXP_PER_LEVEL;
    }

    return fullExp + currentExp;
  }
}
