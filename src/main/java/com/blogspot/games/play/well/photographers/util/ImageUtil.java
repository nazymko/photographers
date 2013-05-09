package com.blogspot.games.play.well.photographers.util;

import com.blogspot.games.play.well.photographers.ac.AcPre;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

/**
 * User: patronus
 */
public class ImageUtil {
    public static String getBigImage(String page) throws IOException {
        Document document = Jsoup.connect(page).get();

        String src = document.select("img#theImage").attr("src");
        if (src == null) {
            return null;
        }

        return AcPre.BASE_PATH + src;
    }
}
