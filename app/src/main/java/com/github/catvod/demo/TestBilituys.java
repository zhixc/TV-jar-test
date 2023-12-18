package com.github.catvod.demo;

import android.content.Context;
import com.github.catvod.spider.Bilituys;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TestBilituys {
    Bilituys bilituys;

    @Before
    public void init() throws Exception {
        bilituys = new Bilituys();
        bilituys.init(new Context(), "");
    }

    @Test
    public void homeContent() throws Exception {
        // 首页内容测试
        System.out.println(bilituys.homeContent(true));
    }

    @Test
    public void homeVideoContent() throws Exception {
    }

    @Test
    public void categoryContent() throws Exception {
        // 分类内容测试
        HashMap<String, String> extend = new HashMap<>();
        extend.put("year", "2023");
//        System.out.println(bilituys.categoryContent("1", "1", true, extend));
        System.out.println(bilituys.categoryContent("1", "2", true, extend));
    }

    @Test
    public void detailContent() throws Exception {
        // 详情内容测试
        List<String> ids = new ArrayList<>();
        ids.add("/bilidetail/168012.html");
//        ids.add("/bilidetail/53926.html");
        System.out.println(bilituys.detailContent(ids));
    }

    @Test
    public void searchContent() throws Exception {
        // 搜索内容测试
//        System.out.println(bilituys.searchContent("我", true));
        System.out.println(bilituys.searchContent("我", true, "2"));
    }

    @Test
    public void playerContent() throws Exception {
        // 播放内容测试
        List<String> vipFlags = new ArrayList<>();
//        System.out.println(bilituys.playerContent("哔哩¹13", "/biliplay/53926-1-1.html", vipFlags));
        System.out.println(bilituys.playerContent("大雄13", "/biliplay/53926-2-1.html", vipFlags));
    }
}
