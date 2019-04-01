package pro.papaya.canyo.finditrx.view;

import android.animation.Animator;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.Nullable;
import pro.papaya.canyo.finditrx.R;

public class FabItem extends LinearLayout {
  public interface FabItemCallback {
    void onFabClick(FabItem item);
  }

  private View root;
  private FloatingActionButton fab;
  private TextView title;
  private boolean isSwitcher;
  private FabItemCallback callback;

  public FabItem(Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);
    init(attrs);
  }

  public FabItem(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init(attrs);
  }

  public boolean isSwitcher() {
    return isSwitcher;
  }

  public void setHeaderVisibility(boolean isVisible) {
    title.setVisibility(VISIBLE);
    title.setAlpha(isVisible ? .0f : 1f);

    title.animate()
        .translationY(0)
        .alpha(isVisible ? 1f : .0f)
        .setListener(new Animator.AnimatorListener() {
          @Override
          public void onAnimationStart(Animator animation) {
          }

          @Override
          public void onAnimationEnd(Animator animation) {
            title.setVisibility(isVisible ? VISIBLE : GONE);
          }

          @Override
          public void onAnimationCancel(Animator animation) {
          }

          @Override
          public void onAnimationRepeat(Animator animation) {
          }
        })
        .start();
  }

  public void setItemVisibility(boolean isVisible) {
    root.setVisibility(VISIBLE);
    root.setAlpha(isVisible ? .0f : 1f);

    root.animate()
        .translationY(0)
        .alpha(isVisible ? 1f : .0f)
        .setListener(new Animator.AnimatorListener() {
          @Override
          public void onAnimationStart(Animator animation) {
          }

          @Override
          public void onAnimationEnd(Animator animation) {
            root.setVisibility(isVisible ? VISIBLE : GONE);
          }

          @Override
          public void onAnimationCancel(Animator animation) {
          }

          @Override
          public void onAnimationRepeat(Animator animation) {
          }
        })
        .start();
  }

  public void setCallback(FabItemCallback callback) {
    this.callback = callback;
  }

  private void init(@Nullable AttributeSet attributeSet) {
    root = LayoutInflater.from(getContext()).inflate(R.layout.view_floating_item, this);

    fab = root.findViewById(R.id.floating_item_btn);
    title = root.findViewById(R.id.floating_item_title);

    if (attributeSet != null) {
      unpackAttributes(attributeSet);
    }

    fab.setOnClickListener(view -> {
      if (callback != null) {
        callback.onFabClick(this);
      }
    });
    title.setOnClickListener(view -> {
      if (callback != null) {
        callback.onFabClick(this);
      }
    });
  }

  private void unpackAttributes(AttributeSet attributeSet) {
    TypedArray typedArray = getContext().obtainStyledAttributes(attributeSet, R.styleable.FabItem);
    fab.setImageResource(typedArray.getResourceId(
        R.styleable.FabItem_android_src,
        R.drawable.ic_settings)
    );
    title.setText(typedArray.getText(R.styleable.FabItem_android_text));
    isSwitcher = typedArray.getBoolean(R.styleable.FabItem_switcher, false);
    typedArray.recycle();
  }
}
