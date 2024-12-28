package com.reward.quizisty.Modals;

public class HistoryModal {
    String historyDate, historyTime, historyCoinDiamond, historyType, historyMsg;

    boolean isCredited;


    public HistoryModal(String historyDate, String historyTime, String historyCoinDiamond, boolean isCredited, String historyMsg) {
        this.historyDate = historyDate;
        this.historyTime = historyTime;
        this.historyCoinDiamond = historyCoinDiamond;
        this.isCredited = isCredited;
        this.historyMsg = historyMsg;
    }

    public boolean isCredited() {
        return isCredited;
    }

    public void setCredited(boolean credited) {
        isCredited = credited;
    }
    public String getHistoryDate() {
        return historyDate;
    }

    public void setHistoryDate(String historyDate) {
        this.historyDate = historyDate;
    }

    public String getHistoryTime() {
        return historyTime;
    }

    public void setHistoryTime(String historyTime) {
        this.historyTime = historyTime;
    }

    public String getHistoryCoinDiamond() {
        return historyCoinDiamond;
    }

    public void setHistoryCoinDiamond(String historyCoinDiamond) {
        this.historyCoinDiamond = historyCoinDiamond;
    }

    public String getHistoryType() {
        return historyType;
    }

    public void setHistoryType(String historyType) {
        this.historyType = historyType;
    }

    public String getHistoryMsg() {
        return historyMsg;
    }

    public void setHistoryMsg(String historyMsg) {
        this.historyMsg = historyMsg;
    }

}
