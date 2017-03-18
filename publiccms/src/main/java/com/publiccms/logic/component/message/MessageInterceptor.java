package com.publiccms.logic.component.message;

import static com.publiccms.common.base.AbstractController.getUserFromSession;
import static com.publiccms.common.constants.CommonConstants.getSessionUser;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import com.publiccms.common.interceptor.WebContextInterceptor;
import com.publiccms.entities.sys.SysUser;

@Component
public class MessageInterceptor implements HandshakeInterceptor {
    private WebContextInterceptor webInitializingInterceptor;

    @Override
    public boolean beforeHandshake(ServerHttpRequest paramServerHttpRequest, ServerHttpResponse paramServerHttpResponse,
            WebSocketHandler webSocketHandler, Map<String, Object> paramMap) throws Exception {
        if (paramServerHttpRequest instanceof ServletServerHttpRequest
                && paramServerHttpResponse instanceof ServletServerHttpResponse) {
            HttpServletRequest request = ((ServletServerHttpRequest) paramServerHttpRequest).getServletRequest();
            HttpServletResponse response = ((ServletServerHttpResponse) paramServerHttpResponse).getServletResponse();
            webInitializingInterceptor.preHandle(request, response, null);
            HttpSession session = request.getSession();
            SysUser user = getUserFromSession(session);
            if (null != user) {
                paramMap.put(getSessionUser(), new MessageUser(user.getId(), user.getNickName(), user.getSiteId()));
                return true;
            }
        }
        return false;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler webSocketHandler,
            Exception exception) {
    }

    public void setWebInitializingInterceptor(WebContextInterceptor webInitializingInterceptor) {
        this.webInitializingInterceptor = webInitializingInterceptor;
    }

}