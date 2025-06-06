package com.github.catvod.spider;

import com.github.catvod.crawler.Spider;
import com.github.catvod.crawler.SpiderDebug;
import com.github.catvod.net.OkHttp;
import com.github.catvod.parser.MixDemo;

import java.io.ByteArrayInputStream;
import java.util.Map;
import java.util.Objects;

public class Proxy extends Spider {

    private static int port = -1;

    public static Object[] proxy(Map<String, String> params) throws Exception {
        switch (Objects.requireNonNull(params.get("do"))) {
            case "ck":
                return new Object[]{200, "text/plain; charset=utf-8", new ByteArrayInputStream("ok".getBytes("UTF-8"))};
            case "MixDemo":
                return MixDemo.loadHtml(params.get("flag"), params.get("url"));
            case "xunlei8":
                return Xunlei8.loadPic(params.get("pic"));
            case "DoubanWeb":
                return DoubanWeb.loadPic(params.get("pic"));
            default:
                return null;
        }
    }

    static void adjustPort() {
        if (Proxy.port > 0) return;
        int port = 9978;
        while (port < 10000) {
            String resp = OkHttp.string("http://127.0.0.1:" + port + "/proxy?do=ck", null);
            if (resp.equals("ok")) {
                SpiderDebug.log("Found local server port " + port);
                Proxy.port = port;
                break;
            }
            port++;
        }
    }

    public static String getUrl() {
        adjustPort();
        return "http://127.0.0.1:" + port + "/proxy";
    }
}
