package uy1.info430.etiq.listApplication;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import uy1.info430.etiq.R;
import uy1.info430.etiq.listApplication.ApkInfoExtractor;

public class ChoiceAdapter extends ArrayAdapter<String> {
    private Context context;
    private int resource;
    private List<String> choiceName;
    private boolean[] choiceCheck;
    private List<String> choicePack;
    private List<String> choiceNameTmp=new ArrayList<>();
    private boolean[] choiceCheckTmp;
    private List<String> choicePackTmp=new ArrayList<>();
    private Button button;

    public ChoiceAdapter(@NonNull Context context, int resource, List<String> choiceName, boolean[] choiceCheck,List<String> choicePack)  {
        super(context, resource, choiceName);
        int i;
        this.context = context;
        this.resource = resource;
        this.choiceName = choiceName;
        this.choiceCheck = choiceCheck;
        this.choicePack = choicePack;

        this.choiceNameTmp.addAll(choiceName);
        this.choiceCheckTmp = new boolean[choiceCheck.length];
        for(i=0;i<choiceCheck.length;i++){
            this.choiceCheckTmp[i]=choiceCheck[i];
        }
        this.choiceCheckTmp=choiceCheck;
        this.choicePackTmp.addAll(choicePack);

    }
    public void onChange(String application){
        int i=0,j=0;
        choiceName.clear();
        choiceCheck = new boolean[choiceCheckTmp.length];
        choicePack.clear();
        for(String a:choiceNameTmp){
            if (a.toLowerCase().contains(application.toLowerCase())){
                choiceName.add(a);
                choiceCheck[i]=choiceCheckTmp[j];
                choicePack.add(choicePackTmp.get(j));
                i++;
            }
            j++;
        }
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public View getView(final int position, @Nullable final View convertView, @NonNull ViewGroup parent) {
        button = (Button) ((Activity)context).findViewById(R.id.btnNext);
        final Boolean[] reponseCheck = new Boolean[1];
        reponseCheck[0]=false;
        View view = convertView;
        ApkInfoExtractor apkInfoExtractor = new ApkInfoExtractor(context);

        if(view == null){
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            view = inflater.inflate(resource,parent, false);
        }
        String choice = choiceName.get(position);
        if(choice!=null){
            Log.i("Debug",""+position+" -- "+choice);
            TextView choiceText = (TextView) view.findViewById(R.id.choiceText);
            ImageView imageView = (ImageView) view.findViewById(R.id.iconApp);
            final CheckBox checkBox = (CheckBox) view.findViewById(R.id.checkBox);
            checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (checkBox.isChecked()){
                        choiceCheck[position]=true;
                        if(((Activity) context).getLocalClassName().equals("SelectApp")){
                            button.setVisibility(View.VISIBLE);
                        }
                    }
                    else {
                        choiceCheck[position]=false;
                        for(int i=0;i<choiceCheck.length;i++){
                            if(choiceCheck[i]){
                                reponseCheck[0] = true;
                            }
                        }
                        if (!reponseCheck[0]){
                            button.setVisibility(View.INVISIBLE);
                        }


                    }
                }
            });
            checkBox.setChecked(choiceCheck[position]);
            Drawable drawable = apkInfoExtractor.getAppIconByPackageName(choicePack.get(position));
            checkBox.setTag(position);
            choiceText.setText(choice);
            imageView.setImageDrawable(drawable);
        }
        return view;
    }


    @Override
    public int getPosition(@Nullable String item) {
        return super.getPosition(item);
    }

    @NonNull
    @Override
    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public int getResource() {
        return resource;
    }

    public void setResource(int resource) {
        this.resource = resource;
    }

    public List<String> getChoiceName() {
        return choiceName;
    }

    public void setChoiceName(List<String> choiceName) {
        this.choiceName = choiceName;
    }

    public boolean[] getChoiceCheck() {
        return choiceCheck;
    }

    public void setChoiceCheck(boolean[] choiceCheck) {
        this.choiceCheck = choiceCheck;
    }

    public boolean isChecked(int position){
        return choiceCheck[position];
    }




}
