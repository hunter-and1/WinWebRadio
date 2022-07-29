package com.wicoding.winwebradio;

import android.app.ProgressDialog;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.commons.lang3.StringEscapeUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class ChatActivity extends AppCompatActivity {

    private Handler handler;
    private Runnable runnable;
    private String Id;

    private ArrayList<ChatMessage> mListChatMessage;
    private MyChatMessageAdapter myAdapter;
    private EditText mText;
    private ListView mListMessages;
    private ImageButton mSendMsg;

    private String mUserLID;
    private ProgressDialog progressDialog;

    private void openProgressBar(){
        progressDialog = new ProgressDialog(ChatActivity.this);
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Load...");
        progressDialog.show();
    }

    private void closeProgressBar(){
        if(progressDialog.isShowing())
        progressDialog.dismiss();
    }


    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras != null) {

                mUserLID = extras.getString("LID");

                mListChatMessage = new ArrayList<>();
                mListMessages = (ListView)findViewById(R.id.ListMessages);
                myAdapter = new MyChatMessageAdapter(mListChatMessage);
                mListMessages.setAdapter(myAdapter);

                View sectionHeading = getLayoutInflater().inflate(R.layout.template_message_start, mListMessages, false);
                mListMessages.addHeaderView(sectionHeading);

                mText = (EditText)findViewById(R.id.editTextMsg);
                mSendMsg = (ImageButton)findViewById(R.id.btnSendMsg);
                mSendMsg.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View view) {
                        if(!mText.getText().toString().trim().isEmpty())
                        {

                            AsyncHttpClient client = new AsyncHttpClient();
                            RequestParams requestParams = new RequestParams();
                            requestParams.put("lid", mUserLID);
                            requestParams.put("message", StringEscapeUtils.escapeJava(mText.getText().toString()));
                            Log.d("ErrorSy",mText.getText().toString());
                            mText.setText("");
                            client.post(Config.API_SET_MESSAGE,requestParams,new JsonHttpResponseHandler(){
                                @Override
                                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                    super.onSuccess(statusCode, headers, response);

                                    try {
                                        if(Integer.parseInt(response.get("status").toString()) == 404)
                                        {
                                            Toast.makeText(getApplicationContext(),response.get("data").toString(),Toast.LENGTH_LONG).show();
                                            finish();
                                        }
                                    } catch (JSONException e) {
                                        Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_LONG).show();
                                    }
                                }

                            });

                        }
                    }
                });

                handler = new Handler();
                runnable = new Runnable(){
                    public void run() {

                        if(mListChatMessage.size() != 0)
                        {
                            Id = mListChatMessage.get(mListChatMessage.size()-1).getMessageId();
                            getComments(Config.API_GET_MESSAGES_BY_LAST + Id);
                        }

                        if(mListChatMessage.size() == 0 ) {
                            openProgressBar();
                            getComments(Config.API_GET_LAST_MESSAGES);
                            closeProgressBar();
                        }
                        handler.postDelayed(runnable, 1500);
                    }
                };
                handler.post(runnable);
            }
            else
                finish();
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }
        return super.onOptionsItemSelected(item);
    }

    public void getComments(String UrlApi)
    {
        AsyncHttpClient c = new AsyncHttpClient();
        c.get(UrlApi,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                try {
                    if(Integer.parseInt(response.get("status").toString()) == 200)
                    {
                        JSONArray message = response.getJSONArray("data");

                        for (int i = 0; i < message.length(); i++) {
                            JSONObject temp = message.getJSONObject(i);
                            mListChatMessage.add(new ChatMessage(temp.getString("id"),temp.getString("id_listener"),temp.getString("username"), StringEscapeUtils.unescapeJava(temp.getString("message")),temp.getString("time")));
                        }
                        myAdapter.notifyDataSetChanged();
                    }

                } catch (JSONException e) {
                    Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private class MyChatMessageAdapter extends BaseAdapter {
        public  ArrayList<ChatMessage>  listnewsDataAdpater ;

        public MyChatMessageAdapter(ArrayList<ChatMessage>  listnewsDataAdpater) {
            this.listnewsDataAdpater=listnewsDataAdpater;
        }

        @Override
        public int getCount() {
            return listnewsDataAdpater.size();
        }

        @Override
        public String getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent)
        {
            LayoutInflater mInflater = getLayoutInflater();
            final  ChatMessage s = listnewsDataAdpater.get(position);

            if(!mUserLID.equals(s.getMessageLID())){
                convertView = mInflater.inflate(R.layout.template_message_left, null);
            }
            else {
                convertView = mInflater.inflate(R.layout.template_message_right, null);
            }

            TextView lblMsgFrom = (TextView)convertView.findViewById(R.id.lblMsgFrom);
            TextView txtMsg = (TextView)convertView.findViewById(R.id.txtMsg);
            lblMsgFrom.setText(s.getMessageUser());

            txtMsg.setText(fromHtml(s.getMessageText().replace("\n","<br>")+"<br><small style='float:right'>"+s.getMessageTime()+"</small>"));
            return convertView;
        }

    }

    @SuppressWarnings("deprecation")
    public static Spanned fromHtml(String html){
        Spanned result;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            result = Html.fromHtml(html,Html.FROM_HTML_MODE_LEGACY);
        } else {
            result = Html.fromHtml(html);
        }
        return result;
    }

}
