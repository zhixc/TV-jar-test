package com.github.catvod.spider;

import android.content.Context;
import android.text.TextUtils;

import com.github.catvod.crawler.Spider;
import com.github.catvod.crawler.SpiderDebug;
import com.github.catvod.net.OkHttp;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @author zhixc
 * 迅雷电影天堂
 */
public class Xl720 extends Spider {
    private final String siteUrl = "https://www.xl720.com";

    private final String userAgent = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/39.0.2171.71 Safari/537.36";
    private final Pattern NAME_PATTERN = Pattern.compile("《(.*?)》");

    private String req(String url, Map<String, String> header) {
        return OkHttp.string(url, header);
    }

    private Response req(Request request) throws Exception {
        return okClient().newCall(request).execute();
    }

    private String req(Response response) throws Exception {
        if (!response.isSuccessful()) return "";
        String content = response.body().string();
        response.close();
        return content;
    }

    private OkHttpClient okClient() {
        return OkHttp.client();
    }

    private Map<String, String> getHeader() {
        Map<String, String> header = new HashMap<>();
        header.put("User-Agent", userAgent);
        return header;
    }

    private Map<String, String> getHeader(String referer) {
        Map<String, String> header = new HashMap<>();
        header.put("User-Agent", userAgent);
        header.put("Referer", referer);
        return header;
    }

    private Map<String, String> getSearchHeader() {
        Map<String, String> header = new HashMap<>();
        header.put("User-Agent", userAgent);
        return header;
    }

    private String find(Pattern pattern, String html) {
        Matcher m = pattern.matcher(html);
        return m.find() ? m.group(1).trim() : "";
    }

    private JSONArray parseVodListFromDoc(String html) throws Exception {
        JSONArray videos = new JSONArray();
        Elements items = Jsoup.parse(html).select("[class=post-grid clearfix] > [class=post clearfix]");
        for (Element item : items) {
            String vodId = item.select(".entry-title > a").attr("href");
            String name = getName(item);
            String pic = item.select("img").attr("src");
            String remark = getRemark(item);

            JSONObject vod = new JSONObject();
            vod.put("vod_id", vodId);
            vod.put("vod_name", name);
            vod.put("vod_pic", pic);
            vod.put("vod_remarks", remark);
            videos.put(vod);
        }
        return videos;
    }

    private String getName(Element item) {
        String title = item.select(".entry-title > a").attr("title");
        String s = find(NAME_PATTERN, title);
        return "".equals(s) ? title : s;
    }

    private String getRemark(Element item) {
        try {
            String remark = item.select(".entry-title > a").attr("title").split("》")[1];
            return remark;
        } catch (Exception e) {
            return "";
        }
    }

    private String getActor(String html) {
        String actor = find(Pattern.compile("演　　员　(.*?)<p>"), html);
        if ("".equals(actor)) actor = find(Pattern.compile("主　　演　(.*?)\n<p>"), html);
        return removeHtmlTag(actor);
    }

    private String removeHtmlTag(String str) {
        return str.replaceAll("</?[^>]+>", "");
    }

    @Override
    public String homeContent(boolean filter) throws Exception {
        String html = req(siteUrl, getHeader());
        Document doc = Jsoup.parse(html);
        Elements aList = doc.select(".sf-menu > li > a");
        JSONArray classes = new JSONArray();
        for (int i = 1; i < aList.size(); i++) {
            Element a = aList.get(i);
            String typeId = a.attr("href").split("/category/")[1];
            String typeName = a.text();
            classes.put(new JSONObject().put("type_id", typeId).put("type_name", typeName));
        }
        Elements elements = doc.select("[class=slider clearfix] > ul > li");
        JSONArray videos = new JSONArray();
        for (Element element : elements) {
            Element a = element.selectFirst("a");
            String vodId = a.attr("href");
            String name = a.select(".cap").text();
            String pic = a.select("img").attr("src");
            String remark = element.select(".entry-rating").text();

            JSONObject vod = new JSONObject();
            vod.put("vod_id", vodId);
            vod.put("vod_name", name);
            vod.put("vod_pic", pic);
            vod.put("vod_remarks", remark);
            videos.put(vod);
        }
        JSONObject result = new JSONObject();
        result.put("class", classes);
        result.put("list", videos);
        return result.toString();
    }

    @Override
    public String categoryContent(String tid, String pg, boolean filter, HashMap<String, String> extend) throws Exception {
        String cateUrl = siteUrl + "/category/" + tid;
        // https://www.xl720.com/category/dongzuopian/page/2
        if (!pg.equals("1")) cateUrl += "/page/" + pg;
        String html = req(cateUrl, getHeader(siteUrl+"/"));
        JSONArray videos = parseVodListFromDoc(html);
        int page = Integer.parseInt(pg), count = parseLastPageNumber(html);
        JSONObject result = new JSONObject();
        result.put("page", page);
        result.put("pagecount", count);
        result.put("limit", videos.length());
        result.put("total", Integer.MAX_VALUE);
        result.put("list", videos);
        return result.toString();
    }

    private int parseLastPageNumber(String html) {
        try {
            Element last = Jsoup.parse(html).select(".wp-pagenavi > a.last").last();
            int lastPageNum = Integer.parseInt(last.attr("href").split("/page/")[1]);
            return lastPageNum;
        } catch (Exception ignore) {
            return 1;
        }
    }

