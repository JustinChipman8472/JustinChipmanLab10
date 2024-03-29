package justin.chipman.n01598472.jc;

import static java.lang.String.format;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.util.Log;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Chip2manFragment extends Fragment {

    private TextView weatherInfoTextView;
    private Spinner citySpinner;
    private RadioGroup temperatureUnitRadioGroup;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    // Replace YOUR_API_KEY with your actual OpenWeatherMap API key
    private final String API_KEY = "4a9109aa76c6113b14442edfa8e68d27";

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chip2man, container, false);

        weatherInfoTextView = view.findViewById(R.id.weatherInfoTextView);
        citySpinner = view.findViewById(R.id.citySpinner);
        temperatureUnitRadioGroup = view.findViewById(R.id.temperatureUnitRadioGroup);

        setupSpinner();
        return view;
    }

    private void setupSpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.cities, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        citySpinner.setAdapter(adapter);

        citySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String[] latitudes = getResources().getStringArray(R.array.city_latitudes);
                String[] longitudes = getResources().getStringArray(R.array.city_longitudes);
                fetchWeatherData(latitudes[position], longitudes[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void fetchWeatherData(String lat, String lon) {
        executorService.execute(() -> {
            try {
                URL url = new URL("https://api.openweathermap.org/data/2.5/weather?lat=" + lat + "&lon=" + lon + "&appid=" + API_KEY);
                Log.d("api url", "https://api.openweathermap.org/data/2.5/weather?lat=" + lat + "&lon=" + lon + "&appid=" + API_KEY);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                Log.d("Content-Length", "" + connection.getContentLength());
                connection.setReadTimeout(10000); // 10 seconds
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder json = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    Log.d("WhileLoop", line);
                    json.append(line);
                }
                String finalJson = json.toString();
                Log.d("Json response",finalJson); // Debugging: Log the JSON response
                getActivity().runOnUiThread(() -> updateWeatherInfo(finalJson));
            } catch (Exception e) {
                e.printStackTrace(); // Debugging: Print stack trace for exceptions

                    // Debugging: Update UI with error message for visibility
                Log.e("Chip2manFragment", "Exception fetching weather", e);


            }
        });
    }


    private void updateWeatherInfo(String json) {
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONObject main = jsonObject.getJSONObject("main");
            double tempKelvin = main.getDouble("temp");
            // Default to Celsius. Adjust based on user selection if needed.
            int selectedId = temperatureUnitRadioGroup.getCheckedRadioButtonId();
            double temp = tempKelvin - 273.15; // Convert to Celsius
            if (selectedId == R.id.fahrenheitRadioButton) {
                temp = temp * 9 / 5 + 32; // Convert to Fahrenheit if selected
            }
            String input = format("Temp: %.1f", temp);
            weatherInfoTextView.setText(input);
            Log.d("Chip2manFragment", "Updating UI with weather info: " + input);
        } catch (Exception e) {
            Log.e("Chip2manFragment", "Exception fetching weather", e);
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        executorService.shutdown();
    }
}


