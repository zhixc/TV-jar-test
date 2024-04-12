package com.github.catvod.demo;

import android.content.Context;
import com.github.catvod.spider.Xunlei8;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 测试
 */
public class TestXunlei8 extends TestExample {

    private Xunlei8 xunlei8;

    @Before
    @Override
    public void init() throws Exception {
        xunlei8 = new Xunlei8();
        xunlei8.init(new Context(), "");
    }

    @Test
    @Override
    public void homeContent() throws Exception {
    }

    @Test
    @Override
    public void homeVideoContent() throws Exception {
        System.out.println(xunlei8.homeVideoContent());
    }

    @Test
    @Override
    public void categoryContent() throws Exception {
        HashMap<String, String> extend = new HashMap<>();
        extend.put("cateId", "科幻");
        extend.put("year", "2023");
        System.out.println(xunlei8.categoryContent("list", "1", true, extend));
    }

    @Test
    @Override
    public void detailContent() throws Exception {
        ArrayList<String> ids = new ArrayList<>();
//        ids.add("/movie/40555095.html");
        ids.add("/movie/75984095.html");
        System.out.println(xunlei8.detailContent(ids));
    }

    @Test
    @Override
    public void searchContent() throws Exception {
        //System.out.println(xunlei8.searchContent("周处除三害", true));
        System.out.println(xunlei8.searchContent("变形", true));
    }

    @Test
    @Override
    public void playerContent() throws Exception {
    }
}
