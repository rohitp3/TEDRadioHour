package com.etuloser.padma.rohit.homework07;

import android.app.ProgressDialog;
import android.content.Context;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class MainActivity extends AppCompatActivity implements CustomAsync.Idata,CustomAdapter.Callback {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
   // private RecyclerView.LayoutManager gridLayoutManager;
    ProgressDialog pd;
    static MediaPlayer mp;
    Handler handler=new Handler();
    SeekBar sb;
    ImageView ivpause;

    Runnable maRunnable=null;
ArrayList<CustomObject> colist=new ArrayList<CustomObject>();
static LinearLayout ll;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle(" ");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ll=(LinearLayout) findViewById(R.id.playlayout);
        sb=(SeekBar)findViewById(R.id.sbmain);
        ivpause=(ImageView)findViewById(R.id.mainpbtn);
recyclerView=(RecyclerView)findViewById(R.id.rvlist);
        recyclerView.setHasFixedSize(true);
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

     //   gridLayoutManager=new GridLayoutManager(this);

        ConnectivityManager cm=(ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni=cm.getActiveNetworkInfo();
        if(ni!=null && ni.isConnected()) {

            pd = new ProgressDialog(this);
            pd.setTitle("Loading Episodes...");
            pd.show();

            getdata();

        }
        else
        {
            Toast.makeText(this,"No Wifi / Mobile Data",Toast.LENGTH_SHORT).show();
        }



        sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (mp.isPlaying()) {
                    sb.setProgress(progress);
                    mp.seekTo(progress);
                    mp.start();
                    Log.d("changed",String.valueOf(progress));
                } else {
                    sb.setProgress(progress);
                    mp.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
Log.d("Start","Start");
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Log.d("Start","Stop");
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
     //   if (id == R.id) {
     //       return true;
      //  }

        return super.onOptionsItemSelected(item);
    }

public void changeview(View v) {


    ConnectivityManager cm=(ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
    NetworkInfo ni=cm.getActiveNetworkInfo();
    if(ni!=null && ni.isConnected()) {
        if (((ImageView) findViewById(R.id.rimag)).getTag().equals("1")) {
            ((ImageView) findViewById(R.id.rimag)).setTag("2");
           if(mp==null) {
               ll.setVisibility(View.INVISIBLE);

           }// if (mp != null) {
           //     mp.stop();
           // }
        } else {
            ((ImageView) findViewById(R.id.rimag)).setTag("1");
           if(mp==null) {
               ll.setVisibility(View.INVISIBLE);
           }

               //  if (mp != null) {
          //      mp.stop();
            // }
        }
        ;
        getdata();

    }
    else
    {
        Toast.makeText(this,"No Wifi / Mobile Data",Toast.LENGTH_SHORT).show();
    }
 //   Toast.makeText(this,"Toast refresh",Toast.LENGTH_SHORT).show();
}

public void getdata()
{
    String url="https://www.npr.org/rss/podcast.php?id=510298";
    new CustomAsync(this).execute(url);
}

    @Override
    public void SetCustomobjects(ArrayList<CustomObject> aglist) {

        pd.hide();
        Collections.sort(aglist, CustomObject.DescComparator);
       colist=aglist;
        display(aglist);

       // Toast.makeText(this,"Toast refresh"+String.valueOf(aglist.size()),Toast.LENGTH_SHORT).show();
    }

    public void display(ArrayList<CustomObject> colist)
    {
        if(((ImageView)findViewById(R.id.rimag)).getTag().equals("1")){

            mLayoutManager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(mLayoutManager);
            CustomAdapter ca = new CustomAdapter(colist,"1",this);
        recyclerView.setAdapter(ca);
    }
else
        {


          //  Toast.makeText(this,"2",Toast.LENGTH_SHORT).show();
            recyclerView.setLayoutManager(new GridLayoutManager
                    (this,
                            2,
                            GridLayoutManager.VERTICAL, false));

            CustomAdapter ca = new CustomAdapter(colist,"2",this);

            recyclerView.setAdapter(ca);
            ca.notifyDataSetChanged();

        }

    }

    public void removelay()
    {
        ll.setVisibility(View.INVISIBLE);
        if(mp!=null)
        {
           mp.stop();
            mp=null;
        }
    }

    public void onplayclick(int position)
    {

if(mp!=null)
{
    mp.stop();
}
        ll.setVisibility(View.VISIBLE);
        try {
            playradio(colist.get(position).getAudiourl());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



public void playradio(String url)throws IOException
{
maRunnable=null;
    if(maRunnable!=null) {
        handler.removeCallbacks(maRunnable);
    }
        if(mp==null) {
            mp = new MediaPlayer();
            mp.setDataSource(url);
            mp.prepare();
            mp.start();

            sb.setMax(mp.getDuration());

              maRunnable = new Runnable() {
                @Override
                public void run() {

                    Log.d("show final ","final");
                    if (mp != null) {
                        int mcurrp = mp.getCurrentPosition();
                        sb.setProgress(mcurrp);
                        Log.d("sbtotalvalue",String.valueOf(sb.getMax()));
                        Log.d("show progress", String.valueOf(mp.getCurrentPosition()));
                        Log.d("totaltime", String.valueOf(mp.getDuration()));
                     /*   if (mp.getCurrentPosition() >= (mp.getDuration()-1000))
                        {
                            ll.setVisibility(View.INVISIBLE);
                            if (mp != null) {
                                mp.stop();
                            }
                            mp.reset();
                            sb.setProgress(0);
                           // handler.removeCallbacks(maRunnable);
                        }*/

                        handler.postDelayed(this,1000);
                    }
                }
            };
handler.postDelayed(maRunnable,1000);
        }


    mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mp) {
            ll.setVisibility(View.INVISIBLE);
            ll.setVisibility(View.INVISIBLE);
            if (mp != null) {
                mp.stop();
            }
            mp.reset();
            sb.setProgress(0);
            handler.removeCallbacks(maRunnable);
        }
    });

}


public void pauseclick(View v)
{


    if(ivpause.getTag().equals("1"))
    {
        ivpause.setImageResource(R.drawable.pplay);
        ivpause.setTag("2");
        mp.pause();
    }
    else
    {
        ivpause.setImageResource(R.drawable.pauseicon);
        ivpause.setTag("1");
        mp.start();
    }



}


    @Override
    protected void onStop() {

        super.onStop();
      // if(mp!=null) {
        //    mp.stop();
       // }
       // this.finish();
    }
    @Override
    protected void onPause() {

        super.onPause();
        //if(mp!=null)
        //{        mp.stop();}
       // this.finish();
    }


    @Override
    protected void onDestroy() {
        //if(mp!=null)
        //{        mp.stop();}

        super.onDestroy();
    }
}
