package com.reward.quizisty.Modals;

public class WatchVidModal {
    String btnTitle;
    int id;
    boolean buttonEnable;

    public WatchVidModal(String btnTitle, int id, boolean buttonEnable) {
        this.btnTitle = btnTitle;
        this.id = id;
        this.buttonEnable = buttonEnable;
    }

    public String getBtnTitle() {
        return btnTitle;
    }

    public void setBtnTitle(String btnTitle) {
        this.btnTitle = btnTitle;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isButtonEnable() {
        return buttonEnable;
    }

    public void setButtonEnable(boolean buttonEnable) {
        this.buttonEnable = buttonEnable;
    }
}
