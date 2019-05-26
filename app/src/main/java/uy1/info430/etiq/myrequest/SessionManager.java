package uy1.info430.etiq.myrequest;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {

    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;
    private final  static String PREFS_NAME="app_prefs";
    private final static int PRIVATE_MODE=0;
    private final static String IS_LOGGED="islogged";
    private final static String PSEUDO="pseudo";
    private final static String ID ="id";
    private final static String SELECT_APPLICATION="false";
    private Context context;
    public SessionManager(Context context)
    {
        this.context=context;
        prefs=context.getSharedPreferences(PREFS_NAME,PRIVATE_MODE);
        editor=prefs.edit();
    }
    public  boolean islogged(){
        return  prefs.getBoolean(IS_LOGGED,false);
    }
    public String getPseudo(){
        return  prefs.getString(PSEUDO,null);
    }
    public  String getId(){
        return  prefs.getString(ID,null);
    }
    public void insertUser(String id,String pseudo){
        editor.putBoolean(IS_LOGGED,true);
        editor.putString(ID,id);
        editor.putString(PSEUDO,pseudo);
        editor.commit();
    }
    public void selectApplication(){
        editor.putBoolean(SELECT_APPLICATION,true);
        editor.commit();
    }
    public boolean isSelectApplication(){
        return  prefs.getBoolean(SELECT_APPLICATION,false);
    }
    public void logout(){
        editor.putBoolean(IS_LOGGED,false);
        editor.putString(ID,null);
        editor.putString(PSEUDO,null);
        editor.commit();
    }
}
