package uy1.info430.etiq;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import java.util.List;

import uy1.info430.etiq.database.Application;
import uy1.info430.etiq.database.Database;
import uy1.info430.etiq.listApplication.ApkInfoExtractor;

public class App {

    private Context context;
    private Database database;
    public static ApkInfoExtractor apkInfoExtractor;
    public static List<String> applications;
    public static List<String> applicationsPack;
    public App(Context context, Database database) {
        this.context = context;
        this.database = database;
        this.apkInfoExtractor = new ApkInfoExtractor(context);
    }
    public static List<String> getChoiceExempleName(){
        if(applications==null){
            applications = apkInfoExtractor.getListApplication();
        }
        return applications;
    }
    public List<String> getChoiceExemplePack(){
        if(applicationsPack==null){
            applicationsPack = apkInfoExtractor.GetAllInstalledApkInfo();
        }
        return applicationsPack;
    }

    public boolean[] getListExampleChoice(){

        boolean[] choiceCheck;
        List<Application> applicationList = database.selectAll();
        choiceCheck = new boolean[getChoiceExempleName().size()];
        for (int k = 0 ; k < getChoiceExempleName().size(); k++){
            for(int j =0; j < applicationList.size(); j++) {
                if(applicationList.get(j).getNameApp().equals(getChoiceExempleName().get(k))){
                    choiceCheck[k]=true;
                }
            }
        }
        return choiceCheck;
    }


    public Database getDatabase() {
        return database;
    }

    public void setDatabase(Database database) {
        this.database = database;
    }

    public void dialog(String message, final String afaire){
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle(R.string.app_name);
        alertDialog.setMessage(message);
        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE,"OK", new AlertDialog.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(context.getApplicationContext(),afaire, Toast.LENGTH_LONG).show();
            }
        });
        alertDialog.show();
    }
}
