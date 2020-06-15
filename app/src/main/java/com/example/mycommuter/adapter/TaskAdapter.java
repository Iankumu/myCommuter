package com.example.mycommuter.adapter;


import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mycommuter.R;
import com.example.mycommuter.model.Tasks;
import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.List;

import static android.content.ContentValues.TAG;


public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.MyViewHolder> {
    private List<Tasks> tasks;
    Context context;
    public boolean shimmer=true;

    public void setShimmer(boolean shimmer) {
        this.shimmer = shimmer;
        Log.e(TAG, "setShimmer: called" );
    }

    int item_shimmer=5;

    public TaskAdapter(Context context, List<Tasks> tasks) {
        this.context = context;
        this.tasks = tasks;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ShimmerFrameLayout shimmerFrameLayout;
        
        public TextView title;
        public TextView description;
        public TextView due;



        public MyViewHolder(View v) {
            super(v);
            this.shimmerFrameLayout = v.findViewById(R.id.shimmerframelay);
            this.title = (TextView) v.findViewById(R.id.title);
            this.description = (TextView) v.findViewById(R.id.description);
            this.due = (TextView) v.findViewById(R.id.due);

        }
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.cardviewer, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);

        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull TaskAdapter.MyViewHolder holder, int position) {
        if (shimmer){
               holder.shimmerFrameLayout.startShimmer();
        }
        else {
            holder.shimmerFrameLayout.stopShimmer();
              holder.shimmerFrameLayout.setShimmer(null);
            final Tasks current_task = tasks.get(position);
            holder.title.setText(current_task.getTitle());
            holder.description.setText(current_task.getDescritpion());
            holder.due.setText(current_task.getDue());
        }
    }

    @Override
    public int getItemCount() {
        return shimmer?item_shimmer:tasks.size();

    }
//public  class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHolder>{
//private ArrayList<Tasks> tasks;
//
//    public TaskAdapter(ArrayList<Tasks> tasks) {
//        this.tasks = tasks;
//    }
//
//    @Override
//    public TaskAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardviewer,parent,false);
//        return new ViewHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull TaskAdapter.ViewHolder holder, int position) {
//        holder.title.setText(tasks.get(position).getTitle() + "");
//        holder.description.setText(tasks.get(position).getDescritpion() + "");
//        holder.due.setText(tasks.get(position).getDue() + "");
//    }
//
//
//
//    @Override
//    public int getItemCount() {
//        return tasks.size();
//    }
//
//    public class ViewHolder extends RecyclerView.ViewHolder {
//        private TextView title;
//        private TextView description;
//        private TextView due;
//
//        public ViewHolder(View v) {
//            super(v);
//
//
//
//            this.title = (TextView) v.findViewById(R.id.title);
//          this.description = (TextView) v.findViewById(R.id.description);
//           this.due = (TextView) v.findViewById(R.id.due);
//
//
//        }
//    }
}
