package com.wicoding.winwebradio;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.app.Notification;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;

import java.io.IOException;

import cz.msebera.android.httpclient.Header;

import static android.provider.ContactsContract.Directory.PACKAGE_NAME;

public class RadioService extends Service {

    final static int IdNotifRadio = 1;
    private NotificationCompat.Builder mBuilder;
    private NotificationManager nManager;
    private RemoteViews remoteViews;

    private final IBinder iBinder =  new LocalBinder();
    private MediaPlayer mediaPlayer;
    private String URL = "http://www.radioking.com/play/win-web-radio";

    @Override
    public void onCreate() {
        try {
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setDataSource(URL);
            mediaPlayer.setVolume((float) (1 - (Math.log(100 - 50) / Math.log(100))),(float) (1 - (Math.log(100 - 50) / Math.log(100))));
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException e) {
            Toast.makeText(getApplicationContext(),"Error of System I/O.",Toast.LENGTH_SHORT).show();
        }
        nManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        mBuilder = new NotificationCompat.Builder(this);
        remoteViews = new RemoteViews(getPackageName(),R.layout.custom_notification);
        remoteViews.setImageViewResource(R.id.imageLauncher,R.mipmap.ic_launcher);
        remoteViews.setImageViewResource(R.id.imageExit,R.drawable.ic_notif_exit);

        Intent button_intent = new Intent("button_click");
        PendingIntent button_pending_event = PendingIntent.getBroadcast(this,IdNotifRadio,
                button_intent,0);

        remoteViews.setOnClickPendingIntent(R.id.imageExit,button_pending_event);

        Intent notification_intent = new Intent(this,MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,IdNotifRadio,notification_intent,0);

        mBuilder.setSmallIcon(R.drawable.mini_icon)
                .setOngoing(true)
                .setCustomContentView(remoteViews)
                .setContentIntent(pendingIntent);

        nManager.notify(IdNotifRadio,mBuilder.build());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return iBinder;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        nManager.cancel(IdNotifRadio);
        if(mediaPlayer!=null && mediaPlayer.isPlaying()){
            mediaPlayer.stop();
        }
    }


    public class LocalBinder extends Binder{
        RadioService getService() {
            return  RadioService.this;
        }
    }

    public void setVolume(float v)
    {
        mediaPlayer.setVolume(v, v);
    }

}
