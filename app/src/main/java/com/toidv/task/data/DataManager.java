package com.toidv.task.data;


import com.toidv.task.data.local.RealmHelper;
import com.toidv.task.data.model.Data;
import com.toidv.task.data.model.Task;
import com.toidv.task.data.remote.ApplicationService;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.realm.Realm;
import io.realm.RealmResults;
import rx.Observable;

/**
 * Created by TOIDV on 4/5/2016.
 */

@Singleton
public class DataManager {
    private final ApplicationService applicationService;
    private final RealmHelper realmHelper;

    @Inject
    public DataManager(ApplicationService inploiService, RealmHelper realmHelper) {
        this.applicationService = inploiService;
        this.realmHelper = realmHelper;
    }

    public Observable<Data> getInitialList() {
        return applicationService.getInitialList();
    }

    public Observable<RealmResults<Task>> getPendingTask(int taskState, Realm realm) {
        return realmHelper.getPendingTask(taskState, realm);
    }
}
