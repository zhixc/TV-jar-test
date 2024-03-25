package com.github.catvod.demo;

import android.content.Context;
import com.github.catvod.spider.KuaikanZy;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * 测试
 */
public class TestKuaikanzy {

    private KuaikanZy kuaikanZy;

    @Before
    public void init() throws Exception {
        kuaikanZy = new KuaikanZy();
        kuaikanZy.init(new Context(), "");
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
        ids.add("66351");
        System.out.println(kuaikanZy.detailContent(ids));
    }

    @Test
    public void searchContent() throws Exception {
        System.out.println(kuaikanZy.searchContent("斗破", true));
    }

    @Test
    public void playerContent() throws Exception {
    }
}