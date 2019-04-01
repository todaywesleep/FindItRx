package pro.papaya.canyo.finditrx.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;

public abstract class BaseDialog extends AlertDialog {
  private Dialog dialog;

  protected BaseDialog(Context context) {
    super(context);
    init();
  }

  protected BaseDialog(Context context, int themeResId) {
    super(context, themeResId);
    init();
  }

  protected BaseDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
    super(context, cancelable, cancelListener);
    init();
  }

  protected abstract String getTitle();

  protected abstract String getMessage();

  protected abstract String getPositiveButtonMessage();

  protected abstract DialogInterface.OnClickListener getPositiveOnClick();

  private void init() {
    dialog = new AlertDialog.Builder(getContext())
        .setTitle(getTitle())
        .setMessage(getMessage())
        .setPositiveButton(
            getPositiveButtonMessage(),
            getPositiveOnClick())
        .create();
  }

  @Override
  public void show() {
    dialog.show();
  }

  @Override
  public void dismiss() {
    dialog.dismiss();
  }
}
