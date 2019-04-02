package pro.papaya.canyo.finditrx.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.fotoapparat.Fotoapparat;
import io.fotoapparat.configuration.UpdateConfiguration;
import io.fotoapparat.parameter.Flash;
import io.fotoapparat.parameter.ScaleType;
import io.fotoapparat.selector.FlashSelectorsKt;
import io.fotoapparat.selector.FocusModeSelectorsKt;
import io.fotoapparat.selector.LensPositionSelectorsKt;
import io.fotoapparat.selector.ResolutionSelectorsKt;
import io.fotoapparat.selector.SelectorsKt;
import io.fotoapparat.view.CameraView;
import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;
import kotlin.jvm.functions.Function1;
import pro.papaya.canyo.finditrx.R;
import pro.papaya.canyo.finditrx.firebase.FireBaseDataBaseHelper;
import pro.papaya.canyo.finditrx.model.firebase.SettingsModel;
import pro.papaya.canyo.finditrx.model.view.FabMenuAction;
import pro.papaya.canyo.finditrx.view.FabItem;
import pro.papaya.canyo.finditrx.view.FabMenu;
import pro.papaya.canyo.finditrx.viewmodel.ActionViewModel;

public class ActionPageFragment extends BaseFragment implements FabMenu.FabMenuCallback {
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
  private ActionViewModel actionViewModel;
  private SettingsModel settingsModel = SettingsModel.getStabSettings();

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
    actionViewModel = ViewModelProviders.of(this).get(ActionViewModel.class);
    menu.setCallback(this);
    initFotoapparat();
    subscribeToViewModel();
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
      setFlashState(item.isEnabled(), true);
    }
  }

  public void setCallback(ActionPageCallback callback) {
    this.callback = callback;
  }

  public void refresh() {
    initFotoapparat();
  }

  private void subscribeToViewModel() {
    getSettings();
  }

  private void getSettings() {
    actionViewModel.getSettings().subscribe(new SingleObserver<SettingsModel>() {
      @Override
      public void onSubscribe(Disposable d) {
      }

      @Override
      public void onSuccess(SettingsModel settingsModel) {
        if (settingsModel != null) {
          logDebug("Settings got: %s", settingsModel.toString());
          applySettings(settingsModel);
        } else {
          logDebug("Settings in null");
          actionViewModel.setStableSettings();
          applySettings(SettingsModel.getStabSettings());
        }
      }

      @Override
      public void onError(Throwable e) {
        if (e != null) {
          showSnackBar(e.getLocalizedMessage());
          logError(e);
        }
      }
    });
  }

  private void applySettings(SettingsModel settingsModel) {
    this.settingsModel = settingsModel;

    setFlashState(settingsModel.isFlashEnabled(), false);

    menu.applySettings(settingsModel);
  }

  private void setFlashState(boolean isEnabled, boolean updateRemote) {
    Function1<Iterable<? extends Flash>, Flash> newFlashState = isEnabled
        ? FlashSelectorsKt.autoFlash()
        : FlashSelectorsKt.off();

    UpdateConfiguration.builder()
        .flash(newFlashState)
        .build();

    if (updateRemote) {
      FireBaseDataBaseHelper.setFlashState(settingsModel, isEnabled)
          .subscribe(new SingleObserver<Boolean>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onSuccess(Boolean aBoolean) {
              logDebug("Settings flash updated");
            }

            @Override
            public void onError(Throwable e) {
              logDebug("Settings flash update error %s", e.getLocalizedMessage());
            }
          });
    }
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
