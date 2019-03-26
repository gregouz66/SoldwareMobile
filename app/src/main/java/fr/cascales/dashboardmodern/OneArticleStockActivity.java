package fr.cascales.dashboardmodern;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class OneArticleStockActivity extends AppCompatActivity {

    //REST
    private int value = -1; // or other values
    private Article article = null;
    //ELEMENTS
    private ImageView imgArticle;
    private TextView txtTitle;
    private TextView txtPrix;
    private TextView txtStock;
    private EditText txtAddStock;
    private Button btnAddStock;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one_article_stock);

        imgArticle = findViewById(R.id.image_article_stock);
        txtTitle = findViewById(R.id.title_article_stock);
        txtPrix = findViewById(R.id.txtPrix_stock);
        txtStock = findViewById(R.id.txtStock_stock);
        txtAddStock = findViewById(R.id.txtAddStock);
        btnAddStock = findViewById(R.id.btnAddStock);

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

                //new DownloadImageTask(imgArticle).execute(article.getImageDefault());
                Picasso.get()
                        .load(article.getImageDefault())
                        .placeholder(R.drawable.loader)
                        .fit()
                        .into(imgArticle);
                txtTitle.setText(article.getTitle());
                txtPrix.setText(myPrice);
                txtStock.setText(String.valueOf(article.getStock()));
            }

            @Override
            public void onFailure(Call<Article> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        //EVENT CLICK ADD STOCK
        btnAddStock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                //Add Stock article
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("http://api.cascales.fr/REST-SoldwareMobile/api/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                JsonPlaceHolderApi jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);
                Call<String> call = jsonPlaceHolderApi.addStockArticle(value, txtAddStock.getText().toString());
                call.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        if(!response.isSuccessful()){
                            Toast.makeText(getApplicationContext(), "Code : "+ response.code(), Toast.LENGTH_SHORT).show();
                            return;
                        }
                        String result = response.body();

                        //Set elements
                        if(result == "0"){
                            Toast.makeText(getApplicationContext(), "Erreur lors de l'envoi au serveur", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "+" + txtAddStock.getText().toString() + " stock ajouté à " + article.getTitle(), Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });


    }


}
