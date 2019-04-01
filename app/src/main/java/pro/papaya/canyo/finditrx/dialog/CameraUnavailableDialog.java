package pro.papaya.canyo.finditrx.dialog;

import android.content.Context;

import pro.papaya.canyo.finditrx.R;

public class CameraUnavailableDialog extends BaseDialog {
  public CameraUnavailableDialog(Context context) {
    super(context);
  }

  @Override
  protected String getTitle() {
    return getContext().getString(R.string.camera_dialog_title);
  }

  @Override
  protected String getMessage() {
    return getContext().getString(R.string.camera_dialog_message);
  }

  @Override
  protected String getPositiveButtonMessage() {
    return getContext().getString(R.string.camera_dialog_positive_btn);
  }

  @Override
  protected OnClickListener getPositiveOnClick() {
    return (dialog, which) -> dismiss();
  }
}
