package pro.papaya.canyo.finditrx.model.firebase;

public class NotificationModel {
  private String creator;
  private String dateTime;
  private String content;

  public NotificationModel(){}

  public NotificationModel(String creator, String dateTime, String content) {
    this.creator = creator;
    this.dateTime = dateTime;
    this.content = content;
  }

  public String getCreator() {
    return creator;
  }

  public String getDateTime() {
    return dateTime;
  }

  public String getContent() {
    return content;
  }
}
