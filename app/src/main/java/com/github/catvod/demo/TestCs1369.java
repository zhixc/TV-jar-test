package com.github.catvod.demo;

import android.content.Context;
import com.github.catvod.spider.Cs1369;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 测试
 */
public class TestCs1369 {

    Cs1369 cs1369;

    @Before
    public void init() throws Exception {
        cs1369 = new Cs1369();
        cs1369.init(new Context(), "");
    }

    @Test
    public void homeContent() throws Exception {
        System.out.println(cs1369.homeContent(true));
    }

    @Test
    public void homeVideoContent() throws Exception {
    }

    @Test
    public void categoryContent() throws Exception {
        System.out.println(cs1369.categoryContent("1", "1", true, new HashMap<>()));
    }

    @Test
    public void detailContent() throws Exception {
        List<String> ids = new ArrayList<>();
        ids.add("https://www.cs1369.com/detail/55296.html");
        System.out.println(cs1369.detailContent(ids));
    }

    @Test
    public void searchContent() throws Exception {
        // 网站搜索功能关闭了，这个就没法了
        System.out.println(cs1369.searchContent("周处", true));
    }

    @Test
    public void playerContent() throws Exception {
        List<String> vipFlags = new ArrayList<>();
        System.out.println(cs1369.playerContent("Qile", "https://www.cs1369.com/play/55296-1-1.html", vipFlags));
    }
}
