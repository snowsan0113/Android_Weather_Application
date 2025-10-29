package snowsan0113.weather_app.android.listener;

import android.app.Activity;
import android.graphics.Color;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import snowsan0113.weather_app.android.R;

public class WeatherSearchViewListener implements View.OnKeyListener {

    private final Activity activity;

    public WeatherSearchViewListener(Activity activity) {
        this.activity = activity;
    }

    @Override
    public boolean onKey(View view, int i, KeyEvent keyEvent) {
        if (view.getId() == R.id.search_location_edittext) {
            Log.d("WeatherSearchViewListener", "1");
            if (keyEvent.getAction() == KeyEvent.ACTION_DOWN && keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                Log.d("WeatherSearchViewListener", "2");
                LinearLayout result_layout = activity.findViewById(R.id.search_result_layout);
                EditText edit_text = activity.findViewById(R.id.search_location_edittext);

                LinearLayout linear = new LinearLayout(activity);
                LinearLayout.LayoutParams linear_pram = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                linear_pram.setMargins(0, 5, 0, 3);
                linear.setLayoutParams(linear_pram);
                linear.setBackgroundColor(Color.parseColor("#FFFFFF"));
                linear.setOrientation(LinearLayout.VERTICAL);
                TextView name_text = new TextView(activity);
                name_text.setTextSize(30);
                name_text.setText(edit_text.getText().toString());
                TextView location_text = new TextView(activity);
                location_text.setTextSize(20);
                location_text.setText("（緯度経度）");
                linear.addView(name_text);
                linear.addView(location_text);

                result_layout.addView(linear);
                Log.d("WeatherSearchViewListener", result_layout.getChildCount() + "");
            }
        }
        return false;
    }

}
