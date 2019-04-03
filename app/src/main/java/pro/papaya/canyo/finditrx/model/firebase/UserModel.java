package pro.papaya.canyo.finditrx.model.firebase;

public class UserModel {
  private String email;
  private String nickName;
  private String id;
  private Long questTimestamp;

  public UserModel() {
  }

  public UserModel(String email, String nickName, String id, Long questTimestamp) {
    this.email = email;
    this.nickName = nickName;
    this.id = id;
    this.questTimestamp = questTimestamp;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getNickName() {
    return nickName;
  }

  public void setNickName(String nickName) {
    this.nickName = nickName;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public Long getQuestTimestamp() {
    return questTimestamp;
  }

  public void setQuestTimestamp(Long questTimestamp) {
    this.questTimestamp = questTimestamp;
  }
}
