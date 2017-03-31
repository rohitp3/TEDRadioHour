package com.etuloser.padma.rohit.homework07;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;

/**
 * Created by Rohit on 3/9/2017.
 */

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder>{

    ArrayList<CustomObject> colist;
    String flag="1";
    Callback callback;


    public CustomAdapter(ArrayList<CustomObject> colist,String flag,Callback callback)

    {
        this.flag=flag;
        this.colist=colist;
        this.callback=callback;

    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView txttitle;
        public TextView txtpub;
        public ImageView imgradio;
        public ImageView implaynow;

        public ImageView imagecview1;
        public ImageView imagecview;

        public TextView titlecview;
        public LinearLayout ll;



        public ViewHolder(View v) {

            super(v);

            if(flag.equals("1")) {
                txttitle = (TextView) v.findViewById(R.id.templtitle);
                txtpub = (TextView) v.findViewById(R.id.templpublishdate);
                imgradio = (ImageView) v.findViewById(R.id.templimage);
                implaynow = (ImageView) v.findViewById(R.id.playnow);
            }
            else
            {
                titlecview=(TextView)v.findViewById(R.id.temptitle);
                imagecview1=(ImageView)v.findViewById(R.id.tempcimage1);
                imagecview=(ImageView)v.findViewById(R.id.tempcimage);
            }

            v.setOnClickListener(new View.OnClickListener(){
                                     @Override
                                     public void onClick(View v) {

                                         //callback.removelay();
                                         CustomObject cusobj=colist.get(getAdapterPosition());
                                         Intent i=new Intent(v.getContext(),Playactivity.class);
                                        // i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                         i.putExtra("obj",cusobj);
                                         v.getContext().startActivity(i);
                                     }
            });

        }


    }

  /*  public void add(int position, CustomObject item) {
        colist.add(position, item);
        //notifyItemInserted(position);
    }
*/


        @Override
    public CustomAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            if(flag.equals("1")) {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.childlistview, parent, false);
                ViewHolder vh = new ViewHolder(v);
                return vh;
            }
            else
            {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.childcardview, parent, false);
                ViewHolder vh = new ViewHolder(v);
                return vh;

            }
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

         CustomObject co = colist.get(position);

        if(flag.equals("1")) {
            holder.txttitle.setText(co.getTitle());
            holder.txtpub.setText("posted: "+co.getPubdate());
            if (co.getImageurl()!=null) {
                Picasso.with(holder.imgradio.getContext()).load(co.getImageurl()).into(holder.imgradio);
            }
            holder.implaynow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                  //  Toast.makeText(v.getContext(), "ITEM PRESSED = " + String.valueOf(position), Toast.LENGTH_SHORT).show();

callback.onplayclick(position);
                        }
            });


        }
        else
        {
            holder.titlecview.setText(co.getTitle());
            holder.imagecview1.setTag(colist.get(position).getImageurl());

            holder.imagecview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    callback.onplayclick(position);
                }
            });
            if (co.getImageurl().length() > 0) {

              // holder.imagecview.setBackground(null);
             Picasso.with(holder.imagecview1.getContext()).load(co.getImageurl()).into(holder.imagecview1);
           /*    target=new Target() {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {

                        holder.imagecview.setBackground(new BitmapDrawable(holder.imagecview.getContext().getResources(), bitmap));

                    }

                    @Override
                    public void onBitmapFailed(Drawable errorDrawable) {

                    }

                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {

                     //   holder.imagecview.setBackground(null);
                    }
                };

                Picasso.with(holder.imagecview.getContext()).cancelRequest(target);
                Picasso.with(holder.imagecview.getContext()).load(co.getImageurl()).tag(holder.imagecview.getTag()).placeholder(R.mipmap.ic_launcher)
                        .error(R.mipmap.ic_launcher).into(target);
*/

            }
            else
            {
                holder.imagecview1.setBackground(holder.imagecview1.getContext().getResources().getDrawable(R.drawable.playiconn));
            }
            co=null;
          /// target=null;
          //  holder.imagecview.invalidate();
            //mHolder.imageView.invalidate();

        }

    }

    @Override
    public int getItemCount() {
        return colist.size();
    }

    public interface Callback {
        void onplayclick(int position);
        void removelay();

    }

}