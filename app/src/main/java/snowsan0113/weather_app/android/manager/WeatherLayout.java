package snowsan0113.weather_app.android.manager;

import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.LocalDateTime;

public class WeatherLayout {

    private LinearLayout root_layout;
    private WeatherLayoutType type;
    private LocalDateTime localDateTime;
    private TextView date_textview;
    private ImageView weather_icon_imageview;
    @Nullable
    private TextView weather_textview;

    private TextView max_temp_textview;
    private TextView min_temp_textview;

    public WeatherLayout(WeatherLayoutType type, LocalDateTime localDateTime, LinearLayout linearLayout) {
        this.root_layout = linearLayout;
        this.type = type;
        this.localDateTime = localDateTime;

        //共通
        this.date_textview = (TextView) linearLayout.getChildAt(0);
        this.weather_icon_imageview = (ImageView) linearLayout.getChildAt(1);

        if (type == WeatherLayoutType.TODAY_TOMORROW) {
            this.weather_textview = (TextView) linearLayout.getChildAt(2);
            LinearLayout weather_info_layout = (LinearLayout) linearLayout.getChildAt(3);
            this.max_temp_textview = (TextView) weather_info_layout.getChildAt(0);
            this.min_temp_textview = (TextView) weather_info_layout.getChildAt(1);
        }
        if (type == WeatherLayoutType.FEW_HOUR || type == WeatherLayoutType.TWO_WEEK) {
            this.max_temp_textview = (TextView) linearLayout.getChildAt(2);
            this.min_temp_textview = (TextView) linearLayout.getChildAt(3);
        }

    }

    public LocalDateTime getWeatherTime() {
        return localDateTime;
    }

    public void setWeatherTime(LocalDateTime localDateTime) {
        this.localDateTime = localDateTime;
    }

    public WeatherLayoutType getType() {
        return type;
    }

    public TextView getMinTempTextView() {
        return min_temp_textview;
    }

    public TextView getMaxTempTextView() {
        return max_temp_textview;
    }

    public @Nullable TextView getWeatherTextView() {
        return weather_textview;
    }

    public ImageView getWeatherIcon() {
        return weather_icon_imageview;
    }

    public TextView getDateTextView() {
        return date_textview;
    }

    public LinearLayout getRootLayout() {
        return root_layout;
    }


    public enum WeatherLayoutType {
        TODAY_TOMORROW,
        TWO_WEEK,
        FEW_HOUR,
    }
}
