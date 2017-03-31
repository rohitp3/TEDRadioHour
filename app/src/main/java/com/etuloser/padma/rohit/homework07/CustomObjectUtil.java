package com.etuloser.padma.rohit.homework07;

import android.util.Log;
import android.widget.Switch;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Rohit on 3/8/2017.
 */

public class CustomObjectUtil {

    public static class customobjectparser{

        static public ArrayList<CustomObject> parseCustomObject(InputStream in) throws XmlPullParserException, IOException, ParseException {
           ArrayList<CustomObject> colist=new ArrayList<CustomObject>();
            CustomObject co=null;
            XmlPullParser xpp= XmlPullParserFactory.newInstance().newPullParser();
            xpp.setInput(in,"UTF-8");
            int event=xpp.getEventType();


            while(event!=XmlPullParser.END_DOCUMENT) {


                switch(event)
                {

                    case XmlPullParser.START_TAG :
                    {
                         if(xpp.getName().equals("title"))
                         {
                             co=new CustomObject();
                             co.setTitle(xpp.nextText().trim());
                         }
                         else if(xpp.getName().equals("itunes:image"))
                         {
                            // Log.d("errormsg",xpp.nextText().trim());
                             co.setImageurl(xpp.getAttributeValue(null,"href").trim());
                         }
                         else if(xpp.getName().equals("pubDate"))
                         {
                             SimpleDateFormat parserDate = new SimpleDateFormat("EEE,d MMM yyyy HH:mm:ss zzz");
                             SimpleDateFormat formatter = new SimpleDateFormat("EEE,d,MMM,yyyy");
                             String input = xpp.nextText().trim();
                             Date date = parserDate.parse(input);
                             String formattedDate = formatter.format(date);


                             co.setPubdate(formattedDate);
                         }

                         else if(xpp.getName().equals("itunes:duration"))
                         {
                             //Log.d("errormsg",xpp.nextText().trim());
                             co.setDuration(xpp.nextText().trim());
                         }
                         else if(xpp.getName().equals("enclosure"))
                         {
                             //Log.d("errormsg",xpp.nextText().toString().trim());
                             co.setAudiourl(xpp.getAttributeValue(null,"url").trim());

                         }
                         else if(xpp.getName().equals("description"))
                         {
                             //Log.d("errormsg",xpp.nextText().trim());
                             co.setDescription(xpp.nextText().trim());

                         }

                    }
                        break;
                     case XmlPullParser.END_TAG: {
                         if(xpp.getName().equals("item"))
                         {
                             colist.add(co);
                             co=null;
                         }

                     }
                        break;

default:break;
                }

                event=xpp.next();



                }


                return colist;

        }
    }
}
