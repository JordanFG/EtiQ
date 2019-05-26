package uy1.info430.etiq.connection;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import uy1.info430.etiq.App;
import uy1.info430.etiq.MainActivity;
import uy1.info430.etiq.R;
import uy1.info430.etiq.SelectApp;
import uy1.info430.etiq.database.Utilisateur;
import uy1.info430.etiq.myrequest.VolleySingleton;
import uy1.info430.etiq.database.Database;
import uy1.info430.etiq.myrequest.MyRequest;

public class Inscription extends AppCompatActivity  {
    private List<String> choiceName;
    private List<String> choicePack;
    private boolean[] choiceCheck;
    private int conf = 0;
    private Database database;
    private App app;
    private ArrayList<Integer> choicePosition;
    private Button btnIncrire;
    private Utilisateur utilisateur;
    private EditText username;
    private EditText password;
    private EditText email;
    private EditText confPassword;
    private RequestQueue queue;
    private MyRequest request;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inscription);



        btnIncrire = (Button) findViewById(R.id.inscrire_2);
        username = (EditText) findViewById(R.id.username);
        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        confPassword = (EditText) findViewById(R.id.confpassword);



        queue = VolleySingleton.getInstance(this).getRequestQueue();
        request = new MyRequest(this, queue);


        // utilisateur = new Utilisateur(username.getText().toString(),email.getText().toString(),password.getText().toString(),confPassword.getText().toString());



        btnIncrire.setOnClickListener(
                new View.OnClickListener(){

                    @Override
                    public void onClick(View v) {
                        // ici je dit ce qui ce passe quand je clique
                        String pseudo = username.getText().toString().trim();
                        String mail=email.getText().toString().trim();
                        String password1 = password.getText().toString().trim();
                        String password2 = confPassword.getText().toString().trim();
                        if(pseudo.length()>0&&email.length()>0&&password.length()>0&&password2.length()>0){

                            request.register(pseudo, mail, password1, password2, new MyRequest.ResgisterCallback() {
                                @Override
                                public void onSuccess(String message) {
                                    Intent intent=new Intent(getApplicationContext(),SelectApp.class);
                                    startActivity(intent);
                                    finish();
                                }

                                @Override
                                public void inputErrors(Map<String, String> errors) {

                                    if(errors.get("pseudo")!=null){

                                    }else {

                                    }
                                    if(errors.get("email")!=null){

                                    }else {

                                    }
                                    if(errors.get("password")!=null){

                                    }else {

                                    }


                                }

                                @Override
                                public void onError(String message) {

                                    Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();


                                }
                            });

                        }else {

                            Toast.makeText(getApplicationContext(),"bien vouloir remplir tout les champ",Toast.LENGTH_SHORT).show();

                        }


                    }
                }
        );


    }


}












