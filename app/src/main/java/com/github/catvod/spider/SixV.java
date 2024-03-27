package com.github.catvod.spider;

import android.content.Context;
import android.text.TextUtils;

import com.github.catvod.crawler.Spider;
//import com.github.catvod.net.OkHttp;
import com.github.catvod.utils.okhttp.OkHttpUtil;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


import java.util.Arrays;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author zhixc
 * 6V电影网（新版页面）
 */
public class SixV extends Spider {

    private String siteUrl;

    private final String userAgent = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/39.0.2171.71 Safari/537.36";

    private String req(String url, Map<String, String> header) {
//        return OkHttp.string(url, header);
        return OkHttpUtil.string(url, header);
    }

    private OkHttpClient getOkHttpClient() {
//        return OkHttp.client();
        return OkHttpUtil.defaultClient();
    }

    private Map<String, String> getHeader() {
        Map<String, String> header = new HashMap<>();
        header.put("User-Agent", userAgent);
        header.put("Referer", siteUrl + "/");
        return header;
    }

    private Map<String, String> getDetailHeader() {
        Map<String, String> header = new HashMap<>();
        header.put("User-Agent", userAgent);
        return header;
    }

    private String find(Pattern pattern, String html) {
        Matcher matcher = pattern.matcher(html);
        return matcher.find() ? matcher.group(1).trim() : "";
    }

    private String getActorOrDirector(Pattern pattern, String str) {
        return find(pattern, str)
                .replaceAll("<br>", "")
                .replaceAll("&nbsp;", "")
                .replaceAll("&amp;", "")
                .replaceAll("middot;", "・")
                .replaceAll("　　　　　", ",")
                .replaceAll("　　　　 　", ",");
    }

    private String getDescription(Pattern pattern, String str) {
        return find(pattern, str)
                .replaceAll("</?[^>]+>", "")  // 去掉 html 标签
                .replaceAll("\n", "") // 去掉换行符
                .replaceAll("　　　　", "")
                .replaceAll("&amp;", "")
                .replaceAll("middot;", "・")
                .replaceAll("ldquo;", "【")
                .replaceAll("rdquo;", "】");
    }

    @Override
    public void init(Context context, String extend) throws Exception {
        super.init(context, extend);
        if (extend.endsWith("/")) extend = extend.substring(0, extend.lastIndexOf("/"));
        siteUrl = extend;
    }

    @Override
    public String homeContent(boolean filter) throws Exception {
        JSONArray classes = new JSONArray();
        List<String> typeIds = Arrays.asList("xijupian", "dongzuopian", "aiqingpian", "kehuanpian", "kongbupian", "juqingpian", "zhanzhengpian", "jilupian", "donghuapian", "dianshiju/guoju", "dianshiju/rihanju", "dianshiju/oumeiju");
        List<String> typeNames = Arrays.asList("喜剧片", "动作片", "爱情片", "科幻片", "恐怖片", "剧情片", "战争片", "纪录片", "动画片", "国剧", "日韩剧", "欧美剧");
        for (int i = 0; i < typeIds.size(); i++) {
            JSONObject c = new JSONObject();
            c.put("type_id", typeIds.get(i));
            c.put("type_name", typeNames.get(i));
            classes.put(c);
        }
        JSONObject result = new JSONObject();
        result.put("class", classes);
        return result.toString();
    }

    @Override
    public String categoryContent(String tid, String pg, boolean filter, HashMap<String, String> extend) throws Exception {
        String cateUrl = siteUrl + "/" + tid;
        if (!pg.equals("1")) cateUrl += "/index_" + pg + ".html";
        String html = req(cateUrl, getHeader());
        Elements items = Jsoup.parse(html).select("#post_container .post_hover");
        JSONArray videos = new JSONArray();
        for (Element item : items) {
            Element li = item.select("[class=zoom]").get(0);
            String vid = li.attr("href");
            String name = li.attr("title");
            String pic = li.select("img").attr("src");
            String remark = item.select("[rel=category tag]").text();

            JSONObject vod = new JSONObject();
            vod.put("vod_id", vid);
            vod.put("vod_name", name);
            vod.put("vod_pic", pic);
            vod.put("vod_remarks", remark);
            videos.put(vod);
        }
        JSONObject result = new JSONObject();
        result.put("pagecount", 999);
        result.put("list", videos);
        return result.toString();
    }

