package pro.papaya.canyo.finditrx.model.firebase;

import android.graphics.Bitmap;

import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.label.FirebaseVisionImageLabel;
import com.google.firebase.ml.vision.label.FirebaseVisionImageLabeler;

import java.util.List;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import pro.papaya.canyo.finditrx.utils.BitmapUtils;

public class FireBaseLabeler {
  private static FireBaseLabeler INSTANCE;

  public static FireBaseLabeler getInstance() {
    if (INSTANCE == null) {
      INSTANCE = new FireBaseLabeler();
    }

    return INSTANCE;
  }

  public Single<List<FirebaseVisionImageLabel>> postTask(Bitmap bitmap, int rotationDegrees) {
    return new Single<List<FirebaseVisionImageLabel>>() {
      @Override
      protected void subscribeActual(SingleObserver<? super List<FirebaseVisionImageLabel>> observer) {
        // Rotate bitmap before send to firebase
        Bitmap preparedBitmap = BitmapUtils.rotateBitmap(bitmap, Math.abs(360 - rotationDegrees));
        FirebaseVisionImage firebaseImage = FirebaseVisionImage.fromBitmap(
            preparedBitmap
        );
        FirebaseVisionImageLabeler labeler = FirebaseVision.getInstance()
            .getCloudImageLabeler();
        labeler.processImage(firebaseImage)
            .addOnSuccessListener(observer::onSuccess)
            .addOnFailureListener(observer::onError);
      }
    };
  }

  private FireBaseLabeler() {
  }
}
