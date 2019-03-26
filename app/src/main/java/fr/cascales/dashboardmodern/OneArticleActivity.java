package fr.cascales.dashboardmodern;

import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class OneArticleActivity extends AppCompatActivity {

    //REST
    private int value = -1; // or other values
    private Article article;
    //ELEMENTS
    private ImageView imgArticle;
    private TextView txtTitle;
    private TextView txtPrix;
    private TextView txtStock;
    private TextView txtLanguage;
    private TextView txtCompatibility;
    private TextView txtCategory;
    private TextView txtMarque;
    private TextView txtDescription;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one_article);

        //Snackbar.make(getWindow().getDecorView().getRootView(), "message here", Snackbar.LENGTH_SHORT).show();

        imgArticle = findViewById(R.id.image_article);
        txtTitle = findViewById(R.id.title_article);
        txtPrix = findViewById(R.id.txtPrix);
        txtStock = findViewById(R.id.txtStock);
        txtLanguage = findViewById(R.id.txtLanguage);
        txtCompatibility = findViewById(R.id.txtCompatibility);
        txtCategory = findViewById(R.id.txtCategory);
        txtMarque = findViewById(R.id.txtMarque);
        txtDescription = findViewById(R.id.txtDescription);

        //Get ID Parameters or Redirect
        Bundle b = getIntent().getExtras();
        if(b != null) {
            value = b.getInt("id");
        } else {
            //Intent mainActivity = new Intent(getApplicationContext(), MainActivity.class);
            //startActivity(mainActivity);
            Toast.makeText(getApplicationContext(), "Problème lors de l'affichage de l'article", Toast.LENGTH_SHORT).show();
            //finish();
        }

        //Get article
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://api.cascales.fr/REST-SoldwareMobile/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        JsonPlaceHolderApi jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);
        Call<Article> call = jsonPlaceHolderApi.getOneArticle(value);
        call.enqueue(new Callback<Article>() {
            @Override
            public void onResponse(Call<Article> call, Response<Article> response) {
                if(!response.isSuccessful()){
                    Toast.makeText(getApplicationContext(), "Code : "+ response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }
                article = response.body();

                //Numbers to String
                String myPrice = String.valueOf(article.getPrice()) + "€";

                //Get category
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("http://api.cascales.fr/REST-SoldwareMobile/api/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                JsonPlaceHolderApi jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);
                Call<Category> call2 = jsonPlaceHolderApi.getOneCategory(article.getCategory());
                call2.enqueue(new Callback<Category>() {
                    @Override
                    public void onResponse(Call<Category> call2, Response<Category> response) {
                        if(!response.isSuccessful()){
                            txtCategory.setText(String.valueOf(response.code()));
                            return;
                        }
                        //Set infos
                        Category categoryDynamic = response.body();
                        txtCategory.setText(String.valueOf(categoryDynamic.getTitle()));
                    }

                    @Override
                    public void onFailure(Call<Category> call2, Throwable t) {
                        txtCategory.setText(t.getMessage());
                        return;
                    }
                });

                //Get marque
                Retrofit retrofit2 = new Retrofit.Builder()
                        .baseUrl("http://api.cascales.fr/REST-SoldwareMobile/api/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                JsonPlaceHolderApi jsonPlaceHolderApi2 = retrofit2.create(JsonPlaceHolderApi.class);
                Call<Marque> call3 = jsonPlaceHolderApi2.getOneMarque(article.getMarque());
                call3.enqueue(new Callback<Marque>() {
                    @Override
                    public void onResponse(Call<Marque> call3, Response<Marque> response) {
                        if(!response.isSuccessful()){
                            txtMarque.setText(String.valueOf(response.code()));
                            return;
                        }
                        //Set infos
                        Marque marqueDynamic = response.body();
                        txtMarque.setText(String.valueOf(marqueDynamic.getTitle()));
                    }

                    @Override
                    public void onFailure(Call<Marque> call3, Throwable t) {
                        txtMarque.setText(t.getMessage());
                        return;
                    }
                });

                //Set infos
                //new DownloadImageTask(imgArticle).execute(article.getImageDefault());
                Picasso.get()
                        .load(article.getImageDefault())
                        .placeholder(R.drawable.loader)
                        .fit()
                        .into(imgArticle);
                txtTitle.setText(article.getTitle());
                txtPrix.setText(myPrice);
                txtStock.setText(String.valueOf(article.getStock()));
                txtLanguage.setText(article.getLanguage());
                txtCompatibility.setText(article.getCompatibility());
                txtMarque.setText(String.valueOf(article.getMarque()));
                txtDescription.setText(article.getContent());
            }

            @Override
            public void onFailure(Call<Article> call, Throwable t) {
                //Intent mainActivity = new Intent(getApplicationContext(), MainActivity.class);
                //startActivity(mainActivity);
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                //finish();
            }
        });
    }
}
