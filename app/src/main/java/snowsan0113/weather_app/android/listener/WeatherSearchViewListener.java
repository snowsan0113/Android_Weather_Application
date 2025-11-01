package snowsan0113.weather_app.android.listener;

import android.app.Activity;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;

import java.io.IOException;
import java.util.List;

import snowsan0113.weather_app.android.R;
import snowsan0113.weather_app.android.manager.LayoutManager;

public class WeatherSearchViewListener implements View.OnKeyListener {

    private final Activity activity;
    private static WeatherSearchButtonListener buttonListener = null;

    public WeatherSearchViewListener(Activity activity) {
        this.activity = activity;
        buttonListener = new WeatherSearchButtonListener(activity);
    }

    @Override
    public boolean onKey(View view, int i, KeyEvent keyEvent) {
        if (view.getId() == R.id.search_location_edittext) {
            Log.d("WeatherSearchViewListener", "1");
            if (keyEvent.getAction() == KeyEvent.ACTION_DOWN && keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                Log.d("WeatherSearchViewListener", "2");
                LinearLayout result_layout = activity.findViewById(R.id.search_result_layout);
                result_layout.removeAllViews();
                EditText edit_text = activity.findViewById(R.id.search_location_edittext);

                new Thread(() -> {
                    Geocoder geocoder = new Geocoder(activity);
                    try {
                        List<Address> address_list = geocoder.getFromLocationName(edit_text.getText().toString(), 10);
                        activity.runOnUiThread(() -> {
                            for (Address address : address_list) {
                                MaterialButton materialButton = new MaterialButton(activity);
                                materialButton.setOnClickListener(buttonListener);
                                materialButton.setBackgroundColor(Color.WHITE);
                                materialButton.setRippleColor(ColorStateList.valueOf(Color.LTGRAY));
                                materialButton.setStateListAnimator(null);
                                materialButton.setCornerRadius(0);
                                FrameLayout frame = new FrameLayout(activity);
                                frame.addView(materialButton);
                                LinearLayout.LayoutParams linear_pram = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                linear_pram.setMargins(0, 5, 0, 3);
                                frame.setLayoutParams(linear_pram);
                                frame.setBackgroundColor(Color.parseColor("#FFFFFF"));
                                LinearLayout linearLayout = new LinearLayout(activity);
                                linearLayout.setOrientation(LinearLayout.VERTICAL);
                                TextView name_text = new TextView(activity);
                                name_text.setTextSize(30);
                                name_text.setText(edit_text.getText().toString());
                                TextView location_text = new TextView(activity);
                                location_text.setTextSize(20);
                                location_text.setText("（緯度" + address.getLatitude() + ",経度：" + address.getLongitude() + ")");
                                linearLayout.addView(name_text);
                                linearLayout.addView(location_text);
                                frame.addView(linearLayout);
                                LayoutManager.search_button.put(materialButton, address);

                                result_layout.addView(frame);
                                Log.d("WeatherSearchViewListener", result_layout.getChildCount() + "");
                            }
                        });
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }).start();
            }
        }
        return false;
    }

}
