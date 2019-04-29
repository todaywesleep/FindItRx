package pro.papaya.canyo.finditrx.utils;

import android.content.Context;
import android.content.res.Resources;
import android.util.TypedValue;

public final class PixelUtils {
  public static int dipToPx(Context context, int dip) {
    Resources r = context.getResources();
    float px = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        dip,
        r.getDisplayMetrics()
    );

    return (int) px;
  }
}
