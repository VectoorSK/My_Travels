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
import com.jakewharton.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.CustomViewHolder> {

    private List<Country> dataList;
    private Context context;
    private Picasso picasso;

    public MyAdapter(Context context, List<Country> dataList) {
        this.context = context;
        this.dataList = dataList;

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
    }

    @Override
    public MyAdapter.CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.row_layout, parent, false);
        CustomViewHolder vh = new CustomViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, final int position) {
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