package com.github.catvod.spider;

import android.content.Context;
import android.util.Base64;

import com.github.catvod.bean.Class;
import com.github.catvod.bean.Result;
import com.github.catvod.bean.Vod;
import com.github.catvod.crawler.Spider;
import com.github.catvod.crawler.SpiderDebug;
import com.github.catvod.net.OkHttp;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Headers;
import okhttp3.Response;

/**
 * @author zhixc
 * 豆瓣影片
 */
public class DoubanWeb extends Spider {
    private final static String userAgent = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10.14; rv:102.0) Gecko/20100101 Firefox/102.0";

    private Map<String, String> getHeader(String referer) {
        Map<String, String> header = new HashMap<>();
        header.put("Connection", "keep-alive");
        header.put("Host", "m.douban.com");
        header.put("Origin", "https://movie.douban.com");
        header.put("User-Agent", userAgent);
        header.put("Referer", referer);
        return header;
    }

    private Map<String, String> getHeader() {
        Map<String, String> header = new HashMap<>();
        header.put("Connection", "keep-alive");
        header.put("Host", "movie.douban.com");
        header.put("Referer", "https://movie.douban.com/");
        header.put("User-Agent", userAgent);
        header.put("X-Requested-With", "XMLHttpRequest");
        return header;
    }

    private Map<String, String> getDetailHeader() {
        Map<String, String> header = new HashMap<>();
        header.put("User-Agent", userAgent);
        return header;
    }

    @Override
    public String homeContent(boolean filter) throws Exception {
        List<Class> classes = new ArrayList<>();
        List<String> typeIds = Arrays.asList("hot_movie", "hot_tv", "movie", "tv", "variety");
        List<String> typeNames = Arrays.asList("热门电影", "热门电视剧", "电影", "电视剧", "综艺");
        for (int i = 0; i < typeIds.size(); i++) classes.add(new Class(typeIds.get(i), typeNames.get(i)));
        String f = "{\"movie\": [{\"key\": \"type\", \"name\": \"类型\", \"value\": [{\"n\": \"全部类型\", \"v\": \"\"}, {\"n\": \"喜剧\", \"v\": \"喜剧\"}, {\"n\": \"爱情\", \"v\": \"爱情\"}, {\"n\": \"动作\", \"v\": \"动作\"}, {\"n\": \"科幻\", \"v\": \"科幻\"}, {\"n\": \"动画\", \"v\": \"动画\"}, {\"n\": \"悬疑\", \"v\": \"悬疑\"}, {\"n\": \"犯罪\", \"v\": \"犯罪\"}, {\"n\": \"惊悚\", \"v\": \"惊悚\"}, {\"n\": \"冒险\", \"v\": \"冒险\"}, {\"n\": \"音乐\", \"v\": \"音乐\"}, {\"n\": \"历史\", \"v\": \"历史\"}, {\"n\": \"奇幻\", \"v\": \"奇幻\"}, {\"n\": \"恐怖\", \"v\": \"恐怖\"}, {\"n\": \"战争\", \"v\": \"战争\"}, {\"n\": \"传记\", \"v\": \"传记\"}, {\"n\": \"歌舞\", \"v\": \"歌舞\"}, {\"n\": \"武侠\", \"v\": \"武侠\"}, {\"n\": \"情色\", \"v\": \"情色\"}, {\"n\": \"灾难\", \"v\": \"灾难\"}, {\"n\": \"西部\", \"v\": \"西部\"}, {\"n\": \"记录片\", \"v\": \"记录片\"}, {\"n\": \"短片\", \"v\": \"短片\"}]}, {\"key\": \"area\", \"name\": \"地区\", \"value\": [{\"n\": \"全部地区\", \"v\": \"\"}, {\"n\": \"华语\", \"v\": \"华语\"}, {\"n\": \"欧美\", \"v\": \"欧美\"}, {\"n\": \"韩国\", \"v\": \"韩国\"}, {\"n\": \"日本\", \"v\": \"日本\"}, {\"n\": \"中国大陆\", \"v\": \"中国大陆\"}, {\"n\": \"美国\", \"v\": \"美国\"}, {\"n\": \"中国香港\", \"v\": \"中国香港\"}, {\"n\": \"中国台湾\", \"v\": \"中国台湾\"}, {\"n\": \"英国\", \"v\": \"英国\"}, {\"n\": \"法国\", \"v\": \"法国\"}, {\"n\": \"德国\", \"v\": \"德国\"}, {\"n\": \"意大利\", \"v\": \"意大利\"}, {\"n\": \"西班牙\", \"v\": \"西班牙\"}, {\"n\": \"印度\", \"v\": \"印度\"}, {\"n\": \"泰国\", \"v\": \"泰国\"}, {\"n\": \"俄罗斯\", \"v\": \"俄罗斯\"}, {\"n\": \"加拿大\", \"v\": \"加拿大\"}, {\"n\": \"澳大利亚\", \"v\": \"澳大利亚\"}, {\"n\": \"爱尔兰\", \"v\": \"爱尔兰\"}, {\"n\": \"瑞典\", \"v\": \"瑞典\"}, {\"n\": \"巴西\", \"v\": \"巴西\"}, {\"n\": \"丹麦\", \"v\": \"丹麦\"}]}, {\"key\": \"year\", \"name\": \"年代\", \"value\": [{\"n\": \"全部年代\", \"v\": \"\"}, {\"n\": \"2025\", \"v\": \"2025\"}, {\"n\": \"2024\", \"v\": \"2024\"}, {\"n\": \"2023\", \"v\": \"2023\"}, {\"n\": \"2022\", \"v\": \"2022\"}, {\"n\": \"2021\", \"v\": \"2021\"}, {\"n\": \"2020\", \"v\": \"2020\"}, {\"n\": \"2019\", \"v\": \"2019\"}, {\"n\": \"2020年代\", \"v\": \"2020年代\"}, {\"n\": \"2010年代\", \"v\": \"2010年代\"}, {\"n\": \"2000年代\", \"v\": \"2000年代\"}, {\"n\": \"90年代\", \"v\": \"90年代\"}, {\"n\": \"80年代\", \"v\": \"80年代\"}, {\"n\": \"70年代\", \"v\": \"70年代\"}, {\"n\": \"60年代\", \"v\": \"60年代\"}, {\"n\": \"更早\", \"v\": \"更早\"}]}, {\"key\": \"by\", \"name\": \"排序\", \"value\": [{\"n\": \"综合排序\", \"v\": \"\"}, {\"n\": \"首映时间\", \"v\": \"&sort=R\"}, {\"n\": \"近期热度\", \"v\": \"&sort=T\"}, {\"n\": \"高分优先\", \"v\": \"&sort=S\"}]}], \"tv\": [{\"key\": \"type\", \"name\": \"类型\", \"value\": [{\"n\": \"全部类型\", \"v\": \"\"}, {\"n\": \"喜剧\", \"v\": \"喜剧\"}, {\"n\": \"爱情\", \"v\": \"爱情\"}, {\"n\": \"悬疑\", \"v\": \"悬疑\"}, {\"n\": \"动画\", \"v\": \"动画\"}, {\"n\": \"武侠\", \"v\": \"武侠\"}, {\"n\": \"古装\", \"v\": \"古装\"}, {\"n\": \"家庭\", \"v\": \"家庭\"}, {\"n\": \"犯罪\", \"v\": \"犯罪\"}, {\"n\": \"科幻\", \"v\": \"科幻\"}, {\"n\": \"恐怖\", \"v\": \"恐怖\"}, {\"n\": \"历史\", \"v\": \"历史\"}, {\"n\": \"战争\", \"v\": \"战争\"}, {\"n\": \"动作\", \"v\": \"动作\"}, {\"n\": \"冒险\", \"v\": \"冒险\"}, {\"n\": \"传记\", \"v\": \"传记\"}, {\"n\": \"剧情\", \"v\": \"剧情\"}, {\"n\": \"奇幻\", \"v\": \"奇幻\"}, {\"n\": \"惊悚\", \"v\": \"惊悚\"}, {\"n\": \"灾难\", \"v\": \"灾难\"}, {\"n\": \"歌舞\", \"v\": \"歌舞\"}, {\"n\": \"音乐\", \"v\": \"音乐\"}]}, {\"key\": \"area\", \"name\": \"地区\", \"value\": [{\"n\": \"全部地区\", \"v\": \"\"}, {\"n\": \"华语\", \"v\": \"华语\"}, {\"n\": \"欧美\", \"v\": \"欧美\"}, {\"n\": \"国外\", \"v\": \"国外\"}, {\"n\": \"韩国\", \"v\": \"韩国\"}, {\"n\": \"日本\", \"v\": \"日本\"}, {\"n\": \"中国大陆\", \"v\": \"中国大陆\"}, {\"n\": \"中国香港\", \"v\": \"中国香港\"}, {\"n\": \"美国\", \"v\": \"美国\"}, {\"n\": \"英国\", \"v\": \"英国\"}, {\"n\": \"泰国\", \"v\": \"泰国\"}, {\"n\": \"中国台湾\", \"v\": \"中国台湾\"}, {\"n\": \"意大利\", \"v\": \"意大利\"}, {\"n\": \"法国\", \"v\": \"法国\"}, {\"n\": \"德国\", \"v\": \"德国\"}, {\"n\": \"西班牙\", \"v\": \"西班牙\"}, {\"n\": \"俄罗斯\", \"v\": \"俄罗斯\"}, {\"n\": \"瑞典\", \"v\": \"瑞典\"}, {\"n\": \"巴西\", \"v\": \"巴西\"}, {\"n\": \"丹麦\", \"v\": \"丹麦\"}, {\"n\": \"印度\", \"v\": \"印度\"}, {\"n\": \"加拿大\", \"v\": \"加拿大\"}, {\"n\": \"爱尔兰\", \"v\": \"爱尔兰\"}, {\"n\": \"澳大利亚\", \"v\": \"澳大利亚\"}]}, {\"key\": \"year\", \"name\": \"年代\", \"value\": [{\"n\": \"全部年代\", \"v\": \"\"}, {\"n\": \"2025\", \"v\": \"2025\"}, {\"n\": \"2024\", \"v\": \"2024\"}, {\"n\": \"2023\", \"v\": \"2023\"}, {\"n\": \"2022\", \"v\": \"2022\"}, {\"n\": \"2021\", \"v\": \"2021\"}, {\"n\": \"2020\", \"v\": \"2020\"}, {\"n\": \"2019\", \"v\": \"2019\"}, {\"n\": \"2020年代\", \"v\": \"2020年代\"}, {\"n\": \"2010年代\", \"v\": \"2010年代\"}, {\"n\": \"2000年代\", \"v\": \"2000年代\"}, {\"n\": \"90年代\", \"v\": \"90年代\"}, {\"n\": \"80年代\", \"v\": \"80年代\"}, {\"n\": \"70年代\", \"v\": \"70年代\"}, {\"n\": \"60年代\", \"v\": \"60年代\"}, {\"n\": \"更早\", \"v\": \"更早\"}]}, {\"key\": \"by\", \"name\": \"排序\", \"value\": [{\"n\": \"综合排序\", \"v\": \"\"}, {\"n\": \"首映时间\", \"v\": \"&sort=R\"}, {\"n\": \"近期热度\", \"v\": \"&sort=T\"}, {\"n\": \"高分优先\", \"v\": \"&sort=S\"}]}], \"variety\": [{\"key\": \"type\", \"name\": \"类型\", \"value\": [{\"n\": \"全部类型\", \"v\": \"\"}, {\"n\": \"真人秀\", \"v\": \"真人秀\"}, {\"n\": \"脱口秀\", \"v\": \"脱口秀\"}, {\"n\": \"音乐\", \"v\": \"音乐\"}, {\"n\": \"歌舞\", \"v\": \"歌舞\"}]}, {\"key\": \"area\", \"name\": \"地区\", \"value\": [{\"n\": \"全部地区\", \"v\": \"\"}, {\"n\": \"华语\", \"v\": \"华语\"}, {\"n\": \"欧美\", \"v\": \"欧美\"}, {\"n\": \"国外\", \"v\": \"国外\"}, {\"n\": \"韩国\", \"v\": \"韩国\"}, {\"n\": \"日本\", \"v\": \"日本\"}, {\"n\": \"中国大陆\", \"v\": \"中国大陆\"}, {\"n\": \"中国香港\", \"v\": \"中国香港\"}, {\"n\": \"美国\", \"v\": \"美国\"}, {\"n\": \"英国\", \"v\": \"英国\"}, {\"n\": \"泰国\", \"v\": \"泰国\"}, {\"n\": \"中国台湾\", \"v\": \"中国台湾\"}, {\"n\": \"意大利\", \"v\": \"意大利\"}, {\"n\": \"法国\", \"v\": \"法国\"}, {\"n\": \"德国\", \"v\": \"德国\"}, {\"n\": \"西班牙\", \"v\": \"西班牙\"}, {\"n\": \"俄罗斯\", \"v\": \"俄罗斯\"}, {\"n\": \"瑞典\", \"v\": \"瑞典\"}, {\"n\": \"巴西\", \"v\": \"巴西\"}, {\"n\": \"丹麦\", \"v\": \"丹麦\"}, {\"n\": \"印度\", \"v\": \"印度\"}, {\"n\": \"加拿大\", \"v\": \"加拿大\"}, {\"n\": \"爱尔兰\", \"v\": \"爱尔兰\"}, {\"n\": \"澳大利亚\", \"v\": \"澳大利亚\"}]}, {\"key\": \"year\", \"name\": \"年代\", \"value\": [{\"n\": \"全部年代\", \"v\": \"\"}, {\"n\": \"2025\", \"v\": \"2025\"}, {\"n\": \"2024\", \"v\": \"2024\"}, {\"n\": \"2023\", \"v\": \"2023\"}, {\"n\": \"2022\", \"v\": \"2022\"}, {\"n\": \"2021\", \"v\": \"2021\"}, {\"n\": \"2020\", \"v\": \"2020\"}, {\"n\": \"2019\", \"v\": \"2019\"}, {\"n\": \"2020年代\", \"v\": \"2020年代\"}, {\"n\": \"2010年代\", \"v\": \"2010年代\"}, {\"n\": \"2000年代\", \"v\": \"2000年代\"}, {\"n\": \"90年代\", \"v\": \"90年代\"}, {\"n\": \"80年代\", \"v\": \"80年代\"}, {\"n\": \"70年代\", \"v\": \"70年代\"}, {\"n\": \"60年代\", \"v\": \"60年代\"}, {\"n\": \"更早\", \"v\": \"更早\"}]}, {\"key\": \"by\", \"name\": \"排序\", \"value\": [{\"n\": \"综合排序\", \"v\": \"\"}, {\"n\": \"首映时间\", \"v\": \"&sort=R\"}, {\"n\": \"近期热度\", \"v\": \"&sort=T\"}, {\"n\": \"高分优先\", \"v\": \"&sort=S\"}]}]}";
        return Result.string(classes, new JSONObject(f));
    }

    @Override
    public String categoryContent(String tid, String pg, boolean filter, HashMap<String, String> extend) throws Exception {
        String type = extend.get("type") == null ? "" : extend.get("type");
        String area = extend.get("area") == null ? "" : extend.get("area");
        String year = extend.get("year") == null ? "" : extend.get("year");
        // 综合排序:""   首映时间: &sort=R  近期热度: &sort=T  高分优先: &sort=S
        String by = extend.get("by") == null ? "" : extend.get("by");
        String typeId = tid.equals("variety") ? "tv" : tid; // 综艺的 cateUrl 接口 tid 拼接字符串也是 tv

        if (typeId.equals("hot_movie")) return getHotMovieOrTV("movie", pg);// 获取热门电影，直接返回
        if (typeId.equals("hot_tv")) return getHotMovieOrTV("tv", pg);// 获取热门电影，直接返回

        // 全部电影 https://m.douban.com/rexxar/api/v2/movie/recommend?refresh=0&start=0&count=20&selected_categories={"地区":"","类型":""}&uncollect=false&playable=true&tags=
        // 华语电影 https://m.douban.com/rexxar/api/v2/movie/recommend?refresh=0&start=0&count=20&selected_categories={"地区":"华语","类型":""}&uncollect=false&playable=true&tags=华语
        // 喜剧华语电影 https://m.douban.com/rexxar/api/v2/movie/recommend?refresh=0&start=0&count=20&selected_categories={"地区":"华语","类型":"喜剧"}&uncollect=false&playable=true&tags=华语,喜剧

        // 全部电影第二页 https://m.douban.com/rexxar/api/v2/movie/recommend?refresh=0&start=20&count=20&selected_categories={"地区":"","类型":""}&uncollect=false&playable=true&tags=
        // 全部电影第三页 https://m.douban.com/rexxar/api/v2/movie/recommend?refresh=0&start=40&count=20&selected_categories={"地区":"","类型":""}&uncollect=false&playable=true&tags=
        // 由此可见， start = (页码-1)*count  count 默认为 20
//            String cateUrl = "https://m.douban.com/rexxar/api/v2/movie/recommend?refresh=0&start=0&count=20&selected_categories=%7B%22%E5%9C%B0%E5%8C%BA%22:%22%E5%8D%8E%E8%AF%AD%22,%22%E7%B1%BB%E5%9E%8B%22:%22%22%7D&uncollect=false&playable=true&tags=%E5%8D%8E%E8%AF%AD";

        JSONObject selectObj = new JSONObject().put("地区", area).put("类型", type);
        String tags = ""; // 地区、类型均为空字符串
        tags = joinStr(tags, type, ",");
        tags = joinStr(tags, area, ",");
        tags = joinStr(tags, year, ",");

        // 电视剧、综艺的额外处理 start
        if (type.equals("") && tid.equals("tv")) tags = joinStr(tags, "电视剧", ",");
        if (type.equals("") && tid.equals("variety")) tags = joinStr(tags, "综艺", ",");
        if (tid.equals("tv")) selectObj.put("形式", "电视剧");
        if (tid.equals("variety")) selectObj.put("形式", "综艺");
        // 电视剧、综艺的额外处理 end

        String playable = "";
        if (typeId.equals("movie")
                && type.equals("")
                && area.equals("")
                && year.equals("")
                && by.equals("")) {
            // 电影分类页面首次访问，没有任何筛选时
            playable = "&playable=true";
        }
        String selectedCategories = selectObj.toString();
        // int start = (Integer.parseInt(pg) - 1) * 20;
//            int count = 58; // 修改为查询 58 条记录，选了二级筛选华语、2019年翻页翻到第6页会出现请求超时异常，而且58条数据豆瓣查询接口特别慢
        int count = 20; // 修改为默认查询 20 条记录
        int start = (Integer.parseInt(pg) - 1) * count;
//            String cateUrl = "https://m.douban.com/rexxar/api/v2/" + typeId + "/recommend?refresh=0&start=" + start + "&count=20&selected_categories=" + selectedCategories + "&uncollect=false" + playable + "&tags=" + tags + by;
        String cateUrl = "https://m.douban.com/rexxar/api/v2/" + typeId + "/recommend?refresh=0&start=" + start + "&count=" + count + "&selected_categories=" + selectedCategories + "&uncollect=false" + playable + "&tags=" + tags + by;

        // 创建一个请求对象
        String refererForMovie = "https://movie.douban.com/explore"; // 电影
        String refererForTV = "https://movie.douban.com/tv/"; // 电视剧或综艺
        String referer = tid.equals("movie") ? refererForMovie : refererForTV;
        String rs = OkHttp.string(cateUrl, getHeader(referer));
        List<Vod> list = new ArrayList<>();
        JSONArray items = new JSONObject(rs).getJSONArray("items");
        for (int i = 0; i < items.length(); i++) {
            JSONObject item = items.getJSONObject(i);
            String name = item.optString("title");
            if (name.contains("斗罗大陆") || name.contains("斗破苍穹")) continue; // 这两个动漫名称不规范，太垃圾
            String pic = getPic(item);
            if (pic == null) continue;
            if (pic.contains("doubanio.com/f/frodo")) continue; // 跳过这条数据，这种是属于没有图片的，没有图片的影片肯定是没有资源的
            String vodId = item.optString("id") + "###" + name + "###" + pic;
//            String remark = getRemark(item);
//            list.add(new Vod(vodId, name, pic, remark));
            list.add(new Vod(vodId, name, pic));
        }
        int page = Integer.parseInt(pg), pageCount = Integer.MAX_VALUE, limit = list.size(), total = Integer.MAX_VALUE;
        return Result.get().vod(list).page(page, pageCount, limit, total).string();
    }

    private String getPic(JSONObject item) throws Exception {
        JSONObject picObj = item.optJSONObject("pic");
        if (picObj == null) return null;
        //return picObj.optString("normal") + "@Referer=https://movie.douban.com/@User-Agent=" + userAgent;
        return fixCover(picObj.optString("normal"));
    }

    private String getRemark(JSONObject item) throws Exception {
        JSONObject rating = item.optJSONObject("rating");
        String value = rating == null ? "" : rating.optString("value");
        return "评分：" + value;
    }

    private String getHotMovieOrTV(String type, String pg) throws Exception {
        if (!pg.equals("1")) return ""; // 这个是为了防止 FongMi 的影视会继续翻页的问题。
        // 热门电影
        // https://movie.douban.com/j/search_subjects?type=movie&tag=热门&page_limit=50&page_start=0
        // 热门电视剧
        // https://movie.douban.com/j/search_subjects?type=tv&tag=热门&page_limit=50&page_start=0
        // https://movie.douban.com/j/search_subjects?type=tv&tag=%E7%83%AD%E9%97%A8&page_limit=50&page_start=0
        String hotURL = "https://movie.douban.com/j/search_subjects?type=" + type + "&tag=%E7%83%AD%E9%97%A8&page_limit=50&page_start=0";
        String rs = OkHttp.string(hotURL, getHeader());
        JSONArray subjects = new JSONObject(rs).getJSONArray("subjects");
        List<Vod> list = new ArrayList<>();
        for (int i = 0; i < subjects.length(); i++) {
            JSONObject item = subjects.getJSONObject(i);
            String name = item.optString("title");
            //String pic = item.optString("cover") + "@Referer=https://movie.douban.com/@User-Agent=" + userAgent;
            String pic = fixCover(item.optString("cover"));
            String vodId = item.optString("id") + "###" + name + "###" + pic;
//            String remark = "评分：" + item.optString("rate");
//            list.add(new Vod(vodId, name, pic, remark));
            list.add(new Vod(vodId, name, pic));
        }
        return Result.string(list);
    }

    private String joinStr(String s1, String s2, String joinWith) {
        if (s1 == null || s2 == null || joinWith == null) return "传递进来的参数不能为null";
        if (s1.equals("") && s2.equals("")) return ""; // 两个都为空字符串
        if (s1.equals("")) return s2;
        if (s2.equals("")) return s1;
        return s1 + joinWith + s2;
    }

    private String fixCover(String cover) {
        try {
            return "proxy://do=DoubanWeb&pic=" + Base64.encodeToString(cover.getBytes("UTF-8"), Base64.DEFAULT | Base64.URL_SAFE | Base64.NO_WRAP);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cover;
    }

    private static HashMap<String, String> doubanHeaders = null;

    public static Object[] loadPic(String pic) {
        try {
            pic = new String(Base64.decode(pic, Base64.DEFAULT | Base64.URL_SAFE | Base64.NO_WRAP), "UTF-8");
            if (doubanHeaders == null) {
                doubanHeaders = new HashMap<>();
                doubanHeaders.put("User-Agent", userAgent);
                doubanHeaders.put("referer", "https://movie.douban.com/");
            }
            Response response = OkHttp.newCall(pic, doubanHeaders);
            if (response.code() == 200) {
                Headers headers = response.headers();
                String type = headers.get("Content-Type");
                if (type == null) {
                    type = "application/octet-stream";
                }
                Object[] result = new Object[3];
                result[0] = 200;
                result[1] = type;
                SpiderDebug.log("pic=" + pic + "\n" + "type" + type);
                result[2] = response.body().byteStream();
                return result;
            }
        } catch (Throwable th) {
            th.printStackTrace();
        }
        return null;
    }

    @Override
    public String detailContent(List<String> ids) throws Exception {
        String vodId = ids.get(0);
        String[] split = vodId.split("###");
        JSONObject vod = new JSONObject();
        vod.put("vod_id", ids.get(0));
        vod.put("vod_name", split[1]); // 影片名称
        vod.put("vod_pic", split[2]); // 图片
        vod.put("type_name", "typeName"); // 影片类型 选填
        vod.put("vod_year", "year"); // 年份 选填
        vod.put("vod_area", "area"); // 地区 选填
        vod.put("vod_remarks", "remark"); // 备注 选填
        vod.put("vod_actor", "actor"); // 主演 选填
        vod.put("vod_director", "director"); // 导演 选填
        vod.put("vod_content", "暂无播放数据"); // 简介 选填
//        if (playMap.size() > 0) {
//            vod.put("vod_play_from", join("$$$", playMap.keySet()));
//            vod.put("vod_play_url", join("$$$", playMap.values()));
//        }
        JSONArray jsonArray = new JSONArray().put(vod);
        JSONObject result = new JSONObject().put("list", jsonArray);
        return result.toString();
    }

    public static void main(String[] args) {
        DoubanWeb doubanWeb = new DoubanWeb();
        try {
            doubanWeb.init(new Context());
            String homeContent = doubanWeb.homeContent(true);
            SpiderDebug.log(homeContent);

            String categoryContent = doubanWeb.categoryContent("movie", "2", true, new HashMap<String, String>());
            SpiderDebug.log(categoryContent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
