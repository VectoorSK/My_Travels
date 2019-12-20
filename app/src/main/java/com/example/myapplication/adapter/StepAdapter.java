package com.example.myapplication.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.model.Step;
import com.jakewharton.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

public class StepAdapter extends RecyclerView.Adapter<StepAdapter.StepsViewHolder> {

    private List<Step> dataList;
    private Context context;
    private Picasso picasso;

    public interface OnItemClickListener {
        void onItemClick(Step item);
    }
    private final OnItemClickListener listener;

    public StepAdapter(Context context, List<Step> dataList, OnItemClickListener listener) {
        this.context = context;
        this.dataList = dataList;
        this.listener = listener;

        Picasso.Builder builder = new Picasso.Builder(context);
        builder.downloader(new OkHttp3Downloader(context));
        picasso = builder.build();
    }

    class StepsViewHolder extends RecyclerView.ViewHolder {

        public final View mView;

        public TextView txtTitle;
        public TextView txtDesc;
        public ImageView coverImage;

        public StepsViewHolder(View itemView) {
            super(itemView);
            mView = itemView;

            txtTitle = (TextView) mView.findViewById(R.id.steps_title);
            txtDesc = (TextView) mView.findViewById(R.id.steps_description);
            coverImage = (ImageView) mView.findViewById(R.id.steps_icon);
        }

        public void bind(final Step item, final OnItemClickListener listener) {
            mView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    listener.onItemClick(item);
                }
            });
        }
    }

    @Override
    public StepAdapter.StepsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.steps_row_layout, parent, false);
        StepsViewHolder vh = new StepsViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(StepsViewHolder holder, final int position) {
        holder.bind(dataList.get(position), listener);
        holder.txtTitle.setText(dataList.get(position).getCity());
        holder.txtDesc.setText(dataList.get(position).getDesc());
        picasso.load(dataList.get(position).getImg())
            .placeholder((R.drawable.ic_launcher_background))
            .error(R.drawable.ic_launcher_background)
            .into(holder.coverImage);
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }
}