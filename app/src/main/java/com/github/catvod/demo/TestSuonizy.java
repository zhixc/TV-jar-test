package com.github.catvod.demo;

import android.content.Context;
import com.github.catvod.spider.SuoniZy;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * 测试
 */
public class TestSuonizy extends TestExample{

    private SuoniZy suoniZy;

    @Before
    @Override
    public void init() throws Exception {
        suoniZy = new SuoniZy();
        suoniZy.init(new Context(), "");
    }

    @Test
    @Override
    public void homeContent() throws Exception {
    }

    @Test
    @Override
    public void homeVideoContent() throws Exception {
    }

    @Test
    @Override
    public void categoryContent() throws Exception {
    }

    @Test
    @Override
    public void detailContent() throws Exception {
        List<String> ids = new ArrayList<>();
//        ids.add("64676"); // 紫川·光明三杰
        ids.add("34382"); // 完美世界
        System.out.println(suoniZy.detailContent(ids));
    }

    @Test
    @Override
    public void searchContent() throws Exception {
        System.out.println(suoniZy.searchContent("神印", true));
    }

    @Test
    @Override
    public void playerContent() throws Exception {
    }
}
