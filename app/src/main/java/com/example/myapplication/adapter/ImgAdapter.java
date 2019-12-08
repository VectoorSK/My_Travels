package com.example.myapplication.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.model.Img;
import com.jakewharton.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ImgAdapter extends RecyclerView.Adapter<ImgAdapter.ImgViewHolder> {

    private List<Img> dataList;
    private Context context;
    private Picasso picasso;

    public interface OnItemClickListener {
        void onItemClick(Img item);
    }
    private final OnItemClickListener listener;

    public ImgAdapter(Context context, List<Img> dataList, OnItemClickListener listener) {
        this.context = context;
        this.dataList = dataList;
        this.listener = listener;

        Picasso.Builder builder = new Picasso.Builder(context);
        builder.downloader(new OkHttp3Downloader(context));
        picasso = builder.build();
    }

    class ImgViewHolder extends RecyclerView.ViewHolder {

        public final View mView;

        //public TextView txtDesc;
        public ImageView coverImage;

        public ImgViewHolder(View itemView) {
            super(itemView);
            mView = itemView;

            //txtDesc = (TextView) mView.findViewById(R.id.img_caption);
            coverImage = (ImageView) mView.findViewById(R.id.img_icon);
        }

        public void bind(final Img item, final OnItemClickListener listener) {
            mView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    listener.onItemClick(item);
                }
            });
        }
    }

    @Override
    public ImgAdapter.ImgViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.img_row_layout, parent, false);
        ImgViewHolder vh = new ImgViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(ImgViewHolder holder, final int position) {
        holder.bind(dataList.get(position), listener);
        //holder.txtDesc.setText(dataList.get(position).getCaption());
        picasso.load(dataList.get(position).getUrl())
            .placeholder((R.drawable.ic_launcher_background))
            .error(R.drawable.ic_launcher_background)
            .into(holder.coverImage);
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }
}