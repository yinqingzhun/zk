package qs.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;


//@Component
//@ConfigurationProperties(prefix = "sample")
//@EnableConfigurationProperties
//@Validated
public class SampleProperties {

    /**
     * Sample host.
     */
    @NotNull
    private String host;

    /**
     * Sample port.
     */
    @Min(1)
    private Integer port = 0;

    public String getHost() {
        return this.host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Integer getPort() {
        return this.port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

}