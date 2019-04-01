package pro.papaya.canyo.finditrx.activity;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;
import butterknife.ButterKnife;
import pro.papaya.canyo.finditrx.R;
import pro.papaya.canyo.finditrx.adapter.MainPageAdapter;
import pro.papaya.canyo.finditrx.model.view.MainViewPagerModel;
import pro.papaya.canyo.finditrx.viewmodel.MainViewModel;

public class MainActivity extends BaseActivity {
  @BindView(R.id.main_view_pager)
  ViewPager mainViewPager;

  private MainViewModel mainViewModel;
  private MainPageAdapter adapter;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.activity_main);
    mainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
    ButterKnife.bind(this);
    initViews();
  }

  private void initViews(){
    adapter = new MainPageAdapter(getSupportFragmentManager());
    mainViewPager.setAdapter(adapter);
    mainViewPager.setCurrentItem(MainViewPagerModel.CAMERA_PAGE.ordinal());
  }
}
