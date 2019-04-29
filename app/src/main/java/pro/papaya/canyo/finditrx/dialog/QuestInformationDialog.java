package pro.papaya.canyo.finditrx.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import butterknife.BindView;
import butterknife.ButterKnife;
import pro.papaya.canyo.finditrx.R;
import pro.papaya.canyo.finditrx.model.firebase.UserQuestModel;
import pro.papaya.canyo.finditrx.model.local.QuestRarity;

public class QuestInformationDialog extends Dialog {
  public interface QuestInformationDialogCallback {
    void onRejectPress(UserQuestModel model);
  }

  @BindView(R.id.quest_info_root)
  ConstraintLayout root;
  @BindView(R.id.quest_info_title)
  TextView title;
  @BindView(R.id.quest_info_reward_coins)
  TextView rewardCoins;
  @BindView(R.id.quest_info_rarity)
  TextView rarity;
  @BindView(R.id.quest_info_reward_experience)
  TextView rewardExperience;
  @BindView(R.id.quest_info_reject)
  Button reject;
  @BindView(R.id.quest_info_close)
  Button close;

  private UserQuestModel userQuestModel;
  private QuestInformationDialogCallback callback;
  private QuestRarity questRarity;

  public QuestInformationDialog(@NonNull Context context,
                                UserQuestModel userQuestModel,
                                QuestInformationDialogCallback callback) {
    super(context);
    this.userQuestModel = userQuestModel;
    this.callback = callback;
    this.questRarity = QuestRarity.getRarity(userQuestModel.getReward());
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (getWindow() != null) {
      getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }
    setContentView(R.layout.dialog_quest_info);
    ButterKnife.bind(this);

    setupViews();
  }

  private void setupViews() {
    title.setText(getContext().getString(R.string.quest_info_title, userQuestModel.getLabel()));
    rewardCoins.setText(getContext().getString(
        R.string.quest_info_reward_coins,
        userQuestModel.getReward())
    );
    rewardExperience.setText(getContext().getString(
        R.string.quest_info_reward_exp,
        userQuestModel.getExperience())
    );

    reject.setOnClickListener(v -> {
      if (callback != null) {
        callback.onRejectPress(userQuestModel);
      }

      dismiss();
    });
    close.setOnClickListener(v -> dismiss());


    setUpViewAccordingRarity();
  }

  private void setUpViewAccordingRarity() {
    QuestRarity rarityModel = QuestRarity.getRarity(userQuestModel.getReward());
//    TODO uncomment to set color whole card
//    GradientDrawable bgShape = (GradientDrawable) root.getBackground();
//    bgShape.setColor(ContextCompat.getColor(getContext(), rarity.getColor()));

    String rarityString = questRarity.getRarityString();
    String original = getContext().getString(
        R.string.quest_info_rarity,
        rarityString
    );
    int rarityColor = ContextCompat.getColor(getContext(), rarityModel.getColor());
    Spannable spannable = new SpannableString(original);

    int idxStart = original.indexOf(rarityString);
    spannable.setSpan(new ForegroundColorSpan(rarityColor),
        idxStart,
        idxStart + rarityString.length(),
        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

    rarity.setText(spannable);
  }
}
