package com.github.catvod.demo;

import android.content.Context;
import com.github.catvod.spider.NongMing;
import org.junit.Before;
import org.junit.Test;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 测试
 */
public class TestNongMing {

    NongMing nongMing;

    @Before
    public void init() throws Exception {
        nongMing = new NongMing();
        nongMing.init(new Context(), "");
    }

    @Test
    public void homeContent() throws Exception {
        System.out.println(nongMing.homeContent(true));
    }

    @Test
    public void homeVideoContent() throws Exception {
    }

    @Test
    public void categoryContent() throws Exception {
        HashMap<String, String> extend = new HashMap<>();
        extend.put("area", "大陆");
        extend.put("year", "2023");
        System.out.println(nongMing.categoryContent("1", "1", true, extend));
    }

    @Test
    public void detailContent() throws Exception {
        List<String> ids = new ArrayList<>();
        ids.add("/vod-detail-id-22271.html");
        System.out.println(nongMing.detailContent(ids));
    }

    @Test
    public void searchContent() throws Exception {
        System.out.println(nongMing.searchContent("我", true));
    }

    @Test
    public void playerContent() throws Exception {
        List<String> vipFlags = new ArrayList<>();
//        System.out.println(nongMing.playerContent("线路①", "https://m.xiangdao.me/vod-play-id-38670-src-1-num-19.html", vipFlags));
//        System.out.println(nongMing.playerContent("线路①", "https://m.xiangdao.me/vod-play-id-38670-src-1-num-2.html", vipFlags));

        // 待测试
//        System.out.println(nongMing.playerContent("云播①", "https://m.xiangdao.me/vod-play-id-22271-src-1-num-1.html", vipFlags));

        System.out.println(nongMing.playerContent("云播①", "https://m.xiangdao.me/vod-play-id-36635-src-1-num-41.html", vipFlags));

    }
}
