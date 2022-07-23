package com.xapaya.xapaporra;

import com.xapaya.xapaporra.config.AppProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(AppProperties.class)
public class XapaporraApplication {

	public static void main(String[] args) {
		SpringApplication.run(XapaporraApplication.class, args);
	}

}
