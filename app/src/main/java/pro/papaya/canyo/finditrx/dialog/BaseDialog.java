package pro.papaya.canyo.finditrx.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.Button;
import android.widget.LinearLayout;

public abstract class BaseDialog extends AlertDialog {
  protected AlertDialog.Builder dialogBuilder;
  protected Dialog dialog;
  private boolean hasNegative = false;

  protected BaseDialog(Context context) {
    super(context);
    init();
  }

  protected BaseDialog(Context context, boolean isInitRequired) {
    super(context);
    if (isInitRequired) {
      init();
    }
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

  protected void fillDialog() {
  }

  protected void setupNegativeButton(String negativeLabel, OnClickListener onClickListener) {
    hasNegative = true;
    dialogBuilder.setNegativeButton(
        negativeLabel,
        onClickListener
    );
  }

  protected Dialog getDialog() {
    return dialog;
  }

  protected void init() {
    dialogBuilder = new AlertDialog.Builder(getContext())
        .setTitle(getTitle())
        .setMessage(getMessage())
        .setPositiveButton(
            getPositiveButtonMessage(),
            getPositiveOnClick());
    fillDialog();
    createDialog();
  }

  private void createDialog() {
    dialog = dialogBuilder.create();
    if (hasNegative) {
      dialog.setOnShowListener(dialog -> {
        Button negButton = ((AlertDialog) dialog).getButton(DialogInterface.BUTTON_NEGATIVE);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(0, 0, 20, 0);
        negButton.setLayoutParams(params);
      });
    }
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
