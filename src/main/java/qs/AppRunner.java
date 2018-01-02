package qs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import qs.service.WoaitingshuDownloadService;

@Component
public class AppRunner implements ApplicationRunner {
    @Autowired
    WoaitingshuDownloadService woaitingshuDownloadService;

    /**
     * Callback used to run the bean.
     *
     * @param args incoming application arguments
     * @throws Exception on error
     */
    @Override
    public void run(ApplicationArguments args) throws Exception {
        //baseService.getMp3Url("http://www.ysts8.com/play_4654_50_1_531.html");
    }
}
