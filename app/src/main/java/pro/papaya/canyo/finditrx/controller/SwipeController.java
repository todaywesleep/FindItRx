package pro.papaya.canyo.finditrx.controller;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

import pro.papaya.canyo.finditrx.R;
import pro.papaya.canyo.finditrx.utils.BitmapUtils;
import timber.log.Timber;

import static androidx.recyclerview.widget.ItemTouchHelper.LEFT;

public class SwipeController extends ItemTouchHelper.Callback {
  public interface ChangeButtonClick {
    void onItemClick(int position);
  }

  private GestureDetector.SimpleOnGestureListener gestureListener = new GestureDetector.SimpleOnGestureListener() {
    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {
      for (Map.Entry<Integer, ChangeButton> button : buttons.entrySet()) {
        if (button.getValue().onClick(e.getX(), e.getY())) {
          break;
        }
      }

      return true;
    }
  };

  private final static float RADIUS_FROM_WIDTH_PERCENTAGE = 0.15f;
  private Map<Integer, ChangeButton> buttons = new HashMap<>();
  private GestureDetector gestureDetector;
  private RecyclerView recyclerView;

  private boolean swipeBack = false;
  private int swipedPos = -1;
  private Context context;
  private Paint controlPaint;
  private Paint backgroundPaint;

  @SuppressLint("ClickableViewAccessibility")
  public SwipeController(Context context, RecyclerView recyclerView) {
    this.context = context;
    this.recyclerView = recyclerView;
    setupControlPaint();
    setupBackgroundPaint();
    gestureDetector = new GestureDetector(context, gestureListener);
    recyclerView.setOnTouchListener((view, e) -> {
      Point point = new Point((int) e.getRawX(), (int) e.getRawY());
      RecyclerView.ViewHolder swipedViewHolder = recyclerView.findViewHolderForAdapterPosition(swipedPos);

      if (swipedViewHolder != null) {
        View swipedItem = swipedViewHolder.itemView;
        Rect rect = new Rect();
        swipedItem.getGlobalVisibleRect(rect);

        if (e.getAction() == MotionEvent.ACTION_DOWN || e.getAction() == MotionEvent.ACTION_UP || e.getAction() == MotionEvent.ACTION_MOVE) {
          if (rect.top < point.y && rect.bottom > point.y)
            gestureDetector.onTouchEvent(e);
        }
        return false;
      }

      return false;
    });
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
    swipedPos = viewHolder.getAdapterPosition();
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
    int position = viewHolder.getAdapterPosition();

    if (position < 0) {
      swipedPos = position;
      return;
    }

    if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
      View itemView = viewHolder.itemView;
      Bitmap icon = BitmapUtils.getBitmapFromVectorDrawable(context, R.drawable.ic_repeat);
      float width = ((float) itemView.getBottom() - (float) itemView.getTop()) / 3;
      float backgroundRadius = width - width * RADIUS_FROM_WIDTH_PERCENTAGE;
      RectF iconDest = new RectF(
          (float) itemView.getRight() - 2 * width,
          (float) itemView.getTop() + width,
          (float) itemView.getRight() - width,
          (float) itemView.getBottom() - width
      );
      PointF backgroundLocation = new PointF(
          itemView.getRight() - width * 1.5f,
          itemView.getTop() + width * 1.5f
      );

      if (dX < 0) {
        if (!buttons.containsKey(position)) {
          buttons.put(position, new ChangeButton(position,
              icon,
              iconDest,
              backgroundPaint,
              pos -> Timber.d("TEST %s item clicked", pos)));
        }

        drawButtons(c, backgroundRadius, iconDest, backgroundLocation);
      }
    }

    super.onChildDraw(c, recyclerView, viewHolder, dX / 4, dY, actionState, isCurrentlyActive);
  }

  public void drawButtons(Canvas c, float backgroundRadius, RectF iconDest, PointF backgroundLocation) {
    for (Map.Entry<Integer, ChangeButton> button : buttons.entrySet()) {
      button.getValue().onDraw(c, backgroundRadius, iconDest, controlPaint, backgroundLocation);
    }
  }

  public static class ChangeButton {
    private int position;
    private Bitmap image;
    private RectF backgroundRect;
    private Paint backgroundPaint;
    private ChangeButtonClick callback;

    public ChangeButton(int position,
                        Bitmap image,
                        RectF backgroundRect,
                        Paint backgroundPaint,
                        ChangeButtonClick callback) {
      this.position = position;
      this.image = image;
      this.backgroundRect = backgroundRect;
      this.backgroundPaint = backgroundPaint;
      this.callback = callback;
    }

    public boolean onClick(float x, float y) {
      if (backgroundRect != null && backgroundRect.contains(x, y)) {
        callback.onItemClick(position);
        return true;
      }

      return false;
    }

    public void onDraw(Canvas c, float backgroundRadius, RectF iconDestination, Paint controlPaint, PointF backgroundLocation) {
      c.drawCircle(backgroundLocation.x, backgroundLocation.y, backgroundRadius, backgroundPaint);
      c.drawBitmap(image, null, iconDestination, controlPaint);
    }
  }
}
