package justin.chipman.n01598472.jc;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class N015938472Fragment extends Fragment implements VideoListAdapter.ItemClickListener {

    public N015938472Fragment() {
        // Required empty public constructor
    }
    private WebView videoWebView;
    private RecyclerView videoListRecyclerView;
    private VideoListAdapter adapter;
    private List<VideoDetails> videoDetailsList; // A hypothetical class to hold video data.

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_n015938472, container, false);

        videoWebView = view.findViewById(R.id.videoWebView);
        videoListRecyclerView = view.findViewById(R.id.videoListRecyclerView);

        // Configure the WebView settings
        WebSettings webSettings = videoWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);

        // Initialize the RecyclerView and adapter
        videoListRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        initializeVideoList(); // Initialize your list of videos
        adapter = new VideoListAdapter(getContext(), videoDetailsList);
        adapter.setClickListener(this);
        videoListRecyclerView.setAdapter(adapter);

        return view;
    }


    private void initializeVideoList() {
        videoDetailsList = new ArrayList<>();
        // Get video titles from strings.xml
        String[] videoTitles = getResources().getStringArray(R.array.video_titles);
        String[] videoIds = getResources().getStringArray(R.array.video_ids);

        // Check if the titles and IDs arrays are of the same length to avoid IndexOutOfBoundsException
        if (videoTitles.length != videoIds.length) {
            throw new IllegalStateException("The length of video titles and video IDs must match.");
        }

        // Combine titles and IDs into videoDetailsList
        for (int i = 0; i < videoTitles.length; i++) {
            videoDetailsList.add(new VideoDetails(videoTitles[i], videoIds[i]));
        }
    }

    @Override
    public void onItemClick(View view, int position) {
        VideoDetails selectedVideo = videoDetailsList.get(position);
        String videoId = selectedVideo.getVideoId();
        playYouTubeVideo(videoId);
    }

    private void playYouTubeVideo(String videoId) {
        String frameVideo = "<html><body>YouTube video .. <br> <iframe width=\"320\" height=\"180\" src=\"https://www.youtube.com/embed/" + videoId + "\" frameborder=\"0\" allowfullscreen></iframe></body></html>";
        videoWebView.loadData(frameVideo, "text/html", "utf-8");
    }

    @Override
    public void onDestroy() {
        if (videoWebView != null) {
            videoWebView.destroy();
        }
        super.onDestroy();
    }

    // Inner class for Video details
    public static class VideoDetails {
        private final String title;
        private final String videoId;

        public VideoDetails(String title, String videoId) {
            this.title = title;
            this.videoId = videoId;
        }

        public String getTitle() {
            return title;
        }

        public String getVideoId() {
            return videoId;
        }
    }
}