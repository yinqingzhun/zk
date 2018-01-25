package qs.util;

import com.google.common.base.Preconditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.Optional;
import java.util.function.Consumer;

public class ShellUtil {
   static Logger logger = LoggerFactory.getLogger(ShellUtil.class);
    public static boolean shellkit(Consumer<String> consumer, String... cmd) {

        Preconditions.checkNotNull(consumer, "consumer is null~!");

        Preconditions.checkNotNull(cmd, "cmd is null~1");

        for (String cmdItem : cmd)
            Preconditions.checkNotNull(cmdItem, "cmd element is null~1");

        ProcessBuilder pb = new ProcessBuilder(cmd);

        pb.redirectErrorStream(true);

        int runningStatus = 0;

        String s = null;

        try {
            Process p = pb.start();

            try (BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream(), Charset.forName("UTF-8")))) {

                while ((s = stdInput.readLine()) != null) {
                    logger.info(s);
                    consumer.accept(s);
                }

                runningStatus = p.waitFor();

            } finally {
                p.destroy();
            }
        } catch (Exception ex) {
            runningStatus = 1;
            logger.error(ex.getMessage(), ex);
        }


        return runningStatus == 0;
    }

    public static Optional<Object> invokeJavascript(String script, String funcName, Object... args) {
        ScriptEngineManager engineManager = new ScriptEngineManager();

        ScriptEngine engine = engineManager.getEngineByName("JavaScript");

        Optional<Object> result = Optional.empty();

        try {
            engine.eval(script);

            Invocable inv = (Invocable) engine;

            result = Optional.of(inv.invokeFunction(funcName, args));

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

        return result;
    }
}
