package pro.papaya.canyo.finditrx.model.view;

public enum FabMenuAction {
  ACTION_NOT_SPECIFIED(-1),
  ACTION_AUTO_FLASH(0);

  private int action;

  public static FabMenuAction from(Integer action) {
    for (FabMenuAction item : FabMenuAction.values()) {
      if (item.getAction() == action) {
        return item;
      }
    }

    return ACTION_NOT_SPECIFIED;
  }

  FabMenuAction(int action) {
    this.action = action;
  }

  public int getAction() {
    return action;
  }
}
