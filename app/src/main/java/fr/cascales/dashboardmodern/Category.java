package fr.cascales.dashboardmodern;

import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Category {

    private int id;
    private String title;

    //ACCESSEURS
    public int getId() {
        return id;
    }
    public String getTitle() {
        return title;
    }

}
