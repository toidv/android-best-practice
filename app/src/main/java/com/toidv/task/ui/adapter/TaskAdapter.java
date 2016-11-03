package com.toidv.task.ui.adapter;

/**
 * Created by TOIDV on 11/3/2016.
 */

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.dragselectrecyclerview.DragSelectRecyclerViewAdapter;
import com.toidv.task.R;
import com.toidv.task.data.model.Task;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class TaskAdapter extends DragSelectRecyclerViewAdapter<TaskAdapter.ViewHolder> {
    private final ClickListener callback;
    private final List<Task> taskList = new ArrayList<>();

    public TaskAdapter(ClickListener callback) {
        this.callback = callback;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View noteView = inflater.inflate(R.layout.item_task, parent, false);
        return new ViewHolder(noteView, callback);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        super.onBindViewHolder(viewHolder, position);
        Task data = taskList.get(position);
        viewHolder.title.setText(data.getName());
        viewHolder.description.setText(data.getState() == 1 ? "Done" : "Pending");
        final Context c = viewHolder.itemView.getContext();
        if (isIndexSelected(position)) {
            viewHolder.cvTask.setCardBackgroundColor(ContextCompat.getColor(c, R.color.grid_label_task_selected));
        } else {
            viewHolder.cvTask.setCardBackgroundColor(Color.WHITE);
        }

    }


    public List<Integer> getDeleteTaskList(Integer[] selectedIndices) {
        List<Integer> deletedTaskList = new ArrayList<>();
        for (int index : selectedIndices) {
            deletedTaskList.add(taskList.get(index).getId());
        }
        return deletedTaskList;
    }

    public void deleteTask(List<Integer> deletedTaskList) {
        for (int i = deletedTaskList.size() - 1; i > -1; i--) {
            for (Task task : taskList) {
                if (deletedTaskList.get(i).equals(task.getId())) {
                    int index = taskList.indexOf(task);
                    taskList.remove(index);
                    notifyItemRemoved(index);
                }
            }
        }

    }

    public void addTaskList(List<Task> taskList) {
        this.taskList.clear();
        this.taskList.addAll(taskList);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }


    public interface ClickListener {
        void onClick(int index);

        void onLongClick(int index);

        void onChangeState(int index);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        private final ClickListener callback;
        private final Runnable runnable;
        private final Handler handler;
        @BindView(R.id.note_detail_title)
        TextView title;
        @BindView(R.id.note_detail_description)
        TextView description;
        @BindView(R.id.cv_task)
        CardView cvTask;
        @BindView(R.id.img_cancel)
        ImageView imgCancel;

        public ViewHolder(View itemView, ClickListener callback) {
            super(itemView);
            this.callback = callback;
            ButterKnife.bind(this, itemView);
            this.itemView.setOnClickListener(this);
            this.itemView.setOnLongClickListener(this);
            this.imgCancel.setOnClickListener(this);

            handler = new Handler();

            runnable = new Runnable() {

                @Override
                public void run() {
                    showCancelMode(false);
                    int adapterPosition = getAdapterPosition();
                    if (adapterPosition >= taskList.size() || adapterPosition < 0) return;
                    ViewHolder.this.callback.onChangeState(taskList.get(adapterPosition).getId());
                }
            };

        }

        @Override
        public void onClick(View v) {

            if (callback == null) return;
            showCancelMode(true);
            callback.onClick(getAdapterPosition());
            handler.postDelayed(runnable, 5000);
            if (v.getId() == R.id.img_cancel) {
                if (handler != null) {
                    showCancelMode(false);
                    handler.removeCallbacks(runnable);
                }
            }
        }

        private void showCancelMode(boolean value) {
            if (value) {
                cvTask.setCardBackgroundColor(Color.RED);
                imgCancel.setVisibility(View.VISIBLE);
            } else {
                cvTask.setCardBackgroundColor(Color.WHITE);
                imgCancel.setVisibility(View.GONE);
            }
        }

        @Override
        public boolean onLongClick(View v) {
            if (callback != null)
                callback.onLongClick(getAdapterPosition());
            return true;
        }
    }
}
