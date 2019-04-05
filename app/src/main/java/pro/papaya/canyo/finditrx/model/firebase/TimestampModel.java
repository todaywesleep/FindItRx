package pro.papaya.canyo.finditrx.model.firebase;

public class TimestampModel {
  Long lastRequestedQuestTime;

  public TimestampModel(){}
  public TimestampModel(Long lastRequestedQuestTime){
    this.lastRequestedQuestTime = lastRequestedQuestTime;
  }

  public Long getLastRequestedQuestTime() {
    return lastRequestedQuestTime;
  }

  public void setLastRequestedQuestTime(Long lastRequestedQuestTime) {
    this.lastRequestedQuestTime = lastRequestedQuestTime;
  }
}
