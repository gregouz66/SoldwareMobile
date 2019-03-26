package fr.cascales.dashboardmodern;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, MyRecyclerViewAdapter.ItemClickListener {

    //FOR DESIGN
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Button btnProfilMenu;

    //REQUEST URL
    private TextView textViewResult;

    //CARDS ARTICLES
    MyRecyclerViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Open Menu Custom
        this.btnProfilMenu = findViewById(R.id.btnProfilMenu);
        btnProfilMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        //Check connexion
        internetOrExit();

        //REST ARTICLES
        this.getArticlesREST();

        // 6 - Configure all views
        //this.configureToolBar();
        this.configureDrawerLayout();
        this.configureNavigationView();
    }

    @Override
    public void onBackPressed() {
        // 5 - Handle back click to close menu
        if (this.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            this.drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        // 4 - Handle Navigation Item Click
        int id = item.getItemId();

        switch (id){
            //Click dashboard
            case R.id.activity_main_drawer_dashboard :
                //Open other activity
                Intent articleActivity = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(articleActivity);
                finish();
                break;
            //Click inventaire
            case R.id.activity_main_drawer_inventaire :
                //Open other activity
                Intent inventaireActivity = new Intent(getApplicationContext(), ScannedBarcodeActivity.class);
                startActivity(inventaireActivity);
                //finish();
                break;
            //Click Profile
            case R.id.activity_main_drawer_parametres:
                //ALERT
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Paramètres")
                        .setMessage("Cette fonctionnalité arrive bientôt !")
                        .show();
                break;
            //Click Default
            default:
                break;
        }

        this.drawerLayout.closeDrawer(GravityCompat.START);

        return true;
    }

    //Click in single article
    @Override
    public void onItemClick(View view, int position) {
        //Create activity
        Intent articleActivity = new Intent(getApplicationContext(), OneArticleActivity.class);
        //Set parameters (id)
        Bundle b = new Bundle();
        b.putInt("id", adapter.getArticle(position).getId()); //Your id
        articleActivity.putExtras(b); //Put your id to your next Intent
        //Start Activity
        startActivity(articleActivity);
    }

    // ---------------------
    // CONFIGURATION TOOLBAR
    // ---------------------

    // 1 - Configure Toolbar
    //private void configureToolBar(){
    //    this.toolbar = (Toolbar) findViewById(R.id.activity_main_toolbar);
    //    setSupportActionBar(toolbar);
    //}

    // 2 - Configure Drawer Layout
    private void configureDrawerLayout(){
        this.drawerLayout = (DrawerLayout) findViewById(R.id.activity_main_drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }

    // 3 - Configure NavigationView
    private void configureNavigationView(){
        this.navigationView = findViewById(R.id.activity_main_nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    // ---------------------
    // CONFIGURATION REST API ARTICLES
    // ---------------------

    //REST API
    private void getArticlesREST(){
        //REQUEST
        this.textViewResult = (TextView) findViewById(R.id.txtDashboard);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://api.cascales.fr/REST-SoldwareMobile/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        JsonPlaceHolderApi jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);

        Call<List<Article>> call = jsonPlaceHolderApi.getArticles();

        call.enqueue(new Callback<List<Article>>() {
            @Override
            public void onResponse(Call<List<Article>> call, Response<List<Article>> response) {
                if(!response.isSuccessful()){
                    //textViewResult.setText("Code : "+ response.code());
                    textViewResult.setText("Aucune connexion internet");
                    return;
                }

                List<Article> articles = response.body();

                //Instacie & make elements in grid
                getingrids_articles(articles);
            }

            @Override
            public void onFailure(Call<List<Article>> call, Throwable t) {
                //textViewResult.setText(t.getMessage());
                textViewResult.setText("Aucune connexion internet");
            }
        });
    }

    //GRIDS ARTICLE
    private void getingrids_articles(List<Article> articles){
        RecyclerView recyclerView = findViewById(R.id.rvNumbers);
        int numberOfColumns = 2;
        recyclerView.setLayoutManager(new GridLayoutManager(this, numberOfColumns));
        adapter = new MyRecyclerViewAdapter(this, articles);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);
        //Set items count
        TextView txt_itemCount = findViewById(R.id.length_items);
        txt_itemCount.setText(adapter.getItemCount() + " articles");
    }

    //CHECK CONNECTION
    private void internetOrExit(){
        //Check connexion
        if(!NetworkConnexion.isNetworkAvailable(getApplicationContext())){
            //message
            //Snackbar.make(getWindow().getDecorView().getRootView(), "Aucune connexion à internet !", Snackbar.LENGTH_SHORT).show();

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setCancelable(false);
            builder.setTitle("Oops");
            builder.setMessage("Aucune connexion à internet, veuillez relancer l'application.");
            builder.setPositiveButton("Relancer",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //restart
                            Intent restartIntent = getBaseContext().getPackageManager()
                                    .getLaunchIntentForPackage(getBaseContext().getPackageName());
                            restartIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(restartIntent);
                            finish();
                        }
                    });
            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }

}
