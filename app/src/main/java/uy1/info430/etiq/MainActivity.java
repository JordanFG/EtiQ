package uy1.info430.etiq;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.mancj.materialsearchbar.MaterialSearchBar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import uy1.info430.etiq.checkConnection.ConnectivityReceiver;
import uy1.info430.etiq.connection.Login;
import uy1.info430.etiq.database.Application;
import uy1.info430.etiq.database.Database;
import uy1.info430.etiq.database.Message;
import uy1.info430.etiq.getNotification.CustomListAdapter;
import uy1.info430.etiq.getNotification.Model;
import uy1.info430.etiq.listApplication.ApkInfoExtractor;
import uy1.info430.etiq.listApplication.ChoiceAdapter;
import uy1.info430.etiq.myrequest.SessionManager;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, ConnectivityReceiver.ConnectivityReceiverListener  {
    private Database database;
    private Toolbar toolbar;
    private DrawerLayout drawer;
    private App app;
    private ListView listView;
    private ChoiceAdapter choiceAdapter;
    private List<String> choiceName;
    private List<String> choicePack;
    private boolean[] choiceCheck;
    private Menu menu;
    private Boolean connected;
    public static int id;
    private Map<Integer,String> application;
    private ListView list;
    private ListView listMessage;
    private CustomListAdapter adapter;
    private ArrayList<Model> modelList;
    private List<Message> modelMessage;
    private List<Message> messages;
    private List<Message> modelMessageCheck;
    private ApkInfoExtractor apkInfoExtractor;
    MaterialSearchBar materialSearchBar;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        LocalBroadcastManager.getInstance(this).registerReceiver(onNotice, new IntentFilter("Msg"));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sessionManager=new SessionManager(this);
        if(sessionManager.isSelectApplication()){
            int n=0,i=0;
            listMessage=(ListView)findViewById(R.id.list);
            toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.addDrawerListener(toggle);
            toggle.syncState();

            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
            menu = navigationView.getMenu();
            application = new HashMap<>();
            database = new Database(this);
            app = new App(this,database);
            choiceName = app.getChoiceExempleName();
            choicePack = app.getChoiceExemplePack();
            choiceCheck = app.getListExampleChoice();
            apkInfoExtractor = new ApkInfoExtractor(MainActivity.this);
            List<Application> applicationList = database.selectAllPrint();
            Log.i("Debug Errror:",applicationList.toString());
            for (i = 0; i < applicationList.size(); i++){
                Drawable draw = apkInfoExtractor.getAppIconByPackageName(applicationList.get(i).getPack());
                menu.add(R.id.application, i, 0,applicationList.get(i).getNameApp()).setIcon(draw);
                application.put(i,applicationList.get(i).getNameApp());
            }
            List<Application> applications = database.getApplication(application.get(0));

            menu.add(R.id.application,99, 0,"Ajouter réseau social").setIcon(R.drawable.ic_add_black_24dp);
            setTitle(application.get(0));
            adapter= new CustomListAdapter(this,getMessageCheck(0),getApplication(0,applications).getPack());
            listMessage.setAdapter(adapter);
            navigationView.setNavigationItemSelectedListener(this);

            applicationList = database.selectAllPrint();
            for (i=0; i < choiceName.size(); i++){
                for(int j =0; j < applicationList.size(); j++){
                    if(applicationList.get(j).getNameApp().equals(choiceName.get(i))){
                        choiceCheck[i] = true;
                    }
                }
            }

            materialSearchBar = (MaterialSearchBar) findViewById(R.id.search_bar);
            materialSearchBar.setHint("Search");
            materialSearchBar.setCardViewElevation(10);

            materialSearchBar.addTextChangeListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    String message = s.toString();
                    Log.i("SEARCH",message);
                    adapter.onChange(message);
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
        }
        else{
            Intent intent=new Intent(getApplicationContext(),SelectApp.class);
            startActivity(intent);
            finish();
        }


    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int idnav = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (idnav == R.id.dec) {
            sessionManager.logout();
            Intent intent=new Intent(getApplicationContext(),Login.class);
            startActivity(intent);
            finish();
        }
        if (idnav==R.id.action_settings){

            Intent intent = new Intent(
                    "android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS");
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        id = item.getItemId();

        List<Message> messages = null;
        int i = 0,n=0;
        String icon;
        for(i=0;i < application.size(); i++){
            if (id==i){
                List<Application> applications = database.getApplication(application.get(i));
                Application messageApp = null;
                modelMessageCheck = new ArrayList<>();
                if(applications.size()>=1){
                    setTitle(application.get(i));
                    messageApp = applications.get(0);
                    modelMessage = database.selectAllMessage();
                    for(n=0;n<modelMessage.size();n++){
                        Application a = modelMessage.get(n).getApplication();
                        if(modelMessage.get(n).getApplication().getNameApp().equals(messageApp.getNameApp())){
                            modelMessageCheck.add(modelMessage.get(n));
                        }
                    }
                }
                adapter = new CustomListAdapter(MainActivity.this, modelMessageCheck, messageApp.getPack());
                listMessage.setAdapter(adapter);
            }
        }
        if (id == R.id.m_etiquette) {

        } else if (id == R.id.m_recommendation) {
        }
        else if (id==99){
            AlertDialog alert = new AlertDialog.Builder(this).create();
            alert.setTitle("Choix du réseau social");
            alert.setCancelable(false);
            Log.i("DebugLenght",""+choiceCheck.length);
            listView = new ListView(MainActivity.this);
            final ChoiceAdapter choiceAdapter = new ChoiceAdapter(MainActivity.this,R.layout.item,choiceName, choiceCheck, choicePack);
            listView.setAdapter(choiceAdapter);
            alert.setView(listView);
            alert.setButton(DialogInterface.BUTTON_POSITIVE, "ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    List<Application> applicationList = database.selectAll();
                    int k,l;
                    Boolean reponse;
                    Intent intent = null;
                    byte[] icon = null;
                    //Enregistrer les applications qui sont cochées dans la BD
                    for (k = 0 ; k < choiceName.size(); k++){
                        reponse = false;
                        //Test pour ne pas inserer les applications déjà insérées
                        for(int j =0; j < applicationList.size(); j++){
                            if(applicationList.get(j).getNameApp().equals(choiceName.get(k))){
                                reponse = true;
                            }
                        }
                        //Si une application n'est pas insérée et qu'elle est cochée elle est insérée

                        if (reponse == false && choiceAdapter.getChoiceCheck()[k]==true){
                            for (l = 0; l < applicationList.size(); l++){
                                menu.removeItem(l);
                            }
                            menu.removeItem(99);
                            Application a = new Application(choiceName.get(k),choicePack.get(k),true);
                            database.insertApplication(a);
                            applicationList = database.selectAllPrint();
                            for (l = 0; l < applicationList.size(); l++){
                                Drawable draw = apkInfoExtractor.getAppIconByPackageName(applicationList.get(l).getPack());
                                menu.add(R.id.application, l, 0,applicationList.get(l).getNameApp()).setIcon(draw);
                                application.put(l,applicationList.get(l).getNameApp());
                            }
                            menu.add(R.id.application,99, 0,"Ajouter réseau social");
                        }
                        else if(reponse == true && choiceAdapter.getChoiceCheck()[k]==false){
                            for (l = 0; l < applicationList.size(); l++){
                                menu.removeItem(l);
                            }
                            menu.removeItem(99);
                            Application a = new Application(choiceName.get(k),choicePack.get(k),true);
                            database.updateAplicationPrint(a,false);
                            applicationList = database.selectAllPrint();
                            for (l = 0; l < applicationList.size(); l++){
                                Drawable draw = apkInfoExtractor.getAppIconByPackageName(applicationList.get(l).getPack());
                                menu.add(R.id.application, l, 0,applicationList.get(l).getNameApp()).setIcon(draw);
                                application.put(l,applicationList.get(l).getNameApp());
                            }
                            menu.add(R.id.application,99, 0,"Ajouter réseau social");
                        }
                        else if(reponse == true && choiceAdapter.getChoiceCheck()[k]==true){
                            for (l = 0; l < applicationList.size(); l++){
                                menu.removeItem(l);
                            }
                            menu.removeItem(99);
                            Application a = new Application(choiceName.get(k),choicePack.get(k),true);
                            database.updateAplicationPrint(a,true);
                            applicationList = database.selectAllPrint();
                            for (l = 0; l < applicationList.size(); l++){
                                Drawable draw = apkInfoExtractor.getAppIconByPackageName(applicationList.get(l).getPack());
                                menu.add(R.id.application, l, 0,applicationList.get(l).getNameApp()).setIcon(draw);
                                application.put(l,applicationList.get(l).getNameApp());
                            }
                            menu.add(R.id.application,99, 0,"Ajouter réseau social");
                        }
                        Log.i("DATABASE","Coche : "+ choiceName.get(k) + " - "+reponse + " - " + choiceAdapter.getChoiceCheck()[k]);
                    }
                }
            });
            alert.show();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    private BroadcastReceiver onNotice= new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            // String pack = intent.getStringExtra("package");
            String title = intent.getStringExtra("title");
            String text = intent.getStringExtra("text");
            String pack = intent.getStringExtra("package");
            Log.i("Debug Package",pack);
            Log.i("Verite1 ",text);
            checkConnection();
            /*
            * messages est une liste de messages ou chaque message est une instance de la classe message
            * ce sont les message non envoyé
            * */
            if(connected==true){
                List<Message> messages = database.getMessageNotSend();
                Log.i("Debug NOTSEND",String.valueOf(messages));
                /*
                c'est ici que tu dois modifier
                le code pour l'envoie des messages non étiquettés
                 */
            }
            Context remotePackageContext = null;
            try {
                byte[] byteArray =intent.getByteArrayExtra("icon");
                Bitmap bmp = null;
                int n=0;
                Application messageApp = null;
                List<Application> applications = database.getApplication(application.get(id));
                if(applications.size()>=1){
                    messageApp = applications.get(0);
                }
                Log.i("Debug Message",""+title);
                if(byteArray !=null) {
                    bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
                }

                Model model = new Model();
                model.setName(title +" " +text);
                model.setImage(bmp);

                if(modelList !=null) {
                    modelList.add(model);
                    adapter.notifyDataSetChanged();
                }else{
                    listMessage=(ListView)findViewById(R.id.list);
                    modelList = new ArrayList<Model>();
                    modelList.add(model);
                    modelMessage = database.selectAllMessage();
                    modelMessageCheck = new ArrayList<>();
                    for(n=0;n<modelMessage.size();n++){
                        if(modelMessage.get(n).getApplication().getNameApp().equals(messageApp.getNameApp())){
                            modelMessageCheck.add(modelMessage.get(n));
                        }
                    }
                    Log.i("Debug MGet",modelMessageCheck.toString());
                    adapter = new CustomListAdapter(MainActivity.this, modelMessageCheck,messageApp.getPack());
                    listMessage.setAdapter(adapter);

                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    private void checkConnection() {
        setConnected(ConnectivityReceiver.isConnected());
    }
    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        setConnected(isConnected);
    }
    public boolean isConnected() {
        return connected;
    }
    public void setConnected(boolean connected) {
        this.connected = connected;
    }


    private List<Message> getMessageCheck(int i) {
        int n;
        List<Application> applications = database.getApplication(application.get(i));
        modelMessageCheck = new ArrayList<>();
        if (applications.size() >= 1) {
            setTitle(application.get(i));
            modelMessage = database.selectAllMessage();
            for (n = 0; n < modelMessage.size(); n++) {
                Application a = modelMessage.get(n).getApplication();
                if (modelMessage.get(n).getApplication().getNameApp().equals(getApplication(i,applications).getNameApp())) {
                    modelMessageCheck.add(modelMessage.get(n));
                }
            }
        }
        return modelMessageCheck;
    }

    private Application getApplication(int i, List<Application> applications){
        Application messageApp = null;
        if (applications.size() >= 1) {
            messageApp = applications.get(i);
        }
        return messageApp;
    }
}