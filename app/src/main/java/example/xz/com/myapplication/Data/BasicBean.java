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
    private List<Story> stories;

    @SerializedName("top_stories")
    private List<TopStory> topStory;

    public Gson gson=new Gson();

    public BasicBean() {
    }

    public BasicBean(String date, List<Story> stories, List<TopStory> topStory) {
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

    public List<Story> getStories() {
        return stories;
    }

    public void setStories(List<Story> stories) {
        this.stories = stories;
    }

    public List<TopStory> getTopStory() {
        return topStory;
    }

    public void setTopStory(List<TopStory> topStory) {
        this.topStory = topStory;
    }
}
