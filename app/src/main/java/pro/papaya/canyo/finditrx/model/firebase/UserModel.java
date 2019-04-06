package pro.papaya.canyo.finditrx.model.firebase;

public class UserModel {
  private String email;
  private String nickName;
  private String id;
  private long balance = 0;
  private int level = 0;
  private long experience = 0;

  public UserModel() {
    super();
  }

  public UserModel(String email, String nickName, String id, int level, long experience) {
    this.email = email;
    this.nickName = nickName;
    this.id = id;
    this.balance = 0;
    this.level = 0;
    this.experience = 0;
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

  public long getBalance() {
    return balance;
  }

  public void setBalance(long balance) {
    this.balance = balance;
  }

  public int getLevel() {
    return level;
  }

  public void setLevel(int level) {
    this.level = level;
  }

  public long getExperience() {
    return experience;
  }

  public void setExperience(long experience) {
    this.experience = experience;
  }
}
