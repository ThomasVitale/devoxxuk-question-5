package com.thomasvitale.game.config;

import io.cloudevents.spring.webflux.CloudEventHttpMessageReader;
import io.cloudevents.spring.webflux.CloudEventHttpMessageWriter;
import org.springframework.boot.web.codec.CodecCustomizer;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.codec.CodecConfigurer;

@Configuration
public class CloudEventConfig implements CodecCustomizer {

    @Override
    public void customize(CodecConfigurer configurer) {
        configurer.customCodecs().register(new CloudEventHttpMessageReader());
        configurer.customCodecs().register(new CloudEventHttpMessageWriter());
    }

}
