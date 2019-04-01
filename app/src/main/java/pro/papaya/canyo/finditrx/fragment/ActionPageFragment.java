package pro.papaya.canyo.finditrx.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.fotoapparat.Fotoapparat;
import io.fotoapparat.configuration.UpdateConfiguration;
import io.fotoapparat.parameter.ScaleType;
import io.fotoapparat.selector.FlashSelectorsKt;
import io.fotoapparat.selector.FocusModeSelectorsKt;
import io.fotoapparat.selector.LensPositionSelectorsKt;
import io.fotoapparat.selector.ResolutionSelectorsKt;
import io.fotoapparat.selector.SelectorsKt;
import io.fotoapparat.view.CameraView;
import pro.papaya.canyo.finditrx.R;
import pro.papaya.canyo.finditrx.model.view.FabMenuAction;
import pro.papaya.canyo.finditrx.view.FabItem;
import pro.papaya.canyo.finditrx.view.FabMenu;

public class ActionPageFragment extends Fragment implements FabMenu.FabMenuCallback {
  public static ActionPageFragment INSTANCE = null;

  public interface ActionPageCallback {
    void requestCameraPermissions();

    boolean isCameraPermissionsGranted();
  }

  @BindView(R.id.action_camera_view)
  CameraView cameraView;
  @BindView(R.id.action_menu)
  FabMenu menu;

  private Fotoapparat fotoapparat;
  private ActionPageCallback callback;

  public static ActionPageFragment getInstance(ActionPageCallback callback) {
    if (INSTANCE == null) {
      ActionPageFragment fragment = new ActionPageFragment();
      fragment.setCallback(callback);
      INSTANCE = fragment;
    } else if (callback != INSTANCE.callback) {
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
    ButterKnife.bind(this, view);
    menu.setCallback(this);
    initFotoapparat();
  }

  @Override
  public void onPause() {
    super.onPause();
    fotoapparat.stop();
  }

  @Override
  public void onResume() {
    super.onResume();
    fotoapparat.start();
  }

  @Override
  public void onFabItemClick(FabItem item) {
    if (item.getAction() == FabMenuAction.ACTION_AUTO_FLASH) {
      fotoapparat.updateConfiguration(
          UpdateConfiguration.builder()
              .flash(FlashSelectorsKt.off())
              .build()
      );
    }
  }

  public void setCallback(ActionPageCallback callback) {
    this.callback = callback;
  }

  public void refresh() {
    initFotoapparat();
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
//          .flash(SelectorsKt.firstAvailable(
//              FlashSelectorsKt.autoFlash(),
//          ))
//          .frameProcessor(myFrameProcessor)   // (optional) receives each frame from preview stream
          .build();

      fotoapparat.start();
    } else {
      callback.requestCameraPermissions();
    }
  }
}
