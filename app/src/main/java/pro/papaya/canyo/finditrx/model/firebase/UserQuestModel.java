package pro.papaya.canyo.finditrx.model.firebase;

public class UserQuestModel extends QuestModel {
  private Integer reward;

  public UserQuestModel() {
  }

  public UserQuestModel(String identifier, String label, Integer reward) {
    super(identifier, label);
    this.reward = reward;
  }

  public static UserQuestModel from(QuestModel model, Integer reward) {
    return new UserQuestModel(
        model.identifier,
        model.label,
        reward
    );
  }

  public Integer getReward() {
    return reward;
  }

  public void setReward(int reward) {
    this.reward = reward;
  }
}
