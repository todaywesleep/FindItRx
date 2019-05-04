package pro.papaya.canyo.finditrx.dialog;

import android.content.Context;

import pro.papaya.canyo.finditrx.R;
import pro.papaya.canyo.finditrx.model.firebase.QuestModel;

public class RejectQuestConfirmationDialog extends BaseDialog {
  private OnClickListener positiveListener;
  private QuestModel questModel;

  public RejectQuestConfirmationDialog(Context context,
                                       QuestModel questModel,
                                       OnClickListener positiveListener) {
    super(context, false);

    this.positiveListener = positiveListener;
    this.questModel = questModel;

    init();
  }

  @Override
  protected void fillDialog() {
    setupNegativeButton(
        getContext().getString(R.string.reject_quest_dialog_negative),
        (dialog, which) -> dialog.dismiss()
    );
  }

  @Override
  protected String getTitle() {
    return getContext().getString(R.string.reject_quest_dialog_title);
  }

  @Override
  protected String getMessage() {
    return getContext().getString(R.string.reject_quest_dialog_message, questModel.getLabel());
  }

  @Override
  protected String getPositiveButtonMessage() {
    return getContext().getString(R.string.reject_quest_dialog_positive);
  }

  @Override
  protected OnClickListener getPositiveOnClick() {
    return positiveListener;
  }
}
