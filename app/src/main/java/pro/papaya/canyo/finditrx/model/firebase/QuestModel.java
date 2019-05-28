package pro.papaya.canyo.finditrx.model.firebase;

import androidx.annotation.NonNull;

import com.google.firebase.ml.vision.label.FirebaseVisionImageLabel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class QuestModel {
  private String label;
  private String identifier;

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
        userQuestModel.getIdentifier(),
        userQuestModel.getLabel()
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

  @NonNull
  @Override
  public String toString() {
    return (label == null ? "NONE LABEL" : label) +
        " with " +
        (identifier == null ? "NULL IDENTIFIER" : identifier);
  }

  @Override
  public boolean equals(Object o) {
    return identifier.equals(((QuestModel) o).identifier);
  }

  @Override
  public int hashCode() {
    int result = label != null ? label.hashCode() : 0;
    result = 31 * result + (identifier != null ? identifier.hashCode() : 0);
    return result;
  }
}
