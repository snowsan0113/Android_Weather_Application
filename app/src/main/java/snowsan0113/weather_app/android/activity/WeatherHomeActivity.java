package snowsan0113.weather_app.android.activity;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.android.material.tabs.TabLayout;

import snowsan0113.weather_app.android.R;
import snowsan0113.weather_app.android.listener.BottomButtonClickListener;
import snowsan0113.weather_app.android.listener.TabClickListener;
import snowsan0113.weather_app.android.listener.WeatherDateButtonListener;
import snowsan0113.weather_app.android.manager.LayoutManager;

public class WeatherHomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_weather_home);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        MaterialButtonToggleGroup group = findViewById(R.id.weather_datebutton_group);
        group.addOnButtonCheckedListener(new WeatherDateButtonListener(this));
        LayoutManager.getInstance(this).setWeatherLayout(findViewById(R.id.weather_scroll_layout));
        TabLayout tab = findViewById(R.id.tabLayout);
        tab.addOnTabSelectedListener(new TabClickListener(this));
        MaterialButtonToggleGroup button = findViewById(R.id.bottom_button_group);
        button.addOnButtonCheckedListener(new BottomButtonClickListener(this));
    }
}