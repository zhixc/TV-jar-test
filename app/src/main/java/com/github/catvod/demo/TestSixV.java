package com.github.catvod.demo;

import android.content.Context;

import com.github.catvod.spider.SixV;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TestSixV {
    SixV sixV;

    @Before
    public void init() throws Exception {
        sixV = new SixV();
        sixV.init(new Context(), "https://www.6vdy.org/");
    }

    @Test
    public void homeContent() throws Exception {
        // 首页测试，输出...
        System.out.println(sixV.homeContent(true));
    }

    @Test
    public void categoryContent() throws Exception {
        // 分类页面数据测试
        HashMap<String, String> extend = new HashMap<>();
//        System.out.println(sixV.categoryContent("xijupian", "1", true, extend));
        System.out.println(sixV.categoryContent("xijupian", "3", true, extend));
    }

    @Test
    public void detailContent() throws Exception {
        // 详情页面数据测试
        ArrayList<String> ids = new ArrayList<>();
//        ids.add("/xijupian/20346.html");
//        ids.add("/xijupian/20531.html");
//        ids.add("/xijupian/20620.html");
        ids.add("/donghuapian/21608.html");
//        ids.add("/xijupian/21836.html");
//        ids.add("/xijupian/21667.html");
        System.out.println(sixV.detailContent(ids));
    }

    @Test
    public void searchContent() throws Exception {
//        System.out.println(sixV.searchContent("保镖", true));
        System.out.println(sixV.searchContent("周处除三害", true));
//        System.out.println(sixV.searchContent("我", true));
//        System.out.println(sixV.searchContent("我", true, "2"));
    }

    @Test
    public void playerContent() throws Exception {
        String flag = "磁力1";
        String id = "magnet:?xt=urn:btih:8c935a998671e1f0938ae51597d8819b9f6871cf";
        List<String> vipFlags = new ArrayList<>();
        sixV.playerContent(flag, id, vipFlags);
    }
}
