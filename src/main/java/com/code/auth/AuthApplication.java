package com.code.auth;

import com.corundumstudio.socketio.SocketIOServer;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication(scanBasePackages = "com.code.auth")

public class AuthApplication {

  public static void main(String[] args) {
    SpringApplication.run(AuthApplication.class, args);
  }

  @Bean
  public CommandLineRunner runner(SocketIOServer server) {
    return args -> {
      server.start();
      Runtime.getRuntime().addShutdownHook(new Thread(server::stop));
    };
  }
}
