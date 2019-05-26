package uy1.info430.etiq.getNotification;

import android.annotation.TargetApi;
import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import uy1.info430.etiq.listApplication.ApkInfoExtractor;
import uy1.info430.etiq.database.Application;
import uy1.info430.etiq.database.Database;
import uy1.info430.etiq.database.Message;


@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
public class NotificationService extends NotificationListenerService {

    Context context;
    private Database database;
    private List<Application> applications;
    @Override

    public void onCreate() {

        super.onCreate();
        context = getApplicationContext();

    }
    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        String pack = sbn.getPackageName();
        String ticker ="";
        String characterFilter = "[^\\p{L}\\p{M}\\p{N}\\p{P}\\p{Z}\\p{Cf}\\p{Cs}\\s]";
        int n=0;
        if(sbn.getNotification().tickerText !=null) {
            ticker = sbn.getNotification().tickerText.toString();
        }
        Bundle extras = sbn.getNotification().extras;
        if(extras.getString("android.title")!=null &&  extras.getString("android.text")!=null){
            String title = extras.getString("android.title").replaceAll(characterFilter,"");
            String text = extras.getCharSequence("android.text").toString().replaceAll(characterFilter,"");
            int id1 = extras.getInt(Notification.EXTRA_SMALL_ICON),i;
            Bitmap id = sbn.getNotification().largeIcon;


            Log.i("Package",pack);
            Log.i("Ticker",ticker);
            Log.i("Title",title);
            Log.i("Text",text);

            Intent msgrcv = new Intent("Msg");
            msgrcv.putExtra("package", pack);
            msgrcv.putExtra("ticker", ticker);
            msgrcv.putExtra("title", title);
            msgrcv.putExtra("text", text);

            Map<Integer,String> application = new HashMap<>();
            ApkInfoExtractor apkInfoExtractor = new ApkInfoExtractor(context);
            database = new Database(this);

            applications = database.getApplication(apkInfoExtractor.GetAppName(pack));
            if(applications.size()>=1){

                List<Message> messages = database.getMessage(text,title);
                Log.i("Debug TextMessage",text);
                Log.i("Debug TestEq",messages.toString());
                if(messages.isEmpty()){
                    Message message = new Message(text,title,applications.get(0),-1,null,false);
                    database.insertMessage(message);
                }
            }
            if(id != null) {
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                id.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] byteArray = stream.toByteArray();
                msgrcv.putExtra("icon",byteArray);
            }
            LocalBroadcastManager.getInstance(context).sendBroadcast(msgrcv);
        }
    }

    @Override

    public void onNotificationRemoved(StatusBarNotification sbn) {
        Log.i("Msg","Notification Removed");

    }
}
