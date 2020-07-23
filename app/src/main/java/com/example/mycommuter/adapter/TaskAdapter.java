package com.example.mycommuter.adapter;


import android.content.Context;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mycommuter.R;
import com.example.mycommuter.fragments.TodoFragment;
import com.example.mycommuter.model.Tasks;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.checkbox.MaterialCheckBox;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import es.dmoral.toasty.Toasty;

import static android.content.ContentValues.TAG;


public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.MyViewHolder> {
    private List<Tasks> tasks;
    Context context;
    TodoFragment todoFragment = new TodoFragment();

    public TaskAdapter() {
    }

    public TaskAdapter(Context context, List<Tasks> tasks) {
        this.context = context;
        this.tasks = tasks;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {


        public TextView title;
        public TextView description;
        public TextView due;
        private MaterialCardView materialCardView;
        private MaterialCheckBox materialCheckBox;


        public MyViewHolder(View v) {
            super(v);
            this.materialCardView = v.findViewById(R.id.taskcardviewer);
            this.title = (TextView) v.findViewById(R.id.title);
            this.description = (TextView) v.findViewById(R.id.description);
            this.due = (TextView) v.findViewById(R.id.due);

            materialCardView.setOnCreateContextMenuListener(this);

        }


        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {

            menu.add(this.getAdapterPosition(), 120, 1, "delete item");
            menu.add(this.getAdapterPosition(), 121, 1, "Edit item");
        }
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.cardviewer, parent, false);
        MyViewHolder myviewHolder = new MyViewHolder(view);
        myviewHolder.setIsRecyclable(false);
        return myviewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull TaskAdapter.MyViewHolder holder, int position) {

        holder.setIsRecyclable(false);
        Date myDate = null;
        final Tasks current_task = tasks.get(position);

        holder.materialCardView.setAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_scale_animation));
        holder.title.setText(current_task.getTitle());
        holder.description.setText(current_task.getDescritpion());
        SimpleDateFormat myFormat = new SimpleDateFormat("MM-dd-yyyy");
        try {
            myDate = myFormat.parse(current_task.getDue());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        holder.due.setText(myDate.toString());


    }

    public void setTasks(List<Tasks> tasks) {
        if (tasks == null) {
            this.tasks.clear();
        }
        this.tasks = tasks;

        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (tasks != null) {
            return tasks.size();
        }
        return 0;

    }

}
