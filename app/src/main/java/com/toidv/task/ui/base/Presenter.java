package com.toidv.task.ui.base;

/**
 * Created by TOIDV on 4/4/2016.
 */
public interface Presenter<V extends MvpView> {

    void attachView(V mvpView);

    void detachView();
}
