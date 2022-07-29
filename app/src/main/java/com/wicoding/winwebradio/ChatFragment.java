package com.wicoding.winwebradio;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;


public class ChatFragment extends Fragment{


    private Button button2;

    public ChatFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_chat, container, false);

        button2 = (Button)rootView.findViewById(R.id.button2);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AsyncHttpClient c = new AsyncHttpClient();
                c.get(Config.API_LOGIN+Config.getUID(getContext()),new JsonHttpResponseHandler(){
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        try {
                            switch (Integer.parseInt(response.get("status").toString()))
                            {
                                case 100:
                                    JSONObject Listener = response.getJSONObject("data");
                                    Intent mainIntent = new Intent(getActivity(),ChatActivity.class);
                                    mainIntent.putExtra("LID",Listener.get("id").toString());
                                    startActivity(mainIntent);
                                    break;
                                case 200:
                                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                                    builder.setTitle("Veuillez entrer votre nom d'utilisateur:");

                                    final EditText input = new EditText(getContext());

                                    input.setInputType(InputType.TYPE_CLASS_TEXT );
                                    builder.setView(input);

                                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            if(!input.getText().toString().trim().isEmpty())
                                            {
                                                AsyncHttpClient client = new AsyncHttpClient();
                                                RequestParams requestParams = new RequestParams();
                                                requestParams.put("uid", Config.getUID(getContext()));
                                                requestParams.put("username", input.getText().toString());

                                                client.post(Config.API_SET_LOGIN,requestParams,new JsonHttpResponseHandler(){
                                                    @Override
                                                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                                        super.onSuccess(statusCode, headers, response);

                                                        try {
                                                            if(Integer.parseInt(response.get("status").toString()) == 404)
                                                            {
                                                                Toast.makeText(getContext(),response.get("data").toString(),Toast.LENGTH_LONG).show();
                                                            }
                                                            else
                                                            {
                                                                Integer Listener = response.getInt("data");
                                                                Intent mainIntent = new Intent(getActivity(),ChatActivity.class);
                                                                mainIntent.putExtra("LID",Listener.toString());
                                                                startActivity(mainIntent);
                                                            }
                                                        } catch (JSONException e) {
                                                            Toast.makeText(getContext(),e.toString(),Toast.LENGTH_LONG).show();
                                                        }
                                                    }

                                                });
                                            }

                                        }
                                    });
                                    builder.setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.cancel();
                                        }
                                    });

                                    builder.show();
                                    break;
                                case 300:Toast.makeText(getContext(),response.get("data").toString(),Toast.LENGTH_LONG).show(); break;
                            }

                            //Log.d("ErrorSy",Config.API_LOGIN+Config.getUID(getContext()));

                        } catch (JSONException e) {
                            //7488565c6a9052ad
                            Toast.makeText(getContext(),"Contactez-nous. Error N°: 07",Toast.LENGTH_SHORT).show();
                            //Log.d("ErrorSy",response.toString());
                            //Log.d("ErrorSy",e.toString());
                        }
                    }

                });
            }
        });


        return rootView;
    }

}
