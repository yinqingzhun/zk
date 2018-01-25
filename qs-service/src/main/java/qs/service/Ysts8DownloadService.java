package qs.service;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import qs.util.ShellUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Ysts8DownloadService {
    Logger logger = LoggerFactory.getLogger(Ysts8DownloadService.class);
    String domain = "http://www.ysts8.com";

    public String getMp3Url(String url) {
        try {
            Document doc = Jsoup.connect(url).get();
            String src = doc.select("iframe").attr("src");
            doc = Jsoup.connect(domain + src).get();
            String[] lst = new String[]{"", "", ""};
            doc.select("script").forEach(p -> {
                Matcher matcher = Pattern.compile("mp3:'(.*)'\\+(\\w+)\\+'(.*)'").matcher(p.html());
                if (matcher.find()) {
                    lst[0] = matcher.group(1);
                    lst[2] = matcher.group(3);
                    matcher = Pattern.compile(matcher.group(2) + "\\s*=\\s*'(.*)'").matcher(p.html());
                    if (!lst[0].endsWith("mp3")) {
                        while (matcher.find()) {
                            lst[1] = matcher.group(1);
                        }
                    }else{
                        lst[1]="?";
                    }
                }
            });

//            Arrays.asList(lst).forEach(System.out::println);
            String mp3Url = String.join("", lst);
//            logger.info("mp3 url is :{}", mp3Url);
            return mp3Url;
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
        return null;
    }

    public void download() {
        try {
            Document document = Jsoup.connect("http://www.ysts8.com/Yshtml/Ys4654.html").get();
            List<String> lst = new ArrayList<>();
            document.select("a[title]").forEach(p -> lst.add(domain + p.attr("href")));
            for (int i = 343; i < lst.size(); i++) {
                String url = lst.get(i);
                String mp3Url = getMp3Url(url);
                System.out.println((i + 1) + "->" + mp3Url);
                ShellUtil.shellkit(p -> {
                }, "D:\\curl\\I386\\curl.exe", "-C", "-", "-o", String.format("d:\\mp3\\%s.mp3", i + 1), String.format("\"%s\"", mp3Url), "--progress");
            }
        } catch (IOException e) {
            logger.error(e.getMessage());
        }

    }
}
