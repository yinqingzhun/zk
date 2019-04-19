package qs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import qs.config.SampleProperties;

@Component
public class AppRunner implements ApplicationRunner {
    Logger logger = LoggerFactory.getLogger(AppRunner.class);
    @Autowired
    SampleProperties sampleProperties;
@Autowired

    /**
     * Callback used to run the bean.
     *
     * @param args incoming application arguments
     * @throws Exception on error
     */
    @Override
    public void run(ApplicationArguments args) throws Exception {
        logger.info("sample host:{},port:{}", sampleProperties.getHost(), sampleProperties.getPort());


    }
}
