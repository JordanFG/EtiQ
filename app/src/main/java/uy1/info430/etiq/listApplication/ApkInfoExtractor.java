package uy1.info430.etiq.listApplication;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import uy1.info430.etiq.R;

/**
 * Created by Juned on 4/15/2017.
 */

public class ApkInfoExtractor {

    Context context1;
    private String[] listSocialNetwork = {
            "com.whatsapp",
            "com.facebook.katana",
            "com.facebook.orca",
            "com.instagram.android",
            "com.twitter.android",
            "com.skype.raider",
            "com.pinterest",
            "com.imo.android.imoim",
            "com.linkedin.android",
            "org.telegram.messenger",
            "com.google.android.youtube",
            "jp.naver.line.android",
            "com.snapchat.android",
            "com.tencent.mm",
            "com.google.android.talk"
    };

    public ApkInfoExtractor(Context context2){

        context1 = context2;
    }
    public List<String> GetAllInstalledApkInfo(){

        List<String> ApkPackageName = new ArrayList<>();

        Intent intent = new Intent(Intent.ACTION_MAIN,null);

        intent.addCategory(Intent.CATEGORY_LAUNCHER);

        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED );

        List<ResolveInfo> resolveInfoList = context1.getPackageManager().queryIntentActivities(intent,0);

        for(ResolveInfo resolveInfo : resolveInfoList){

            ActivityInfo activityInfo = resolveInfo.activityInfo;


            if (Arrays.asList(listSocialNetwork).contains(activityInfo.applicationInfo.packageName)) {
//                boolean contains = Arrays.stream(listSocialNetwork).anyMatch(getAppIconByPackageName::equals);
                ApkPackageName.add(activityInfo.applicationInfo.packageName);

            }

        }

        return ApkPackageName;

    }

    public boolean isSystemPackage(ResolveInfo resolveInfo){

        return ((resolveInfo.activityInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0);
    }

    public Drawable getAppIconByPackageName(String ApkTempPackageName){

        Drawable drawable;

        try{
            drawable = context1.getPackageManager().getApplicationIcon(ApkTempPackageName);

        }
        catch (PackageManager.NameNotFoundException e){

            e.printStackTrace();

            drawable = ContextCompat.getDrawable(context1, R.mipmap.ic_launcher);
        }
        return drawable;
    }

    public String GetAppName(String ApkPackageName){

        String Name = "";

        ApplicationInfo applicationInfo;

        PackageManager packageManager = context1.getPackageManager();

        try {

            applicationInfo = packageManager.getApplicationInfo(ApkPackageName, 0);

            if(applicationInfo!=null){

                Name = (String)packageManager.getApplicationLabel(applicationInfo);
            }

        }catch (PackageManager.NameNotFoundException e) {

            e.printStackTrace();
        }
        return Name;
    }

    public List<String> getListApplication() {
        int i;
        List<String> lApp = new ArrayList<>();

        for(i=0;i<GetAllInstalledApkInfo().size();i++){
            lApp.add(GetAppName(GetAllInstalledApkInfo().get(i)));
        }

        return lApp;
    }


}
