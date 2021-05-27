package com.example.protography.ui.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import com.example.protography.databinding.ImageItemBinding;
import com.example.protography.ui.Activities.ImageActivity;
import com.example.protography.ui.Models.Image;
import com.squareup.picasso.Picasso;

public class RecyclerViewAdapterFind extends RecyclerView.Adapter<RecyclerViewAdapterFind.ImageViewHolder> {

    private static final String TAG = "RecyclerViewAdapterFind";
    private List<Image> imageList;
    private ImageItemBinding binding;
    private Context context;

    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Image image);
    }

    public RecyclerViewAdapterFind(List<Image> imageList, Context context) {
        this.imageList = imageList;
        this.context = context;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Create a new view, which defines the UI of the list item
        binding = ImageItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        View view = binding.getRoot();

        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        holder.bind(imageList.get(position));
    }

    @Override
    public int getItemCount() {
        return imageList.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private Image image;
        private ImageView imageView;
        private TextView userTextView;
        private TextView titleTextView;
        private TextView descriptionTextView;
        private Context context;

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = binding.title;
            userTextView = binding.user;
            descriptionTextView = binding.description;
            imageView = binding.imageView;
            this.context = context;

            itemView.setOnClickListener(this);
        }

        public void bind(Image image) {
            this.image = image;
            titleTextView.setText(image.getImageTitle());
            userTextView.setText("User");
            if(image.getImageDescription().length() >= 20)
                descriptionTextView.setText(image.getImageDescription().substring(0, 20) + " ...");
            Picasso.get().load(image.getImageUrl()).into(imageView);
        }


        @Override
        public void onClick(View v) {

            Intent intent = new Intent(itemView.getContext(), ImageActivity.class);
            intent.putExtra("Immagine", image);

            // L'animazione funziona solo con sdk >= 21
            if (android.os.Build.VERSION.SDK_INT >= 21) {
                ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) itemView.getContext(), imageView, ViewCompat.getTransitionName(imageView));
                itemView.getContext().startActivity(intent, options.toBundle());
            } else
                itemView.getContext().startActivity(intent);
        }


        /*public Filter getFilter() {
            if(filter==null)
            {
                filter=new CustomFilter(filterList,this);
            }
            return filter;
        }*/
    }
}
