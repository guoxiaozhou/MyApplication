package example.xz.com.myapplication.Data;

import com.google.gson.JsonArray;

import java.util.List;

/**
 * Created by Administrator on 2017/8/23.
 */

public class Story {
    private JsonArray images;
    private int type;
    private int id;
    private String ga_prefix;

    public Story(JsonArray images, int type, int id, String ga_prefix, String title) {
        this.images = images;
        this.type = type;
        this.id = id;
        this.ga_prefix = ga_prefix;
        this.title = title;
    }

    public JsonArray getImages() {
        return images;
    }

    public void setImages(JsonArray images) {
        this.images = images;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getGa_prefix() {
        return ga_prefix;
    }

    public void setGa_prefix(String ga_prefix) {
        this.ga_prefix = ga_prefix;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    private String title;
}
