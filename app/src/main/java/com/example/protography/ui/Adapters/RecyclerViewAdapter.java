package com.example.protography.ui.Adapters;

import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.protography.MainActivity;
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



    public RecyclerViewAdapter(List<Image> imageList) {
        this.imageList = imageList;
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
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private Image image;
        private ImageView imageView;
        private TextView userTextView;
        private TextView titleTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            titleTextView = itemView.findViewById(R.id.title);
            userTextView = itemView.findViewById(R.id.user);
            imageView = itemView.findViewById(R.id.imageView);

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
            Toast.makeText(itemView.getContext(), "immagine cliccata", Toast.LENGTH_SHORT).show();
        }
    }
}
