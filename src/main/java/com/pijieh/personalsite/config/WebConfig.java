package com.pijieh.personalsite.config;

import java.time.OffsetDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.pijieh.personalsite.helpers.OffsetDateTimeAdapter;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Autowired
    private AuthenticationInterceptor authenticationInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authenticationInterceptor).addPathPatterns("/admin/**");
    }

    @Bean
    Gson gson() {
        return new GsonBuilder()
                .registerTypeAdapter(OffsetDateTime.class, new OffsetDateTimeAdapter().nullSafe())
                .create();
    }
}
