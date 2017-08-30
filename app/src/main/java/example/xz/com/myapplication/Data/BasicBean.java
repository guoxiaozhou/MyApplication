package example.xz.com.myapplication.Data;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;

import java.util.List;

/**
 * Created by Administrator on 2017/8/23.
 */

public class BasicBean {
    @SerializedName("date")
     private String date;

    @SerializedName("stories")
    private JsonArray stories;

    @SerializedName("top_stories")
    private JsonArray topStory;

    public Gson gson=new Gson();

    public BasicBean() {
    }

    public BasicBean(String date, JsonArray stories, JsonArray topStory) {
        this.date = date;
        this.stories = stories;
        this.topStory = topStory;
    }

    public List<Story> setStoriesList(JsonArray jsonArray){
        List<Story> storyList=gson.fromJson(jsonArray,new TypeToken<List<Story>>(){}.getType());
        return storyList;
    }

    public List<TopStory> setTopStoriesList(JsonArray jsonArray){
        return gson.fromJson(jsonArray,new TypeToken<List<TopStory>>(){}.getType());
    }


    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public JsonArray getStories() {
        return stories;
    }

    public void setStories(JsonArray stories) {
        this.stories = stories;
    }

    public JsonArray getTopStory() {
        return topStory;
    }

    public void setTopStory(JsonArray topStory) {
        this.topStory = topStory;
    }


}
