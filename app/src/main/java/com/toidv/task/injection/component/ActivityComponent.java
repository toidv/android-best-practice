package com.toidv.task.injection.component;


import com.toidv.task.injection.PerActivity;
import com.toidv.task.injection.module.ActivityModule;
import com.toidv.task.ui.home.AddTaskFragment;
import com.toidv.task.ui.home.MainActivity;
import com.toidv.task.ui.home.TaskFragment;

import dagger.Component;

/**
 * Created by TOIDV on 4/4/2016.
 */

@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = ActivityModule.class)


public interface ActivityComponent {

    void inject(MainActivity mainActivity);

    void inject(TaskFragment taskFragment);

    void inject(AddTaskFragment addTaskFragment);


}
