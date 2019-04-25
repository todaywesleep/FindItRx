package pro.papaya.canyo.finditrx.controller;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.MotionEvent;
import android.view.View;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import pro.papaya.canyo.finditrx.R;
import pro.papaya.canyo.finditrx.utils.BitmapUtils;

import static androidx.recyclerview.widget.ItemTouchHelper.LEFT;

public class SwipeController extends ItemTouchHelper.Callback {
  private final static float RADIUS_FROM_WIDTH_PERCENTAGE =  0.15f;

  private boolean swipeBack = false;
  private Context context;
  private Paint controlPaint;
  private Paint backgroundPaint;

  public SwipeController(Context context) {
    this.context = context;
    setupControlPaint();
    setupBackgroundPaint();
  }

  private void setupControlPaint() {
    controlPaint = new Paint();
    controlPaint.setColor(ContextCompat.getColor(context, R.color.quest_card_control));
  }

  private void setupBackgroundPaint() {
    backgroundPaint = new Paint();
    backgroundPaint.setColor(ContextCompat.getColor(context, R.color.quest_card_background));
  }

  @Override
  public int getMovementFlags(@NotNull RecyclerView recyclerView,
                              @NotNull RecyclerView.ViewHolder viewHolder) {
    return makeMovementFlags(0, LEFT);
  }

  @Override
  public boolean onMove(@NotNull RecyclerView recyclerView,
                        @NotNull RecyclerView.ViewHolder viewHolder,
                        @NotNull RecyclerView.ViewHolder target) {
    return false;
  }

  @Override
  public void onSwiped(@NotNull RecyclerView.ViewHolder viewHolder, int direction) {

  }

  @Override
  public int convertToAbsoluteDirection(int flags, int layoutDirection) {
    if (swipeBack) {
      swipeBack = false;
      return 0;
    }

    return super.convertToAbsoluteDirection(flags, layoutDirection);
  }

  @Override
  public void onChildDraw(@NotNull Canvas c,
                          @NotNull RecyclerView recyclerView,
                          @NotNull RecyclerView.ViewHolder viewHolder,
                          float dX, float dY,
                          int actionState, boolean isCurrentlyActive) {

    Bitmap icon;
    if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {

      View itemView = viewHolder.itemView;
      float height = (float) itemView.getBottom() - (float) itemView.getTop();
      float width = height / 3;

      if (dX < 0) {
        RectF iconDest = new RectF(
            (float) itemView.getRight() - 2 * width,
            (float) itemView.getTop() + width,
            (float) itemView.getRight() - width,
            (float) itemView.getBottom() - width
        );
        float backgroundRadius = width - width * RADIUS_FROM_WIDTH_PERCENTAGE;
        float backgroundTop = itemView.getTop() + width * 1.5f;
        float backgroundLeft = itemView.getRight() - width * 1.5f;

        c.drawCircle(backgroundLeft, backgroundTop, backgroundRadius, backgroundPaint);
        icon = BitmapUtils.getBitmapFromVectorDrawable(context, R.drawable.ic_repeat);
        c.drawBitmap(icon, null, iconDest, controlPaint);
      }
    }
    super.onChildDraw(c, recyclerView, viewHolder, dX / 5, dY, actionState, isCurrentlyActive);
  }

  @SuppressLint("ClickableViewAccessibility")
  private void setTouchListener(RecyclerView recyclerView) {
    recyclerView.setOnTouchListener((v, event) -> {
      swipeBack = event.getAction() == MotionEvent.ACTION_CANCEL || event.getAction() == MotionEvent.ACTION_UP;
      return false;
    });
  }
}
