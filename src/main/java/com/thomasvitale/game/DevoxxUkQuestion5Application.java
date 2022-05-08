package com.thomasvitale.game;

import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.nativex.hint.NativeHint;
import org.springframework.nativex.hint.TypeAccess;
import org.springframework.nativex.hint.TypeHint;

@SpringBootApplication
@ConfigurationPropertiesScan
@NativeHint(trigger = NioSocketChannel.class, types = {
        @TypeHint(types = NioSocketChannel.class, access = {TypeAccess.DECLARED_CONSTRUCTORS, TypeAccess.DECLARED_METHODS}),
        @TypeHint(types = NioDatagramChannel.class)
})
public class DevoxxUkQuestion5Application {

    public static void main(String[] args) {
        SpringApplication.run(DevoxxUkQuestion5Application.class, args);
    }

}
