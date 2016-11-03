package com.toidv.task.ui.home;

import com.toidv.task.consts.Consts;
import com.toidv.task.data.DataManager;
import com.toidv.task.data.model.Task;
import com.toidv.task.ui.base.BasePresenter;

import java.util.List;

import javax.inject.Inject;

import io.realm.Realm;
import io.realm.RealmResults;
import retrofit2.Retrofit;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * Created by TOIDV on 11/3/2016.
 */

public class TaskFragmentPresenter extends BasePresenter<TaskFragmentMvpView> {
    private final DataManager dataManager;
    private final Retrofit retrofit;
    private final Realm realm;
    private Subscription subscription;

    @Inject
    TaskFragmentPresenter(DataManager dataManager, Retrofit retrofit, Realm realm) {
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

    public void getTask(int taskState) {
        getMvpView().showProgressDialog(true);
        subscription = dataManager.getPendingTask(taskState, realm)
                .subscribe(new Subscriber<RealmResults<Task>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(RealmResults<Task> taskList) {
                        getMvpView().showProgressDialog(false);
                        getMvpView().updateTask(realm.copyFromRealm(taskList));
                    }
                });
    }

    public void deleteTask(final List<Integer> taskList) {
        realm.executeTransactionAsync(
                new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        for (int id : taskList) {
                            RealmResults<Task> result = realm.where(Task.class).equalTo(Task.ID, id).findAll();
                            result.deleteAllFromRealm();
                        }
                    }
                }, new Realm.Transaction.OnSuccess() {
                    @Override
                    public void onSuccess() {
                        updateTaskList();
                    }
                }
                , new Realm.Transaction.OnError() {
                    @Override
                    public void onError(Throwable error) {
                        error.printStackTrace();
                    }
                });
    }

    private void updateTaskList() {
        subscription = Observable.just(null).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Object>() {
                    @Override
                    public void call(Object o) {
                        getMvpView().updateTaskList();
                    }
                });


    }

    public void changeState(final int id) {
        realm.executeTransactionAsync(
                new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        Task task = realm.where(Task.class).equalTo(Task.ID, id).findFirst();
                        if (task != null) {
                            if (Consts.TASK_STATE_DONE == task.getState()) {
                                task.setState(Consts.TASK_STATE_PENDING);
                            } else {
                                task.setState(Consts.TASK_STATE_DONE);
                            }
                        }
                    }
                }, new Realm.Transaction.OnSuccess() {
                    @Override
                    public void onSuccess() {

                        showStateChange();
                    }
                }, new Realm.Transaction.OnError() {
                    @Override
                    public void onError(Throwable error) {
                        error.printStackTrace();
                    }
                });
    }

    private void showStateChange() {
        subscription = Observable.just(null).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Object>() {
                    @Override
                    public void call(Object o) {
                        getMvpView().refresh();
                    }
                });
    }
}
