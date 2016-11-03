package com.toidv.task.ui.home;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.afollestad.dragselectrecyclerview.DragSelectRecyclerView;
import com.afollestad.dragselectrecyclerview.DragSelectRecyclerViewAdapter;
import com.toidv.task.R;
import com.toidv.task.data.model.Task;
import com.toidv.task.ui.adapter.TaskAdapter;
import com.toidv.task.ui.base.BaseFragment;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class TaskFragment extends BaseFragment implements
        TaskFragmentMvpView,
        SwipeRefreshLayout.OnRefreshListener,
        DragSelectRecyclerViewAdapter.SelectionListener,
        TaskAdapter.ClickListener {
    public static final String TASK_STATE = "state";
    @BindView(R.id.swipe_to_refresh)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.recycler_view_task)
    DragSelectRecyclerView recyclerView;
    @Inject
    TaskFragmentPresenter taskFragmentPresenter;
    private TaskAdapter taskAdapter;
    private int taskState;
    private boolean isDeleteMode;
    private MenuItem menuItemAdd, menuItemDelete;
    private Integer[] selectedIndices;

    public TaskFragment() {
        // Required empty public constructor
    }

    public static TaskFragment newInstance(int state) {
        TaskFragment taskFragment = new TaskFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(TASK_STATE, state);
        taskFragment.setArguments(bundle);
        return taskFragment;

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
        if (arguments != null) {
            taskState = arguments.getInt(TASK_STATE);
        }

        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pending_task, container, false);
        ButterKnife.bind(this, view);
        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getActivityComponent().inject(this);
        taskFragmentPresenter.attachView(this);
        taskAdapter = new TaskAdapter(this);
        taskAdapter.setSelectionListener(this);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        recyclerView.setAdapter(taskAdapter);
        swipeRefreshLayout.setColorSchemeColors(
                ContextCompat.getColor(getActivity(), R.color.colorPrimary),
                ContextCompat.getColor(getActivity(), R.color.colorAccent),
                ContextCompat.getColor(getActivity(), R.color.colorPrimaryDark));
        swipeRefreshLayout.setOnRefreshListener(this);
        onRefresh();

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.activity_main_menu_add_task, menu);
        menuItemAdd = menu.findItem(R.id.action_add);
        menuItemDelete = menu.findItem(R.id.action_delete);
        menuItemDelete.setVisible(false);
        menuItemAdd.setVisible(taskState == 0);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.action_add) {
            Intent intent = new Intent(getActivity(), AddTaskActivity.class);
            startActivity(intent);
            return true;
        } else if (itemId == R.id.action_delete) {
            deleteTask();
        }
        return super.onOptionsItemSelected(item);
    }

    private void deleteTask() {
        selectedIndices = taskAdapter.getSelectedIndices();
        List<Integer> deleteTaskList = taskAdapter.getDeleteTaskList(selectedIndices);
        taskAdapter.deleteTask(deleteTaskList);
        taskFragmentPresenter.deleteTask(deleteTaskList);

    }

    @Override
    public void updateTask(List<Task> taskList) {

        taskAdapter.addTaskList(taskList);

    }

    @Override
    public void showProgressDialog(boolean value) {
        swipeRefreshLayout.setRefreshing(value);
    }

    @Override
    public void updateTaskList() {

        taskAdapter.clearSelected();
        updateMenuItem(false);
    }

    @Override
    public void refresh() {
        onRefresh();
    }

    @Override
    public void onRefresh() {

        taskFragmentPresenter.getTask(taskState);
    }

    @Override
    public void onDragSelectionChanged(int count) {

    }


    @Override
    public void onClick(int index) {
        if (isDeleteMode) {
            taskAdapter.toggleSelected(index);
        }
        if (taskAdapter.getSelectedCount() == 0) {
            isDeleteMode = false;
            updateMenuItem(false);
        }

    }

    @Override
    public void onChangeState(int id) {
        taskFragmentPresenter.changeState(id);
    }

    @Override
    public void onLongClick(int index) {
        isDeleteMode = true;
        updateMenuItem(true);
        recyclerView.setDragSelectActive(true, index);
    }


    private void updateMenuItem(boolean isDeleteMode) {
        this.isDeleteMode = isDeleteMode;
        if (isDeleteMode) {
            menuItemDelete.setVisible(true);
            menuItemAdd.setVisible(false);
        } else {
            menuItemDelete.setVisible(false);
            menuItemAdd.setVisible(true && (taskState == 0));
        }
    }
}