    @Override
    public String detailContent(List<String> ids) throws Exception {
        String vid = ids.get(0);
        String detailUrl = siteUrl + vid;
        String html = req(detailUrl, getDetailHeader());
        Document doc = Jsoup.parse(html);
        Elements sourceList = doc.select("#post_content");

        String vod_play_from = "magnet";
        Map<String, String> playMap = new LinkedHashMap<>();
        int i = 0;
        for (Element source : sourceList) {
            i++;
            Elements aList = source.select("table a");
            List<String> vodItems = new ArrayList<>();
            for (Element a : aList) {
                String episodeURL = a.attr("href");
                String episodeName = a.text();
                if (!episodeURL.startsWith("magnet")) continue;
                vodItems.add(episodeName + "$" + episodeURL);
            }
            if (vodItems.size() > 0) {
                playMap.put(vod_play_from + i, TextUtils.join("#", vodItems));
            }
        }

        String partHTML = doc.select(".context").html();
        String name = doc.select(".article_container > h1").text();
        String pic = doc.select("#post_content img").attr("src");
        String typeName = find(Pattern.compile("◎类　　别　(.*?)<br>"), partHTML);
        String year = find(Pattern.compile("◎年　　代　(.*?)<br>"), partHTML);
        String area = find(Pattern.compile("◎产　　地　(.*?)<br>"), partHTML);
        String remark = find(Pattern.compile("◎上映日期　(.*?)<br>"), partHTML);
        String actor = getActorOrDirector(Pattern.compile("◎演　　员　(.*?)</p>"), partHTML);
        if (actor.equals("")) {
            actor = getActorOrDirector(Pattern.compile("◎主　　演　(.*?)</p>"), partHTML);
        }
        String director = getActorOrDirector(Pattern.compile("◎导　　演　(.*?)<br>"), partHTML);
        String description = getDescription(Pattern.compile("◎简　　介(.*?)<hr>", Pattern.CASE_INSENSITIVE | Pattern.DOTALL), partHTML);

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

        JSONArray jsonArray = new JSONArray().put(vod);
        JSONObject result = new JSONObject().put("list", jsonArray);
        return result.toString();
    }

    @Override
    public String searchContent(String key, boolean quick) throws Exception {
        String searchUrl = siteUrl + "/e/search/index.php";
        RequestBody formBody = new FormBody.Builder()
                .add("show", "title")
                .add("tempid", "1")
                .add("tbname", "article")
                .add("mid", "1")
                .add("dopost", "search")
                .add("submit", "")
                .addEncoded("keyboard", key)
                .build();
        Request request = new Request.Builder()
                .url(searchUrl)
                .addHeader("User-Agent", userAgent)
                .addHeader("Origin", siteUrl)
                .addHeader("Referer", siteUrl + "/")
                .post(formBody)
                .build();
        Response response = getOkHttpClient().newCall(request).execute();
        if (response.body() == null) return "";
        String html = response.body().string();
        response.close(); // 关闭响应资源
        Elements items = Jsoup.parse(html).select("#post_container [class=zoom]");
        JSONArray videos = new JSONArray();
        for (Element item : items) {
            String vid = item.attr("href");
            String name = item.attr("title").replaceAll("</?[^>]+>", "");
            String pic = item.select("img").attr("src");
            JSONObject vod = new JSONObject();
            vod.put("vod_id", vid);
            vod.put("vod_name", name);
            vod.put("vod_pic", pic);
            vod.put("vod_remarks", "");
            videos.put(vod);
        }

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
}
