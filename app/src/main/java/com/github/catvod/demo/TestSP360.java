package com.github.catvod.demo;

import android.content.Context;
import com.github.catvod.spider.SP360;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TestSP360 {
    SP360 sp360;

    @Before
    public void init() throws Exception {
        sp360 = new SP360();
        sp360.init(new Context(), "douyin,xigua");
    }

    @Test
    public void homeContent() throws Exception {
        System.out.println(sp360.homeContent(true));
    }

    @Test
    public void homeVideoContent() {
    }

    @Test
    public void categoryContent() throws Exception {
        HashMap<String, String> extend = new HashMap<>();
        extend.put("year", "2023");
//        extend.put("year", "2022");
//        extend.put("actor", "成龙");
        System.out.println(sp360.categoryContent("1", "1", true, extend));
//        System.out.println(sp360.categoryContent("2", "1", true, extend));
//        System.out.println(sp360.categoryContent("3", "1", true, extend));
    }

    @Test
    public void detailContent() throws Exception {
        List<String> ids = new ArrayList<>();
        // 电影
//        ids.add("{\"detailReferer\":\"https://www.360kan.com/m/gKTiahH7QXH4TB.html\",\"detailUrl\":\"https://api.web.360kan.com/v1/detail?cat=1&id=gKTiahH7QXH4TB&callback=\"}");
//        ids.add("{\"detailReferer\":\"https://www.360kan.com/m/faLnYkMmQXn6UB.html\",\"detailUrl\":\"https://api.web.360kan.com/v1/detail?cat=1&id=faLnYkMmQXn6UB&callback=\"}");
        // 电视剧
//        ids.add("{\"detailReferer\":\"https://www.360kan.com/tv/QLFtcX7mRmDqMX.html\",\"detailUrl\":\"https://api.web.360kan.com/v1/detail?cat=2&id=QLFtcX7mRmDqMX&callback=\"}");
//        ids.add("{\"detailReferer\":\"https://www.360kan.com/tv/QbFpaX7mRmPqMH.html\",\"detailUrl\":\"https://api.web.360kan.com/v1/detail?cat=2&id=QbFpaX7mRmPqMH&callback=\"}");
        // 综艺
//        ids.add("{\"detailReferer\":\"https://www.360kan.com/va/Z8cla3Nz8pYCFD.html\",\"detailUrl\":\"https://api.web.360kan.com/v1/detail?cat=3&id=Z8cla3Nz8pYCFD&callback=\"}");
//        ids.add("{\"detailReferer\":\"https://www.360kan.com/va/ZsclbnNy7pgCDD.html\",\"detailUrl\":\"https://api.web.360kan.com/v1/detail?cat=3&id=ZsclbnNy7pgCDD&callback=\"}");
//        ids.add("{\"detailReferer\":\"https://www.360kan.com/va/Y8glbalv7Jc6DD.html\",\"detailUrl\":\"https://api.web.360kan.com/v1/detail?cat=3&id=Y8glbalv7Jc6DD&callback=\"}");
        // 动漫
//        ids.add("{\"detailReferer\":\"https://www.360kan.com/ct/OUPkaZ7kNY7vDj.html\",\"detailUrl\":\"https://api.web.360kan.com/v1/detail?cat=4&id=OUPkaZ7kNY7vDj&callback=\"}");
        ids.add("{\"detailReferer\":\"https://www.360kan.com/ct/QE8qcp7jMoOxDj.html\",\"detailUrl\":\"https://api.web.360kan.com/v1/detail?cat=4&id=QE8qcp7jMoOxDj&callback=\"}");
        System.out.println(sp360.detailContent(ids));
    }

    @Test
    public void searchContent() throws Exception {
        System.out.println(sp360.searchContent("三体", true));
//        System.out.println(sp360.searchContent("满江红", true));
    }

    @Test
    public void playerContent() throws Exception {
    }
}