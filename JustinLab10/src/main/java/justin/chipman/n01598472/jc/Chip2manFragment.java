// justin chipman n01598472
package justin.chipman.n01598472.jc;

import static java.lang.String.format;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.util.Log;
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

    private TextView weatherInfoTextView, countryTextView, humidityTextView, lonTextView, latTextView, cityTextView, descTextView;
    private Spinner citySpinner;
    private RadioGroup temperatureUnitRadioGroup;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private String latestWeatherJson;

    private String API_KEY;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chip2man, container, false);

        API_KEY = getString(R.string.api_key);
        weatherInfoTextView = view.findViewById(R.id.jusweatherInfoTextView);
        countryTextView = view.findViewById(R.id.juscountryTextView);
        humidityTextView = view.findViewById(R.id.jushumidityTextView);
        lonTextView = view.findViewById(R.id.juslonTextView);
        latTextView = view.findViewById(R.id.juslatTextView);
        cityTextView = view.findViewById(R.id.juscityTextView);
        descTextView = view.findViewById(R.id.jusdescTextView);
        citySpinner = view.findViewById(R.id.juscitySpinner);
        temperatureUnitRadioGroup = view.findViewById(R.id.justemperatureUnitRadioGroup);

        String savedUnit = getTemperatureUnitPreference();
        if ("Fahrenheit".equals(savedUnit)) {
            temperatureUnitRadioGroup.check(R.id.jusfahrenheitRadioButton);
        } else {
            temperatureUnitRadioGroup.check(R.id.juscelsiusRadioButton);
        }

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

        temperatureUnitRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                String unit = checkedId == R.id.juscelsiusRadioButton ? getString(R.string.celsius) : getString(R.string.fahrenheit);
                saveTemperatureUnitPreference(unit);

                if (latestWeatherJson != null && !latestWeatherJson.isEmpty()) {
                    updateWeatherInfo(latestWeatherJson);
                }
            }
        });
    }

    private void saveTemperatureUnitPreference(String unit) {
        SharedPreferences sharedPref = getActivity().getSharedPreferences(getString(R.string.weatherprefs), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(getString(R.string.temperatureunit), unit);
        editor.apply();
    }

    private String getTemperatureUnitPreference() {
        SharedPreferences sharedPref = getActivity().getSharedPreferences(getString(R.string.weatherprefs), Context.MODE_PRIVATE);
        return sharedPref.getString(getString(R.string.temperatureunit), getString(R.string.celsius)); // Default to Celsius
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
                latestWeatherJson = finalJson;
                Log.d("Json response",finalJson); // Debugging: Log the JSON response
                getActivity().runOnUiThread(() -> updateWeatherInfo(finalJson));
            } catch (Exception e) {
                Log.e("Chip2manFragment", "Exception fetching weather", e);
            }
        });
    }


    private void updateWeatherInfo(String json) {
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONObject main = jsonObject.getJSONObject("main");
            double tempKelvin = main.getDouble("temp");
            JSONObject sys = jsonObject.getJSONObject("sys");
            JSONObject weather = jsonObject.getJSONArray("weather").getJSONObject(0);

            String savedUnit = getTemperatureUnitPreference();
            double temp = savedUnit.equals(getString(R.string.celsius)) ? tempKelvin - 273.15 : (tempKelvin - 273.15) * 9 / 5 + 32;
            String unitSymbol = savedUnit.equals(getString(R.string.celsius)) ? getString(R.string.c) : getString(R.string.f);

            String temperature = String.format(getString(R.string.temperature_1f_s), temp, unitSymbol);
            String country = getString(R.string.country) + sys.getString("country");
            String humidity = getString(R.string.humidity) + main.getString("humidity") + "%";
            String lon = getString(R.string.lon) + jsonObject.getJSONObject("coord").getString("lon");
            String lat = getString(R.string.lat) + jsonObject.getJSONObject("coord").getString("lat");
            String city = getString(R.string.city) + jsonObject.getString("name");

            String description = getString(R.string.desc) + weather.getString("descriipton");

            weatherInfoTextView.setText(temperature);
            countryTextView.setText(country);
            humidityTextView.setText(humidity);
            lonTextView.setText(lon);
            latTextView.setText(lat);
            cityTextView.setText(city);
            descTextView.setText(description);

        } catch (Exception e) {
            Log.e("Chip2manFragment", "Exception parsing weather data", e);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        executorService.shutdown();
    }
}


