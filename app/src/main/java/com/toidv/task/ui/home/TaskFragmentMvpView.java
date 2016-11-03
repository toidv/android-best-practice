package com.toidv.task.ui.home;

import com.toidv.task.data.model.Task;
import com.toidv.task.ui.base.MvpView;

import java.util.List;

/**
 * Created by TOIDV on 11/3/2016.
 */

public interface TaskFragmentMvpView extends MvpView {

    void updateTask(List<Task> taskList);

    void showProgressDialog(boolean value);

    void updateTaskList();

    void refresh();
}
