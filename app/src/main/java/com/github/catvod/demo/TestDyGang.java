package com.github.catvod.demo;

import android.content.Context;
import com.github.catvod.spider.DyGang;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 测试
 */
public class TestDyGang extends TestExample {

    DyGang dyGang;

    @Before
    @Override
    public void init() throws Exception {
        dyGang = new DyGang();
        dyGang.init(new Context(), "");
    }

    @Test
    @Override
    public void homeContent() throws Exception {
        System.out.println(dyGang.homeContent(true));
    }

    @Test
    @Override
    public void homeVideoContent() throws Exception {
        System.out.println(dyGang.homeVideoContent());
    }

    @Test
    @Override
    public void categoryContent() throws Exception {
        HashMap<String, String> extend = new HashMap<>();
        System.out.println(dyGang.categoryContent("ys", "1", true, extend));
    }

    @Test
    @Override
    public void detailContent() throws Exception {
        List<String> ids = new ArrayList<>();
//        ids.add("/ys/20240402/54327.htm");
//        ids.add("/dmq/20211031/48089.htm");
//        ids.add("/ys/20240402/54324.htm");
//        ids.add("/ys/20240403/54333.htm");
//        ids.add("/ys/20240126/53858.htm");
//        ids.add("/dmq/20220805/49858.htm");
        ids.add("/dmq/20240304/54101.htm");
        System.out.println(dyGang.detailContent(ids));
    }

    @Test
    @Override
    public void searchContent() throws Exception {
        System.out.println(dyGang.searchContent("周处除三害", true));
//        System.out.println(dyGang.searchContent("我", true));
//        System.out.println(dyGang.searchContent("我", true, "2"));
    }

    @Test
    @Override
    public void playerContent() throws Exception {
        String id = "magnet:?xt=urn:btih:7df6fc1a473d519a47ee415a285ea3cc39653a0d&dn=%e8%b6%8a%e8%bf%87%e5%b1%b1%e4%b8%98";
        String flag = "磁力";
        List<String> vipFlags = new ArrayList<>();
        System.out.println(dyGang.playerContent(flag, id, vipFlags));
    }
}
