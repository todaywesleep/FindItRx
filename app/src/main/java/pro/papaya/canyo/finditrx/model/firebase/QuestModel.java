package pro.papaya.canyo.finditrx.model.firebase;

import com.google.firebase.ml.vision.label.FirebaseVisionImageLabel;

import java.util.ArrayList;
import java.util.List;

public class QuestModel {
  protected String label;
  protected String identifier;

  public static List<QuestModel> fromFirebaseCollection(List<FirebaseVisionImageLabel> firebaseCollection) {
    List<QuestModel> items = new ArrayList<>();

    for (FirebaseVisionImageLabel label : firebaseCollection) {
      if (label.getText() != null) {
        items.add(new QuestModel(
            label.getText().toLowerCase(),
            label.getText()
        ));
      }
    }

    return items;
  }

  public static List<QuestModel> fromUserCollection(List<UserQuestModel> userQuestModels) {
    List<QuestModel> items = new ArrayList<>();

    for (UserQuestModel questModel : userQuestModels) {
      items.add(QuestModel.from(questModel));
    }

    return items;
  }

  public static QuestModel from(UserQuestModel userQuestModel) {
    return new QuestModel(
        userQuestModel.identifier,
        userQuestModel.label
    );
  }

  public QuestModel() {
  }

  public QuestModel(String identifier, String label) {
    this.identifier = identifier;
    this.label = label;
  }

  public String getLabel() {
    return label;
  }

  public void setLabel(String label) {
    this.label = label;
  }

  public String getIdentifier() {
    return identifier;
  }

  public void setIdentifier(String identifier) {
    this.identifier = identifier;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    QuestModel that = (QuestModel) o;

    if (label != null ? !label.equals(that.label) : that.label != null) return false;
    return identifier != null ? identifier.equals(that.identifier) : that.identifier == null;
  }

  @Override
  public int hashCode() {
    int result = label != null ? label.hashCode() : 0;
    result = 31 * result + (identifier != null ? identifier.hashCode() : 0);
    return result;
  }
}
