package snowsan0113.weather_app.android.activity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import snowsan0113.weather_app.android.R;
import snowsan0113.weather_app.android.listener.TopButtonListener;
import snowsan0113.weather_app.android.listener.WeatherSearchViewListener;

public class WeatherSearchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.weather_search_activity);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ImageButton button = findViewById(R.id.main_activity_backbutton);
        button.setOnClickListener(new TopButtonListener(this));
        EditText editText = findViewById(R.id.search_location_edittext);
        editText.setOnKeyListener(new WeatherSearchViewListener(this));
    }
}