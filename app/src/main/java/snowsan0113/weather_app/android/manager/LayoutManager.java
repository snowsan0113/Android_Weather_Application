package snowsan0113.weather_app.android.manager;

import android.app.Activity;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
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
import snowsan0113.weather_app.android.fragment.WeatherFragment;

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
                        todaytomorrow_layout.getWeatherTextView().setText(first_weather.getDescription());
                        if (first_weather.getDescription().contains("晴れ")) {
                            todaytomorrow_layout.getWeatherIcon().setImageResource(R.drawable.mark_tenki_hare);
                        }
                        else if (first_weather.getDescription().contains("曇り")) {
                            todaytomorrow_layout.getWeatherIcon().setImageResource(R.drawable.mark_tenki_kumori);
                        }
                        else if (first_weather.getDescription().contains("雨")) {
                            todaytomorrow_layout.getWeatherIcon().setImageResource(R.drawable.mark_tenki_umbrella);
                        }
                    }
                });
            }
        }).start();
    }

    public void setTodayTomorrowLayout(LinearLayout weather_layout) {
        weather_layout.removeAllViews();
        todaytomorrow_layout_list.clear();

        LocalDateTime localDateTime = LocalDateTime.now();
        for (int n = 0; n < 2; n++) {
            //レイアウト
            LinearLayout linear = new LinearLayout(activity);
            LinearLayout.LayoutParams linear_param = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
            linear.setLayoutParams(linear_param);
            linear.setOrientation(LinearLayout.VERTICAL);
            linear.setGravity(Gravity.CENTER);

            //日付テキスト
            TextView date_text = new TextView(activity);
            date_text.setGravity(Gravity.CENTER);
            date_text.setTextSize(16);
            date_text.setText(localDateTime.getDayOfMonth() + "日(" + localDateTime.getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.JAPAN) + ")");

            //アイコン
            ImageView weather_icon = new ImageView(activity);
            LinearLayout.LayoutParams icon_param = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 33, activity.getResources().getDisplayMetrics())
            );
            icon_param.topMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, activity.getResources().getDisplayMetrics());
            weather_icon.setLayoutParams(icon_param);
            weather_icon.setImageResource(R.drawable.mark_tenki_hare);

            //天気
            TextView weather_text = new TextView(activity);
            LinearLayout.LayoutParams weathertext_param = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
            weather_text.setLayoutParams(weathertext_param);
            weather_text.setText("晴れのち曇り");
            weather_text.setTextSize(16);

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

            linear.addView(date_text);
            linear.addView(weather_icon);
            linear.addView(weather_text);
            linear.addView(weather_info_layout);

            weather_layout.addView(linear);
            localDateTime = localDateTime.plusDays(1);

            todaytomorrow_layout_list.add(new WeatherLayout(WeatherLayout.WeatherLayoutType.TODAY_TOMORROW, linear));
        }
    }

    public void setOnehourLayout(LinearLayout weather_layout) {
        weather_layout.removeAllViews();
        onehour_layout_list.clear();

        LocalDateTime localDateTime = LocalDateTime.now();
        for (int n = 0; n < 24; n++) {
            //レイアウト
            LinearLayout linear = new LinearLayout(activity);
            linear.setOrientation(LinearLayout.VERTICAL);
            linear.setGravity(Gravity.CENTER);

            //時間表示
            TextView hour_text = new TextView(activity);
            hour_text.setGravity(Gravity.CENTER);
            hour_text.setText(String.valueOf(localDateTime.getHour()));

            //天気アイコン
            ImageView imageView = new ImageView(activity);
            LinearLayout.LayoutParams imgParams = new LinearLayout.LayoutParams(
                    (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 38, activity.getResources().getDisplayMetrics()),
                    (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 33, activity.getResources().getDisplayMetrics())
            );
            imgParams.topMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, activity.getResources().getDisplayMetrics());
            imageView.setLayoutParams(imgParams);
            imageView.setImageResource(R.drawable.mark_tenki_hare);

            //最高気温
            TextView max_text = new TextView(activity);
            max_text.setText("xx℃");
            max_text.setTextColor(MAX_TEMP_COLOR);
            max_text.setGravity(Gravity.CENTER);

            //最低気温
            TextView min_text = new TextView(activity);
            min_text.setText("xx℃");
            min_text.setTextColor(MIN_TEMP_COLOR);
            min_text.setGravity(Gravity.CENTER);

            //レイアウト追加する
            linear.addView(hour_text);
            linear.addView(imageView);
            linear.addView(max_text);
            linear.addView(min_text);

            weather_layout.addView(linear);

            onehour_layout_list.add(new WeatherLayout(WeatherLayout.WeatherLayoutType.FEW_HOUR, linear));
            localDateTime = localDateTime.plusHours(1);
        }
    }

    public void setTwoWeekLayout(LinearLayout weather_layout) {
        weather_layout.removeAllViews();
        twoweek_layout_list.clear();

        LocalDateTime localDateTime = LocalDateTime.now();
        for (int n = 0; n < 14; n++) {
            //レイアウト
            LinearLayout linear = new LinearLayout(activity);
            linear.setOrientation(LinearLayout.VERTICAL);
            linear.setGravity(Gravity.CENTER);

            //日付表示
            TextView hour_text = new TextView(activity);
            hour_text.setGravity(Gravity.CENTER);
            hour_text.setText(localDateTime.getDayOfMonth() + "日\n(" + localDateTime.getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.JAPAN) + ")");

            //天気アイコン
            ImageView imageView = new ImageView(activity);
            LinearLayout.LayoutParams imgParams = new LinearLayout.LayoutParams(
                    (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 38, activity.getResources().getDisplayMetrics()),
                    (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 33, activity.getResources().getDisplayMetrics())
            );
            imgParams.topMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, activity.getResources().getDisplayMetrics());
            imageView.setLayoutParams(imgParams);
            imageView.setImageResource(R.drawable.mark_tenki_hare);

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

            //レイアウト追加する
            linear.addView(hour_text);
            linear.addView(imageView);
            linear.addView(max_text);
            linear.addView(min_text);

            weather_layout.addView(linear);

            twoweek_layout_list.add(new WeatherLayout(WeatherLayout.WeatherLayoutType.TWO_WEEK, linear));
            localDateTime = localDateTime.plusDays(1);
        }
    }

    public static LayoutManager getInstance(Activity activity) {
        if (instance == null) instance = new LayoutManager(activity);
        return instance;
    }
}
