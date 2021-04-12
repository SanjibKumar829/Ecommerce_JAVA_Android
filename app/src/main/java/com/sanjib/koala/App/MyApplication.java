package com.sanjib.koala.App;

import android.app.Application;

import butterknife.BuildConfig;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import timber.log.Timber;

import static com.sanjib.koala.App.Constant.DB_NAME;

public class MyApplication extends Application {

    public static  MyApplication mInstance ;

    @Override
    public void onCreate() {
        super.onCreate();

        mInstance = this;

        initRealm();
        plantTimber();
        
    }

    private void plantTimber() {

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }
    }

    private void initRealm() {
        Realm.init(this);
        RealmConfiguration defaultRealmConfiguration = new RealmConfiguration
                .Builder()
                .deleteRealmIfMigrationNeeded()
                .name(DB_NAME)
                .build();
        Realm.setDefaultConfiguration(defaultRealmConfiguration);
    }
    public static MyApplication getInstance() {
        return mInstance;
    }
}
