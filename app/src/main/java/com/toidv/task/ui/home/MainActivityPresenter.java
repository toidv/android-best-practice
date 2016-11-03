package com.toidv.task.ui.home;


import com.toidv.task.data.DataManager;
import com.toidv.task.data.model.Data;
import com.toidv.task.data.model.Task;
import com.toidv.task.ui.base.BasePresenter;
import com.toidv.task.utils.ApplicationUtils;

import javax.inject.Inject;

import io.realm.Realm;
import retrofit2.Retrofit;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by TOIDV on 6/29/2016.
 */
public class MainActivityPresenter extends BasePresenter<MainActivityMvpView> {

    private final DataManager dataManager;
    private final Retrofit retrofit;
    private final Realm realm;

    private Subscription subscription;

    @Inject
    MainActivityPresenter(DataManager dataManager, Retrofit retrofit, Realm realm) {
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


    public void getInitialList() {
        dataManager.getInitialList()
                .concatMap(new Func1<Data, Observable<Task>>() {
                    @Override
                    public Observable<Task> call(Data data) {
                        return Observable.from(data.getData());
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Task>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        String error = ApplicationUtils.getError(e, retrofit);
                        getMvpView().showAlertDialog(error);
                    }

                    @Override
                    public void onNext(Task task) {
                        saveToRealm(task);
                    }
                });

    }

    private void saveToRealm(final Task task) {
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealmOrUpdate(task);
            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                error.printStackTrace();
            }
        });
    }
}
