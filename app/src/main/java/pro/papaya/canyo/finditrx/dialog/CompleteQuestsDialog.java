package pro.papaya.canyo.finditrx.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import pro.papaya.canyo.finditrx.R;
import pro.papaya.canyo.finditrx.model.firebase.UserQuestModel;
import pro.papaya.canyo.finditrx.model.local.QuestRarity;
import pro.papaya.canyo.finditrx.utils.Constants;

public class CompleteQuestsDialog extends Dialog {
  @BindView(R.id.complete_quests_message)
  TextView message;
  @BindView(R.id.complete_quests_ok_button)
  Button btnOk;

  private SpannableStringBuilder rewardText;

  public CompleteQuestsDialog(@NonNull Context context,
                              @NonNull List<UserQuestModel> completedQuests) {
    super(context);

    rewardText = getAdditionalQuestMessage(completedQuests);
  }

  private SpannableStringBuilder getAdditionalQuestMessage(@NonNull List<UserQuestModel> completedQuests) {
    SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();
    String rewardMessage = getContext().getString(R.string.complete_quest_dialog_message);

    spannableStringBuilder.append(rewardMessage);
    spannableStringBuilder.append("\n\n");

    for (UserQuestModel quest : completedQuests) {
      QuestRarity questRarity = QuestRarity.getRarity(quest.getReward());

      String questName = quest.getLabel();
      String questReward = quest.getReward().toString();
      String questExperience = Integer.toString(quest.getExperience());
      String coinsLabel = " Coins, ";
      String experienceLabel = " Experience";

      String resultString = questName.concat(Constants.SPACE)
          .concat(Constants.BRACKET_OPEN)
          .concat(questReward)
          .concat(coinsLabel)
          .concat(questExperience)
          .concat(experienceLabel)
          .concat(Constants.BRACKET_CLOSE);

      Spannable sb = new SpannableString(resultString);
      sb.setSpan(new ForegroundColorSpan(questRarity.getColor()),
          0,
          questName.length(),
          Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
      );
      sb.setSpan(new StyleSpan(android.graphics.Typeface.BOLD),
          0,
          questName.length(),
          Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
      );

      spannableStringBuilder.append(sb);
    }

    return spannableStringBuilder;
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (getWindow() != null) {
      getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }
    setContentView(R.layout.dialog_complete_quests);
    ButterKnife.bind(this);

    setupViews();
  }

  private void setupViews() {
    message.setText(rewardText);
    btnOk.setOnClickListener(v -> dismiss());
  }
}
