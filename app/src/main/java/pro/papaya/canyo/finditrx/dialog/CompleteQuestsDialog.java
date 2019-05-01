package pro.papaya.canyo.finditrx.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import pro.papaya.canyo.finditrx.R;
import pro.papaya.canyo.finditrx.model.firebase.UserQuestModel;
import pro.papaya.canyo.finditrx.utils.Constants;

public class CompleteQuestsDialog extends Dialog {
  @BindView(R.id.complete_quests_message)
  TextView message;
  @BindView(R.id.complete_quests_ok_button)
  Button btnOk;

  private String additionalQuestMessage;

  public CompleteQuestsDialog(@NonNull Context context,
                              @NonNull List<UserQuestModel> completedQuests) {
    super(context);
    additionalQuestMessage = getAdditionalQuestMessage(completedQuests);
  }

  private String getAdditionalQuestMessage(@NonNull List<UserQuestModel> completedQuests) {
    StringBuilder result = new StringBuilder("\n");

    for (UserQuestModel quest : completedQuests) {
      result.append(quest.getLabel()
          .concat(Constants.BRACKET_OPEN)
          .concat(quest.getExperience() + " EXP ")
          .concat(quest.getReward() + " COINS")
          .concat(Constants.BRACKET_CLOSE)
          .concat("\n"));
    }

    return result.toString();
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
    message.setText(
        getContext().getString(R.string.complete_quest_dialog_message)
            .concat(additionalQuestMessage)
    );

    btnOk.setOnClickListener(v -> dismiss());
  }
}
