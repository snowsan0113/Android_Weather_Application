package snowsan0113.weather_app.android.fragment;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.android.material.tabs.TabLayout;

import java.time.LocalDateTime;

import snowsan0113.weather_app.android.R;
import snowsan0113.weather_app.android.listener.BottomButtonClickListener;
import snowsan0113.weather_app.android.listener.TabClickListener;
import snowsan0113.weather_app.android.listener.WeatherDateButtonListener;
import snowsan0113.weather_app.android.manager.LayoutManager;

public class WeatherFragment extends Fragment {

    public WeatherFragment() {

    }

    public static WeatherFragment newInstance() {
        WeatherFragment fragment = new WeatherFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_weather, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FragmentActivity activity = getActivity();
        MaterialButtonToggleGroup group = view.findViewById(R.id.weather_datebutton_group);
        group.addOnButtonCheckedListener(new WeatherDateButtonListener(activity));
        LayoutManager.getInstance(activity).setWeatherLayout(view.findViewById(R.id.weather_scroll_layout));
        TabLayout tab = view.findViewById(R.id.tabLayout);
        tab.addOnTabSelectedListener(new TabClickListener(activity));
        MaterialButtonToggleGroup button = view.findViewById(R.id.bottom_button_group);
        button.addOnButtonCheckedListener(new BottomButtonClickListener(activity));
    }
}