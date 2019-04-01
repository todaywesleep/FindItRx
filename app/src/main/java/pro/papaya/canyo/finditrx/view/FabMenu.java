package pro.papaya.canyo.finditrx.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import pro.papaya.canyo.finditrx.R;

public class FabMenu extends LinearLayout {
  private View root;

  public FabMenu(Context context) {
    super(context);
    init();
  }

  public FabMenu(Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);
    init();
  }

  public FabMenu(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init();
  }

  public FabMenu(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
    super(context, attrs, defStyleAttr, defStyleRes);
    init();
  }

  private void init() {
    root = LayoutInflater.from(getContext()).inflate(R.layout.view_floating_menu, this);
  }
}
