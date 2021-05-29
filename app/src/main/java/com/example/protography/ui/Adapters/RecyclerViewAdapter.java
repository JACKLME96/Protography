package com.example.protography.ui.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.protography.databinding.ImageItemBinding;
import com.example.protography.ui.Activities.ImageActivity;
import com.example.protography.ui.Models.Image;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private static final String TAG = "RecyclerViewAdapter";
    private List<Image> imageList;
    private ImageItemBinding binding;
    private Context context;



    public RecyclerViewAdapter(List<Image> imageList, Context context) {
        this.imageList = imageList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Create a new view, which defines the UI of the list item
        binding = ImageItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        View view = binding.getRoot();

        return new ViewHolder(view, context);
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
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private Image image;
        private ImageView imageView;
        private TextView userTextView;
        private TextView titleTextView;
        private Context context;


        public ViewHolder(@NonNull View itemView, Context context) {
            super(itemView);

            titleTextView = binding.title;
            userTextView = binding.user;
            imageView = binding.imageView;
            this.context = context;

            itemView.setOnClickListener(this);
        }

        public void bind(Image image) {
            this.image = image;
            titleTextView.setText(image.getImageTitle());

            userTextView.setText(image.getImageNameUser());
            Picasso.get().load(image.getImageUrl()).into(imageView);

            // La descrizione non serve nel profilo
            binding.description.setVisibility(View.GONE);
        }

        @Override
        public void onClick(View v) {

            Intent intent = new Intent(itemView.getContext(), ImageActivity.class);
            intent.putExtra("Immagine", image);

            // L'animazione funziona solo con sdk >= 21 e se lo screen è verticale
            if (android.os.Build.VERSION.SDK_INT >= 21 && context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) itemView.getContext(), imageView, ViewCompat.getTransitionName(imageView));
                ((Activity) itemView.getContext()).startActivity(intent, options.toBundle());
            } else
                itemView.getContext().startActivity(intent);
        }

    }
}
