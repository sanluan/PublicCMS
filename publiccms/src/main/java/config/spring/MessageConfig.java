package config.spring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import com.publiccms.common.interceptor.WebContextInterceptor;
import com.publiccms.logic.component.message.MessageHandler;
import com.publiccms.logic.component.message.MessageInterceptor;

@Configuration
@EnableWebSocket
public class MessageConfig implements WebSocketConfigurer {
    @Autowired
    private Environment env;
    @Autowired
    private MessageHandler messageHandler;
    @Autowired
    private MessageInterceptor messageInterceptor;
    @Autowired
    private WebContextInterceptor webInitializingInterceptor;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        messageInterceptor.setWebInitializingInterceptor(webInitializingInterceptor);
        if ("true".equalsIgnoreCase(env.getProperty("cms.websocket.enable", "false"))) {
            registry.addHandler(messageHandler, "/message").addInterceptors(messageInterceptor);
            registry.addHandler(messageHandler, "/sockjs/message").addInterceptors(messageInterceptor).withSockJS();
        }
    }
}
