package fr.cascales.dashboardmodern;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class LoginActivity extends AppCompatActivity {

    private EditText editTextEmail;
    private EditText editTextPassword;
    private Button btnConnexion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //ONCLICK CONNEXION
        this.btnConnexion = findViewById(R.id.btnConnexion);

        btnConnexion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Get EditText
                editTextEmail = findViewById(R.id.editEmail);
                editTextPassword = findViewById(R.id.editPassword);

                //Get values in EditText
                String txtEmail = editTextEmail.getText().toString();
                String txtPassword = editTextPassword.getText().toString();

                //IF infos match with valide values
                if (txtEmail.equals("admin") && txtPassword.equals("admin"))
                {
                    //Open other activity
                    Intent dashboardActivity = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(dashboardActivity);
                    finish();
                }
                else
                {
                    new AlertDialog.Builder(LoginActivity.this)
                            .setTitle("La connexion a échoué")
                            .setMessage("Les identifiants sont invalides.")
                            .show();
                }
            }
        });


    }


}
