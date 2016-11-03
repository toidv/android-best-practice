package com.toidv.task.ui.base;

import android.support.v7.app.AppCompatActivity;

import com.toidv.task.TaskApplication;
import com.toidv.task.injection.component.ActivityComponent;
import com.toidv.task.injection.component.DaggerActivityComponent;


/**
 * Created by TOIDV on 4/4/2016.
 */
public class BaseActivity extends AppCompatActivity {

    ActivityComponent activityComponent;

    public ActivityComponent getActivityComponent() {
        if (activityComponent == null) {
            activityComponent = DaggerActivityComponent.builder()
                    .applicationComponent(TaskApplication.get(this).getComponent())
                    .build();
        }
        return activityComponent;
    }
}
