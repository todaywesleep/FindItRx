package pro.papaya.canyo.finditrx.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.YuvImage;

import java.io.ByteArrayOutputStream;

import io.fotoapparat.preview.Frame;

public final class BitmapUtils {
  public static Bitmap getBitmapFromFrame(Frame frame) {
    byte[] data = frame.getImage();
    int width = frame.getSize().width;
    int height = frame.getSize().height;

    YuvImage yuvImage = new YuvImage(data, ImageFormat.NV21, width, height, null);
    ByteArrayOutputStream os = new ByteArrayOutputStream();
    yuvImage.compressToJpeg(new Rect(0, 0, width, height), 100, os);
    byte[] jpegByteArray = os.toByteArray();

    Bitmap readBitmap = BitmapFactory.decodeByteArray(jpegByteArray, 0, jpegByteArray.length);
    Matrix matrix = new Matrix();
    matrix.postRotate(90);
    readBitmap = Bitmap.createBitmap(readBitmap, 0, 0,
        readBitmap.getWidth(), readBitmap.getHeight(),
        matrix, false
    );

    return readBitmap;
  }

  public static Bitmap rotateBitmap(Bitmap src, int degrees){
    Matrix matrix = new Matrix();
    matrix.postRotate(degrees);

    src = Bitmap.createBitmap(src, 0, 0, src.getWidth(), src.getHeight(), matrix, false);
    return src;
  }
}
