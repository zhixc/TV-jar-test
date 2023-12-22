package com.github.catvod.demo;

import android.content.Context;
import com.github.catvod.spider.Live2Vod;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class TestLive2Vod {

    Live2Vod live2Vod;

    @Before
    public void init() throws Exception {
        live2Vod = new Live2Vod();
//        live2Vod.init(new Context(), "Box醒人室$https://agit.ai/fantaiying/fty/raw/branch/master/live.txt#影视范精选$https://agit.ai/fantaiying/fmm/raw/branch/main/tv/m3u/global.m3u&&&https://cdn.jsdelivr.net/gh/zhixc/CatVodTVSpider@main/other/pic/live.png");
//        live2Vod.init(new Context(), "Box醒人室$https://agit.ai/fantaiying/fty/raw/branch/master/live.txt#影视范精选$https://agit.ai/fantaiying/fmm/raw/branch/main/tv/m3u/global.m3u&&&");
        live2Vod.init(new Context(), "https://raw.githubusercontent.com/zhixc/CatVodTVSpider/main/other/json/live.json"); // 远程 json 配置文件
    }

    @Test
    public void homeContent() throws Exception {
        // 首页测试，输出...
        System.out.println(live2Vod.homeContent(true));
    }

    @Test
    public void categoryContent() throws Exception {
        // 分类页面数据测试
//        String tid = "{\"pic\": \"https://cdn.jsdelivr.net/gh/zhixc/CatVodTVSpider@main/other/pic/live.png\", \"url\": \"https://agit.ai/fantaiying/fty/raw/branch/master/live.txt\", \"circuit\": 1}";
//        String tid = "{\"pic\": \"https://cdn.jsdelivr.net/gh/zhixc/CatVodTVSpider@main/other/pic/live.png\", \"url\": \"https://agit.ai/xiaohu/tvbox/raw/branch/main/live.txt\", \"circuit\": 1}";
        String tid = "{\"circuit\":\"\",\"pic\":\"https://cdn.jsdelivr.net/gh/zhixc/CatVodTVSpider@main/other/pic/live.png\",\"url\":\"https://raw.githubusercontent.com/Ftindy/IPTV-URL/main/bestv.m3u\",\"group\":\"1\"}";
        System.out.println(live2Vod.categoryContent(tid, "1", true, null));
    }

    @Test
    public void detailContent() throws Exception {
        // m3u 格式的参数
        String s = "{\"vod_play_url\":\"CCTV1$https://cntv.sbs/live?auth=230601&id=cctv1\",\"pic\":\"https://live.fanmingming.com/tv/CCTV1.png\"}";
        JSONObject videoInfoObj = new JSONObject(s);
        List<String> ids = new ArrayList<>();
        ids.add(videoInfoObj.toString());
        System.out.println(live2Vod.detailContent(ids));
    }

    @Test
    public void playerContent() throws Exception {
        System.out.println(live2Vod.playerContent("", "https://cntv.sbs/live?auth=230601&id=cctv1", new ArrayList<>()));
    }
}