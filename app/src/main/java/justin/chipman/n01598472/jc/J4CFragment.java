package justin.chipman.n01598472.jc;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class J4CFragment extends Fragment {
    private TextView dateTimeDisplay;
    private AdView mAdView;
    private int adClickCounter = 0;
    private Handler handler = new Handler();
    private Runnable runnable;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_j4c, container, false);

        dateTimeDisplay = view.findViewById(R.id.clockTextView);
        mAdView = view.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdClicked() {
                super.onAdClicked();
                adClickCounter++;
                Toast.makeText(getActivity(), "Justin Chipman: " + adClickCounter, Toast.LENGTH_LONG).show();
            }
        });

        updateDateTime();
        return view;
    }

    private void updateDateTime() {
        runnable = new Runnable() {
            @Override
            public void run() {
                String currentTime = new java.text.SimpleDateFormat("YYYY:MM:dd HH:mm:ss", java.util.Locale.getDefault()).format(new java.util.Date());
                dateTimeDisplay.setText(currentTime);
                handler.postDelayed(this, 1000);
            }
        };
        handler.postDelayed(runnable, 1000);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (handler != null && runnable != null) {
            handler.removeCallbacks(runnable);
        }
    }
}



