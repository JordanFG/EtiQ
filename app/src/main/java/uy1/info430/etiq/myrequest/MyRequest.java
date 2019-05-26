package uy1.info430.etiq.myrequest;


import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MyRequest {
    //ici je gere les fonctions pour la connection et linscriptoion

    private Context context;
    private RequestQueue queue;

    public MyRequest(Context context, RequestQueue queue) {
        this.context = context;
        this.queue = queue;
    }

    public void register(final String pseudo, final String email, final String password, final String password2, final ResgisterCallback callback){

        String url="http://192.168.43.27:2145/etiq/create.php";
        //creation de la requete
        StringRequest request =new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Map<String,String> errors=new HashMap<>();

                try {
                    JSONObject json =new JSONObject(response);
                    Boolean error= json.getBoolean("error");
                    if (!error)
                    {
                        //l'inscription cest bien passé
                        callback.onSuccess("linscription cest bien passé");
                    }else{
                            JSONObject messages=json.getJSONObject("message");
                            if(messages.has("pseudo"))
                            {
                                errors.put("pseudo",messages.getString("pseudo"));
                            }
                        if(messages.has("email"))
                        {
                            errors.put("email",messages.getString("email"));
                        }
                        if(messages.has("password"))
                        {
                            errors.put("password",messages.getString("password"));
                        }
                        callback.inputErrors(errors);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }



            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if(error instanceof NetworkError){
                    callback.onError("impossible de se connecter");
                }else if (error instanceof VolleyError){
                    callback.onError("une erreur sest produit");
                }
            }
        }){
            @Override
            //cest ici que jenvoi les parametre
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String,String> map =new HashMap<>();
                map.put("pseudo", pseudo);
                map.put("email", email);
                map.put("password", password);
                map.put("password2", password2);

                return map;
            }



        };

        queue.add(request);


    }


    public interface ResgisterCallback
    {
        void onSuccess(String message);
        void inputErrors(Map<String, String> errors);
        void  onError(String message);
    }

    public  void connection(final String pseudo, final String password, final Logincallback callback){
        String url="http://192.168.43.27:2145/etiq/use.php";
        //creation de la requete
        StringRequest request =new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                JSONObject json= null;
                try {
                    json = new JSONObject(response);
                    Boolean  error=json.getBoolean("error");
                    if(!error){
                        String id=json.getString("id");
                        String pseudo=json.getString("pseudo");
                        callback.onSuccess(id,pseudo);
                    }else {
                        callback.onError(json.getString("message"));
                    }




                } catch (JSONException e) {
                    callback.onError("une erreur sest produit 2");
                    e.printStackTrace();
                }



            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if(error instanceof NetworkError){
                    callback.onError("impossible de se connecter");
                }else if (error instanceof VolleyError){
                    callback.onError("une erreur sest produit");
                }
            }
        }){
            @Override
            //cest ici que jenvoi les parametre
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String,String> map =new HashMap<>();
                map.put("pseudo", pseudo);

                map.put("password", password);


                return map;
            }



        };

        queue.add(request);

    }
    public interface Logincallback{
        void onSuccess(String id, String pseudo);
        void  onError(String message);
    }



    public  void envoimessage(final String message, final String id,final String etiquette ){
        String url="http://192.168.43.27:2145/etiq/envoi.php";
        //creation de la requete
        StringRequest request =new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                JSONObject json= null;
                try {
                    json = new JSONObject(response);
                    Boolean  error=json.getBoolean("error");
                    if(!error){

                        String pseudo=json.getString("message");

                    }else {

                    }
                } catch (JSONException e) {

                    e.printStackTrace();
                }



            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if(error instanceof NetworkError){

                }else if (error instanceof VolleyError){

                }
            }
        }){
            @Override
            //cest ici que jenvoi les parametre
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String,String> map =new HashMap<>();
                map.put("message", message);

                map.put("id", id);
                map.put("etiquette",etiquette);


                return map;
            }



        };

        queue.add(request);

    }




}
