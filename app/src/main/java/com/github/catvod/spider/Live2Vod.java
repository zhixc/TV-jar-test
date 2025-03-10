package com.github.catvod.spider;

import android.content.Context;
import android.text.TextUtils;

import com.github.catvod.crawler.Spider;
import com.github.catvod.crawler.SpiderDebug;
import com.github.catvod.net.OkHttp;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author zhixc
 * 电视直播(爬虫版)
 */
public class Live2Vod extends Spider {

    private String myExtend;

    private final String userAgent = "okhttp/3.12.11";

    private String req(String url, Map<String, String> header) {
        return OkHttp.string(url, header);
    }

    private Map<String, String> getHeader() {
        Map<String, String> header = new HashMap<>();
        header.put("User-Agent", userAgent);
        return header;
    }

    @Override
    public void init(Context context, String extend) throws Exception {
        super.init(context, extend);
        myExtend = extend;
    }

    @Override
    public String homeContent(boolean filter) throws Exception {
        JSONArray classes = new JSONArray();
        // 如果是远程配置文件的话，尝试发起请求查询
        if (!myExtend.contains("$")) {
            JSONArray jsonArray = new JSONArray(req(myExtend, getHeader()));
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject liveObj = jsonArray.getJSONObject(i);
                String name = liveObj.optString("name");
                String url = liveObj.optString("url");
                String group = liveObj.optString("group");
                String circuit = liveObj.optString("circuit");
                String diyPic = "";
                if (url.contains("&&&")) {
                    String[] split = url.split("&&&");
                    url = split[0];
                    diyPic = split.length > 1 ? split[1] : "";
                }
                JSONObject typeIdObj = new JSONObject()
                        .put("url", url)
                        .put("pic", diyPic)
                        .put("group", group)
                        .put("circuit", circuit);
                JSONObject obj = new JSONObject()
                        .put("type_id", typeIdObj.toString())
                        .put("type_name", name);
                classes.put(obj);
            }
            JSONObject result = new JSONObject()
                    .put("class", classes);
            return result.toString();
        }

