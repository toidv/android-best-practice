package com.toidv.task.ui.base;

import android.support.v4.app.Fragment;

import com.toidv.task.TaskApplication;
import com.toidv.task.injection.component.ActivityComponent;
import com.toidv.task.injection.component.DaggerActivityComponent;


/**
 * Created by TOIDV on 5/19/2016.
 */
public class BaseFragment extends Fragment {

    ActivityComponent activityComponent;

    public ActivityComponent getActivityComponent() {
        if (activityComponent == null) {
            activityComponent = DaggerActivityComponent.builder()
                    .applicationComponent(TaskApplication.get(getActivity()).getComponent())
                    .build();
        }
        return activityComponent;
    }

}
