package com.github.catvod.demo;

import android.content.Context;
import com.github.catvod.spider.Dm84;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 测试
 */
public class TestDM84 {
    Dm84 dm84;

    @Before
    public void init() throws Exception {
        dm84 = new Dm84();
        dm84.init(new Context(), "");
    }

    @Test
    public void homeContent() throws Exception {
        System.out.println(dm84.homeContent(true));
    }

    @Test
    public void homeVideoContent() throws Exception {
        System.out.println(dm84.homeVideoContent());
    }

    @Test
    public void categoryContent() throws Exception {
        HashMap<String, String> extend = new HashMap<>();
        extend.put("year", "2023");
        System.out.println(dm84.categoryContent("1", "1", true, extend));
    }

    @Test
    public void detailContent() throws Exception {
        List<String> ids = new ArrayList<>();
        ids.add("/v/4045.html");
        System.out.println(dm84.detailContent(ids));
    }

    @Test
    public void searchContent() throws Exception {
        System.out.println(dm84.searchContent("修仙", true));
    }

    @Test
    public void playerContent() throws Exception {
        List<String> vipFlags = new ArrayList<>();
        System.out.println(dm84.playerContent("线路1", "https://dm84.tv/p/4045-1-1.html", vipFlags));
    }
}
