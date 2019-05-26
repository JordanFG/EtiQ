package uy1.info430.etiq;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.app.Activity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.mancj.materialsearchbar.MaterialSearchBar;

import java.util.List;

import uy1.info430.etiq.database.Application;
import uy1.info430.etiq.database.Database;
import uy1.info430.etiq.listApplication.ApkInfoExtractor;
import uy1.info430.etiq.listApplication.ChoiceAdapter;
import uy1.info430.etiq.myrequest.SessionManager;

public class SelectApp extends Activity {
    private ListView listView;
    private App app;
    private Button button;
    private Database database;
    private ChoiceAdapter choiceAdapter;
    private List<String> choiceName;
    private List<String> choicePack;
    private boolean[] choiceCheck;
    private ApkInfoExtractor apkInfoExtractor;
    private MaterialSearchBar materialSearchBar;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_app);
        sessionManager=new SessionManager(this);
        setTitle("Selectioner une Application");
        database = new Database(this);
        apkInfoExtractor = new ApkInfoExtractor(SelectApp.this);
        app = new App(this,database);
        choiceName = app.getChoiceExempleName();
        choicePack = app.getChoiceExemplePack();
        choiceCheck = app.getListExampleChoice();
        final ChoiceAdapter choiceAdapter = new ChoiceAdapter(SelectApp.this,R.layout.item,choiceName, choiceCheck, choicePack);
        materialSearchBar = (MaterialSearchBar) findViewById(R.id.search_bar_app);
        materialSearchBar.setHint("Rechercher Application");
        materialSearchBar.setCardViewElevation(10);
        button = (Button) findViewById(R.id.btnNext);

        materialSearchBar.addTextChangeListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String application = s.toString();
                Log.i("Debug",application);
                choiceAdapter.onChange(application);
            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        for(int i=0;i<choiceAdapter.getChoiceCheck().length;i++){
            if(choiceAdapter.isChecked(i)){
                button.setVisibility(View.VISIBLE);
            }
        }
        listView=(ListView)findViewById(R.id.list_app);
        listView.setAdapter(choiceAdapter);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int k = 0 ; k < choiceName.size(); k++){
                    if(choiceAdapter.getChoiceCheck()[k]){
                        Application a = new Application(choiceName.get(k),choicePack.get(k),true);
                        database.insertApplication(a);
                    }
                }
                sessionManager.selectApplication();
                Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

}
