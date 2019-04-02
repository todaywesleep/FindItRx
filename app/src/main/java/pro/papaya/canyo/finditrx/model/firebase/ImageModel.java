package pro.papaya.canyo.finditrx.model.firebase;

import java.util.List;

public class ImageModel {
  private List<String> labels;

  public ImageModel() {
  }

  public ImageModel(List<String> labels) {
    this.labels = labels;
  }

  public List<String> getLabel() {
    return labels;
  }

  public void setLabel(List<String> label) {
    this.labels = labels;
  }
}
