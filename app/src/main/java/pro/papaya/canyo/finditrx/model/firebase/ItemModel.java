package pro.papaya.canyo.finditrx.model.firebase;

import com.google.firebase.ml.vision.label.FirebaseVisionImageLabel;

import java.util.ArrayList;
import java.util.List;

public class ItemModel {
  private String label;
  private String identifier;

  public static List<ItemModel> fromCollection(List<FirebaseVisionImageLabel> firebaseCollection) {
    List<ItemModel> items = new ArrayList<>();

    for (FirebaseVisionImageLabel label : firebaseCollection) {
      if (label.getText() != null) {
        items.add(new ItemModel(
            label.getText().toLowerCase(),
            label.getText()
        ));
      }
    }

    return items;
  }

  public ItemModel() {
  }

  public ItemModel(String identifier, String label) {
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
