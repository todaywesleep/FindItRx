package pro.papaya.canyo.finditrx.dialog;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import pro.papaya.canyo.finditrx.R;
import pro.papaya.canyo.finditrx.model.firebase.UserQuestModel;

public class NewQuestsDialog extends AlertDialog {
  private List<UserQuestModel> newQuests;
  private Dialog dialog;

  @BindView(R.id.new_quests_container)
  LinearLayout itemsContainer;
  @BindView(R.id.new_quests_positive_btn)
  Button positiveButton;

  public NewQuestsDialog(Context context,
                         List<UserQuestModel> newQuests) {
    super(context);
    this.newQuests = newQuests;
    init();
  }

  private void init() {
    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

    LayoutInflater layoutInflater = getLayoutInflater();
    View dialogView = layoutInflater.inflate(R.layout.dialog_new_quests, null);
    builder.setView(dialogView);

    ButterKnife.bind(this, dialogView);

    setListeners();
    bindData();

    dialog = builder.create();
  }

  private void setListeners() {
    positiveButton.setOnClickListener(v -> dismiss());
  }

  @SuppressLint("SetTextI18n")
  private void bindData() {
    Random random = new Random();

    for (UserQuestModel quest : newQuests) {
      TextView tvNewQuest = new TextView(getContext());
      tvNewQuest.setText("Item name: " + quest.getLabel() + ", reward: " + quest.getReward());
      itemsContainer.addView(tvNewQuest);
    }
  }

  @Override
  public void show() {
    dialog.show();
  }

  @Override
  public void dismiss() {
    dialog.dismiss();
  }
}
