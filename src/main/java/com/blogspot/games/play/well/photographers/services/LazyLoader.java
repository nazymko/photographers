package com.blogspot.games.play.well.photographers.services;

import android.app.IntentService;
import android.content.Intent;
import com.blogspot.games.play.well.photographers.Image;
import com.blogspot.games.play.well.photographers.ImageNormalRegister;
import com.blogspot.games.play.well.photographers.util.Log;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.SocketException;

/**
 * User: patronus
 * <p/>
 * Special for photographers.com.ua site
 */
public class LazyLoader extends IntentService {

    public static final String PHOTOGRAPHERS_SUPPLY = "http://photographers.com.ua/pictures/days/30/";
    public static final String PAGE = "PAGE";
    //Activity actions
    public static final String PAGE_LOADED = "PAGE_LOADED";
    public static final String NEW_ELEMENT = "NEW_ELEMENT";
    public static final String ITEM = "ITEM";
    public static final String PAGE_NUMBER = "?page=";

    public LazyLoader(String name) {
        super(name);
    }


    public LazyLoader() {
        super("lazy.loader.service");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        //Get required page from intent

        int page = intent.getExtras().getInt(PAGE);

        Log.d("service started, page" + page);
        //prepare url
        String gridViewPage;
        if (page == 0) {
            gridViewPage = PHOTOGRAPHERS_SUPPLY;
        } else {
            gridViewPage = PHOTOGRAPHERS_SUPPLY + PAGE_NUMBER + page;
        }


        //get connection for reading
        Connection connect = null;

        while (connect == null) {
            connect = Jsoup.connect(gridViewPage);

        }
        connect.timeout(5000);
        Log.w("Page:" + gridViewPage);
        try {
            Log.d("after connect");
            Document document = connect.get();
            Log.d("after get");

            Elements select = document.select("div.photo");
//            Elements select = document.select("div.pictures-search table tr td div.photo");

            Log.w("Selection: " + select.size());
            for (Element pictureWithInfo : select) {
                //Get information about image, author and where we can find other
                String smallImage = pictureWithInfo.select("a img").attr("src");

                String bigImagePageUrl = pictureWithInfo.select("a").attr("href");

                String imageTitle = pictureWithInfo.select("div.info div.name a").text();

                String authorPage = pictureWithInfo.select("div.about a").attr("href");

                String authorName = pictureWithInfo.select("div.info div.about a").text();

                String imageRate = pictureWithInfo.select("div.stat div.fl span.score.plus").text();


                Image img = new Image();

                img.setSmallImageUrl(smallImage);
                img.setAuthor(authorName);
                img.setAuthorPage(authorPage);
                img.setImageName(imageTitle);
                img.setRate(imageRate);
                img.setBigImagePage(bigImagePageUrl);

                ImageNormalRegister.getInstance().getImages().add(img);
                //add all this information to the cache manager - he must do something smart with this


            }

        } catch (IOException e) {
            e.printStackTrace();
            //TODO:
        }

        //Start load all big images url
        Intent imageGetter = new Intent(this, BigImageGetter.class);
        startService(imageGetter);

        ImageNormalRegister.getInstance().setPage(page);

        Intent message = new Intent();
        //Send message for updating
        message.setAction(PAGE_LOADED);
        Log.d("Broadcast message send");
        sendBroadcast(message);
    }

}
