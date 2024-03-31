package justin.chipman.n01598472.jc;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class J4CFragment extends Fragment {

    private TextView clockTextView, latitudeTextView, longitudeTextView;
    private int adClickCounter = 0;
    private Handler handler = new Handler(Looper.getMainLooper());
    private Runnable runnable;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_j4c, container, false);

        clockTextView = view.findViewById(R.id.clockTextView);
        latitudeTextView = view.findViewById(R.id.latitudeTextView);
        longitudeTextView = view.findViewById(R.id.longitudeTextView);

        // Initialize the clock updater
        initializeClock();

        Button locationButton = view.findViewById(R.id.locationButton);
        locationButton.setOnClickListener(v -> {
            // Here you should handle location permission request and retrieve location
        });

        // Placeholder frame layout for the ad, replace this with your AdView
        FrameLayout adPlaceholder = view.findViewById(R.id.adPlaceholder);
        adPlaceholder.setOnClickListener(v -> {
            adClickCounter++;
            // Here you would handle the ad click event
        });

        return view;
    }

    private void initializeClock() {
        final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        runnable = new Runnable() {
            @Override
            public void run() {
                clockTextView.setText(dateFormat.format(new Date()));
                handler.postDelayed(this, 1000);
            }
        };
        handler.post(runnable);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (handler != null && runnable != null) {
            handler.removeCallbacks(runnable);
        }
    }
}
