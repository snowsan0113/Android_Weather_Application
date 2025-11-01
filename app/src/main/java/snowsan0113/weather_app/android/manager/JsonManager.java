package snowsan0113.weather_app.android.manager;

import android.app.Activity;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

import snowsan0113.weather_app.android.R;

public class JsonManager {

    private static Map<FileType, JsonManager> memory_json_map = new HashMap<>();

    private Activity activity;
    private JsonElement raw_element;
    private final FileType type;
    private final Gson gson;

    public JsonManager(Activity activity, FileType type) {
        this.activity = activity;
        this.type = type;
        this.gson = new GsonBuilder().setPrettyPrinting().create();
    }

    /**
     * メモリー上からJSONを取得する
     * @return {@link JsonElement}として返す
     */
    public JsonElement getRawElement() {
        createJson();
        if (raw_element == null) updateJson();
        return raw_element;
    }

    /**
     * ファイルからJsonを直接取得する
     * @deprecated ファイルから直接アクセスするため、{@link JsonManager#getRawElement()} を推奨
     * @return {@link JsonElement}として返す
     */
    @Deprecated
    public JsonElement getFileRawElement() throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(Files.newInputStream(getFile().toPath()), StandardCharsets.UTF_8))) {
            return gson.fromJson(reader, JsonElement.class);
        }
    }

    /**
     * @return 現在のJSONがどこにあるかを、ファイルとして返す
     */
    public File getFile() {
        return new File(activity.getFilesDir(), type.getName());
    }

    /**
     * メモリー上のJSONを更新する。
     * @apiNote ファイルの方が大きい場合、メモリー上のJSONはファイルのJSONに置き換えられる
     */
    public void updateJson() {
        try {
            if (!getFile().exists()) createJson(); //無い場合は作成
            if (raw_element == null) {
                raw_element = getFileRawElement();
            }
            else {
                JsonElement memory_json = getRawElement(); //メモリー上
                JsonElement file_json = getFileRawElement(); //ファイルから直接
                if (memory_json.toString().length() < file_json.toString().length()) {
                    raw_element = file_json;
                    Log.d("snow_weatherapp_json", "ファイルのJSONの方が大きいため、ファイルのJSONが使用されます。");
                }
            }
            memory_json_map.put(type, this);

            try (BufferedWriter write = new BufferedWriter(new OutputStreamWriter(Files.newOutputStream(getFile().toPath()), StandardCharsets.UTF_8))) {
                write.write(gson.toJson(raw_element));
                Log.d("snow_weatherapp_json", getFile().getName() + "の更新に成功しました。");
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * JSONを作成する関数。
     * @return ファイルの作成に成功した場合 もしくは 既にファイルが存在する場合 は trueを返し、失敗した場合は false を返す。
     */
    public boolean createJson() {
        boolean status = false;
        if (!getFile().exists()) {
            try (InputStream in = activity.getResources().openRawResource(type.getId())) {
                Files.copy(in, getFile().toPath());
                updateJson();
                status = true;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        else {
            status = true;
        }
        return status;
    }

    public enum FileType {
        CONFIG("config.json", R.raw.config);

        private final String name;
        private final int id;

        FileType(String name, int id) {
            this.name = name;
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public int getId() {
            return id;
        }
    }

}
