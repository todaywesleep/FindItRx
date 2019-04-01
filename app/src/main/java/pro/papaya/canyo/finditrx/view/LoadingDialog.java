package pro.papaya.canyo.finditrx.view;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import pro.papaya.canyo.finditrx.R;

public final class LoadingDialog {
  private Dialog dialog;

  public LoadingDialog(Context context){
    dialog = new Dialog(context, R.style.loadingDialog);
  }

  public void show(Context context, CharSequence title) {
    LayoutInflater inflater = (LayoutInflater) context
        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    final View view = inflater.inflate(R.layout.view_progress_bar, null);
    if (title != null) {
      final TextView tv = view.findViewById(R.id.id_title);
      tv.setText(title);
    }

    dialog.setContentView(view);
    dialog.setCancelable(false);
    dialog.show();
  }

  public void dismiss(){
    dialog.dismiss();
  }
}