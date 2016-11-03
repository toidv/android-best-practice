package com.toidv.task;

import android.app.Application;
import android.content.Context;

import com.facebook.stetho.Stetho;
import com.toidv.task.injection.component.ApplicationComponent;
import com.toidv.task.injection.component.DaggerApplicationComponent;
import com.toidv.task.injection.module.ApplicationModule;
import com.uphyca.stetho_realm.RealmInspectorModulesProvider;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by TOIDV on 4/4/2016.
 */
public class TaskApplication extends Application {
    private ApplicationComponent mApplicationComponent;

    public static TaskApplication get(Context context) {
        return (TaskApplication) context.getApplicationContext();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder(this)
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(realmConfiguration);
        Stetho.initialize(
                Stetho.newInitializerBuilder(this)
                        .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
                        .enableWebKitInspector(RealmInspectorModulesProvider.builder(this).build())
                        .build());


    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }

    public synchronized ApplicationComponent getComponent() {
        if (mApplicationComponent == null) {
            mApplicationComponent = DaggerApplicationComponent.builder()
                    .applicationModule(new ApplicationModule(this))
                    .build();
        }
        return mApplicationComponent;

    }


}
