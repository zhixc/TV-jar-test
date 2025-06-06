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

/**
 * @author zhixc
 * Vodflix
 */
public class Voflix extends Spider {

    private String siteUrl;
    private final String userAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/117.0.0.0 Safari/537.36";

    private String req(String url, Map<String, String> header) {
        return OkHttp.string(url, header);
    }

    private Map<String, String> getHeader() {
        Map<String, String> header = new HashMap<>();
        header.put("User-Agent", userAgent);
        header.put("Referer", siteUrl + "/");
        return header;
    }

    /**
     * 正则获取字符串
     *
     * @param regex 正则表达式字符串
     * @param html  网页源码
     * @return 返回正则获取的字符串结果
     */
    private String find(String regex, String html) {
        Pattern p = Pattern.compile(regex, Pattern.DOTALL | Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(html);
        return m.find() ? m.group(1) : "";
    }

    /**
     * 正则获取字符串
     *
     * @param pattern 正则表达式 pattern 对象
     * @param html    网页源码
     * @return 返回正则获取的字符串结果
     */
    private String find(Pattern pattern, String html) {
        Matcher m = pattern.matcher(html);
        return m.find() ? m.group(1) : "";
    }

    private String clean(String str) {
        return removeHtmlTag(str).replace("\n", "").replace("\t", "").trim();
    }

    private String removeHtmlTag(String str) {
        return str.replaceAll("</?[^>]+>", "");
    }

    private JSONArray parseVodList(Elements items) throws Exception {
        JSONArray videos = new JSONArray();
        for (Element item : items) {
            String vodId = item.attr("href");
            String name = item.attr("title");
            String pic = item.select("img").attr("data-original");
            String remark = item.select("[class=module-item-note]").text();

            JSONObject vod = new JSONObject();
            vod.put("vod_id", vodId);
            vod.put("vod_name", name);
            vod.put("vod_pic", pic);
            vod.put("vod_remarks", remark);
            videos.put(vod);
        }
        return videos;
    }

    /**
     * 爬虫代码初始化
     *
     * @param context 上下文对象
     * @param extend  配置文件的 ext 参数
     */
    @Override
    public void init(Context context, String extend) throws Exception {
        super.init(context, extend);
        // 域名经常性发生变化，通过外部配置文件传入，可以方便修改
        if (extend.endsWith("/")) extend = extend.substring(0, extend.lastIndexOf("/"));
        siteUrl = extend;
    }

    /**
     * 首页内容初始化，主要要完成分类id和值、二级筛选等，
     * 也可以在这个方法里面完成首页推荐视频获取
     *
     * @param filter true: 开启二级筛选 false:关闭
     * @return 返回字符串
     */
    @Override
    public String homeContent(boolean filter) throws Exception {
        JSONArray classes = new JSONArray();
        List<String> typeIds = Arrays.asList("1", "2", "4", "3");
        List<String> typeNames = Arrays.asList("电影", "剧集", "动漫", "综艺");
        for (int i = 0; i < typeIds.size(); i++) {
            JSONObject c = new JSONObject();
            c.put("type_id", typeIds.get(i));
            c.put("type_name", typeNames.get(i));
            classes.put(c);
        }
        String f = "{\"1\": [{\"key\": \"cateId\", \"name\": \"类型\", \"value\": [{\"n\": \"全部类型\", \"v\": \"1\"}, {\"n\": \"动作\", \"v\": \"6\"}, {\"n\": \"喜剧\", \"v\": \"7\"}, {\"n\": \"爱情\", \"v\": \"8\"}, {\"n\": \"科幻\", \"v\": \"9\"}, {\"n\": \"恐怖\", \"v\": \"10\"}, {\"n\": \"剧情\", \"v\": \"11\"}, {\"n\": \"战争\", \"v\": \"12\"}, {\"n\": \"动画\", \"v\": \"23\"}]}, {\"key\": \"class\", \"name\": \"剧情\", \"value\": [{\"n\": \"全部剧情\", \"v\": \"\"}, {\"n\": \"喜剧\", \"v\": \"喜剧\"}, {\"n\": \"爱情\", \"v\": \"爱情\"}, {\"n\": \"恐怖\", \"v\": \"恐怖\"}, {\"n\": \"动作\", \"v\": \"动作\"}, {\"n\": \"科幻\", \"v\": \"科幻\"}, {\"n\": \"剧情\", \"v\": \"剧情\"}, {\"n\": \"战争\", \"v\": \"战争\"}, {\"n\": \"警匪\", \"v\": \"警匪\"}, {\"n\": \"犯罪\", \"v\": \"犯罪\"}, {\"n\": \"动画\", \"v\": \"动画\"}, {\"n\": \"奇幻\", \"v\": \"奇幻\"}, {\"n\": \"武侠\", \"v\": \"武侠\"}, {\"n\": \"冒险\", \"v\": \"冒险\"}, {\"n\": \"枪战\", \"v\": \"枪战\"}, {\"n\": \"恐怖\", \"v\": \"恐怖\"}, {\"n\": \"悬疑\", \"v\": \"悬疑\"}, {\"n\": \"惊悚\", \"v\": \"惊悚\"}, {\"n\": \"经典\", \"v\": \"经典\"}, {\"n\": \"青春\", \"v\": \"青春\"}, {\"n\": \"文艺\", \"v\": \"文艺\"}, {\"n\": \"微电影\", \"v\": \"微电影\"}, {\"n\": \"古装\", \"v\": \"古装\"}, {\"n\": \"历史\", \"v\": \"历史\"}, {\"n\": \"运动\", \"v\": \"运动\"}, {\"n\": \"农村\", \"v\": \"农村\"}, {\"n\": \"儿童\", \"v\": \"儿童\"}, {\"n\": \"网络电影\", \"v\": \"网络电影\"}]}, {\"key\": \"area\", \"name\": \"地区\", \"value\": [{\"n\": \"全部地区\", \"v\": \"\"}, {\"n\": \"中国大陆\", \"v\": \"中国大陆\"}, {\"n\": \"中国香港\", \"v\": \"中国香港\"}, {\"n\": \"中国台湾\", \"v\": \"中国台湾\"}, {\"n\": \"美国\", \"v\": \"美国\"}, {\"n\": \"法国\", \"v\": \"法国\"}, {\"n\": \"英国\", \"v\": \"英国\"}, {\"n\": \"日本\", \"v\": \"日本\"}, {\"n\": \"韩国\", \"v\": \"韩国\"}, {\"n\": \"德国\", \"v\": \"德国\"}, {\"n\": \"泰国\", \"v\": \"泰国\"}, {\"n\": \"印度\", \"v\": \"印度\"}, {\"n\": \"意大利\", \"v\": \"意大利\"}, {\"n\": \"西班牙\", \"v\": \"西班牙\"}, {\"n\": \"加拿大\", \"v\": \"加拿大\"}, {\"n\": \"其他\", \"v\": \"其他\"}]}, {\"key\": \"year\", \"name\": \"年份\", \"value\": [{\"n\": \"全部年份\", \"v\": \"\"}, {\"n\": \"2024\", \"v\": \"2024\"}, {\"n\": \"2023\", \"v\": \"2023\"}, {\"n\": \"2022\", \"v\": \"2022\"}, {\"n\": \"2021\", \"v\": \"2021\"}, {\"n\": \"2020\", \"v\": \"2020\"}, {\"n\": \"2019\", \"v\": \"2019\"}, {\"n\": \"2018\", \"v\": \"2018\"}, {\"n\": \"2017\", \"v\": \"2017\"}, {\"n\": \"2016\", \"v\": \"2016\"}, {\"n\": \"2015\", \"v\": \"2015\"}, {\"n\": \"2014\", \"v\": \"2014\"}, {\"n\": \"2013\", \"v\": \"2013\"}, {\"n\": \"2012\", \"v\": \"2012\"}, {\"n\": \"2011\", \"v\": \"2011\"}, {\"n\": \"2010\", \"v\": \"2010\"}]}, {\"key\": \"by\", \"name\": \"排序\", \"value\": [{\"n\": \"时间\", \"v\": \"time\"}, {\"n\": \"人气\", \"v\": \"hits\"}, {\"n\": \"评分\", \"v\": \"score\"}]}], \"2\": [{\"key\": \"cateId\", \"name\": \"类型\", \"value\": [{\"n\": \"全部类型\", \"v\": \"2\"}, {\"n\": \"国产剧\", \"v\": \"13\"}, {\"n\": \"港台剧\", \"v\": \"14\"}, {\"n\": \"日韩剧\", \"v\": \"15\"}, {\"n\": \"欧美剧\", \"v\": \"16\"}, {\"n\": \"纪录片\", \"v\": \"21\"}, {\"n\": \"泰国剧\", \"v\": \"24\"}]}, {\"key\": \"class\", \"name\": \"剧情\", \"value\": [{\"n\": \"全部剧情\", \"v\": \"\"}, {\"n\": \"古装\", \"v\": \"古装\"}, {\"n\": \"战争\", \"v\": \"战争\"}, {\"n\": \"青春偶像\", \"v\": \"青春偶像\"}, {\"n\": \"喜剧\", \"v\": \"喜剧\"}, {\"n\": \"家庭\", \"v\": \"家庭\"}, {\"n\": \"犯罪\", \"v\": \"犯罪\"}, {\"n\": \"动作\", \"v\": \"动作\"}, {\"n\": \"奇幻\", \"v\": \"奇幻\"}, {\"n\": \"剧情\", \"v\": \"剧情\"}, {\"n\": \"历史\", \"v\": \"历史\"}, {\"n\": \"经典\", \"v\": \"经典\"}, {\"n\": \"乡村\", \"v\": \"乡村\"}, {\"n\": \"情景\", \"v\": \"情景\"}, {\"n\": \"商战\", \"v\": \"商战\"}, {\"n\": \"网剧\", \"v\": \"网剧\"}, {\"n\": \"其他\", \"v\": \"其他\"}]}, {\"key\": \"area\", \"name\": \"地区\", \"value\": [{\"n\": \"全部地区\", \"v\": \"\"}, {\"n\": \"中国大陆\", \"v\": \"中国大陆\"}, {\"n\": \"中国台湾\", \"v\": \"中国台湾\"}, {\"n\": \"中国香港\", \"v\": \"中国香港\"}, {\"n\": \"韩国\", \"v\": \"韩国\"}, {\"n\": \"日本\", \"v\": \"日本\"}, {\"n\": \"美国\", \"v\": \"美国\"}, {\"n\": \"泰国\", \"v\": \"泰国\"}, {\"n\": \"英国\", \"v\": \"英国\"}, {\"n\": \"新加坡\", \"v\": \"新加坡\"}, {\"n\": \"其他\", \"v\": \"其他\"}]}, {\"key\": \"year\", \"name\": \"年份\", \"value\": [{\"n\": \"全部年份\", \"v\": \"\"}, {\"n\": \"2024\", \"v\": \"2024\"}, {\"n\": \"2023\", \"v\": \"2023\"}, {\"n\": \"2022\", \"v\": \"2022\"}, {\"n\": \"2021\", \"v\": \"2021\"}, {\"n\": \"2020\", \"v\": \"2020\"}, {\"n\": \"2019\", \"v\": \"2019\"}, {\"n\": \"2018\", \"v\": \"2018\"}, {\"n\": \"2017\", \"v\": \"2017\"}, {\"n\": \"2016\", \"v\": \"2016\"}, {\"n\": \"2015\", \"v\": \"2015\"}, {\"n\": \"2014\", \"v\": \"2014\"}, {\"n\": \"2013\", \"v\": \"2013\"}, {\"n\": \"2012\", \"v\": \"2012\"}, {\"n\": \"2011\", \"v\": \"2011\"}, {\"n\": \"2010\", \"v\": \"2010\"}, {\"n\": \"2009\", \"v\": \"2009\"}, {\"n\": \"2008\", \"v\": \"2008\"}, {\"n\": \"2006\", \"v\": \"2006\"}, {\"n\": \"2005\", \"v\": \"2005\"}, {\"n\": \"2004\", \"v\": \"2004\"}]}, {\"key\": \"by\", \"name\": \"排序\", \"value\": [{\"n\": \"时间\", \"v\": \"time\"}, {\"n\": \"人气\", \"v\": \"hits\"}, {\"n\": \"评分\", \"v\": \"score\"}]}], \"4\": [{\"key\": \"class\", \"name\": \"剧情\", \"value\": [{\"n\": \"全部剧情\", \"v\": \"\"}, {\"n\": \"情感\", \"v\": \"情感\"}, {\"n\": \"科幻\", \"v\": \"科幻\"}, {\"n\": \"热血\", \"v\": \"热血\"}, {\"n\": \"推理\", \"v\": \"推理\"}, {\"n\": \"搞笑\", \"v\": \"搞笑\"}, {\"n\": \"冒险\", \"v\": \"冒险\"}, {\"n\": \"萝莉\", \"v\": \"萝莉\"}, {\"n\": \"校园\", \"v\": \"校园\"}, {\"n\": \"动作\", \"v\": \"动作\"}, {\"n\": \"机战\", \"v\": \"机战\"}, {\"n\": \"运动\", \"v\": \"运动\"}, {\"n\": \"战争\", \"v\": \"战争\"}, {\"n\": \"少年\", \"v\": \"少年\"}, {\"n\": \"少女\", \"v\": \"少女\"}, {\"n\": \"社会\", \"v\": \"社会\"}, {\"n\": \"原创\", \"v\": \"原创\"}, {\"n\": \"亲子\", \"v\": \"亲子\"}, {\"n\": \"益智\", \"v\": \"益智\"}, {\"n\": \"励志\", \"v\": \"励志\"}, {\"n\": \"其他\", \"v\": \"其他\"}]}, {\"key\": \"area\", \"name\": \"地区\", \"value\": [{\"n\": \"全部地区\", \"v\": \"\"}, {\"n\": \"中国\", \"v\": \"中国\"}, {\"n\": \"日本\", \"v\": \"日本\"}, {\"n\": \"欧美\", \"v\": \"欧美\"}, {\"n\": \"其他\", \"v\": \"其他\"}]}, {\"key\": \"year\", \"name\": \"年份\", \"value\": [{\"n\": \"全部年份\", \"v\": \"\"}, {\"n\": \"2024\", \"v\": \"2024\"}, {\"n\": \"2023\", \"v\": \"2023\"}, {\"n\": \"2022\", \"v\": \"2022\"}, {\"n\": \"2021\", \"v\": \"2021\"}, {\"n\": \"2020\", \"v\": \"2020\"}, {\"n\": \"2019\", \"v\": \"2019\"}, {\"n\": \"2018\", \"v\": \"2018\"}, {\"n\": \"2017\", \"v\": \"2017\"}, {\"n\": \"2016\", \"v\": \"2016\"}, {\"n\": \"2015\", \"v\": \"2015\"}, {\"n\": \"2014\", \"v\": \"2014\"}, {\"n\": \"2013\", \"v\": \"2013\"}, {\"n\": \"2012\", \"v\": \"2012\"}, {\"n\": \"2011\", \"v\": \"2011\"}, {\"n\": \"2010\", \"v\": \"2010\"}, {\"n\": \"2009\", \"v\": \"2009\"}, {\"n\": \"2008\", \"v\": \"2008\"}, {\"n\": \"2007\", \"v\": \"2007\"}, {\"n\": \"2006\", \"v\": \"2006\"}, {\"n\": \"2005\", \"v\": \"2005\"}, {\"n\": \"2004\", \"v\": \"2004\"}]}, {\"key\": \"by\", \"name\": \"排序\", \"value\": [{\"n\": \"时间\", \"v\": \"time\"}, {\"n\": \"人气\", \"v\": \"hits\"}, {\"n\": \"评分\", \"v\": \"score\"}]}], \"3\": [{\"key\": \"class\", \"name\": \"剧情\", \"value\": [{\"n\": \"全部剧情\", \"v\": \"\"}, {\"n\": \"选秀\", \"v\": \"选秀\"}, {\"n\": \"情感\", \"v\": \"情感\"}, {\"n\": \"访谈\", \"v\": \"访谈\"}, {\"n\": \"播报\", \"v\": \"播报\"}, {\"n\": \"旅游\", \"v\": \"旅游\"}, {\"n\": \"音乐\", \"v\": \"音乐\"}, {\"n\": \"美食\", \"v\": \"美食\"}, {\"n\": \"纪实\", \"v\": \"纪实\"}, {\"n\": \"曲艺\", \"v\": \"曲艺\"}, {\"n\": \"生活\", \"v\": \"生活\"}, {\"n\": \"游戏互动\", \"v\": \"游戏互动\"}, {\"n\": \"财经\", \"v\": \"财经\"}, {\"n\": \"求职\", \"v\": \"求职\"}]}, {\"key\": \"area\", \"name\": \"地区\", \"value\": [{\"n\": \"全部地区\", \"v\": \"\"}, {\"n\": \"内地\", \"v\": \"内地\"}, {\"n\": \"港台\", \"v\": \"港台\"}, {\"n\": \"日韩\", \"v\": \"日韩\"}, {\"n\": \"欧美\", \"v\": \"欧美\"}]}, {\"key\": \"year\", \"name\": \"年份\", \"value\": [{\"n\": \"全部年份\", \"v\": \"\"}, {\"n\": \"2024\", \"v\": \"2024\"}, {\"n\": \"2023\", \"v\": \"2023\"}, {\"n\": \"2022\", \"v\": \"2022\"}, {\"n\": \"2021\", \"v\": \"2021\"}, {\"n\": \"2020\", \"v\": \"2020\"}, {\"n\": \"2019\", \"v\": \"2019\"}, {\"n\": \"2018\", \"v\": \"2018\"}, {\"n\": \"2017\", \"v\": \"2017\"}, {\"n\": \"2016\", \"v\": \"2016\"}, {\"n\": \"2015\", \"v\": \"2015\"}, {\"n\": \"2014\", \"v\": \"2014\"}, {\"n\": \"2013\", \"v\": \"2013\"}, {\"n\": \"2012\", \"v\": \"2012\"}, {\"n\": \"2011\", \"v\": \"2011\"}, {\"n\": \"2010\", \"v\": \"2010\"}, {\"n\": \"2009\", \"v\": \"2009\"}, {\"n\": \"2008\", \"v\": \"2008\"}, {\"n\": \"2007\", \"v\": \"2007\"}, {\"n\": \"2006\", \"v\": \"2006\"}, {\"n\": \"2005\", \"v\": \"2005\"}, {\"n\": \"2004\", \"v\": \"2004\"}]}, {\"key\": \"by\", \"name\": \"排序\", \"value\": [{\"n\": \"时间\", \"v\": \"time\"}, {\"n\": \"人气\", \"v\": \"hits\"}, {\"n\": \"评分\", \"v\": \"score\"}]}]}";
        JSONObject filterConfig = new JSONObject(f);
        JSONObject result = new JSONObject();
        result.put("class", classes);
        result.put("filters", filterConfig);
        return result.toString();
    }

    /**
     * 获取首页推荐视频
     *
     * @return 返回字符串
     */
    @Override
    public String homeVideoContent() throws Exception {
        String hotUrl = siteUrl + "/label/new.html";
        String html = req(hotUrl, getHeader());
        Elements items = Jsoup.parse(html).select(".module-items").get(0).select(".module-item");
        JSONArray videos = parseVodList(items);
        JSONObject result = new JSONObject();
        result.put("list", videos);
        return result.toString();
    }

    /**
     * 分类内容方法
     *
     * @param tid    影片分类id值，来自 homeContent 里面的 type_id 值
     * @param pg     第几页
     * @param filter 不用管这个参数
     * @param extend 用户已经选择的二级筛选数据
     * @return 返回字符串
     */
    @Override
    public String categoryContent(String tid, String pg, boolean filter, HashMap<String, String> extend) throws Exception {
        String cateId = extend.get("cateId") == null ? tid : extend.get("cateId");
        String area = extend.get("area") == null ? "" : extend.get("area");
        String year = extend.get("year") == null ? "" : extend.get("year");
        String by = extend.get("by") == null ? "" : extend.get("by");
        String classType = extend.get("class") == null ? "" : extend.get("class");
        String cateUrl = siteUrl + String.format("/show/%s-%s-%s-%s-----%s---%s.html", cateId, area, by, classType, pg, year);
        String html = req(cateUrl, getHeader());
        Elements items = Jsoup.parse(html).select(".module-items .module-item");
        JSONArray videos = parseVodList(items);
        int page = Integer.parseInt(pg), count = Integer.MAX_VALUE, limit = 40, total = Integer.MAX_VALUE;
        JSONObject result = new JSONObject();
        result.put("page", page);
        result.put("pagecount", count);
        result.put("limit", limit);
        result.put("total", total);
        result.put("list", videos);
        return result.toString();
    }

    /**
     * 影片详情方法
     *
     * @param ids ids.get(0) 来源于分类方法或搜索方法的 vod_id
     * @return 返回字符串
     */
    @Override
    public String detailContent(List<String> ids) throws Exception {
        String vodId = ids.get(0);
        String detailUrl = siteUrl + vodId;
        String html = req(detailUrl, getHeader());
        Document doc = Jsoup.parse(html);
        String name = doc.select(".module-info-heading > h1").text();
        String pic = doc.select("[class=ls-is-cached lazy lazyload]").attr("data-original");
        Elements elements = doc.select(".module-info-heading .module-info-tag-link");
        String typeName = ""; // 影片类型
        String year = ""; // 影片年代
        String area = ""; // 地区
        if (elements.size() >= 3) {
            typeName = elements.get(2).select("a").text();
            year = elements.get(0).select("a").text();
            area = elements.get(1).select("a").text();
        }
        String remark = clean(find("备注：(.*?)</div>", html));
        String actor = clean(find("主演：(.*?)</div>", html));
        String director = clean(find("导演：(.*?)</div>", html));
        String description = doc.select(".module-info-introduction-content").text();

        Elements sourceList = doc.select(".module-play-list");
        Elements circuits = doc.select(".module-tab-item");
        Map<String, String> playMap = new LinkedHashMap<>();
        for (int i = 0; i < sourceList.size(); i++) {
            String spanText = circuits.get(i).select("span").text();
            if (spanText.contains("境外") || spanText.contains("网盘") || spanText.contains("暴风")) continue;
            String smallText = circuits.get(i).select("small").text();
            String circuitName = spanText + "【共" + smallText + "集】";
            List<String> vodItems = new ArrayList<>();
            for (Element a : sourceList.get(i).select("a")) {
                String episodeUrl = a.attr("href");
                String episodeName = a.select("span").text();
                vodItems.add(episodeName + "$" + episodeUrl);
            }
            if (vodItems.size() > 0) playMap.put(circuitName, TextUtils.join("#", vodItems));
        }

        JSONObject vod = new JSONObject();
        vod.put("vod_id", ids.get(0));
        vod.put("vod_name", name); // 影片名称
        vod.put("vod_pic", pic); // 图片
        vod.put("type_name", typeName);// 影片类型 选填
        vod.put("vod_year", year); // 年份 选填
        vod.put("vod_area", area);// 地区 选填
        vod.put("vod_remarks", remark);// 备注 选填
        vod.put("vod_actor", actor); // 主演 选填
        vod.put("vod_director", director);// 导演 选填
        vod.put("vod_content", description); // 简介 选填
        if (playMap.size() > 0) {
            vod.put("vod_play_from", TextUtils.join("$$$", playMap.keySet()));
            vod.put("vod_play_url", TextUtils.join("$$$", playMap.values()));
        }
        JSONArray jsonArray = new JSONArray().put(vod);
        JSONObject result = new JSONObject().put("list", jsonArray);
        return result.toString();
    }

    /**
     * 搜索
     *
     * @param key 关键字/词
     * @return 返回值
     */
    @Override
    public String searchContent(String key, boolean quick) throws Exception {
        String searchUrl = siteUrl + "/index.php/ajax/suggest?mid=1&wd=" + URLEncoder.encode(key) + "&limit=20";
        String html = req(searchUrl, getHeader());
        JSONArray jsonArray = new JSONObject(html).getJSONArray("list");
        JSONArray videos = new JSONArray();
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject item = jsonArray.getJSONObject(i);
            String vodId = "/detail/" + item.getInt("id") + ".html";
            String name = item.getString("name");
            String pic = item.getString("pic");

            JSONObject vod = new JSONObject();
            vod.put("vod_id", vodId);
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
        String playPageUrl = siteUrl + id;
        String html = req(playPageUrl, getHeader());
        String playerConfigStr = find(Pattern.compile("player_aaaa=(.*?)</script>"), html);
        String realPlayUrl = new JSONObject(playerConfigStr).optString("url");
        if (realPlayUrl.contains(".m3u8") || realPlayUrl.contains(".mp4")) {
            JSONObject result = new JSONObject();
            result.put("parse", 0);
            result.put("header", getHeader().toString());
            result.put("playUrl", "");
            result.put("url", realPlayUrl);
            return result.toString();
        }
        return "";
    }

    /**
     * 仅用于测试，编译打包jar时需要去掉这个函数
     * just for test, remove this function when you make jar
     */
    public static void main(String[] args) {
        Voflix voflix = new Voflix();
        try {
            voflix.init(new Context(), "https://www.voflix.vip/");

            SpiderDebug.log(voflix.homeContent(true));
            Thread.sleep(5 * 1000L);

            SpiderDebug.log(voflix.homeVideoContent());
            Thread.sleep(5 * 1000L);

            HashMap<String, String> extend = new HashMap<>();
            extend.put("area", "中国香港");
            SpiderDebug.log(voflix.categoryContent("1", "1", true, extend));
            Thread.sleep(5 * 1000L);

            SpiderDebug.log(voflix.detailContent(Arrays.asList("/detail/162486.html")));
            Thread.sleep(5 * 1000L);

            SpiderDebug.log(voflix.searchContent("我", true));
            Thread.sleep(5 * 1000L);

            SpiderDebug.log(voflix.playerContent("", "/play/139141-2-1.html", null));
            Thread.sleep(5 * 1000L);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
