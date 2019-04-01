package pro.papaya.canyo.finditrx.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;
import butterknife.ButterKnife;
import pro.papaya.canyo.finditrx.R;
import pro.papaya.canyo.finditrx.adapter.MainPageAdapter;
import pro.papaya.canyo.finditrx.dialog.CameraUnavailableDialog;
import pro.papaya.canyo.finditrx.fragment.ActionPageFragment;
import pro.papaya.canyo.finditrx.model.view.MainViewPagerModel;
import pro.papaya.canyo.finditrx.viewmodel.MainViewModel;

public class MainActivity extends BaseActivity implements ActionPageFragment.ActionPageCallback {
  private final static int CAMERA_PERMISSION_CODE = 1000;

  @BindView(R.id.main_view_pager)
  ViewPager mainViewPager;
  @BindView(R.id.main_tab_navigator)
  TabLayout tabLayout;

  private MainViewModel mainViewModel;
  private MainPageAdapter adapter;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.activity_main);
    mainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
    ButterKnife.bind(this);
    initViews();
    setListeners();
  }

  @Override
  public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);

    if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
      adapter.refreshActionFragment();
    }else{
      new CameraUnavailableDialog(this).show();
    }
  }

  private void initViews() {
    adapter = new MainPageAdapter(getSupportFragmentManager(), this);
    mainViewPager.setAdapter(adapter);
    selectPage(MainViewPagerModel.CAMERA_PAGE.ordinal(), false);
    selectTab(MainViewPagerModel.CAMERA_PAGE.ordinal());
  }

  private void selectTab(int idx) {
    TabLayout.Tab tab = tabLayout.getTabAt(idx);
    if (tab != null) {
      tab.select();
    }
  }

  private void selectPage(int idx, boolean smoothScroll) {
    mainViewPager.setCurrentItem(idx, smoothScroll);
  }

  private void setListeners() {
    mainViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
      @Override
      public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
      }

      @Override
      public void onPageSelected(int position) {
        TabLayout.Tab tab = tabLayout.getTabAt(position);

        if (tab != null) {
          tab.select();
        }
      }

      @Override
      public void onPageScrollStateChanged(int state) {
      }
    });

    tabLayout.addOnTabSelectedListener(new TabLayout.BaseOnTabSelectedListener() {
      @Override
      public void onTabSelected(TabLayout.Tab tab) {
        selectPage(tab.getPosition(), true);
      }

      @Override
      public void onTabUnselected(TabLayout.Tab tab) {

      }

      @Override
      public void onTabReselected(TabLayout.Tab tab) {
      }
    });
  }

  @Override
  public void requestCameraPermissions() {
    ActivityCompat.requestPermissions(MainActivity.this,
        new String[]{Manifest.permission.CAMERA},
        CAMERA_PERMISSION_CODE);
  }

  @Override
  public boolean isCameraPermissionsGranted() {
    return ContextCompat.checkSelfPermission(
        MainActivity.this,
        Manifest.permission.CAMERA
    ) == PackageManager.PERMISSION_GRANTED;
  }
}
