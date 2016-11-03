package com.toidv.task.ui.home;

import android.text.TextUtils;

import com.toidv.task.consts.Consts;
import com.toidv.task.data.DataManager;
import com.toidv.task.data.model.Task;
import com.toidv.task.ui.base.BasePresenter;

import javax.inject.Inject;

import io.realm.Realm;
import retrofit2.Retrofit;
import rx.Subscription;

/**
 * Created by TOIDV on 11/3/2016.
 */

public class AddTaskFragmentPresenter extends BasePresenter<AddTaskFragmentMvpView> {

    private final DataManager dataManager;
    private final Retrofit retrofit;
    private final Realm realm;
    private Subscription subscription;

    @Inject
    AddTaskFragmentPresenter(DataManager dataManager, Retrofit retrofit, Realm realm) {
        this.dataManager = dataManager;
        this.retrofit = retrofit;
        this.realm = realm;

    }

    @Override
    public void detachView() {
        if (subscription != null) {
            subscription.unsubscribe();
        }
        if (realm != null) {
            realm.close();
        }
        super.detachView();
    }

    public void saveTask(final String title) {
        if (TextUtils.isEmpty(title)) {
            getMvpView().showError();
            return;
        }

        realm.executeTransactionAsync(
                new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        Task task = realm.createObject(Task.class);
                        Number maxId = realm.where(Task.class).max("id");
                        // Default index, if doesn't exist.
                        int index = 10;
                        if(maxId != null) {
                            index = maxId.intValue() + 1;
                        }
                        task.setId(index);
                        task.setName(title);
                        task.setState(Consts.TASK_STATE_PENDING);
                    }
                }, new Realm.Transaction.OnSuccess() {
                    @Override
                    public void onSuccess() {
                        getMvpView().showTaskList();
                    }
                }, new Realm.Transaction.OnError() {
                    @Override
                    public void onError(Throwable error) {
                        error.printStackTrace();
                    }
                });
    }


}
