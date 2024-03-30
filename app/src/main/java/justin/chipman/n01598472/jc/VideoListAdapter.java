package justin.chipman.n01598472.jc;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class VideoListAdapter extends RecyclerView.Adapter<VideoListAdapter.VideoViewHolder> {

    private final LayoutInflater inflater;
    private final List<String> videoTitles; // The titles of the videos
    private ItemClickListener clickListener; // Click listener to handle video selection

    // Provide a suitable constructor (depends on the kind of dataset)
    public VideoListAdapter(Context context, List<String> videoTitles) {
        this.inflater = LayoutInflater.from(context);
        this.videoTitles = videoTitles;
    }

    // Create new views (invoked by the layout manager)
    @NonNull
    @Override
    public VideoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.item_video, parent, false);
        return new VideoViewHolder(itemView);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(@NonNull VideoViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        String title = videoTitles.get(position);
        holder.videoTitle.setText(title);
        // If you have video thumbnails or logos, set them here
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return videoTitles.size();
    }

    // Convenience method for getting data at click position
    String getItem(int id) {
        return videoTitles.get(id);
    }

    // Allows clicks events to be caught
    void setClickListener(ItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }

    // Parent activity or fragment will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class VideoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView videoTitle;
        ImageView videoLogo;

        VideoViewHolder(View itemView) {
            super(itemView);
            videoTitle = itemView.findViewById(R.id.videoTitle);
            videoLogo = itemView.findViewById(R.id.videoLogo); // Make sure you have an ImageView with this id in your item layout
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (clickListener != null) clickListener.onItemClick(view, getAdapterPosition());
        }
    }
}


