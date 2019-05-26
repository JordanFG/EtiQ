package uy1.info430.etiq.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Debug;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.UpdateBuilder;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import uy1.info430.etiq.checkConnection.ConnectivityReceiver;
import uy1.info430.etiq.myrequest.SessionManager;
import uy1.info430.etiq.myrequest.VolleySingleton;
import uy1.info430.etiq.myrequest.MyRequest;

public class Database extends OrmLiteSqliteOpenHelper implements ConnectivityReceiver.ConnectivityReceiverListener{
    private static final String DATABASE_NAME = "EtiQ.db";
    private static final int DATABASE_VERSION = 37;
    private SessionManager sessionManager;
    private Context context;
    private RequestQueue queue;
    private MyRequest request;
    private boolean connected;

    public Database(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource,Application.class);
            TableUtils.createTable(connectionSource,Message.class);
            TableUtils.createTable(connectionSource,Utilisateur.class);
            Log.i("DATABASE","onCreate invoked");
        }
        catch (Exception e){
            Log.e("DATABASE","Can't create table",e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
            TableUtils.dropTable(connectionSource,Application.class,true);
            TableUtils.dropTable(connectionSource,Message.class,true);
            TableUtils.dropTable(connectionSource,Utilisateur.class,true);
            TableUtils.createTable(connectionSource,Application.class);
            TableUtils.createTable(connectionSource,Message.class);
            TableUtils.createTable(connectionSource,Utilisateur.class);
            Log.i("DATABASE","onUpgrade invoked");
        }
        catch (Exception e){
            Log.e("DATABASE","Can't upgrade table",e);
        }
    }

    public void insertApplication(Application application){
        try {
            Dao<Application,Integer> dao = getDao(Application.class);
            dao.create(application);
            Log.i("DATABASE","Insert "+application.getNameApp());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void insertMessage(Message message){
        try{
            Dao<Message,Integer> dao = getDao(Message.class);
            dao.createIfNotExists(message);
            Log.i("DATABASE","Insert "+message.getTextMessage());
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void insertUtilisateur(Utilisateur utilisateur){
        try {
            Dao<Utilisateur,Integer> dao = getDao(Utilisateur.class);
            dao.create(utilisateur);
            Log.i("DATABASE","Insert "+utilisateur.getUsername());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteApplication(Application application){
        try {
            Dao<Application,Integer> dao = getDao(Application.class);
            dao.delete(application);
            DeleteBuilder<Application,Integer> deleteBuilder = dao.deleteBuilder();
            deleteBuilder.where().eq("NAMEAPP",application.getNameApp());
            deleteBuilder.delete();
            Log.i("DATABASE","Delete "+application.getNameApp());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Application> selectAll() {
        List<Application> applications = new ArrayList<>();
        try {
            Dao<Application,Integer> dao = getDao(Application.class);
            applications = dao.queryForAll();
            Collections.reverse(applications);
            return applications;
        }catch (Exception e) {
            Log.e("DATABASE", "selectAll not invoked", e);
            return applications;
        }
    }

    public List<Application> getApplication(String app){
        List<Application> applications = new ArrayList<>();
        try {
            Dao<Application,Integer> dao = getDao(Application.class);
            QueryBuilder<Application, Integer> queryBuilder =
                    dao.queryBuilder();
            queryBuilder.where().eq("NAMEAPP", app);
            PreparedQuery<Application> preparedQuery = queryBuilder.prepare();
            applications = dao.query(preparedQuery);
            return  applications;
        } catch (SQLException e) {
            e.printStackTrace();
            return applications;
        }
    }

    public List<Application> selectAllPrint(){
        List<Application> applications = new ArrayList<>();
        try {
            Dao<Application,Integer> dao = getDao(Application.class);
            QueryBuilder<Application, Integer> queryBuilder =
                    dao.queryBuilder();
            queryBuilder.where().eq("PRINT", true);
            PreparedQuery<Application> preparedQuery = queryBuilder.prepare();
            applications = dao.query(preparedQuery);
            return  applications;
        } catch (SQLException e) {
            e.printStackTrace();
            return applications;
        }
    }

    public  void updateAplicationPrint(Application application,boolean print){
        try{
            Dao<Application,Integer> dao = getDao(Application.class);
            UpdateBuilder updateBuilder = dao.updateBuilder();
            updateBuilder.where().eq("NAMEAPP",application.getNameApp().replace("'","''")).and().eq("PACK",application.getPack().replace("'","''"));
            updateBuilder.updateColumnValue("PRINT",print);
            updateBuilder.update();
            Log.i("DATABASE","Send"+String.valueOf(application.isPrint()));
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Message> selectAllMessage() {
        List<Message> messages = new ArrayList<>();
        try {
            Dao<Message,Integer> dao = getDao(Message.class);
            messages = dao.queryForAll();
            Collections.reverse(messages);
            return messages;
        }catch (Exception e) {
            Log.e("DATABASE", "selectAll not invoked", e);
            return messages;
        }
    }

    public List<Message> selectMessages(String text) {
        List<Message> messages = new ArrayList<>();
        for(Message m:selectAllMessage()){
            if (m.getTextMessage().toLowerCase().contains(text.toLowerCase()))
                messages.add(m);
        }

        return messages;
    }
    public List<Message> getMessage(String textMessage,String userMessage){
        List<Message> messages = new ArrayList<>();
        try {
            Dao<Message,Integer> dao = getDao(Message.class);
            QueryBuilder<Message, Integer> queryBuilder =
                    dao.queryBuilder();
            queryBuilder.where().eq("TEXTMESSAGE",textMessage.replace("'","''")).and().eq("USERMESSAGE",userMessage.replace("'","''"));
            PreparedQuery<Message> preparedQuery = queryBuilder.prepare();
            messages = dao.query(preparedQuery);
            return  messages;
        } catch (SQLException e) {
            e.printStackTrace();
            return messages;
        }

    }
    public List<Message> getMessageOr(String textMessage,String userMessage){
        List<Message> messages = new ArrayList<>();
        try {
            Dao<Message,Integer> dao = getDao(Message.class);
            QueryBuilder<Message, Integer> queryBuilder =
                    dao.queryBuilder();
            queryBuilder.where().like("TEXTMESSAGE",textMessage.replace("'","''")).or().like("USERMESSAGE",userMessage.replace("'","''"));
            PreparedQuery<Message> preparedQuery = queryBuilder.prepare();
            messages = dao.query(preparedQuery);
            Collections.reverse(messages);
            return  messages;
        } catch (SQLException e) {
            e.printStackTrace();
            return messages;
        }

    }

    public List<Utilisateur> getListUtilisateur() {
        List<Utilisateur> utilisateurs = new ArrayList<>();
        try {
            Dao<Utilisateur,Integer> dao = getDao(Utilisateur.class);
            Collections.reverse(utilisateurs);
            utilisateurs = dao.queryForAll();
            return utilisateurs;
        }catch (Exception e) {
            Log.e("DATABASE", "selectAll not invoked", e);
            return utilisateurs;
        }
    }
    public  void setEtiquette(Message message,String etiquette){
        try{
            Dao<Message,Integer> dao = getDao(Message.class);
            UpdateBuilder updateBuilder = dao.updateBuilder();
            updateBuilder.where().eq("USERMESSAGE",message.getUserMessage().replace("'","''")).and().eq("TEXTMESSAGE",message.getTextMessage().replace("'","''"));
            updateBuilder.updateColumnValue("ETIQUETTE",etiquette);
            updateBuilder.update();
            Log.i("DATABASE","Etiquette "+message.getEtiquette());
            String msg=message.getTextMessage();
            String etiq=message.getEtiquette();
            /*Ici je teste la connexion
            * Mais il est important de tester egalement si l'insertion dans la a ete faite avant de
            * modifier le champs send du message a true
            *
            * */
            checkConnection();
            Log.i("Debug ISconnected",String.valueOf(isConnected()));
            if(isConnected()){
                queue = VolleySingleton.getInstance(context).getRequestQueue();
                request = new MyRequest(context, queue);
                sessionManager=new SessionManager(context);
                final String id=sessionManager.getId();
                Log.i("TEST ENVOIE",id);
                request.envoimessage(msg,id,etiquette);
                /*On doit tester si l'envoie s'est bien passé avant de modifier le champ
                send du message à true
                    if(condition d'insertion dans la bd){
                        setSend(message);
                    }
                 */
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public  void setChoix(Message message,int choix){
        try{
            Dao<Message,Integer> dao = getDao(Message.class);
            UpdateBuilder updateBuilder = dao.updateBuilder();
            updateBuilder.where().eq("USERMESSAGE",message.getUserMessage().replace("'","''")).and().eq("TEXTMESSAGE",message.getTextMessage().replace("'","''"));
            updateBuilder.updateColumnValue("CHOIX",choix);
            updateBuilder.update();
            Log.i("DATABASE","ETIQ_choix"+String.valueOf(message.getChoix()));
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Message> getMessageNotSend(){
        List<Message> messages = new ArrayList<>();
        try {
            Dao<Message,Integer> dao = getDao(Message.class);
            QueryBuilder<Message, Integer> queryBuilder =
                    dao.queryBuilder();
            queryBuilder.where().eq("SEND",false).and().ne("CHOIX",-1);
            PreparedQuery<Message> preparedQuery = queryBuilder.prepare();
            messages = dao.query(preparedQuery);
            Collections.reverse(messages);
            return  messages;
        } catch (SQLException e) {
            e.printStackTrace();
            return messages;
        }
    }

    public  void setSend(Message message){
        try{
            Dao<Message,Integer> dao = getDao(Message.class);
            UpdateBuilder updateBuilder = dao.updateBuilder();
            updateBuilder.where().eq("USERMESSAGE",message.getUserMessage().replace("'","''")).and().eq("TEXTMESSAGE",message.getTextMessage().replace("'","''"));
            updateBuilder.updateColumnValue("SEND",true);
            updateBuilder.update();
            Log.i("DATABASE","Send"+String.valueOf(message.isSend()));
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }


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
}