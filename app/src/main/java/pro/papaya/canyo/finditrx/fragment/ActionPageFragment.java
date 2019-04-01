package pro.papaya.canyo.finditrx.fragment;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.PagerAdapter;
import io.fotoapparat.Fotoapparat;
import io.fotoapparat.parameter.ScaleType;
import io.fotoapparat.selector.FocusModeSelectorsKt;
import io.fotoapparat.selector.LensPositionSelectorsKt;
import io.fotoapparat.selector.ResolutionSelectorsKt;
import io.fotoapparat.selector.SelectorsKt;
import io.fotoapparat.view.CameraView;
import pro.papaya.canyo.finditrx.R;
import timber.log.Timber;

public class ActionPageFragment extends Fragment {
  public static ActionPageFragment INSTANCE = null;

  public interface ActionPageCallback {
    void requestCameraPermissions();

    boolean isCameraPermissionsGranted();
  }

  private CameraView cameraView;
  private Fotoapparat fotoapparat;
  private ActionPageCallback callback;

  public static ActionPageFragment getInstance(ActionPageCallback callback) {
    if (INSTANCE == null) {
      ActionPageFragment fragment = new ActionPageFragment();
      fragment.setCallback(callback);
      INSTANCE = fragment;
    }else if (callback != INSTANCE.callback) {
      INSTANCE.setCallback(callback);
    }

    return INSTANCE;
  }

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater,
                           @Nullable ViewGroup container,
                           @Nullable Bundle savedInstanceState) {

    View view = inflater.inflate(R.layout.main_fragment_action, container, false);
    return view;
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    cameraView = view.findViewById(R.id.camera_view);
    initFotoapparat();
  }

  public void setCallback(ActionPageCallback callback) {
    this.callback = callback;
  }

  private void initFotoapparat() {
    if (getContext() != null && callback.isCameraPermissionsGranted()) {
      fotoapparat = Fotoapparat
          .with(getContext())
          .into(cameraView)
          .previewScaleType(ScaleType.CenterCrop)
          .photoResolution(ResolutionSelectorsKt.highestResolution())
          .lensPosition(LensPositionSelectorsKt.back())
          .focusMode(SelectorsKt.firstAvailable(
              FocusModeSelectorsKt.continuousFocusPicture(),
              FocusModeSelectorsKt.autoFocus(),
              FocusModeSelectorsKt.fixed()
          ))
          .flash(SelectorsKt.firstAvailable(
//              FlashSelectorsKt.autoFlash(),
          ))
//          .frameProcessor(myFrameProcessor)   // (optional) receives each frame from preview stream
          .build();

      fotoapparat.start();
    } else {
      callback.requestCameraPermissions();
    }
  }

  public void refresh(){
    initFotoapparat();
  }
}
