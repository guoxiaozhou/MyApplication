package example.xz.com.myapplication.Creator;

import java.util.ArrayList;
import java.util.List;

import example.xz.com.myapplication.Data.TopStory;
import example.xz.com.myapplication.Data.TopStoryModel;
import example.xz.com.myapplication.Data.TopStoryViewModel;
import example.xz.com.myapplication.MyApplication;
import example.xz.com.myapplication.db.TopstoryDao;

/**
 * Created by Administrator on 2017/8/28.
 */

public class ViewModelCreator {
    private static ViewModelCreator viewModelCreator;
    private ModelCreator modelCreator;
    private MyApplication application;

    public ViewModelCreator(MyApplication application) {
        this.application = application;
        this.modelCreator=ModelCreator.getInstance(application);
    }
    public static synchronized ViewModelCreator getInstance(MyApplication application){
        if(viewModelCreator==null)
            viewModelCreator=new ViewModelCreator(application);
        return viewModelCreator;
    }
    //保存topstory
    public List<TopStoryViewModel> setTopstoryModels(List<TopStory> topStories){
        List<TopStoryViewModel> topStoryViewModels=new ArrayList<TopStoryViewModel>();
        for(TopStory topStory:topStories){
            TopStoryModel topStoryModel=modelCreator.setTopstoryModel(topStory);
            topStoryViewModels.add(new TopStoryViewModel(topStoryModel));
        }
        return topStoryViewModels;
    }
    public List<TopStoryViewModel> getTopstoryModels(){
        List<TopStoryViewModel> topStoryViewModels=new ArrayList<TopStoryViewModel>();
        List<TopStoryModel> topStoryModels=new TopstoryDao().find();
        if(topStoryModels!=null){
            for(TopStoryModel topStoryModel:topStoryModels){
                topStoryViewModels.add(new TopStoryViewModel(topStoryModel));
            }
        }
        return topStoryViewModels;
    }
}
