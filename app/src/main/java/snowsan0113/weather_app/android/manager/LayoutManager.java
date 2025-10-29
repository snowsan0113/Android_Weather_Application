package snowsan0113.weather_app.android.manager;

import android.app.Activity;
import android.graphics.Color;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.time.LocalDateTime;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import snowsan0113.weather_app.android.MainActivity;
import snowsan0113.weather_app.android.R;
import snowsan0113.weather_app.android.api.OpenWeatherAPI;

public class LayoutManager {

    //インスタンス
    private static LayoutManager instance;
    private Activity activity;

    //定数
    public static final int MAX_TEMP_COLOR = Color.parseColor("#E43232");
    public static final int MIN_TEMP_COLOR = Color.parseColor("#3F51B5");
    public static final int BUTTON_ON_COLOR = Color.parseColor("#408EE9");
    public static final int BUTTON_OFF_COLOR = Color.parseColor("#c1c3cc");

    //レイアウト
    private List<WeatherLayout> todaytomorrow_layout_list = new ArrayList<>();
    private List<WeatherLayout> onehour_layout_list = new ArrayList<>();
    private List<WeatherLayout> twoweek_layout_list = new ArrayList<>();

    public LayoutManager(Activity activity) {
        this.activity = activity;
    }


    public void setWeatherLayout(LinearLayout weatherLayout) {
        setTodayTomorrowLayout(weatherLayout);


        new Thread(() -> {
            OpenWeatherAPI openWeatherAPI = OpenWeatherAPI.getInstance(activity);
            for (OpenWeatherAPI.WeatherList weatherList : openWeatherAPI.getWeatherList()) {
                List<OpenWeatherAPI.WeatherList.Weather> weather = weatherList.getWeather();
                OpenWeatherAPI.WeatherList.Weather first_weather = weather.get(0);

                activity.runOnUiThread(() -> {
                    for (WeatherLayout todaytomorrow_layout : todaytomorrow_layout_list) {
                        if (todaytomorrow_layout.getWeatherTime().toLocalDate().equals(weatherList.getLocalDateTime().toLocalDate())) {
                            Log.d("a", weatherList.getLocalDateTime().toString() + "," + first_weather.getDescription() + ":" + weatherList.getMain().getTempMax(true));
                            Log.d("i", todaytomorrow_layout.getWeatherTime().toString());

                            todaytomorrow_layout.getWeatherTextView().setText(first_weather.getDescription());
                            todaytomorrow_layout.getMaxTempTextView().setText(weatherList.getMain().getTempMax(false) + "℃");
                            todaytomorrow_layout.getMinTempTextView().setText(weatherList.getMain().getTempMin(false) + "℃");

                            ImageView icon = todaytomorrow_layout.getWeatherIcon();
                            icon.setImageDrawable(null);
                            if (first_weather.getDescription().contains("晴")) {
                                icon.setImageResource(R.drawable.mark_tenki_hare);
                            }
                            else if (first_weather.getDescription().contains("曇") || first_weather.getDescription().contains("厚い雲")) {
                                icon.setImageResource(R.drawable.mark_tenki_kumori);
                            }
                            else if (first_weather.getDescription().contains("雨")) {
                                icon.setImageResource(R.drawable.mark_tenki_umbrella);
                            }
                        }
                    }
                });
            }
        }).start();
    }

    public void setTodayTomorrowLayout(LinearLayout weather_layout) {
        weather_layout.removeAllViews();
        if (!todaytomorrow_layout_list.isEmpty()) {
            for (WeatherLayout todaytomorrow : todaytomorrow_layout_list) {
                LinearLayout linear = todaytomorrow.getRootLayout();
                weather_layout.addView(linear);
            }
        }
        else {
            LocalDateTime localDateTime = LocalDateTime.now();
            for (int n = 0; n < 2; n++) {
                LinearLayout linear = createWeatherLayout(WeatherLayout.WeatherLayoutType.TODAY_TOMORROW, localDateTime);
                weather_layout.addView(linear);
                todaytomorrow_layout_list.add(new WeatherLayout(WeatherLayout.WeatherLayoutType.TODAY_TOMORROW, localDateTime, linear));
                localDateTime = localDateTime.plusDays(1);
            }
        }
    }

    public void setOnehourLayout(LinearLayout weather_layout) {
        weather_layout.removeAllViews();
        if (!onehour_layout_list.isEmpty()) {
            for (WeatherLayout todaytomorrow : onehour_layout_list) {
                LinearLayout linear = todaytomorrow.getRootLayout();
                weather_layout.addView(linear);
            }
        }
        else {
            LocalDateTime localDateTime = LocalDateTime.now();
            for (int n = 0; n < 24; n++) {
                LinearLayout linear = createWeatherLayout(WeatherLayout.WeatherLayoutType.FEW_HOUR, localDateTime);
                weather_layout.addView(linear);

                onehour_layout_list.add(new WeatherLayout(WeatherLayout.WeatherLayoutType.FEW_HOUR, localDateTime, linear));
                localDateTime = localDateTime.plusHours(1);
            }
        }
    }

