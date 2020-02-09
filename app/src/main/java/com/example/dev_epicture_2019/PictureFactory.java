package com.example.dev_epicture_2019;

public class PictureFactory
{
    private String id;
    private String title;
    private String description;
    private boolean favorite;

    public String getDescription() {
        return description;
    }
    public String getId() {
        return id;
    }
    public String getTitle() {
        return title;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }
    public void setId(String id) {
        this.id = id;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public static PictureFactory createUser(String id, String title, String description, boolean favorite)
    {
        PictureFactory pic = new PictureFactory();
        pic.setId(id);
        pic.setTitle(title);
        pic.setDescription(description);
        pic.setFavorite(favorite);

        return (pic);
    }
}
