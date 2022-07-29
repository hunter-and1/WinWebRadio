package com.wicoding.winwebradio;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

public class IndexFragment extends Fragment {

    private final int mPermission_Call = 150;
    private RadioService mService;
    private boolean mBound = false;

    private AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.8F);
    private TextView textTitle;
    private ImageButton BtnPlay,BtnSon,BtnMute;
    private SeekBar seekBar;

    private ImageButton ivFb,ivIN,ivYT,ivWP,ivTL;

    public IndexFragment() {

    }

    @Override
    public void onResume() {
        super.onResume();
        if(!mBound)
        {
            BtnPlay.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.wi_play));
            BtnPlay.setTag("play");
        }
        else
        {
            BtnPlay.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.wi_stop));
            BtnPlay.setTag("stop");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_index, container, false);
        BtnPlay = (ImageButton) rootView.findViewById(R.id.imageBtnPlay);
        BtnSon = (ImageButton) rootView.findViewById(R.id.imageBtnSon);
        BtnMute = (ImageButton) rootView.findViewById(R.id.imageBtnMute);
        seekBar = (SeekBar) rootView.findViewById(R.id.seekBar);
        textTitle  = (TextView) rootView.findViewById(R.id.textTitle);

        ivFb = (ImageButton) rootView.findViewById(R.id.btnFB);
        ivIN = (ImageButton) rootView.findViewById(R.id.btnInsta);
        ivYT = (ImageButton) rootView.findViewById(R.id.btnYt);
        ivWP = (ImageButton) rootView.findViewById(R.id.btnWeb);
        ivTL = (ImageButton) rootView.findViewById(R.id.btnTel);

        seekBar.setEnabled(false);
        textTitle.setSelected(true);

        ivFb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(buttonClick);
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setPackage("com.facebook.katana");
                    intent.setData(Uri.parse(Config.LINK_FACEBOOK));
                    startActivity(intent);
                } catch(Exception e) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(Config.LINK_FACEBOOK)));
                }
            }
        });
        ivWP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(buttonClick);
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(Config.LINK_SITEWEB)));
            }
        });

        ivYT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(buttonClick);
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setPackage("com.google.android.youtube");
                    intent.setData(Uri.parse(Config.LINK_YOUTUBE));
                    startActivity(intent);
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(Config.LINK_YOUTUBE)));
                }

            }
        });

        ivIN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(buttonClick);
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setPackage("com.instagram.android");
                    intent.setData(Uri.parse(Config.LINK_INSTGRAM));
                    startActivity(intent);
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(Config.LINK_INSTGRAM)));
                }

            }
        });

        ivTL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(buttonClick);
                onCall(Config.LINK_PHONE);
            }
        });


        BtnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(buttonClick);
                if(BtnPlay.getTag().toString().equals("play"))
                    clickPlay();
                else
                    clickStop();
            }
        });
        BtnSon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(buttonClick);
                if(mBound){
                    if(seekBar.getProgress() + 10 <= 100)
                        seekBar.setProgress(seekBar.getProgress()+10);
                    else
                        seekBar.setProgress(100);
                    setVolumeMediaPlay(seekBar.getProgress());
                }

            }
        });
        BtnMute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(buttonClick);
                if(mBound){
                    if(seekBar.getProgress() - 10 >= 0)
                        seekBar.setProgress(seekBar.getProgress()-10);
                    else
                        seekBar.setProgress(0);
                    setVolumeMediaPlay(seekBar.getProgress());
                }

            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                setVolumeMediaPlay(seekBar.getProgress());
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        return rootView;
    }

    public void onCall(String strCall) {
        int permissionCheck = ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.CALL_PHONE);

        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.CALL_PHONE},mPermission_Call );
        } else {
            startActivity(new Intent(Intent.ACTION_CALL).setData(Uri.parse("tel:"+strCall)));
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,String[] permissions,int[] grantResults) {
        switch (requestCode) {

            case mPermission_Call:
                if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    onCall(Config.LINK_PHONE);
                } else
                    Toast.makeText(getContext(),"n'avez pas l'autorisation d'accéder l'appel. et merci.",Toast.LENGTH_LONG).show();
                break;
            default:
                break;
        }
    }
    private void clickPlay()
    {
        if(!mBound) {
            seekBar.setEnabled(true);
            getActivity().bindService(new Intent(getContext(), RadioService.class), mConnection, Context.BIND_AUTO_CREATE);
            BtnPlay.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.wi_stop));
            BtnPlay.setTag("stop");
        }
    }

    private void clickStop()
    {
        if(mBound) {
            seekBar.setEnabled(false);
            getActivity().unbindService(mConnection);
            mBound = false;
            BtnPlay.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.wi_play));
            BtnPlay.setTag("play");
        }
    }

    private void setVolumeMediaPlay(int soundVolume)
    {
        final float volume = (float) (1 - (Math.log(100 - soundVolume) / Math.log(100)));
        mService.setVolume(volume);
    }

    private ServiceConnection mConnection = new ServiceConnection(){

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            RadioService.LocalBinder binder = (RadioService.LocalBinder) iBinder;
            mService = binder.getService();
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mBound = false;
        }
    };

}