    public void setTwoWeekLayout(LinearLayout weather_layout) {
        weather_layout.removeAllViews();
        if (!twoweek_layout_list.isEmpty()) {
            for (WeatherLayout todaytomorrow : twoweek_layout_list) {
                LinearLayout linear = todaytomorrow.getRootLayout();
                weather_layout.addView(linear);
            }
        }
        else {
            LocalDateTime localDateTime = LocalDateTime.now();
            for (int n = 0; n < 14; n++) {
                LinearLayout linear = createWeatherLayout(WeatherLayout.WeatherLayoutType.TWO_WEEK, localDateTime);
                weather_layout.addView(linear);

                twoweek_layout_list.add(new WeatherLayout(WeatherLayout.WeatherLayoutType.TWO_WEEK, localDateTime, linear));
                localDateTime = localDateTime.plusDays(1);
            }
        }
    }

    public LinearLayout createWeatherLayout(WeatherLayout.WeatherLayoutType type, LocalDateTime localDateTime) {
        //レイアウト
        LinearLayout linear = new LinearLayout(activity);
        linear.setOrientation(LinearLayout.VERTICAL);
        linear.setGravity(Gravity.CENTER);

        TextView date_text = new TextView(activity);
        date_text.setGravity(Gravity.CENTER);
        date_text.setTextSize(16);

        if (type == WeatherLayout.WeatherLayoutType.FEW_HOUR) {
            date_text.setText(String.valueOf(localDateTime.getHour()));
        }
        else if (type == WeatherLayout.WeatherLayoutType.TWO_WEEK || type == WeatherLayout.WeatherLayoutType.TODAY_TOMORROW) {
            date_text.setText(localDateTime.getDayOfMonth() + "日\n(" + localDateTime.getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.JAPAN) + ")");
        }
        linear.addView(date_text);

        //アイコン
        ImageView weather_icon = new ImageView(activity);
        LinearLayout.LayoutParams icon_param = new LinearLayout.LayoutParams(
                (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 38, activity.getResources().getDisplayMetrics()),
                (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 33, activity.getResources().getDisplayMetrics())
        );
        icon_param.topMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, activity.getResources().getDisplayMetrics());
        weather_icon.setLayoutParams(icon_param);
        weather_icon.setImageResource(R.drawable.mark_tenki_hare);
        linear.addView(weather_icon);

        if (type == WeatherLayout.WeatherLayoutType.FEW_HOUR || type == WeatherLayout.WeatherLayoutType.TWO_WEEK) {
            //最高気温
            TextView max_text = new TextView(activity);
            max_text.setText("14℃");
            max_text.setTextColor(MAX_TEMP_COLOR);
            max_text.setGravity(Gravity.CENTER);

            //最低気温
            TextView min_text = new TextView(activity);
            min_text.setText("10℃");
            min_text.setTextColor(MIN_TEMP_COLOR);
            min_text.setGravity(Gravity.CENTER);

            linear.addView(max_text);
            linear.addView(min_text);
        }
        else if (type == WeatherLayout.WeatherLayoutType.TODAY_TOMORROW) {
            TextView weather_text = new TextView(activity);
            LinearLayout.LayoutParams weathertext_param = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
            weather_text.setLayoutParams(weathertext_param);
            weather_text.setText("晴れのち曇り");
            weather_text.setTextSize(16);
            linear.addView(weather_text);

            //最低最高
            LinearLayout weather_info_layout = new LinearLayout(activity);
            LinearLayout.LayoutParams weather_info_param = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
            );
            weather_info_layout.setLayoutParams(weather_info_param);
            weather_info_layout.setOrientation(LinearLayout.HORIZONTAL);

            TextView max_temp_text = new TextView(activity);
            max_temp_text.setTextColor(MAX_TEMP_COLOR);
            max_temp_text.setTextSize(16);
            max_temp_text.setText("16℃");

            TextView min_temp_text = new TextView(activity);
            min_temp_text.setTextColor(MIN_TEMP_COLOR);
            min_temp_text.setTextSize(16);
            min_temp_text.setText("10℃");

            weather_info_layout.addView(max_temp_text);
            weather_info_layout.addView(min_temp_text);

            linear.addView(weather_info_layout);
        }

        return linear;
    }

    public static LayoutManager getInstance(Activity activity) {
        if (instance == null) instance = new LayoutManager(activity);
        return instance;
    }
}
