package com.publiccms.logic.component.message;

import static com.publiccms.common.constants.CommonConstants.getSessionUser;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.publiccms.common.api.MessageGateway;
import com.publiccms.entities.sys.SysUser;

@Component
public class MessageHandler extends TextWebSocketHandler {
    private MessageGateway messageGateway;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        MessageUser user = (MessageUser) session.getAttributes().get(getSessionUser());
        if (null != user) {
            messageGateway.addUser(user.getId(), session);
        }
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        if (session.isOpen()) {
            session.close();
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
        SysUser user = (SysUser) session.getAttributes().get(getSessionUser());
        if (null != user) {
            messageGateway.removeUser(user.getId());
        }
    }

    public void setMessageGateway(MessageGateway messageGateway) {
        this.messageGateway = messageGateway;
    }

    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        SysUser user = (SysUser) session.getAttributes().get(getSessionUser());
        if (null != user) {
            UserMessage gatewayMessage = new UserMessage(user.getId(), 0l, message.getPayload());
            messageGateway.send(gatewayMessage);
        }
    }
}