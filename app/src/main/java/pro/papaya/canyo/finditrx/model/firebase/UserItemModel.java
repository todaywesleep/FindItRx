package pro.papaya.canyo.finditrx.model.firebase;

public class UserItemModel extends ItemModel {
  private int reward;

  public UserItemModel(){}
  public UserItemModel(String identifier, String label, int reward){
    super(identifier, label);
    this.reward = reward;
  }

  public int getReward() {
    return reward;
  }

  public void setReward(int reward) {
    this.reward = reward;
  }
}
