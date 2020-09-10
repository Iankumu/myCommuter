package com.example.mycommuter.adapter;


import android.content.Context;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Filter;
import android.widget.Filterable;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import es.dmoral.toasty.Toasty;

import static android.content.ContentValues.TAG;


public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.MyViewHolder> implements Filterable {
    private List<Tasks> tasks;
    private List<Tasks> tasksSearch;
    Context context;
    TodoFragment todoFragment = new TodoFragment();

    public TaskAdapter() {
    }

    public TaskAdapter(Context context, List<Tasks> tasks) {
        this.context = context;
        this.tasks = tasks;
        tasksSearch = new ArrayList<>(tasks);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {


        public TextView title;
        public TextView description;
        public TextView due;
        private MaterialCardView materialCardView;


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
        String myDate2 = null;
        Date myDate3=null;
        final Tasks current_task = tasks.get(position);

        holder.materialCardView.setAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_scale_animation));
        holder.title.setText(current_task.getTitle());
        holder.description.setText(current_task.getDescritpion());
        SimpleDateFormat myFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat myFormat2 = new SimpleDateFormat("EEE, MM , YYYY");
        try {
            myDate = myFormat.parse(current_task.getDue());
            myDate2=myFormat2.format(myDate);
//            myDate3=myFormat.parse(myDate2);
            Log.e(TAG, "onBindViewHolder: " +current_task.getDue() );
        } catch (ParseException e) {
            e.printStackTrace();
        }
        holder.due.setText(myDate2.toString());


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

    @Override
    public Filter getFilter() {
        return filterResult;
    }

    private Filter filterResult = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Tasks> filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(tasksSearch);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (Tasks task : tasksSearch) {
                    if (task.getTitle().toLowerCase().contains(filterPattern)) {
                        filteredList.add(task);
                    }
                }
            }
            FilterResults filterResults = new FilterResults();
            filterResults.values = filteredList;

            return filterResults;

        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            tasks.clear();
            tasks.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };
}
