package snowsan0113.weather_app.android.listener;

import android.app.Activity;
import android.view.View;
import android.widget.LinearLayout;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.button.MaterialButtonToggleGroup;

import snowsan0113.weather_app.android.R;
import snowsan0113.weather_app.android.manager.LayoutManager;

public class WeatherDateButtonListener implements MaterialButtonToggleGroup.OnButtonCheckedListener {

    private Activity activity;

    public WeatherDateButtonListener(Activity activity) {
        this.activity = activity;
    }

    @Override
    public void onButtonChecked(MaterialButtonToggleGroup group, int checkedId, boolean isChecked) {
        if (group.getId() == R.id.weather_datebutton_group) {
            for (int n = 0; n < group.getChildCount(); n++) {
                group.getChildAt(n).setBackgroundColor(LayoutManager.BUTTON_OFF_COLOR);
            }

            LayoutManager layoutManager = LayoutManager.getInstance(activity);
            LinearLayout weather_layout = activity.findViewById(R.id.weather_scroll_layout);
            MaterialButton button = activity.findViewById(checkedId);
            button.setBackgroundColor(LayoutManager.BUTTON_ON_COLOR);

            if (checkedId == R.id.one_hour_button) {
                layoutManager.setOnehourLayout(weather_layout);
            }
            else if (checkedId == R.id.today_tomorrow_button) {
                layoutManager.setTodayTomorrowLayout(weather_layout);
            }
            else if (checkedId == R.id.two_week_button) {
                layoutManager.setTwoWeekLayout(weather_layout);
            }
        }
    }
}
