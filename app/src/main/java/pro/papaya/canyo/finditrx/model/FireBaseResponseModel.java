package pro.papaya.canyo.finditrx.model;

import androidx.annotation.Nullable;

public class FireBaseResponseModel {
  private boolean isSuccessful;

  @Nullable
  private String message;

  public FireBaseResponseModel(boolean isSuccessful, @Nullable String message) {
    this.isSuccessful = isSuccessful;
    this.message = message;
  }

  public boolean isResponseSuccessful() {
    return isSuccessful;
  }

  public String getMessage() {
    return message;
  }
}
