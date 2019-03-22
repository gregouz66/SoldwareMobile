package fr.cascales.dashboardmodern;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface JsonPlaceHolderApi {

    @GET("posts")
    Call<List<Post>> getPosts();

    @GET("articles")
    Call<List<Article>> getArticles();

}