package pro.papaya.canyo.finditrx.model.firebase;

public class UserQuestModel extends QuestModel {
  private int reward = 0;
  private int experience = 0;

  public UserQuestModel() {
  }

  public UserQuestModel(String identifier, String label, int reward, int experience) {
    super(identifier, label);
    this.reward = reward;
    this.experience = experience;
  }

  public static UserQuestModel from(QuestModel model, Integer reward, int experience) {
    return new UserQuestModel(
        model.getIdentifier(),
        model.getLabel(),
        reward,
        experience
    );
  }

  public Integer getReward() {
    return reward;
  }

  public void setReward(int reward) {
    this.reward = reward;
  }

  public int getExperience() {
    return experience;
  }

  public void setExperience(int experience) {
    this.experience = experience;
  }
}
