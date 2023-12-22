package com.github.catvod.demo;

import com.github.catvod.spider.PiaoHua;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;

public class TestPiaoHua {

    PiaoHua piaoHua;

    @Before
    public void init() throws Exception {
        piaoHua = new PiaoHua();
    }

    @Test
    public void homeContent() throws Exception {
        System.out.println(piaoHua.homeContent(true));
    }

    @Test
    public void homeVideoContent() {
    }

    @Test
    public void categoryContent() throws Exception {
        HashMap<String, String> extend = new HashMap<>();
//        System.out.println(piaoHua.categoryContent("/dongzuo/", "3", true, extend));
        System.out.println(piaoHua.categoryContent("https://www.xpiaohua.com/column/dongzuo/20230622/63676.html", "1", true, extend));
    }

    @Test
    public void detailContent() throws Exception {
        ArrayList<String> ids = new ArrayList<>();
//        ids.add("https://www.xpiaohua.com/column/lianxuju/20210221/51623.html");
//        ids.add("https://www.xpiaohua.com/column/dongzuo/20230626/63766.html");
//        ids.add("https://www.xpiaohua.com/column/dongzuo/20230622/63721.html");
        ids.add("https://www.xpiaohua.com/column/dongzuo/20230622/63719.html");
        System.out.println(piaoHua.detailContent(ids));
    }

    @Test
    public void searchContent() throws Exception {
        System.out.println(piaoHua.searchContent("长月", true));
    }

    @Test
    public void playerContent() throws Exception {
    }
}