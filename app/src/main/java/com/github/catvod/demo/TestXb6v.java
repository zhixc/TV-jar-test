package com.github.catvod.demo;

import android.content.Context;
import com.github.catvod.spider.Xb6v;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TestXb6v {
    Xb6v xb6v;

    @Before
    public void init() throws Exception {
        xb6v = new Xb6v();
        xb6v.init(new Context(), null);
    }

    @Test
    public void homeContent() throws Exception {
        System.out.println(xb6v.homeContent(true));
    }

    @Test
    public void homeVideoContent() throws Exception {
    }

    @Test
    public void categoryContent() throws Exception {
        HashMap<String, String> extend = new HashMap<>();
//        System.out.println(xb6v.categoryContent("/xijupian/", "1", true, extend));
        System.out.println(xb6v.categoryContent("/xijupian/", "2", true, extend));
    }

    @Test
    public void detailContent() throws Exception {
        List<String> ids = new ArrayList<>();
        ids.add("/dianshiju/guoju/22075.html");
        System.out.println(xb6v.detailContent(ids));
    }

    @Test
    public void searchContent() throws Exception {
        // 这下面两行需要同时执行，第二行才不会出问题，因为第二页开始需要的 URL 链接需要第一页的数据拼接
        System.out.println(xb6v.searchContent("我", true));
        System.out.println(xb6v.searchContent("我", true, "2"));
    }

    @Test
    public void playerContent() throws Exception {
        List<String> vipFlags = new ArrayList<>();
        System.out.println(xb6v.playerContent("磁力线路1", "magnet?xt=xxxxx", vipFlags));
    }
}
