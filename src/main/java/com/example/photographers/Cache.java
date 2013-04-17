package com.example.photographers;

import java.util.ArrayList;
import java.util.List;

/**
 * User: patronus
 */
public class Cache {
    private static Cache ourInstance = new Cache();
    List<Image> cache = new ArrayList<Image>();

    private Cache() {
    }

    public static Cache getInstance() {
        return ourInstance;
    }

    public List<Image> getCache() {
        return cache;
    }

    public void add(Image img) {
        cache.add(img);
    }


}
