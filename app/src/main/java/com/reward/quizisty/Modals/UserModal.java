package com.reward.quizisty.Modals;

public class UserModal {
    String fullName, userName, email, profilePic, userFirstLetter, coins,diamonds, phone, referCode;
    int totalRefer;

    public String getDiamonds() {
        return diamonds;
    }

    public void setDiamonds(String diamonds) {
        this.diamonds = diamonds;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public String getUserFirstLetter() {
        return userFirstLetter;
    }

    public void setUserFirstLetter(String userFirstLetter) {
        this.userFirstLetter = userFirstLetter;
    }

    public String getCoins() {
        return coins;
    }

    public void setCoins(String coins) {
        this.coins = coins;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getReferCode() {
        return referCode;
    }

    public void setReferCode(String referCode) {
        this.referCode = referCode;
    }

    public int getTotalRefer() {
        return totalRefer;
    }

    public void setTotalRefer(int totalRefer) {
        this.totalRefer = totalRefer;
    }
}
