package pro.papaya.canyo.finditrx.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import pro.papaya.canyo.finditrx.R;

public class FabMenu extends LinearLayout implements FabItem.FabItemCallback {
  public interface FabMenuCallback {
    void onFabItemClick(FabItem item);
  }

  private boolean isMenuVisible = false;
  private LinearLayout root;
  private List<FabItem> items = new ArrayList<>();
  private FabMenuCallback callback;

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

  public void setCallback(FabMenuCallback callback) {
    this.callback = callback;
  }

  private void init() {
    root = LayoutInflater.from(getContext()).inflate(R.layout.view_floating_menu, this)
        .findViewById(R.id.fab_menu_root);

    setItemListeners();
    setItemsVisibility(false);
  }

  private void setItemListeners() {
    for (int i = 0; i < root.getChildCount(); i++) {
      View child = root.getChildAt(i);
      if (child instanceof FabItem) {
        FabItem currentFab = (FabItem) child;
        currentFab.setCallback(this);
      }
    }
  }

  private void setItemsVisibility(boolean isVisible) {
    for (int i = 0; i < root.getChildCount(); i++) {
      View child = root.getChildAt(i);
      if (child instanceof FabItem) {
        FabItem currentFab = (FabItem) child;

        if (!currentFab.isSwitcher()) {
          currentFab.setItemVisibility(isVisible);
        } else {
          currentFab.setHeaderVisibility(isVisible);
        }
      }
    }
  }

  @Override
  public void onFabClick(FabItem item) {
    if (item.isSwitcher()) {
      isMenuVisible = !isMenuVisible;
      setItemsVisibility(isMenuVisible);
    }

    if (callback != null) {
      callback.onFabItemClick(item);
    }
  }
}
