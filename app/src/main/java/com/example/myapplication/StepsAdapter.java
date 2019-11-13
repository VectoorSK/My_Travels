package com.example.myapplication;

import java.io.InputStream;
import java.net.URL;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.model.Country;
import com.example.myapplication.model.Pokemon;
import com.example.myapplication.model.RetroPhoto;
import com.example.myapplication.model.RetroPokemon;
import com.example.myapplication.model.Step;
import com.jakewharton.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

public class StepsAdapter extends RecyclerView.Adapter<StepsAdapter.StepsViewHolder> {

    private List<Step> dataList;
    private Context context;
    private Picasso picasso;

    public StepsAdapter(Context context, List<Step> dataList) {
        this.context = context;
        this.dataList = dataList;

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
    }

    @Override
    public StepsAdapter.StepsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.steps_row_layout, parent, false);
        StepsViewHolder vh = new StepsViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(StepsViewHolder holder, final int position) {
        holder.txtTitle.setText(dataList.get(position).getCity());
        //holder.txtDesc.setText(dataList.get(position));
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