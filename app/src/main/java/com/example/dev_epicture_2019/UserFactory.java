package com.example.dev_epicture_2019;

public class UserFactory {

    private String userUrl;
    private String userBio;
    private String userAvatar;
    private String userReputation;

    /*public UserFactory(String urlUser, String bio, String avatar, int reputation)
    {
        this.userUrl = urlUser;
        this.userBio = bio;
        this.userAvatar = avatar;
        this.userReputation = reputation;
    }*/

    public String getUserUrl() {
        return userUrl;
    }

    public void setUserUrl(String userUrl) {
        this.userUrl = userUrl;
    }

    public String getUserBio() {
        return userBio;
    }

    public void setUserBio(String userBio) {
        this.userBio = userBio;
    }

    public String getUserAvatar() {
        return userAvatar;
    }

    public void setUserAvatar(String userAvatar) {
        this.userAvatar = userAvatar;
    }

    public String getUserReputation() {
        return userReputation;
    }

    public void setUserReputation(String userReputation) {
        this.userReputation = userReputation;
    }

    public static UserFactory createUser(String url, String bio, String avatar, String reputation)
    {
        UserFactory usr = new UserFactory();
        usr.setUserUrl(url);
        usr.setUserBio(bio);
        usr.setUserAvatar(avatar);
        usr.setUserReputation(reputation);

        return (usr);
    }
}
