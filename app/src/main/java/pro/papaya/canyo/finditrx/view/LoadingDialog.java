package pro.papaya.canyo.finditrx.view;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import pro.papaya.canyo.finditrx.R;

public final class LoadingDialog {
  private Dialog dialog;

  public Dialog show(Context context) {
    return show(context, null);
  }

  public Dialog show(Context context, CharSequence title) {
    return show(context, title, false);
  }

  public Dialog show(Context context, CharSequence title, boolean cancelable) {
    LayoutInflater inflater = (LayoutInflater) context
        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    final View view = inflater.inflate(R.layout.view_progress_bar, null);
    if (title != null) {
      final TextView tv = view.findViewById(R.id.id_title);
      tv.setText(title);
    }

    dialog = new Dialog(context, R.style.LoadingDialog);
    dialog = new Dialog(context, R.style.LoadingDialog);
    dialog.setContentView(view);
    dialog.setCancelable(cancelable);
    dialog.show();

    return dialog;
  }

  public void dismiss(){
    dialog.dismiss();
  }

  public Dialog getDialog() {
    return dialog;
  }
}