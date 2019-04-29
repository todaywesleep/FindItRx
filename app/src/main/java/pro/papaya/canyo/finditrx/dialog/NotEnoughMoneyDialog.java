package pro.papaya.canyo.finditrx.dialog;

import android.content.Context;

import pro.papaya.canyo.finditrx.R;

public class NotEnoughMoneyDialog extends BaseDialog {
  public NotEnoughMoneyDialog(Context context) {
    super(context);
  }

  @Override
  protected String getTitle() {
    return getContext().getString(R.string.not_enough_coins_title);
  }

  @Override
  protected String getMessage() {
    return getContext().getString(R.string.not_enough_coins_message);
  }

  @Override
  protected String getPositiveButtonMessage() {
    return getContext().getString(R.string.not_enough_coins_positive);
  }

  @Override
  protected OnClickListener getPositiveOnClick() {
    return (dialog, which) -> dialog.dismiss();
  }
}
