package com.example.protography.ui.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.protography.MainActivity;
import com.example.protography.R;
import com.example.protography.databinding.ImageItemBinding;
import com.example.protography.ui.ImageActivity;
import com.example.protography.ui.Models.Image;
import com.squareup.picasso.Picasso;

import java.util.List;


/**
 * Adapter to show news in a RecyclerView.
 */
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private static final String TAG = "RecyclerViewAdapter";
    private List<Image> imageList;
    private ImageItemBinding binding;



    public RecyclerViewAdapter(List<Image> imageList) {
        this.imageList = imageList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Create a new view, which defines the UI of the list item
        binding = ImageItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        View view = binding.getRoot();

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
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private Image image;
        private ImageView imageView;
        private TextView userTextView;
        private TextView titleTextView;
        private Context context;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            titleTextView = binding.title;
            userTextView = binding.user;
            imageView = binding.imageView;
            this.context = context;
            binding.like.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "Clicked");



                }
            });

            itemView.setOnClickListener(this);

        }

        public void bind(Image image) {
            this.image = image;
            titleTextView.setText(image.getImageTitle());

            userTextView.setText("User");
            Picasso.get().load(image.getImageUrl()).into(imageView);
        }

        @Override
        public void onClick(View v) {
            // Successivamente si aprirà il fragment con i dati della foto cliccata
            //Toast.makeText(itemView.getContext(), "Immagine cliccata", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(itemView.getContext(), ImageActivity.class);
            intent.putExtra("Immagine", image);

            // L'animazione funziona solo con sdk >= 21
            if (android.os.Build.VERSION.SDK_INT >= 21) {
                ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) itemView.getContext(), imageView, ViewCompat.getTransitionName(imageView));
                itemView.getContext().startActivity(intent, options.toBundle());
            } else
                itemView.getContext().startActivity(intent);
        }
    }
}
