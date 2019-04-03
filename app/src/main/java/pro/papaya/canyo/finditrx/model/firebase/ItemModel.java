package pro.papaya.canyo.finditrx.model.firebase;

public class ItemModel {
  private String label;
  private int id;

  public ItemModel() {
  }

  public ItemModel(int id, String label) {
    this.id = id;
    this.label = label;
  }

  public String getLabel() {
    return label;
  }

  public void setLabel(String label) {
    this.label = label;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }
}
