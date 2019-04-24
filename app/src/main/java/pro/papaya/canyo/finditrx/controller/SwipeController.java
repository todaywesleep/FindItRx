package pro.papaya.canyo.finditrx.controller;

import android.annotation.SuppressLint;
import android.graphics.Canvas;
import android.view.MotionEvent;

import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import static androidx.recyclerview.widget.ItemTouchHelper.ACTION_STATE_SWIPE;
import static androidx.recyclerview.widget.ItemTouchHelper.LEFT;
import static androidx.recyclerview.widget.ItemTouchHelper.RIGHT;

public class SwipeController extends ItemTouchHelper.Callback {
  private boolean swipeBack = false;

  @Override
  public int getMovementFlags(@NotNull RecyclerView recyclerView,
                              @NotNull RecyclerView.ViewHolder viewHolder) {
    return makeMovementFlags(0, LEFT | RIGHT);
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

    if (actionState == ACTION_STATE_SWIPE) {
      setTouchListener(recyclerView);
    }
    super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
  }

  @SuppressLint("ClickableViewAccessibility")
  private void setTouchListener(RecyclerView recyclerView) {
    recyclerView.setOnTouchListener((v, event) -> {
      swipeBack = event.getAction() == MotionEvent.ACTION_CANCEL || event.getAction() == MotionEvent.ACTION_UP;
      return false;
    });
  }
}
