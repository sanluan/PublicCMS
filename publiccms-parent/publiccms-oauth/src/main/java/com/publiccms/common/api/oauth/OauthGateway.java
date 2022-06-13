package com.publiccms.common.api.oauth;

import java.io.IOException;
import java.util.function.Supplier;

import org.apache.http.client.ClientProtocolException;

import com.publiccms.common.api.Container;
import com.publiccms.views.pojo.oauth.OauthAccess;
import com.publiccms.views.pojo.oauth.OauthUser;

/**
 *
 * OauthGateway
 *
 */
public interface OauthGateway extends Container<String> {
    /**
     * @return channel
     */
    String getChannel();

    default Supplier<String> keyFunction() {
        return () -> getChannel();
    }

    /**
     * @param siteId
     * @return enabled
     */
    boolean enabled(short siteId);

    /**
     * @param siteId
     * @param state
     * @param mobilde
     * @return authorize url
     */
    String getAuthorizeUrl(short siteId, String state, boolean mobilde);

    /**
     * @param siteId
     * @param state
     * @return authorize url
     */
    String getAuthorizeUrl(short siteId, String state);

    /**
     * @param siteId
     * @param code
     * @return oauth access
     * @throws ClientProtocolException
     * @throws IOException
     */
    OauthAccess getOpenId(short siteId, String code) throws ClientProtocolException, IOException;

    /**
     * @param siteId
     * @param oauthAccess
     * @return user info
     * @throws ClientProtocolException
     * @throws IOException
     */
    OauthUser getUserInfo(short siteId, OauthAccess oauthAccess) throws ClientProtocolException, IOException;
}
