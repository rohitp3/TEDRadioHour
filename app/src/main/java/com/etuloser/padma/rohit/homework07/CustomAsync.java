package com.etuloser.padma.rohit.homework07;

import android.os.AsyncTask;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;

/**
 * Created by Rohit on 3/8/2017.
 */

public class CustomAsync extends AsyncTask<String,Void,ArrayList<CustomObject>> {
Idata act;

    public CustomAsync(Idata act)
    {
     this.act=act;
    }

    @Override
    protected ArrayList<CustomObject> doInBackground(String... params)
    {

        InputStream in=null;

        try {
            URL url=new URL(params[0]);
            HttpURLConnection con=(HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.connect();
            int responseCode=con.getResponseCode();
            if(responseCode==con.HTTP_OK)
            {
                in=con.getInputStream();

                return CustomObjectUtil.customobjectparser.parseCustomObject(in);
            }


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } finally{
            if(in!=null)
            {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }


        return null;
    }

    @Override
    protected void onPostExecute(ArrayList<CustomObject> customObjects) {
        act.SetCustomobjects(customObjects);
        super.onPostExecute(customObjects);
    }

    public static interface Idata{

        public void SetCustomobjects(ArrayList<CustomObject> aglist);
    }

}
