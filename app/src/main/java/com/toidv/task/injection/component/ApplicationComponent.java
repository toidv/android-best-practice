package com.toidv.task.injection.component;

import android.app.Application;
import android.content.Context;

import com.toidv.task.data.DataManager;
import com.toidv.task.data.remote.ApplicationService;
import com.toidv.task.injection.ApplicationContext;
import com.toidv.task.injection.module.ApplicationModule;

import javax.inject.Singleton;

import dagger.Component;
import io.realm.Realm;
import retrofit2.Retrofit;

/**
 * Created by TOIDV on 4/4/2016.
 */

@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {

    @ApplicationContext
    Context context();

    Application application();

    Retrofit retrofit();

    ApplicationService inploiService();

    DataManager dataManager();

    Realm realm();


}
