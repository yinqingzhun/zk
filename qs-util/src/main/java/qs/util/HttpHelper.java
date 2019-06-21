package qs.util;

import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.*;
import java.net.URI;
import java.net.URLEncoder;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
public class HttpHelper {
    private static Logger logger = LoggerFactory.getLogger(HttpHelper.class);
    private static OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .connectTimeout(20, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS).build();

    public void show() {
    }

    public static Tuple2 httpGet(String url) {
        return httpGet(url, null, null);
    }

    public static Tuple2 httpGet(String url, Map<String, Object> fields) {
        return httpGet(url, fields, null);
    }

    public static Tuple2 httpGet(String url, Map<String, Object> fields, Map<String, Object> headers) {
        Request.Builder requestBuilder = new Request.Builder();

        if (fields != null && fields.size() > 0) {
            String query = String.join("&", fields.entrySet().stream().map(p -> p.getKey() + "=" + ConvertHelper.defaultValue(p.getValue(), "").toString()).collect(Collectors.toList()));
            if (StringUtils.hasText(URI.create(url).getQuery()))
                url += "&" + query;
            else
                url += "?" + query;
        }

        if (headers != null) {
            headers.entrySet().forEach(p -> {
                if (p.getKey() != null && p.getValue() != null)
                    requestBuilder.header(p.getKey(), p.getValue().toString());
            });
        }

        Request request = requestBuilder
                .url(url)
                .build();
        log.debug("request url:{}", url);
        return call(request);
    }

    private static Tuple2 call(Request request) {
        Call call = okHttpClient.newCall(request);
        try {
            Response response = call.execute();
            return Tuple2.build(response.code(), response.body().string());
        } catch (IOException e) {
            logger.warn(e.getMessage(), e);
        }
        return Tuple2.build(0, null);
    }


    public static Tuple2 httpPost(String url, Map<String, Object> fields) {
        return httpPost(url, fields, null);
    }

    public static Tuple2 httpPostJson(String url, String body) {
        return httpPostJson(url, body, null);
    }

    public static Tuple2 httpPostJson(String url, String body, Map<String, Object> headers) {

        Request.Builder requestBuilder = new Request.Builder();

        RequestBody reqBody = RequestBody.create(MediaType.parse("application/json"), body);

        if (headers != null) {
            headers.entrySet().forEach(p -> {
                if (p.getKey() != null && p.getValue() != null)
                    requestBuilder.header(p.getKey(), p.getValue().toString());
            });
        }

        Request request = requestBuilder
                .url(url)
                .post(reqBody)
                .build();
        log.debug("request url:{}", url);
        return call(request);
    }


    public static Tuple2 httpPost(String url, Map<String, Object> fields, Map<String, Object> headers) {
        Request.Builder requestBuilder = new Request.Builder();
        FormBody.Builder bodyBuilder = new FormBody.Builder();
        if (fields != null) {
            fields.entrySet().forEach(p -> bodyBuilder.add(p.getKey(), ConvertHelper.defaultValue(p.getValue(), "").toString()));
        }
        if (headers != null) {
            headers.entrySet().forEach(p -> {
                if (p.getKey() != null && p.getValue() != null)
                    requestBuilder.header(p.getKey(), p.getValue().toString());
            });
        }

        Request request = requestBuilder
                .url(url)
                .post(bodyBuilder.build())
                .build();
        log.debug("request url:{}", url);
        return call(request);
    }

    public static class Tuple2 {
        private Integer m;
        private String n;

        private Tuple2(Integer first, String second) {
            m = first;
            n = second;
        }

        public Integer getT1() {
            return m;
        }

        public String getT2() {
            return n;
        }

        public static Tuple2 build(Integer first, String second) {
            return new Tuple2(first, second);
        }
    }

    public static String urlEncoding(String url, Map<String, String> params) {
        StringBuilder sb = new StringBuilder();
        if (params != null && params.size() > 0) {
            Set<Map.Entry<String, String>> entrySet = params.entrySet();
            sb.append("?");
            for (Map.Entry<String, String> entry : entrySet) {
                sb.append(entry.getKey());
                sb.append("=");
                try {
                    sb.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                sb.append("&");
            }
            sb.deleteCharAt(sb.length() - 1);
        }
        return url + sb.toString();
    }


}
