package uy1.info430.etiq.connection;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;

import uy1.info430.etiq.MainActivity;
import uy1.info430.etiq.R;
import uy1.info430.etiq.SelectApp;
import uy1.info430.etiq.myrequest.SessionManager;
import uy1.info430.etiq.myrequest.VolleySingleton;
import uy1.info430.etiq.myrequest.MyRequest;

public class Login extends AppCompatActivity {
    Button btnConnexion;
    Button btnIncription;
    TextView textAide;
    EditText username;
    EditText password;
    private RequestQueue queue;
    private MyRequest request;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        username=(EditText) findViewById(R.id.username);
        password=(EditText) findViewById(R.id.password);
        btnConnexion=(Button)findViewById(R.id.connecter);
        btnIncription=(Button)findViewById(R.id.inscrire_1);
        sessionManager=new SessionManager(this);
        if(sessionManager.islogged()){
            Intent intent=new Intent(this,MainActivity.class);
            startActivity(intent);
            finish();
        }
        queue = VolleySingleton.getInstance(this).getRequestQueue();
        request = new MyRequest(this, queue);
        btnConnexion.setOnClickListener(new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            final String pseudo=username.getText().toString().trim();
            final String pasword=password.getText().toString().trim();
            if(pseudo.length()>0 && pasword.length()>0){
                request.connection(pseudo, pasword, new MyRequest.Logincallback(){
                    @Override
                    public void onSuccess(String id, String pseudo) {
                        sessionManager.insertUser(id,pseudo);
                        Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    @Override
                    public void onError(String message) {
                        Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    });
    btnIncription.setOnClickListener(
        new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),Inscription.class);
                startActivity(intent);
            }
        }
    );


    }
}













