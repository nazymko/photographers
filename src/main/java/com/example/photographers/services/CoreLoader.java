package com.example.photographers.services;

import android.content.Context;
import com.example.photographers.Cache;
import com.example.photographers.Image;
import com.example.photographers.Log;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

/**
 * User: patronus
 */
public class CoreLoader {
    Context context;

    public CoreLoader(Context context) {
        this.context = context;
    }

    public CoreLoader() {
    }

    public void Load(String url, Cache receiver) {

        //get connection for reading
        Log.d("URL:" + url);
        Log.d("before connect");
        Connection connect = Jsoup.connect(url);

        try {
            Log.d("after connect");
            Document document = connect.get();
            Log.d("after get");
            Elements select = document.select("div.pictures-search table tr td div.photo");
            for (Element pictureWithInfo : select) {
                //Get information about image, author and where we can find other
                String smallImage = pictureWithInfo.select("a img").attr("src");

                String bigImagePageUrl = pictureWithInfo.select("a").attr("href");

                String imageTitle = pictureWithInfo.select("div.info div.name a").text();

                String authorPage = pictureWithInfo.select("div.info div.about a").attr("href");

                String authorName = pictureWithInfo.select("div.info div.about a").text();

                String imageRate = pictureWithInfo.select("div.stat div.fl span.score.plus").text();

                Image img = new Image();

                img.setSmallImageUrl(smallImage);
                img.setAuthor(authorName);
                img.setAuthorPage(authorPage);
                img.setImageName(imageTitle);
                img.setRate(imageRate);
                img.setNormalImagePage(bigImagePageUrl);

                //add all this information to the cache manager - he must do something smart with this
                receiver.add(img);

            }


            Log.d("Cache size:" + receiver.getCache().size());

        } catch (IOException e) {
            e.printStackTrace();
            sendMessage(MessageType.EXCEPTION, e);
        }
    }

    private void sendMessage(MessageType exception, IOException e) {

        Log.e(e);
    }
}
