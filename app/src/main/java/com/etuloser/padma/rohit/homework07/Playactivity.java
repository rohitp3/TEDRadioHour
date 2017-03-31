package com.etuloser.padma.rohit.homework07;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Playactivity extends AppCompatActivity {

    TextView des,pub,dur,tit;
    ImageView  iv;
    ImageView ivb;
    SeekBar sb;
MediaPlayer mp =null;
    CustomObject co=null;
    Handler handler=new Handler();
    ProgressDialog pd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playactivity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        co=new CustomObject();
        pd=new ProgressDialog(Playactivity.this);
        sb=(SeekBar)findViewById(R.id.sbplay);
        iv=(ImageView)findViewById(R.id.imagepview);
        ivb=(ImageView)findViewById(R.id.pplaynow);
        tit=(TextView)findViewById(R.id.txtptitle);
        des=(TextView)findViewById(R.id.txtpdes);
        dur=(TextView)findViewById(R.id.txtpduration);
        pub=(TextView)findViewById(R.id.txtppubdate);
if(getIntent().getExtras()!=null)
{
    co=(CustomObject)getIntent().getExtras().getSerializable("obj");
}

sb.setMax(100);
tit.setText(co.getTitle());
        //SpannableStringBuilder builder = new SpannableStringBuilder();
        //SpannableString str1= new SpannableString("Description:");
        //str1.setSpan(new ForegroundColorSpan(Color.rgb(9,54,24)), 0, str1.length(), 0);
        //builder.append(str1);
       // builder.append(" "+co.getDescription());

        String desc = "<font color='#05275e'>Description: </font>";
        des.setText(Html.fromHtml(desc + co.getDescription()));
        String dura="<font color='#05275e'>Duration: "+co.getDuration()+"</font>";
        dur.setText(Html.fromHtml(dura));
        SimpleDateFormat parserDate = new SimpleDateFormat("EEE,d,MMM,yyyy");
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
        String input = co.getPubdate();

        if(input.length()>0) {
            Date date = null;
            try {
                date = parserDate.parse(input);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            String formattedDate = formatter.format(date);
         String publ="<b>Publication Date: "+formattedDate+"</b>";
            pub.setText(Html.fromHtml(publ));
        }
        else
        {
            pub.setText("Publication Date: ");
        }


        Picasso.with(this).load(co.getImageurl()).into(iv);

    }

    public void playradio(View v) throws IOException {

        Runnable mRunnable=null;

        if(MainActivity.mp!=null)
        {
            MainActivity.ll.setVisibility(View.INVISIBLE);
            MainActivity.mp.stop();
        }

        if(mp==null)
        {

            //pd.setTitle("Loading...");
            mp=new MediaPlayer();
            mp.setDataSource(co.getAudiourl());
          /*  mp.prepareAsync();
            mp.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mp.start();
              //      pd.hide();
                }
            });*/
            mp.prepare();
            mp.start();

            sb.setMax(mp.getDuration());
            mRunnable = new Runnable() {
                @Override
                public void run() {
                    if (mp != null) {
                        int mcurrp = mp.getCurrentPosition();
                        sb.setProgress(mcurrp);
                        Log.d("show",String.valueOf(mcurrp));
                        if(mp.getDuration()<=sb.getMax())
                        {
                            ivb.setImageResource(R.drawable.pplay);
                            mp.reset();
                        }

                        handler.postDelayed(this, 1000);
                    }
                }
            };
            //handler.postDelayed(mRunnable, 1000);


        }
        if(ivb.getTag().equals("1")) {
            ivb.setImageResource(R.drawable.pauseicon);
            ivb.setTag("2");
      //      pd.hide();
//            handler.removeCallbacks(mRunnable);
       if(!mp.isPlaying())
       { mp.start(); }
            Log.d("Play","Play");
           // handler.postDelayed(mRunnable,mp.getCurrentPosition());
            handler.postAtTime(mRunnable,mp.getCurrentPosition());
        }
        else
        {
            ivb.setImageResource(R.drawable.pplay);
            ivb.setTag("1");
        //    pd.hide();
           mp.pause();

            handler.removeCallbacks(mRunnable);
            //Log.d("paused","Paused");
        }

        sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
/*
                if(mp.isPlaying())
                {

                  //  mp.seekTo((Integer)(progress/100)*mp.getDuration());
                  //  sb.setProgress(progress);


                }
                else
                {
                    mp.seekTo(mp.getCurrentPosition());
                    mp.start();
                }
            */
                if (mp.isPlaying()) {
                    sb.setProgress(progress);
                    mp.seekTo(progress);
                    mp.start();
                } else {
                    sb.setProgress(progress);
                    mp.seekTo(progress);
                }

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
//                handler.removeCallbacks(mUpdateTimeTask);
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
               // handler.removeCallbacks(mUpdateTimeTask);
                //mp.seekTo(seekBar.getProgress());
               // handler.postDelayed(mUpdateTimeTask,1000);


            }
        });

    }



/*
    @Override
    protected void onStop() {

        super.onStop();
        if(mp!=null) {
            mp.stop();
        }
        this.finish();
    }
    */
    @Override
    protected void onPause() {


        if(mp!=null)
        {        mp.stop();}

        super.onPause();
        finish();
      //  Intent i=new Intent(this,MainActivity.class);
        //startActivity(i);
    }

    @Override
    protected void onStop() {
        if(mp!=null)
        {        mp.stop();}

finish();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        if(mp!=null)
        {
            mp.stop();
        }
        finish();
        super.onDestroy();
    }
}
