// justin chipman n01598472
package justin.chipman.n01598472.jc;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.squareup.picasso.Picasso;

public class Jus1tinFragment extends Fragment {

    private Spinner spinner;
    private ImageView imageView;
    private ProgressBar progressBar;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_jus1tin, container, false);

        spinner = root.findViewById(R.id.jusSpinner);
        Button button = root.findViewById(R.id.jusDownloadButton);
        imageView = root.findViewById(R.id.jusDLPhotoImageView);
        progressBar = root.findViewById(R.id.jusDownloadProgressBar);

        // Initialize spinner with URLs or resource IDs
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.image_urls_array, android.R.layout.simple_spinner_item); // Define this array in strings.xml
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        button.setOnClickListener(v -> downloadImage());

        return root;
    }

    private void downloadImage() {
        String imageUrl = spinner.getSelectedItem().toString();
        progressBar.setVisibility(View.VISIBLE);

        new Handler().postDelayed(() -> {
            Picasso.get().load(imageUrl).into(imageView, new com.squareup.picasso.Callback() {
                @Override
                public void onSuccess() {
                    progressBar.setVisibility(View.GONE);
                }

                @Override
                public void onError(Exception e) {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(getContext(), "Error downloading image", Toast.LENGTH_SHORT).show();
                }
            });
        }, 5000); // Delay to simulate download time
    }
}