package fr.cascales.dashboardmodern;

import com.google.gson.annotations.SerializedName;

public class Article {

    private int id;
    private String title;
    //@SerializedName("body")
    private String content;
    private float price;
    private int stock;
    private String language;
    private String compatibility;
    private String image_default;
    //private String created_at;
    private int category_id;
    private int marque_id;

    public Article(){

    }

    //ACCESSEURS
    public int getId() {
        return id;
    }
    public String getTitle() {
        return title;
    }
    public String getContent() {
        return content;
    }
    public float getPrice() {
        return price;
    }
    public int getStock() {
        return stock;
    }
    public String getLanguage() {
        return language;
    }
    public String getCompatibility() {
        return compatibility;
    }
    public String getImageDefault() {
        return image_default;
    }
    public int getCategory() {
        return category_id;
    }
    public int getMarque() {
        return marque_id;
    }
}
