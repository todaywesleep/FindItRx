package pro.papaya.canyo.finditrx.model.firebase;

import com.google.firebase.ml.vision.label.FirebaseVisionImageLabel;

import java.util.ArrayList;
import java.util.List;

public class QuestModel {
  protected String label;
  protected String identifier;

  public static List<QuestModel> fromCollection(List<FirebaseVisionImageLabel> firebaseCollection) {
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
}
