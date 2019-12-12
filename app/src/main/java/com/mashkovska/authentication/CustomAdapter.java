package com.mashkovska.authentication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.CustomViewHolder> {

    private List<Movie> movieList;
    private Context context;

    private static OnItemListener mOnItemListener;


    public interface OnItemListener{

        void onItemClick(int position);
    }

    public static void setOnItemListener(OnItemListener listener) {
        CustomAdapter.mOnItemListener = listener;
    }


    CustomAdapter(List<Movie> movieList) {
        this.movieList = movieList;
    }

    List<Movie> getMovieList() {
        return movieList;
    }

    @NonNull
    @Override
    public CustomAdapter.CustomViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, final int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_view, parent, false);
        CustomViewHolder customViewHolder = new CustomViewHolder(view);

        return customViewHolder;
    }

    @Override
    public void onBindViewHolder(final CustomViewHolder holder, final int position) {
        holder.textTitle.setText(movieList.get(position).getTitle());
        holder.textDesctiption.setText(movieList.get(position).getDescription());
        Picasso.get().load(movieList.get(position).getPoster()).into(holder.poster);
        holder.movieYear.setText(String.format("Year: %s", Integer.toString(movieList.get(position).getYear())));

    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    static class CustomViewHolder extends RecyclerView.ViewHolder {

        private TextView textTitle;
        private TextView textDesctiption;
        private ImageView poster;
        private TextView movieYear;


        private CustomViewHolder(final View itemView) {
            super(itemView);

            textDesctiption = itemView.findViewById(R.id.custom_desctription);
            poster = itemView.findViewById(R.id.custom_imageView);
            textTitle = itemView.findViewById(R.id.custom_title);
























            movieYear = itemView.findViewById(R.id.custom_year);

            itemView.setOnClickListener(v -> {
                if (mOnItemListener != null){
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION){
                        mOnItemListener.onItemClick(position);
                    }
                }
            });
        }
    }
}