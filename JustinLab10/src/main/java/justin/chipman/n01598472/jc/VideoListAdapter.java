// justin chipman n01598472
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
    private final List<String> videoTitles;
    private ItemClickListener clickListener;


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


    @Override
    public void onBindViewHolder(@NonNull VideoViewHolder holder, int position) {


        String title = videoTitles.get(position);
        holder.videoTitle.setText(title);

    }


    @Override
    public int getItemCount() {
        return videoTitles.size();
    }


    String getItem(int id) {
        return videoTitles.get(id);
    }


    void setClickListener(ItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }


    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }


    public class VideoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView videoTitle;
        ImageView videoLogo;

        VideoViewHolder(View itemView) {
            super(itemView);
            videoTitle = itemView.findViewById(R.id.jusvideoTitle);
            videoLogo = itemView.findViewById(R.id.jusvideoLogo);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (clickListener != null) clickListener.onItemClick(view, getAdapterPosition());
        }
    }
}


