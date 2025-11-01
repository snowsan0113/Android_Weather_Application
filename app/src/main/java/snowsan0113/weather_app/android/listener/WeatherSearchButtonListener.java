package snowsan0113.weather_app.android.listener;

import android.app.Activity;
import android.app.AlertDialog;
import android.location.Address;
import android.text.Editable;
import android.view.View;
import android.widget.EditText;

import com.google.android.material.button.MaterialButton;
import com.google.gson.JsonObject;

import java.util.Map;

import snowsan0113.weather_app.android.R;
import snowsan0113.weather_app.android.manager.JsonManager;
import snowsan0113.weather_app.android.manager.LayoutManager;

public class WeatherSearchButtonListener implements View.OnClickListener {

    private final Activity activity;

    public WeatherSearchButtonListener(Activity activity) {
        this.activity = activity;
    }

    @Override
    public void onClick(View view) {
        for (Map.Entry<MaterialButton, Address> entry : LayoutManager.search_button.entrySet()) {
            MaterialButton key = entry.getKey();
            Address value = entry.getValue();
            if (key.getId() == view.getId()) {
                AlertDialog builder = new AlertDialog.Builder(activity)
                        .setMessage("登録しますか？")
                        .setPositiveButton("はい", (dialogInterface, i) -> {
                            EditText edit = activity.findViewById(R.id.search_location_edittext);
                            JsonManager json = new JsonManager(activity, JsonManager.FileType.CONFIG);
                            JsonObject root_obj = json.getRawElement().getAsJsonObject();
                            JsonObject loc_obj = root_obj.getAsJsonObject("weather_location");
                            JsonObject new_loc = new JsonObject();
                            new_loc.addProperty("latitude", value.getLatitude());
                            new_loc.addProperty("longitude", value.getLongitude());
                            loc_obj.add(value.getAdminArea(), new_loc);
                            json.updateJson();
                            activity.finish();
                        })
                        .setNegativeButton("いいえ", (dialogInterface, i) -> {
                            dialogInterface.cancel();
                        }).create();
                builder.show();
            }
        }
    }

}
