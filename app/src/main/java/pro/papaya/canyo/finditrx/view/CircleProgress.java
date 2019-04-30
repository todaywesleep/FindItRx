package pro.papaya.canyo.finditrx.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import pro.papaya.canyo.finditrx.R;
import pro.papaya.canyo.finditrx.utils.Constants;
import timber.log.Timber;

public class CircleProgress extends View {
  private static final String LEVEL_PREFIX = "lvl ";
  private static final String EXP_POSTFIX = " exp";

  private static final int VIEW_RECTANGLE_SIDE_MIN = 100;
  private static final int VIEW_RECTANGLE_SIDE_MAX = 2000;

  private static final float FULL_CIRCLE_ANGLE_STOCK = 360f;
  private static final float ZERO_ANGLE = -90f;
  private static final float ALPHA_ROOT = 0.99f;
  private static final float VIEW_RECTANGLE_SIDE = 400f;
  private static final float LINE_WIDTH_K = .2f;
  private static final float TEXT_SIZE = 36f;

  //Size section
  private float viewRectangleSide;
  private float circleRadius;
  private float innerCircleRadius;
  private PointF circleCenter;
  private RectF circleRect;

  //Paint section
  private Paint outerCirclePaint;
  private Paint innerCirclePaint;
  private Paint levelPaint;
  private Paint experiencePaint;

  //Data section
  private int userLevel = 0;
  private int userExp = 0;
  private int userTotalExp = 0;

  public CircleProgress(Context context) {
    super(context);
    init();
  }

  public CircleProgress(Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);
    init();
  }

  public CircleProgress(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init();
  }

  public CircleProgress(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
    super(context, attrs, defStyleAttr, defStyleRes);
    init();
  }

  public void setData(int userLevel, int userExp, int userTotalExp) {
    this.userLevel = userLevel;
    this.userExp = userExp;
    this.userTotalExp = userTotalExp;

    invalidate();
  }

  private void init() {
    setAlpha(ALPHA_ROOT);

    circleRadius = viewRectangleSide / 2f;
    innerCircleRadius = circleRadius - circleRadius * LINE_WIDTH_K;
    circleCenter = new PointF(
        viewRectangleSide / 2f,
        viewRectangleSide / 2f
    );
    circleRect = new RectF(0, 0, viewRectangleSide, viewRectangleSide);

    initPaint();
  }

  private void initPaint() {
    int greenColor = ContextCompat.getColor(getContext(), R.color.profileTotalExperience);
    int textGreyColor = ContextCompat.getColor(getContext(), R.color.profileText);

    outerCirclePaint = new Paint();
    outerCirclePaint.setColor(greenColor);

    innerCirclePaint = new Paint();
    innerCirclePaint.setAlpha(0xFF);
    innerCirclePaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));

    levelPaint = new Paint();
    levelPaint.setColor(textGreyColor);
    levelPaint.setTextSize(TEXT_SIZE);

    experiencePaint = new Paint();
    experiencePaint.setColor(greenColor);
    experiencePaint.setTextSize(TEXT_SIZE);
  }

  @Override
  protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    int widthMode = MeasureSpec.getMode(widthMeasureSpec);
    int widthSize = MeasureSpec.getSize(widthMeasureSpec);
    int heightMode = MeasureSpec.getMode(heightMeasureSpec);
    int heightSize = MeasureSpec.getSize(heightMeasureSpec);

    int width;
    int height;

    //Measure Width
    if (widthMode == MeasureSpec.EXACTLY) {
      //Must be this size
      width = widthSize;
    } else if (widthMode == MeasureSpec.AT_MOST) {
      //Can't be bigger than...
      width = Math.min(VIEW_RECTANGLE_SIDE_MAX, widthSize);
    } else {
      //Be whatever you want
      width = VIEW_RECTANGLE_SIDE_MIN;
    }

    //Measure Height
    if (heightMode == MeasureSpec.EXACTLY) {
      //Must be this size
      height = heightSize;
    } else if (heightMode == MeasureSpec.AT_MOST) {
      //Can't be bigger than...
      height = Math.min(VIEW_RECTANGLE_SIDE_MAX, heightSize);
    } else {
      //Be whatever you want
      height = VIEW_RECTANGLE_SIDE_MIN;
    }

    viewRectangleSide = Math.min(width, height);

    //MUST CALL THIS
    setMeasuredDimension((int) viewRectangleSide, (int) viewRectangleSide);
    init();
  }

  @Override
  protected void onDraw(Canvas canvas) {
    super.onDraw(canvas);
    if (canvas != null) {
      canvas.save();

      drawCircles(canvas);
      drawText(canvas);

      canvas.restore();
    }
  }

  private float getRestAngle() {
    float singleAnglePart = userTotalExp / FULL_CIRCLE_ANGLE_STOCK;
    return ZERO_ANGLE + singleAnglePart * userExp;
  }

  private void drawCircles(@NonNull Canvas canvas) {
    // Outer circle
    canvas.drawArc(
        circleRect,
        ZERO_ANGLE,
        getRestAngle(),
        true,
        outerCirclePaint
    );

    // Inner circle
    canvas.drawCircle(
        circleCenter.x,
        circleCenter.y,
        innerCircleRadius,
        innerCirclePaint
    );
  }

  private void drawText(@NonNull Canvas canvas) {
    String levelText = LEVEL_PREFIX.concat(Integer.toString(userLevel));
    String expText =
        Integer.toString(userExp)
            .concat(Constants.SPACE)
            .concat(Constants.SLASH)
            .concat(Constants.SPACE)
            .concat(Integer.toString(userTotalExp))
            .concat(EXP_POSTFIX);

    final float levelTextSize = levelPaint.measureText(levelText);
    final float expTextSize = levelPaint.measureText(expText);
    final float verticalTextOffset = TEXT_SIZE;

    canvas.drawText(levelText,
        circleCenter.x - levelTextSize / 2f,
        circleCenter.y - verticalTextOffset,
        levelPaint
    );
    canvas.drawText(expText,
        circleCenter.x - expTextSize / 2f,
        circleCenter.y + verticalTextOffset,
        experiencePaint
    );
  }
}
