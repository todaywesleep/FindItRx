package pro.papaya.canyo.finditrx.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;
import pro.papaya.canyo.finditrx.R;
import pro.papaya.canyo.finditrx.adapter.viewpager.MainPagerAdapter;
import pro.papaya.canyo.finditrx.dialog.CameraUnavailableDialog;
import pro.papaya.canyo.finditrx.dialog.CompleteQuestsDialog;
import pro.papaya.canyo.finditrx.dialog.NewQuestsDialog;
import pro.papaya.canyo.finditrx.fragment.main.ActionFragment;
import pro.papaya.canyo.finditrx.fragment.main.ProfileFragment;
import pro.papaya.canyo.finditrx.fragment.main.QuestsFragment;
import pro.papaya.canyo.finditrx.listener.ShortObserver;
import pro.papaya.canyo.finditrx.model.firebase.QuestModel;
import pro.papaya.canyo.finditrx.model.firebase.UserQuestModel;
import pro.papaya.canyo.finditrx.model.view.MainViewPagerModel;
import pro.papaya.canyo.finditrx.utils.Constants;
import pro.papaya.canyo.finditrx.viewmodel.MainViewModel;

import static pro.papaya.canyo.finditrx.model.view.MainViewPagerModel.QUESTS_PAGE;

public class MainActivity extends BaseActivity implements
    ProfileFragment.ProfileFragmentCallback,
    ActionFragment.ActionFragmentCallback,
    QuestsFragment.QuestFragmentCallback {
  private final static int CAMERA_PERMISSION_CODE = 1000;

  @BindView(R.id.main_view_pager)
  ViewPager mainViewPager;
  @BindView(R.id.main_tab_navigator)
  TabLayout tabLayout;

  private MainViewModel mainViewModel;
  private MainPagerAdapter adapter;
  private List<QuestModel> itemsCollection = new ArrayList<>();

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.activity_main);
    mainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
    ButterKnife.bind(this);
    initViews();
    setListeners();
    subscribeToViewModel();
  }

  @Override
  public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);

    if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
      adapter.refreshActionFragment();
    } else {
      new CameraUnavailableDialog(this).show();
    }
  }

  @Override
  public void onRequestCameraPermissions() {
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

  @Override
  public void onSnapshotTaken(List<QuestModel> takenSnapshotLabels) {
    mainViewModel.updateLabelsRemote(itemsCollection, takenSnapshotLabels)
        .subscribe(new SingleObserver<List<QuestModel>>() {
          @Override
          public void onSubscribe(Disposable d) {
          }

          @Override
          public void onSuccess(List<QuestModel> newQuests) {
            logDebug("Find %s new quests", newQuests.size());
            if (!newQuests.isEmpty()) {
              int totalReward = 0;
              int totalRewardExperience = 0;
              List<UserQuestModel> rewardModel = new ArrayList<>();
              Random random = new Random();

              for (QuestModel quest : newQuests) {
                int stepReward = generateReward(random);
                int stepRewardExperience = generateRewardExperience(random);
                rewardModel.add(UserQuestModel.from(quest, stepReward, stepRewardExperience));
                totalReward += stepReward;
                totalRewardExperience += stepRewardExperience;
              }

              new NewQuestsDialog(
                  MainActivity.this,
                  rewardModel
              ).show();
              mainViewModel.enrollMoney(totalReward);
              mainViewModel.enrollExperience(totalRewardExperience);
              mainViewModel.addFoundQuests(newQuests);
            }
          }

          @Override
          public void onError(Throwable e) {
            showSnackBar(e.getLocalizedMessage());
            logError(e);
          }
        });

    List<UserQuestModel> completedQuests = mainViewModel.getCompleteQuests(
        ((QuestsFragment) adapter.getItem(QUESTS_PAGE.ordinal())).getUserQuests(),
        takenSnapshotLabels
    );

    if (!completedQuests.isEmpty()) {
      new CompleteQuestsDialog(this, completedQuests).show();

      mainViewModel.completeQuests(
          completedQuests
      );
    }
  }

  private int generateReward(Random random) {
    int result = random.nextInt(
        Constants.UPPER_BORDER_NEW_QUEST_REWARD - Constants.BOTTOM_BORDER_NEW_QUEST_REWARD
    );
    result += 1 + Constants.BOTTOM_BORDER_NEW_QUEST_REWARD;

    return result;
  }

  private int generateRewardExperience(Random random) {
    int result = random.nextInt(
        Constants.UPPER_BORDER_NEW_QUEST_EXP_REWARD - Constants.BOTTOM_BORDER_NEW_QUEST_EXP_REWARD
    );
    result += 1 + Constants.BOTTOM_BORDER_NEW_QUEST_EXP_REWARD;

    return result;
  }

  private void subscribeToViewModel() {
    mainViewModel.getAllQuestsCollection()
        .subscribe(new ShortObserver<List<QuestModel>>() {
          @Override
          public void onNext(List<QuestModel> questModels) {
            itemsCollection = questModels;
            logDebug("Items collection updated");
          }

          @Override
          public void onError(Throwable e) {
            showSnackBar(e.getLocalizedMessage());
            logError(e);
          }
        });
  }

  private void initViews() {
    adapter = new MainPagerAdapter(
        getSupportFragmentManager(),
        this
    );

    mainViewPager.setOffscreenPageLimit(adapter.getCount());
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
  public void onLeaderBoardRequest() {
    Intent leaderBoardIntent = new Intent(MainActivity.this, LeaderBoardActivity.class);
    startActivity(leaderBoardIntent);
//    mainViewModel.getUsersCollection()
//        .subscribe(new SingleObserver<List<UserModel>>() {
//          @Override
//          public void onSubscribe(Disposable d) {
//
//          }
//
//          @Override
//          public void onSuccess(List<UserModel> userModels) {
//            logDebug("Users taken: %s", userModels);
//          }
//
//          @Override
//          public void onError(Throwable e) {
//            logError(e);
//            showSnackBar(e.getLocalizedMessage());
//          }
//        });
  }
}
