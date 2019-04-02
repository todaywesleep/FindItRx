package pro.papaya.canyo.finditrx.view;

import android.animation.Animator;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import pro.papaya.canyo.finditrx.R;
import pro.papaya.canyo.finditrx.model.view.FabMenuAction;

public class FabItem extends LinearLayout implements View.OnClickListener {
  private final static float INVISIBLE_ALPHA = .0f;
  private final static float VISIBLE_ALPHA = 1f;
  private final static int Y_TRANSLATION = 0;

  @Override
  public void onClick(View v) {
    isEnabled = !isEnabled;
    setSelectionState(isEnabled);

    if (callback != null) {
      callback.onFabClick(this);
    }
  }

  public interface FabItemCallback {
    void onFabClick(FabItem item);
  }

  // View
  private View root;
  private FloatingActionButton fab;
  private TextView title;

  // Logic
  private boolean isSwitcher;
  private boolean isEnabled = false;
  private FabItemCallback callback;
  private FabMenuAction action;

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

  public FabMenuAction getAction() {
    return action;
  }

  public boolean isEnabled(){
    return isEnabled;
  }

  public void setIsEnabled(boolean isEnabled){
    this.isEnabled = isEnabled;
  }

  public void setSelectionState(boolean isEnabled) {
    int drawableResource = isEnabled
        ? R.drawable.fab_item_enabled_bg
        : R.drawable.fab_item_disabled_bg;
    int tintColor = isEnabled
        ? R.color.fabEnabledTintColor
        : R.color.fabDisabledTintColor;
    int textColor = isEnabled
        ? R.color.fabEnabledTitleColor
        : R.color.fabDisabledTitleColor;
    int backgroundColor = isEnabled
        ? R.color.fabEnabledNormalColor
        : R.color.fabDisabledNormalColor;

    title.setBackground(ContextCompat.getDrawable(getContext(), drawableResource));
    title.setTextColor(ContextCompat.getColor(getContext(), textColor));

    ColorStateList csl = AppCompatResources.getColorStateList(getContext(), tintColor);
    Drawable drawable = DrawableCompat.wrap(fab.getDrawable());
    DrawableCompat.setTintList(drawable, csl);

    fab.setBackgroundTintList(ColorStateList.valueOf(
        ContextCompat.getColor(
            getContext(), backgroundColor
        )
    ));
    fab.setImageDrawable(drawable);
  }

  public void setHeaderVisibility(boolean isVisible) {
    title.setVisibility(VISIBLE);
    title.setAlpha(isVisible ? INVISIBLE_ALPHA : VISIBLE_ALPHA);

    title.animate()
        .translationY(Y_TRANSLATION)
        .alpha(isVisible ? VISIBLE_ALPHA : INVISIBLE_ALPHA)
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
    root.setAlpha(isVisible ? INVISIBLE_ALPHA : VISIBLE_ALPHA);

    root.animate()
        .translationY(Y_TRANSLATION)
        .alpha(isVisible ? VISIBLE_ALPHA : INVISIBLE_ALPHA)
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

    fab.setOnClickListener(this);
    title.setOnClickListener(this);
  }

  private void unpackAttributes(AttributeSet attributeSet) {
    TypedArray typedArray = getContext().obtainStyledAttributes(attributeSet, R.styleable.FabItem);
    fab.setImageResource(typedArray.getResourceId(
        R.styleable.FabItem_android_src,
        R.drawable.ic_settings)
    );
    title.setText(typedArray.getText(R.styleable.FabItem_android_text));
    isSwitcher = typedArray.getBoolean(R.styleable.FabItem_switcher, false);
    action = FabMenuAction.from(
        typedArray.getInt(
            R.styleable.FabItem_action,
            FabMenuAction.ACTION_NOT_SPECIFIED.getAction()
        )
    );
    typedArray.recycle();
  }
}
