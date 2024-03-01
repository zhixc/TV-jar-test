package com.github.catvod.demo;

import android.content.Context;
import com.github.catvod.spider.XingYiYing;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * 测试模版
 */
public class TestXingYiYing {

    private XingYiYing xingYiYing;

    @Before
    public void init() throws Exception {
        xingYiYing = new XingYiYing();
        xingYiYing.init(new Context(), "");
    }

    @Test
    public void homeContent() throws Exception {
    }

    @Test
    public void homeVideoContent() throws Exception {
    }

    @Test
    public void categoryContent() throws Exception {
    }

    @Test
    public void detailContent() throws Exception {
        List<String> ids = new ArrayList<>();
//        ids.add("205848");
//        ids.add("183491");
        ids.add("31606");
        System.out.println(xingYiYing.detailContent(ids));
    }

    @Test
    public void searchContent() throws Exception {
        System.out.println(xingYiYing.searchContent("斗破", true));
    }

    @Test
    public void playerContent() throws Exception {
        /*String episodeUrl = "https://www.xingyiying.com/index.php/vod/play/id/31606/sid/1/nid/1.html";
        List<String> vipFlags = new ArrayList<>();
        System.out.println(xingYiYing.playerContent("YK【共12集】", episodeUrl, vipFlags));*/

        String episodeUrl = "https://www.xingyiying.com/index.php/vod/play/id/31606/sid/2/nid/1.html";
        List<String> vipFlags = new ArrayList<>();
        System.out.println(xingYiYing.playerContent("豪华【共12集】", episodeUrl, vipFlags));
    }
}