        String sub = myExtend;
        String diyPic = "";
        if (myExtend.contains("&&&")) {
            String[] split = myExtend.split("&&&");
            sub = split[0];
            diyPic = split.length > 1 ? split[1] : "";
        }
        String[] split = sub.split("#");
        for (String s : split) {
            String[] split2 = s.split("\\$");
            String name = split2[0];
            String url = split2[1];
            String pic = url.contains(".txt") ? diyPic : "";
            JSONObject typeIdObj = new JSONObject()
                    .put("url", url)
                    .put("pic", pic);
            JSONObject obj = new JSONObject()
                    .put("type_id", typeIdObj.toString())
                    .put("type_name", name);
            classes.put(obj);
        }
        JSONObject result = new JSONObject()
                .put("class", classes);
        return result.toString();
    }

    @Override
    public String categoryContent(String tid, String pg, boolean filter, HashMap<String, String> extend) throws Exception {
        if (!pg.equals("1")) return "";
        JSONObject typeIdObj = new JSONObject(tid);
        String url = typeIdObj.optString("url");
        String diyPic = typeIdObj.optString("pic");
        String group = typeIdObj.optString("group");
        String circuit = typeIdObj.optString("circuit");
        String content = req(url, getHeader());
        ByteArrayInputStream is = new ByteArrayInputStream(content.getBytes());
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is));
        JSONArray videos = new JSONArray();
        if (content.contains("#genre#")) {
            // 是 txt 格式的直播，调用 txt 直播处理方法
            if (circuit.equals("1")) {
                // 需要按照线路划分
                setTxtLiveCircuit(bufferedReader, videos, diyPic);
            } else {
                setTxtLive(bufferedReader, videos, diyPic);
            }
        }
        if (content.contains("#EXTM3U")) {
            // 是 m3u 格式的直播，调用 m3u 直播处理方法
            if (group.equals("1")) {
                setM3ULiveGroup(bufferedReader, videos, diyPic); // 要分组
            } else {
                setM3ULive(bufferedReader, videos, diyPic);
            }
        }
        // 倒序关闭流
        bufferedReader.close();
        is.close();
        JSONObject result = new JSONObject()
                .put("pagecount", 1)
                .put("list", videos);
        return result.toString();
    }

    /**
     * 字符串截取方法
     *
     * @param prefixMark 前缀字符串
     * @param suffixMark 后缀字符串
     * @param originStr  原始字符串
     * @return 返回的是 从原始字符串中截取 前缀字符串 和 后缀字符串中间的字符串
     */
    private String subStr(String prefixMark, String suffixMark, String originStr) {
        try {
            int i = originStr.indexOf(prefixMark);
            int j = originStr.indexOf(suffixMark, i);
            int len = prefixMark.length();
            return originStr.substring(i + len, j);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    // ######## 处理 m3u 格式的直播
    private void setM3ULive(BufferedReader bufferedReader, JSONArray videos, String diyPic) {
        try {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                if (line.equals("")) continue;
                if (line.contains("#EXTM3U")) continue;
                if (line.contains("#EXTINF")) {
                    String name = subStr("tvg-name=\"", "\" tvg-logo=", line).trim();
                    String pic = subStr("tvg-logo=\"", "\" group-title=", line);
                    if (!diyPic.equals("")) {
                        pic = diyPic; // 如果有自定义图片，那么以自定义图片为主。
                    }
                    String remark = subStr("group-title=\"", "\",", line);
                    if (name.equals("")) {
                        name = line.substring(line.lastIndexOf(",") + 1);
                    }
                    // 再读取一行，就是对应的 url 链接了
                    String url = bufferedReader.readLine().trim();
                    String vod_play_url = name + "$" + url;
                    JSONObject videoInfoObj = new JSONObject()
                            .put("vod_play_url", vod_play_url)
                            .put("pic", pic);
                    JSONObject vod = new JSONObject()
                            .put("vod_id", videoInfoObj.toString())
                            .put("vod_name", name)
                            .put("vod_pic", pic)
                            .put("vod_remarks", remark);
                    videos.put(vod);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ######## 处理 m3u 格式的直播，并进行分组
    private void setM3ULiveGroup(BufferedReader bufferedReader, JSONArray videos, String diyPic) {
        try {
            List<Live> liveList = new ArrayList<>();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                if (line.equals("")) continue;
                if (line.contains("#EXTM3U")) continue;
                if (line.contains("#EXTINF")) {
                    String name = subStr("tvg-name=\"", "\" tvg-logo=", line);
                    String groupTitle = subStr("group-title=\"", "\",", line);
                    if (name.equals("")) {
                        name = line.substring(line.lastIndexOf(",") + 1);
                    }
                    // 再读取一行，就是对应的 url 链接了
                    String url = bufferedReader.readLine().trim();
                    liveList.add(new Live(name, url, groupTitle));
                }
            }
            // 文件流读取完毕后，进行分组
            // 分组重写，以支持安卓7以下设备
            Map<String, List<Live>> collect = new LinkedHashMap<>();
            for (Live live : liveList) {
                String group = live.getGroup();
                if (collect.containsKey(group)) {
                    collect.get(group).add(live);
                } else {
                    List<Live> valueListToPut = new ArrayList<>();
                    valueListToPut.add(live);
                    collect.put(group, valueListToPut);
                }
            }

            for (String group : collect.keySet()) {
                List<Live> lives = collect.get(group);
                List<String> vodItems = new ArrayList<>();
                for (Live it : lives) {
                    vodItems.add(it.getName() + "$" + it.getUrl());
                }
                String vod_play_url = TextUtils.join("#", vodItems);
                JSONObject videoInfoObj = new JSONObject()
                        .put("vod_play_url", vod_play_url)
                        .put("pic", diyPic);
                JSONObject vod = new JSONObject()
                        .put("vod_id", videoInfoObj.toString())
                        .put("vod_name", group)
                        .put("vod_pic", diyPic)
                        .put("vod_remarks", "");
                videos.put(vod);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ######## 处理txt 格式的直播
    private void setTxtLive(BufferedReader bufferedReader, JSONArray videos, String diyPic) {
        try {
            Map<String, String> map = new LinkedHashMap<>();
            List<String> vodItems = new ArrayList<>();
            String group = "";
            int count = 0; // 计数
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                if (line.equals("")) continue; // 空行不管，进入下一次循环
                if (line.contains(",#genre#")) {
                    // 是直播分类
                    count++;
                    if (count > 1) {
                        // count 大于 1 时，可以将直播数据存储起来
                        map.put(group, TextUtils.join("#", vodItems));
                        vodItems.clear(); // 重置 vodItems
                    }
                    group = line.substring(0, line.indexOf(","));
                    continue;
                }
                // 到了这里 line 是一行直播链接代码
                vodItems.add(line.replace(",", "$"));
            }
            // 将最后一次的数据存到 map 集合里面
            if (vodItems.size() > 0) {
                map.put(group, TextUtils.join("#", vodItems));
            }
            for (String key : map.keySet()) {
                String vod_play_url = map.get(key);
                JSONObject videoInfoObj = new JSONObject()
                        .put("vod_play_url", vod_play_url)
                        .put("pic", diyPic);
                JSONObject vod = new JSONObject()
                        .put("vod_id", videoInfoObj.toString())
                        .put("vod_name", key)
                        .put("vod_pic", diyPic)
                        .put("vod_remarks", "");
                videos.put(vod);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ######## 处理txt 格式的直播，分线路
    private void setTxtLiveCircuit(BufferedReader bufferedReader, JSONArray videos, String diyPic) {
        try {
            ArrayList<Live> liveArrayList = new ArrayList<>();
            String group = "";
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                if (line.equals("")) continue; // 空行不管，进入下一次循环
                if (!line.contains(",")) continue;
                if (line.contains(",#genre#")) {
                    // 是直播分类
                    group = line.substring(0, line.indexOf(","));
                    continue;
                }
                // 到了这里 line 是一行直播链接代码
                String[] split = line.split(",");
                String name = split[0];
                String url = split[1];
                liveArrayList.add(new Live(name, url, group));
            }

            Map<String, List<Live>> collect = new LinkedHashMap<>();
            for (Live live : liveArrayList) {
                String groupName = live.getGroup();
                if (collect.containsKey(groupName)) {
                    collect.get(groupName).add(live);
                } else {
                    List<Live> valueListToPut = new ArrayList<>();
                    valueListToPut.add(live);
                    collect.put(groupName, valueListToPut);
                }
            }

            for (String group2 : collect.keySet()) {
                List<Live> lives = collect.get(group2);
                Map<String, List<Live>> collect2 = new LinkedHashMap<>();
                for (Live live : lives) {
                    String name = live.getName();
                    if (collect2.containsKey(name)) {
                        collect2.get(name).add(live);
                    } else {
                        List<Live> valueListToPut = new ArrayList<>();
                        valueListToPut.add(live);
                        collect2.put(name, valueListToPut);
                    }
                }

                int maxSize = 0;
                for (List<Live> value : collect2.values()) {
                    if (value.size() > maxSize) {
                        maxSize = value.size();
                    }
                }
                Map<String, String> play = new LinkedHashMap<>();
                for (int i = 0; i < maxSize; i++) {
                    ArrayList<String> vodItems = new ArrayList<>();
                    for (String name : collect2.keySet()) {
                        try {
                            Live live = collect2.get(name).get(i);
                            vodItems.add(name + "$" + live.getUrl());
                        } catch (Exception e) {
                            // e.printStackTrace();
                        }
                    }
                    if (vodItems.size() > 0) {
                        play.put("线路" + (i + 1), TextUtils.join("#", vodItems));
                    }
                }
                String vod_play_url = TextUtils.join("$$$", play.values());
                String vod_play_from = TextUtils.join("$$$", play.keySet());
                JSONObject videoInfoObj = new JSONObject()
                        .put("vod_play_url", vod_play_url)
                        .put("vod_play_from", vod_play_from)
                        .put("pic", diyPic);
                JSONObject vod = new JSONObject()
                        .put("vod_id", videoInfoObj.toString())
                        .put("vod_name", group2)
                        .put("vod_pic", diyPic)
                        .put("vod_remarks", "");
                videos.put(vod);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public String detailContent(List<String> ids) throws Exception {
        JSONObject videoInfoObj = new JSONObject(ids.get(0));
        String vod_play_url = videoInfoObj.getString("vod_play_url");
        String pic = videoInfoObj.getString("pic");
        String vod_play_from = "选台";  // 线路 / 播放源标题
        String vodPlayFrom = videoInfoObj.optString("vod_play_from");
        if (!vodPlayFrom.equals("")) vod_play_from = vodPlayFrom;

        String description = "";
        String[] split = vod_play_url.split("\\$");
        String name = "电视直播";
        if (split.length == 2) {
            name = split[0];
            description = "播放地址：" + split[1];
        }
        JSONObject vod = new JSONObject()
                .put("vod_id", ids.get(0))
                .put("vod_name", name) // 影片名称
                .put("vod_pic", pic) // 图片/影片封面
                .put("type_name", "电视直播")// 类型
                .put("vod_year", "") // 年份
                .put("vod_area", "") // 地区
                .put("vod_remarks", "") // 备注
                .put("vod_actor", "") // 主演
                .put("vod_director", "") // 导演
                .put("vod_content", description); // 简介

        if (vod_play_url.length() > 0) {
            vod.put("vod_play_from", vod_play_from);
            vod.put("vod_play_url", vod_play_url);
        }

        JSONArray jsonArray = new JSONArray().put(vod);
        JSONObject result = new JSONObject().put("list", jsonArray);
        return result.toString();
    }

    @Override
    public String playerContent(String flag, String id, List<String> vipFlags) throws Exception {
        Map<String, String> header = new HashMap<>();
        header.put("User-Agent", userAgent);
        JSONObject result = new JSONObject()
                .put("parse", 0) // 直播链接都是可以直接播放的，所以直连就行
                .put("header", header.toString())
                .put("playUrl", "")
                .put("url", id);
        return result.toString();
    }

    /**
     * 仅用于测试，编译打包jar时需要去掉这个函数
     * just for test, remove this function when you make jar
     */
    public static void main(String[] args) {
        Live2Vod live2Vod = new Live2Vod();
        try {
//        live2Vod.init(new Context(), "Box醒人室$https://agit.ai/fantaiying/fty/raw/branch/master/live.txt#影视范精选$https://agit.ai/fantaiying/fmm/raw/branch/main/tv/m3u/global.m3u&&&https://cdn.jsdelivr.net/gh/zhixc/CatVodTVSpider@main/other/pic/live.png");
//        live2Vod.init(new Context(), "Box醒人室$https://agit.ai/fantaiying/fty/raw/branch/master/live.txt#影视范精选$https://agit.ai/fantaiying/fmm/raw/branch/main/tv/m3u/global.m3u&&&");
            live2Vod.init(new Context(), "https://raw.githubusercontent.com/zhixc/CatVodTVSpider/main/other/json/live.json"); // 远程 json 配置文件

            // 首页测试，输出...
            SpiderDebug.log(live2Vod.homeContent(true));
            Thread.sleep(5 * 1000L);

            // 分类页面数据测试
//        String tid = "{\"pic\": \"https://cdn.jsdelivr.net/gh/zhixc/CatVodTVSpider@main/other/pic/live.png\", \"url\": \"https://agit.ai/fantaiying/fty/raw/branch/master/live.txt\", \"circuit\": 1}";
//        String tid = "{\"pic\": \"https://cdn.jsdelivr.net/gh/zhixc/CatVodTVSpider@main/other/pic/live.png\", \"url\": \"https://agit.ai/xiaohu/tvbox/raw/branch/main/live.txt\", \"circuit\": 1}";
            String tid = "{\"circuit\":\"\",\"pic\":\"https://cdn.jsdelivr.net/gh/zhixc/CatVodTVSpider@main/other/pic/live.png\",\"url\":\"https://raw.githubusercontent.com/Ftindy/IPTV-URL/main/bestv.m3u\",\"group\":\"1\"}";
            SpiderDebug.log(live2Vod.categoryContent(tid, "1", true, null));
            Thread.sleep(5 * 1000L);

            // m3u 格式的参数
            String s = "{\"vod_play_url\":\"CCTV1$https://cntv.sbs/live?auth=230601&id=cctv1\",\"pic\":\"https://live.fanmingming.com/tv/CCTV1.png\"}";
            JSONObject videoInfoObj = new JSONObject(s);
            List<String> ids = new ArrayList<>();
            ids.add(videoInfoObj.toString());
            SpiderDebug.log(live2Vod.detailContent(ids));
            Thread.sleep(5 * 1000L);

            SpiderDebug.log(live2Vod.playerContent("", "https://cntv.sbs/live?auth=230601&id=cctv1", new ArrayList<>()));
            Thread.sleep(5 * 1000L);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

class Live {
    private String name;
    private String url;
    private String group;

    public Live(String name, String url, String group) {
        this.name = name;
        this.url = url;
        this.group = group;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    public String getGroup() {
        return group;
    }
}
