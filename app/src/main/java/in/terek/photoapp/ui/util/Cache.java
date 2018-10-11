package in.terek.photoapp.ui.util;

import android.util.LruCache;

/**
 * @author Rajdeep Yadav
 * Date : 09-Oct-18
 * Time : 8:49 AM
 */
public class Cache {
    private static Cache instance;
    private LruCache<Object, Object> lru;

    private Cache() {
        lru = new LruCache <Object, Object>(1024);
    }

    public static Cache getInstance() {
        if (instance == null) {
            instance = new Cache();
        }
        return instance;
    }

    public LruCache <Object, Object> getLru() {
        return lru;
    }
}
