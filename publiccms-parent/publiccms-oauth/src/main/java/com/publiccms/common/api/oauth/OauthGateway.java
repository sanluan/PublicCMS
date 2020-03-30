package com.publiccms.common.api.oauth;

import java.io.IOException;
import java.util.function.Supplier;

import org.apache.http.client.ClientProtocolException;

import com.publiccms.common.api.Container;
import com.publiccms.view.pojo.oauth.OauthAccess;
import com.publiccms.view.pojo.oauth.OauthUser;

/**
 *
 * OauthGateway
 *
 */
public interface OauthGateway extends Container<String> {
    /**
     * @return channel
     */
    public String getChannel();

    default Supplier<String> keyFunction() {
        return new Supplier<String>() {

            @Override
            public String get() {
                return getChannel();
            }

        };
    }

    /**
     * @param siteId
     * @return enabled
     */
    public boolean enabled(short siteId);

    /**
     * @param siteId
     * @param state
     * @param mobilde
     * @return authorize url
     */
    public String getAuthorizeUrl(short siteId, String state, boolean mobilde);

    /**
     * @param siteId
     * @param state
     * @return authorize url
     */
    public String getAuthorizeUrl(short siteId, String state);

    /**
     * @param siteId
     * @param code
     * @return oauth access
     * @throws ClientProtocolException
     * @throws IOException
     */
    public OauthAccess getOpenId(short siteId, String code) throws ClientProtocolException, IOException;

    /**
     * @param siteId
     * @param oauthAccess
     * @return user info
     * @throws ClientProtocolException
     * @throws IOException
     */
    public OauthUser getUserInfo(short siteId, OauthAccess oauthAccess) throws ClientProtocolException, IOException;
}
