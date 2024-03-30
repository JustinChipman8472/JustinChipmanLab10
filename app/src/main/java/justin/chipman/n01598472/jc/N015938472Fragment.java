package justin.chipman.n01598472.jc;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class N015938472Fragment extends Fragment implements VideoListAdapter.ItemClickListener {

    public N015938472Fragment() {
        // Required empty public constructor
    }
    private WebView videoWebView;
    private RecyclerView videoListRecyclerView;
    private VideoListAdapter adapter;
    private List<String> videoTitles;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_n015938472, container, false);

        videoWebView = view.findViewById(R.id.videoWebView);
        videoListRecyclerView = view.findViewById(R.id.videoListRecyclerView);

        // Initialize the list of video titles
        videoTitles = Arrays.asList(getResources().getStringArray(R.array.video_titles));

        // Set up the RecyclerView
        videoListRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new VideoListAdapter(getContext(), videoTitles);
        adapter.setClickListener(this);
        videoListRecyclerView.setAdapter(adapter);

        // Set up the WebView
        videoWebView.getSettings().setJavaScriptEnabled(true);
        videoWebView.getSettings().setDomStorageEnabled(true);
        videoWebView.setVisibility(View.GONE); // Initially hide the WebView

        return view;
    }

    @Override
    public void onItemClick(View view, int position) {
        // Here you will need to retrieve the correct YouTube video URL or ID
        String videoUrl = getVideoUrl(position); // Implement this method based on your data
        playYouTubeVideo(videoUrl);
    }

    private String getVideoUrl(int position) {
        // Retrieve the array of video IDs from strings.xml
        String[] videoIds = getResources().getStringArray(R.array.video_ids);
        // Use the position to access the corresponding video ID
        if (position >= 0 && position < videoIds.length) {
            String videoId = videoIds[position];
            // Return the proper YouTube embed URL format
            return "https://www.youtube.com/embed/" + videoId;
        } else {
        // Handle invalid position if necessary
        return ""; // Return empty or a default video URL
        }
    }

    private void playYouTubeVideo(String videoUrl) {
        videoWebView.setVisibility(View.VISIBLE);
        videoWebView.loadUrl(videoUrl); // Load the video URL
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (videoWebView != null) {
            videoWebView.destroy();
        }
    }
}