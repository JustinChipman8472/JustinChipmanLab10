// justin chipman n01598472
package justin.chipman.n01598472.jc;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.snackbar.Snackbar;
import android.Manifest;


public class J4CFragment extends Fragment {
    private FusedLocationProviderClient fusedLocationClient;
    private int permissionDeniedCount = 0;
    private TextView dateTimeDisplay;
    private AdView mAdView;
    private int adClickCounter = 0;
    private Handler handler = new Handler();
    private Runnable runnable;
    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                View view = getView();
                if (isGranted) {
                    getLastLocation(view);
                } else {
                    permissionDeniedCount++;
                    if (permissionDeniedCount > 2) {

                                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                    intent.setData(Uri.fromParts("package", requireActivity().getPackageName(), null));
                                    startActivity(intent);

                    } else {
                        Snackbar.make(view, getString(R.string.permission_denied), Snackbar.LENGTH_INDEFINITE)
                                .setAction(getString(R.string.dismiss), null).show();
                    }
                }
            });

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_j4c, container, false);

        dateTimeDisplay = view.findViewById(R.id.jusclockTextView);
        mAdView = view.findViewById(R.id.jusadView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        Button locationButton = view.findViewById(R.id.juslocationButton);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity());

        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdClicked() {
                super.onAdClicked();
                adClickCounter++;
                String input = getString(R.string.justin_chipman) + adClickCounter;
                Toast.makeText(getActivity(), input, Toast.LENGTH_LONG).show();
            }
        });

        updateDateTime();

        locationButton.setOnClickListener(v -> {
            // Directly ask for the permissions
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION);
        });
        return view;
    }

    private void getLastLocation(View view) {
        fusedLocationClient.getLastLocation().addOnSuccessListener(requireActivity(), location -> {
            if (location != null) {
                // Use the location object
                String locationStr = getString(R.string.latitude) + location.getLatitude() + getString(R.string.longitude) + location.getLongitude();
                Snackbar.make(view, locationStr, Snackbar.LENGTH_INDEFINITE)
                        .setAction(getString(R.string.dismiss), v -> {}).show();
                sendLocationNotification(locationStr);
            } else {
                // Handle the situation where location is null
                Snackbar.make(view, getString(R.string.location_not_determined), Snackbar.LENGTH_INDEFINITE)
                        .setAction(getString(R.string.dismiss), null).show();
            }
        });
    }

    private void sendLocationNotification(String locationText) {
        NotificationManager notificationManager = (NotificationManager) requireActivity().getSystemService(Context.NOTIFICATION_SERVICE);

        // Create an explicit intent for an Activity in your app
        Intent intent = new Intent(requireContext(), J4CFragment.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(requireContext(), 0, intent, PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(requireContext(), getString(R.string.channel_id))
                .setSmallIcon(R.drawable.ic_location)
                .setContentTitle(getString(R.string.device_location))
                .setContentText(locationText)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                // Set the intent that will fire when the user taps the notification
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        // Issue the notification
        notificationManager.notify(1, builder.build());
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



