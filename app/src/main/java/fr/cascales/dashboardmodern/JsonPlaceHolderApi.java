package fr.cascales.dashboardmodern;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface JsonPlaceHolderApi {

    @GET("articles")
    Call<List<Article>> getArticles();

    @GET("article/{id}")
    Call<Article> getOneArticle(@Path("id") int id);

    @FormUrlEncoded
    @POST("article/{id}")
    Call<String> addStockArticle(
            @Path("id") int id,
            @Field("stock_add") String stock_add
    );

    @GET("category/{id}")
    Call<Category> getOneCategory(@Path("id") int id);

    @GET("marque/{id}")
    Call<Marque> getOneMarque(@Path("id") int id);

}
