package snowsan0113.weather_app.android.listener;

import android.app.Activity;
import android.view.View;

import snowsan0113.weather_app.android.R;

public class TopButtonListener implements View.OnClickListener {

    private Activity activity;

    public TopButtonListener(Activity activity) {
        this.activity = activity;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.main_activity_backbutton) {
            activity.finish();
        }
    }

}
