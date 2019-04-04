package pro.papaya.canyo.finditrx.model.firebase;

public class UserQuestModel extends QuestModel {
  private int reward;

  public UserQuestModel() {
  }

  public UserQuestModel(String identifier, String label, int reward) {
    super(identifier, label);
    this.reward = reward;
  }

  public static UserQuestModel from(QuestModel model, int reward) {
    return new UserQuestModel(
        model.identifier,
        model.label,
        reward
    );
  }

  public int getReward() {
    return reward;
  }

  public void setReward(int reward) {
    this.reward = reward;
  }
}
