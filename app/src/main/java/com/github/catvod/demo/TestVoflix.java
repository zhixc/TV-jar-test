package com.github.catvod.demo;


import android.content.Context;
import com.github.catvod.spider.Voflix;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;

public class TestVoflix {
    Voflix voflix;

    @Before
    public void init() throws Exception {
        voflix = new Voflix();
        voflix.init(new Context(), "https://www.voflix.me/");
    }

    @Test
    public void homeContent() throws Exception {
        System.out.println(voflix.homeContent(true));
    }

    @Test
    public void homeVideoContent() throws Exception{
        System.out.println(voflix.homeVideoContent());
    }

    @Test
    public void categoryContent() throws Exception {
        HashMap<String, String> extend = new HashMap<>();
        extend.put("area", "中国香港");
        System.out.println(voflix.categoryContent("1", "1", true, extend));
    }

    @Test
    public void detailContent() throws Exception {
        ArrayList<String> ids = new ArrayList<>();
//        ids.add("/detail/156852.html");
        ids.add("/detail/162486.html");
        System.out.println(voflix.detailContent(ids));
    }

    @Test
    public void searchContent() throws Exception {
        System.out.println(voflix.searchContent("我", true));
    }

    @Test
    public void playerContent() throws Exception {
        System.out.println(voflix.playerContent("", "/play/139141-2-1.html", null));
    }
}