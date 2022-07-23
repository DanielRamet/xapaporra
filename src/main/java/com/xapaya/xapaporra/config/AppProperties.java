package com.xapaya.xapaporra.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties("app")
public class AppProperties {

    private FootballData footballData;

    @Data
    public static class FootballData {
        private String baseUrl;
        private String apiToken;
        private Leagues leagues;
    }

    @Data
    public static class Leagues {
        private String laliga;
    }
}
