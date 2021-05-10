package com.example.protography.ui.Adapters;

import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.protography.R;
import com.example.protography.ui.Models.Image;
import com.squareup.picasso.Picasso;

import java.util.List;


/**
 * Adapter to show news in a RecyclerView.
 */
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private static final String TAG = "RecyclerViewAdapter";
    private List<Image> imageList;
    private OnItemClickListener onItemClickListener;

    // Custom interface to intercept the click on an item of the RecyclerView
    public interface OnItemClickListener {
        void onItemClick(Image image);
    }



    public RecyclerViewAdapter(List<Image> imageList, OnItemClickListener onItemClickListener) {
        this.imageList = imageList;
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(imageList.get(position));
    }

    @Override
    public int getItemCount() {
        return imageList.size();
    }

    // Classe ViewHolder
    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView imageView;
        private TextView userTextView;
        private TextView titleTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.title);
            userTextView = itemView.findViewById(R.id.user);
            imageView = itemView.findViewById(R.id.imageView);
        }

        public void bind(Image image) {
            titleTextView.setText(image.getImageTitle());
            userTextView.setText("User");
            Picasso.get().load(image.getImageUrl()).into(imageView);
            Log.d(TAG, "Uri: " + image.getImageUrl());


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    onItemClickListener.onItemClick(image);
                }
            });

        }
    }
}
