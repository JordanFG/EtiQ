package uy1.info430.etiq.getNotification;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import uy1.info430.etiq.checkConnection.ConnectivityReceiver;
import uy1.info430.etiq.database.Database;
import uy1.info430.etiq.database.Message;
import uy1.info430.etiq.R;
import uy1.info430.etiq.listApplication.ApkInfoExtractor;

/**
 * Created by mukesh on 18/5/15.
 */
public class CustomListAdapter extends BaseAdapter {

    Context context;
    List<Message> modelList;
    private String pack;
    private int post;
    private String selection;
    private int in;
    private Database database;
    private boolean connected;


    public CustomListAdapter(Context context, List<Message> modelList, String pack) {
        this.context = context;
        this.modelList = modelList;
        this.pack = pack;
        this.in = -1;
    }

    @Override
    public int getCount() {
        return modelList.size();
    }

    @Override
    public Object getItem(int position) {
        return modelList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    public void onChange(String s){
        database = new Database(context);
        modelList = database.selectMessages(s);
        Log.i("MESSAGES",modelList.toString());
        notifyDataSetChanged();
    }

    @SuppressLint("SetTextI18n")
    public View getView(final int position, final View view, final ViewGroup parent) {

        final Database database = new Database(context);
        ApkInfoExtractor apkInfoExtractor = new ApkInfoExtractor(context);
        final Drawable drawable = apkInfoExtractor.getAppIconByPackageName(pack);
        LayoutInflater inflater=(LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        final View rowView=inflater.inflate(R.layout.list_item, null,true);
        final TextView txtTitle = (TextView) rowView.findViewById(R.id.Itemname);
        final TextView txtContaint = (TextView) rowView.findViewById(R.id.Itemtext);
        final ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);
        final ImageView imageEtiq = (ImageView) rowView.findViewById(R.id.icon_etiq);
        final Message m = modelList.get(position);
        selection = "";
        Log.i("Debug ETIQUETAGE", String.valueOf((m.getEtiquette()==null)));
        if(m.getEtiquette()!=null){
            Log.i("Debug ETIQ_TRUE", m.getEtiquette());
            if(m.getEtiquette().equals("Bon")){
                Drawable drawable1 = context.getResources().getDrawable(R.drawable.ic_done_all_black_bon_24dp);
                imageEtiq.setVisibility(View.VISIBLE);
                imageEtiq.setImageDrawable(drawable1);
                in=0;
            }
            else if(m.getEtiquette().equals("Haine")){
                Drawable drawable1 = context.getResources().getDrawable(R.drawable.ic_done_all_black_haine_24dp);
                imageEtiq.setVisibility(View.VISIBLE);
                imageEtiq.setImageDrawable(drawable1);
                in=1;
            }else if(m.getEtiquette().equals("Fake")){
                Drawable drawable1 = context.getResources().getDrawable(R.drawable.ic_done_all_black_24dp);
                imageEtiq.setVisibility(View.VISIBLE);
                imageEtiq.setImageDrawable(drawable1);
                in=2;
            }

        }

        if(m.getTextMessage().length()>32){
            txtContaint.setText(m.getTextMessage().substring(0,32)+"...".replace('\n',' '));
        }
        else {
            txtContaint.setText(m.getTextMessage().replace('\n',' '));
        }
        txtTitle.setText(m.getUserMessage().split(":")[0].split("\\(")[0]);
        imageView.setImageDrawable(drawable);
        rowView.setTag(m.getChoix());
//        if(m != null && m.getImage() !=null)
//            imageView.setImageBitmap(m.getImage());
        rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int c;
                final View view1 = v;
                final String[] choices = {
                        "Bon",
                        "Haine",
                        "Fake"
                };
                c = (int) v.getTag();
                AlertDialog.Builder alert = new AlertDialog.Builder(context);
                alert.setTitle("Etiquetage");
                alert.setSingleChoiceItems(choices, c, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        selection = choices[which];
                    }
                });
                alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(selection==null){
                            Toast.makeText(context.getApplicationContext(),"Vous n'avez pas etiquetez ce message",Toast.LENGTH_LONG).show();
                        }
                        else {
                            Log.i("Degug WHICH", String.valueOf(which));
                            if(selection.equals("Bon")){
                                Drawable drawable1 = context.getResources().getDrawable(R.drawable.ic_done_all_black_bon_24dp);
                                imageEtiq.setVisibility(View.VISIBLE);
                                imageEtiq.setImageDrawable(drawable1);
                                database.setEtiquette(modelList.get(position),"Bon");
                                modelList.get(position).setEtiquette("Bon");
                                database.setChoix(modelList.get(position),0);
                                modelList.get(position).setChoix(0);
                                Log.i("Debug ETIQUETTE",modelList.get(position).getEtiquette());
                                modelList.set(position,modelList.get(position));
                                view1.setTag(0);

                            }
                            else if(selection.equals("Haine")){
                                Drawable drawable1 = context.getResources().getDrawable(R.drawable.ic_done_all_black_haine_24dp);
                                imageEtiq.setVisibility(View.VISIBLE);
                                imageEtiq.setImageDrawable(drawable1);
                                database.setEtiquette(modelList.get(position),"Haine");
                                modelList.get(position).setEtiquette("Haine");
                                database.setChoix(modelList.get(position),1);
                                modelList.get(position).setChoix(1);
                                modelList.set(position,modelList.get(position));
                                view1.setTag(1);
                            }
                            else if(selection.equals("Fake")){
                                Drawable drawable1 = context.getResources().getDrawable(R.drawable.ic_done_all_black_24dp);
                                imageEtiq.setVisibility(View.VISIBLE);
                                imageEtiq.setImageDrawable(drawable1);
                                database.setEtiquette(modelList.get(position),"Fake");
                                modelList.get(position).setEtiquette("Fake");
                                database.setChoix(modelList.get(position),2);
                                modelList.get(position).setChoix(2);
                                modelList.set(position,modelList.get(position));
                                view1.setTag(2);
                            }
                        }
                        Toast.makeText(context.getApplicationContext(),"Vous avez etiqueté : "+selection,Toast.LENGTH_LONG).show();
                        selection = null;
                    }
                });
                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(context.getApplicationContext(),"Vous n'avez pas etiquetez ce message",Toast.LENGTH_LONG).show();
                    }
                });
                alert.create().show();
            }
        });

        return rowView;
    };


}