    @Override
    public String detailContent(List<String> ids) throws Exception {
        String detailUrl = ids.get(0);
        String html = req(detailUrl, getHeader());
        Document doc = Jsoup.parse(html);
        Map<String, String> playMap = new LinkedHashMap<>();
        List<String> vodItems = new ArrayList<>();
        for (Element a : doc.select("#play_list > a")) {
            String episodeUrl = a.attr("href").split("path=")[1].replaceAll("ftp", "tvbox-xg:ftp");
            String episodeTitle = a.text();
            vodItems.add(episodeTitle+"$"+episodeUrl);
        }
        if (vodItems.size() > 0) {
            playMap.put("荐片", TextUtils.join("#", vodItems));
        }
        vodItems = new ArrayList<>();
        for (Element a : doc.select("#zdownload > .download-link > a")) {
            String episodeUrl = a.attr("href");
            String episodeTitle = a.text().replaceAll(".torrent", "");
            vodItems.add(episodeTitle+"$"+episodeUrl);
        }
        if (vodItems.size() > 0) {
            playMap.put("磁力", TextUtils.join("#", vodItems));
        }

        String partHTML = doc.select("#info").html();
        String name = find(Pattern.compile("片　　名　(.*?)<br"), partHTML);
        String pic = doc.select("#mainpic > img").attr("src");
        String typeName = find(Pattern.compile("类　　别　(.*?)<br"), partHTML);
        String year = removeHtmlTag(find(Pattern.compile("年　　代　(.*?)<br"), partHTML));
        String area = removeHtmlTag(find(Pattern.compile("产　　地　(.*?)<br"), partHTML));
        String remark = "上映日期：" + find(Pattern.compile("上映日期　(.*?)<br"), partHTML);
        String actor = getActor(partHTML);
        String director = removeHtmlTag(find(Pattern.compile("导　　演　(.*?)\n<br"), partHTML));
        String description = doc.select("#link-report").text().replaceAll("　　", "");

        // 由于部分信息过长，故进行一些调整，将年份、地区等信息放到 类别、备注里面
        typeName += " 地区:" + area;
        area = "";
        typeName += " 年份:" + year;
        remark += " 年份:" + year;
        year = "";

        JSONObject vod = new JSONObject();
        vod.put("vod_id", ids.get(0));
        vod.put("vod_name", name);
        vod.put("vod_pic", pic);
        vod.put("type_name", typeName);
        vod.put("vod_year", year);
        vod.put("vod_area", area);
        vod.put("vod_remarks", remark);
        vod.put("vod_actor", actor);
        vod.put("vod_director", director);
        vod.put("vod_content", description);
        if (playMap.size() > 0) {
            vod.put("vod_play_from", TextUtils.join("$$$", playMap.keySet()));
            vod.put("vod_play_url", TextUtils.join("$$$", playMap.values()));
        }
        JSONArray jsonArray = new JSONArray();
        jsonArray.put(vod);
        JSONObject result = new JSONObject();
        result.put("list", jsonArray);
        return result.toString();
    }

    @Override
    public String searchContent(String key, boolean quick) throws Exception {
        return searchContent(key, quick, "1");
    }

    @Override
    public String searchContent(String key, boolean quick, String pg) throws Exception {
        // 第一页
        // https://www.xl720.com/?s=我
        // 第二页
        // https://www.xl720.com/page/2?s=我
        String searchUrl = siteUrl + "/?s=" + URLEncoder.encode(key);
        String html = "";
        if ("1".equals(pg)) {
            html = req(searchUrl, getHeader(siteUrl+"/"));
        } else {
            searchUrl = siteUrl + "/page/2?s=" + URLEncoder.encode(key);
            html = req(searchUrl, getHeader(siteUrl+"/"));
        }
        JSONArray videos = parseVodListFromDoc(html);
        JSONObject result = new JSONObject();
        result.put("list", videos);
        return result.toString();
    }

    @Override
    public String playerContent(String flag, String id, List<String> vipFlags) throws Exception {
        JSONObject result = new JSONObject();
        result.put("parse", 0);
        result.put("header", "");
        result.put("playUrl", "");
        result.put("url", id);
        return result.toString();
    }

    /**
     * 仅用于测试，编译打包jar时需要去掉这个函数
     * just for test, remove this function when you make jar
     */
    public static void main(String[] args) {
        Xl720 xl720 = new Xl720();
        try {
            xl720.init(new Context());
            SpiderDebug.log(xl720.homeContent(true));
            Thread.sleep(5 * 1000L);

            SpiderDebug.log(xl720.categoryContent("dongzuopian", "1", true, new HashMap<>()));
            Thread.sleep(5 * 1000L);
            SpiderDebug.log(xl720.categoryContent("dongzuopian", "269", true, new HashMap<>()));
            Thread.sleep(5 * 1000L);

            SpiderDebug.log(xl720.detailContent(Arrays.asList("https://www.xl720.com/thunder/50204.html")));
            Thread.sleep(5 * 1000L);

            SpiderDebug.log(xl720.detailContent(Arrays.asList("https://www.xl720.com/thunder/15675.html")));
            Thread.sleep(5 * 1000L);

            SpiderDebug.log(xl720.searchContent("周处", true));
            Thread.sleep(5 * 1000L);

            SpiderDebug.log(xl720.searchContent("我", true, "2"));
            Thread.sleep(5 * 1000L);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
