package snowsan0113.weather_app.android.listener;

import android.app.Activity;
import android.content.Intent;
import android.view.View;

import com.google.android.material.button.MaterialButtonToggleGroup;

import snowsan0113.weather_app.android.R;
import snowsan0113.weather_app.android.activity.WeatherSearchActivity;

public class BottomButtonClickListener implements MaterialButtonToggleGroup.OnButtonCheckedListener {

    private final Activity activity;

    public BottomButtonClickListener(Activity activity) {
        this.activity = activity;
    }

    @Override
    public void onButtonChecked(MaterialButtonToggleGroup group, int checkedId, boolean isChecked) {
        if (group.getId() == R.id.bottom_button_group) {
            if (checkedId == R.id.search_button) {
                Intent intent = new Intent(activity, WeatherSearchActivity.class);
                activity.startActivity(intent);
            }
        }
    }
}
