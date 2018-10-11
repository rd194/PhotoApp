package in.terek.photoapp;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import in.terek.photoapp.db.DaoMaster;
import in.terek.photoapp.db.DaoSession;


/**
 * @author kapil.vij
 */
public class BaseApplication extends Application {

    private static final String TAG = "BaseApplication";


    public static Context mBaseContext;
    private boolean activityVisible;
    private String mFriendChatUserName;
    private boolean activityChatVisible;
    public static DaoSession daoSession;
    public static SQLiteDatabase db;
    private DaoMaster.DevOpenHelper devOpenHelper;


    @Override
    public void onCreate() {
        super.onCreate();

        mBaseContext = getApplicationContext();


        devOpenHelper = new DaoMaster.DevOpenHelper(this,"photoApp-db",null);
        db = devOpenHelper.getWritableDatabase();
        daoSession = new DaoMaster(db).newSession();

    }

    private void insertDummyData() {

    }

    public boolean isActivityVisible() {
        return activityVisible;
    }

    public void activityResumed() {
        activityVisible = true;
    }

    public void activityPaused() {
        activityVisible = false;
    }

    @Override
    public void onTerminate() {
        daoSession.clear();
        super.onTerminate();
    }




    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Log.d(TAG, "onConfigurationChanged: " + newConfig.locale.getLanguage());
    }
}
