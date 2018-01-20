package qs.service;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import qs.util.ShellUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by yinqingzhun on 2017/09/05.
 */
@Service
public class WoaitingshuDownloadService {
    Logger logger = LoggerFactory.getLogger(WoaitingshuDownloadService.class);
    String domain = "http://www.woaitingshu.com";

    public String getMp3Url(String url) {
        try {
            Document doc = Jsoup.connect(url).get();
            List<String> lst = new ArrayList<>();
            doc.select("script").forEach(p -> {
                Matcher matcher = Pattern.compile("FonHen_JieMa[(]'(.+?)'[)]").matcher(p.html());
                if (matcher.find()) {
                    lst.add(matcher.group(1));
                }
            });

//            Arrays.asList(lst).forEach(System.out::println);
            String rs = ShellUtil.invokeJavascript("function FonHen_JieMa(u){\n" +
                    "\tvar tArr = u.split(\"*\");\n" +
                    "\tvar str = '';\n" +
                    "\tfor(var i=1,n=tArr.length;i<n;i++){\n" +
                    "\t\tstr += String.fromCharCode(tArr[i]);\n" +
                    "\t}\n" +
                    "\treturn str;\n" +
                    "}", "FonHen_JieMa", lst.get(0)).get().toString();// String.format("FonHen_JieMa(%s)", lst.get(0));
            String mp3Url = rs.substring(0, rs.indexOf("&"));
//            logger.info("mp3 url is :{}", mp3Url);
            return mp3Url;
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
        return null;
    }

    public void xyz(){}

    ExecutorService executorService = Executors.newWorkStealingPool();

    public void download() {
        try {
            Document document = Jsoup.connect("http://www.woaitingshu.com/mp3/1780.html").get();
            List<String> lst = new ArrayList<>();
            document.select("#vlink_1 a[title]").forEach(p -> lst.add(domain + p.attr("href")));
//            lst.sort(String::compareTo);
            List<Callable<String>> list = new ArrayList<>();
            for (int i = 300; i < 520; i++) {
                String url = lst.get(i);
                final int j = i;
                list.add(() -> {
                    String mp3Url = getMp3Url(url);
                    System.out.println((j + 1) + "->" + mp3Url);
                    ShellUtil.shellkit(p -> {
                    }, "D:\\curl\\I386\\curl.exe", "-C", "-", "-o", String.format("d:\\mp3\\%s.mp3", j + 1), String.format("\"%s\"", mp3Url), "--progress");

                    return null;
                });
            }
            executorService.invokeAll(list);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }

    }
}
