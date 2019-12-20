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
import com.example.myapplication.model.Travel;
import com.jakewharton.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

public class TravelAdapter extends RecyclerView.Adapter<TravelAdapter.CustomViewHolder> {

    private List<Travel> dataList;
    private Context context;
    private Picasso picasso;

    public interface OnItemClickListener {
        void onItemClick(Travel item);
    }
    private final OnItemClickListener listener;

    public TravelAdapter(Context context, List<Travel> dataList, OnItemClickListener listener) {
        this.context = context;
        this.dataList = dataList;
        this.listener = listener;

        Picasso.Builder builder = new Picasso.Builder(context);
        builder.downloader(new OkHttp3Downloader(context));
        picasso = builder.build();
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {

        public final View mView;

        public TextView txtTitle;
        public TextView txtDesc;
        public ImageView coverImage;
        public ImageView contImage;

        public CustomViewHolder(View itemView) {
            super(itemView);
            mView = itemView;

            txtTitle = (TextView) mView.findViewById(R.id.title);
            txtDesc = (TextView) mView.findViewById(R.id.description);
            contImage = (ImageView) mView.findViewById(R.id.contImage);
            coverImage = (ImageView) mView.findViewById(R.id.icon);

        }

        public void bind(final Travel item, final OnItemClickListener listener) {
            mView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    listener.onItemClick(item);
                }
            });
        }
    }

    @Override
    public TravelAdapter.CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.row_layout, parent, false);
        CustomViewHolder vh = new CustomViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, final int position) {
        holder.bind(dataList.get(position), listener);
        holder.txtTitle.setText(dataList.get(position).getCountry());
        holder.txtDesc.setText(toDateFr(dataList.get(position).getDate_from()));

        String continent = dataList.get(position).getContinent();
        if (continent.matches("Europe")) {
            holder.contImage.setImageResource(R.mipmap.ic_europe);
        } else if (continent.matches("Océanie")) {
            holder.contImage.setImageResource(R.mipmap.ic_oceanie);
        } else if (continent.matches("Asie")) {
            holder.contImage.setImageResource(R.mipmap.ic_asie);
        } else if (continent.matches("Amerique du Nord")) {
            holder.contImage.setImageResource(R.mipmap.ic_amerique_nord);
        } else if (continent.matches("Amerique du Sud")) {
            holder.contImage.setImageResource(R.mipmap.ic_amerique_sud);
        }
        picasso.load(dataList.get(position).getFlag())
                .placeholder((R.drawable.ic_launcher_background))
                .error(R.drawable.ic_launcher_background)
                .into(holder.coverImage);
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    private String toDateFr(String date) {

        String[] date_array = date.split("/");
        String year = date_array[0];
        String month = date_array[1];
        String day = date_array[2];

        switch (month) {
            case "01":
                month = "Janvier";
                break;
            case "02":
                month = "Février";
                break;
            case "03":
                month = "Mars";
                break;
            case "04":
                month = "Avril";
                break;
            case "05":
                month = "Mai";
                break;
            case "06":
                month = "Juin";
                break;
            case "07":
                month = "Juillet";
                break;
            case "08":
                month = "Août";
                break;
            case "09":
                month = "Septembre";
                break;
            case "10":
                month = "Octobre";
                break;
            case "11":
                month = "Novembre";
                break;
            case "12":
                month = "Décembre";
                break;
        }
        return day + " " + month + " " + year;
    }
}