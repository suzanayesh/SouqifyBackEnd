package com.code.auth.config;
import com.code.auth.service.SocketIOService;
import com.corundumstudio.socketio.SocketConfig;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.Configuration;
import org.springframework.context.annotation.Bean;

@org.springframework.context.annotation.Configuration
public class SocketIOConfig {
    @Bean
    public SocketIOServer socketIOServer() {
        Configuration config = new Configuration();
        config.setHostname("0.0.0.0");
        config.setPort(9092);
        config.setAllowCustomRequests(true);
        config.setUpgradeTimeout(10000); // milliseconds
        config.setPingTimeout(1800000); // milliseconds
        config.setPingInterval(60000); // milliseconds

        return new SocketIOServer(config);
    }

//    @Bean
//    public SocketIOService springSocketIOHandler(SocketIOServer server) {
//        return new SocketIOService(server);
//    }
}
