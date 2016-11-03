package com.toidv.task.data.local;


import com.toidv.task.data.model.Task;

import javax.inject.Singleton;

import io.realm.Realm;
import io.realm.RealmResults;
import rx.Observable;
import rx.functions.Func1;

/**
 * Created by TOIDV on 8/18/2016.
 */
@Singleton
public class RealmHelper {


    public Observable<RealmResults<Task>> getPendingTask(int taskState, Realm realm) {
        return realm.where(Task.class).equalTo(Task.STATE, taskState)
                .findAllAsync()
                .asObservable()
                .filter(new Func1<RealmResults<Task>, Boolean>() {
                    @Override
                    public Boolean call(RealmResults<Task> tasks) {
                        return tasks.isLoaded();
                    }
                });


    }
}
