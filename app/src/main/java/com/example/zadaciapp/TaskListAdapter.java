package com.example.zadaciapp;

import android.content.Context;
import android.graphics.Paint;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import com.example.zadaciapp.modeli.Task;

public class TaskListAdapter extends BaseAdapter {
    private Context mContext;
    private List<Task> mTaskList;

    public TaskListAdapter(Context mContext, List<Task> mTaskList) {
        this.mContext = mContext;
        this.mTaskList = mTaskList;
    }

    @Override
    public int getCount() {
        return mTaskList.size();
    }

    @Override
    public Object getItem(int position) {
        return mTaskList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return mTaskList.get(position).getId();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View v = View.inflate(mContext, R.layout.item_task_list,null);
        final TextView tvName = (TextView)v.findViewById(R.id.task_name);
        final TextView tvDescription = (TextView)v.findViewById(R.id.task_description);
        final TextView tvDate = (TextView)v.findViewById(R.id.task_date);
        Button btn = (Button)v.findViewById(R.id.button);

        tvName.setText(mTaskList.get(position).getName());
        tvDescription.setText(mTaskList.get(position).getDescription());
        if(mTaskList.get(position).isDone()) {
            setPaintFlags(tvDate, tvName, tvDescription);}
        else {
            removePaintFlags(tvDate, tvName, tvDescription);
        }
        tvDate.setText(mTaskList.get(position).getDate() + (mTaskList.get(position).isDone() ? "   Done":""));
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTaskList.get(position).toggleDone();

                tvDate.setText(mTaskList.get(position).getDate() + (mTaskList.get(position).isDone() ? "   Done":""));
                if(mTaskList.get(position).isDone()) {
                    setPaintFlags(tvDate, tvName, tvDescription);}
                else {
                    removePaintFlags(tvDate, tvName, tvDescription);
                }

                MainActivity.getDbHandler().updateTask(mTaskList.get(position));
                //tvDate.invalidate();
            }
        });
        v.setTag(mTaskList.get(position).getId());
        return v;
    }

    private void removePaintFlags(TextView tvDate, TextView tvName, TextView tvDescription) {
        tvDate.setPaintFlags(0);
        tvName.setPaintFlags(0);
        tvDescription.setPaintFlags(0);
    }

    private void setPaintFlags(TextView tvDate, TextView tvName, TextView tvDescription){
        tvDate.setPaintFlags(tvDate.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);
        tvName.setPaintFlags(tvName.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);
        tvDescription.setPaintFlags(tvDescription.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);
    }
}