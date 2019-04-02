package pro.papaya.canyo.finditrx.model.firebase;

public class SettingsModel {
  private boolean isFlashEnabled;

  public SettingsModel(){}
  public SettingsModel(boolean isFlashEnabled){
    this.isFlashEnabled = isFlashEnabled;
  }

  public static SettingsModel getStabSettings(){
    return new SettingsModel(
        false
    );
  }

  public boolean isFlashEnabled() {
    return isFlashEnabled;
  }

  public void setFlashEnabled(boolean flashEnabled) {
    isFlashEnabled = flashEnabled;
  }
}
