package com.github.wxpay.sdk;

import static com.github.wxpay.sdk.WXPayConstants.USER_AGENT;

import java.io.InputStream;
import java.security.KeyStore;
import java.security.SecureRandom;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.DefaultHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.BasicHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;

public class WXPayRequest {
    private WXPayConfig config;

    public WXPayRequest(WXPayConfig config) throws Exception {
        this.config = config;
    }

    /**
     * 请求，只请求一次，不做重试
     * 
     * @param url
     * @param uuid
     * @param data
     * @param connectTimeoutMs
     * @param readTimeoutMs
     * @param useCert
     *            是否使用证书，针对退款、撤销等操作
     * @return
     * @throws Exception
     */
    private String requestOnce(String url, String uuid, String data, int connectTimeoutMs, int readTimeoutMs, boolean useCert)
            throws Exception {
        BasicHttpClientConnectionManager connManager;
        if (useCert) {
            // 证书
            char[] password = config.getMchID().toCharArray();
            InputStream certStream = config.getCertStream();
            KeyStore ks = KeyStore.getInstance("PKCS12");
            ks.load(certStream, password);

            // 实例化密钥库 & 初始化密钥工厂
            KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            kmf.init(ks, password);

            // 创建 SSLContext
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(kmf.getKeyManagers(), null, new SecureRandom());

            SSLConnectionSocketFactory sslConnectionSocketFactory = new SSLConnectionSocketFactory(sslContext,
                    new String[] { "TLSv1" }, null, new DefaultHostnameVerifier());

            connManager = new BasicHttpClientConnectionManager(RegistryBuilder.<ConnectionSocketFactory> create()
                    .register("http", PlainConnectionSocketFactory.getSocketFactory())
                    .register("https", sslConnectionSocketFactory).build(), null, null, null);
        } else {
            connManager = new BasicHttpClientConnectionManager(RegistryBuilder.<ConnectionSocketFactory> create()
                    .register("http", PlainConnectionSocketFactory.getSocketFactory())
                    .register("https", SSLConnectionSocketFactory.getSocketFactory()).build(), null, null, null);
        }

        HttpClient httpClient = HttpClientBuilder.create().setConnectionManager(connManager).build();

        HttpPost httpPost = new HttpPost(url);

        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(readTimeoutMs).setConnectTimeout(connectTimeoutMs)
                .build();
        httpPost.setConfig(requestConfig);

        StringEntity postEntity = new StringEntity(data, "UTF-8");
        httpPost.addHeader("Content-Type", "text/xml");
        httpPost.addHeader("User-Agent", USER_AGENT + " " + config.getMchID());
        httpPost.setEntity(postEntity);

        HttpResponse httpResponse = httpClient.execute(httpPost);
        HttpEntity httpEntity = httpResponse.getEntity();
        return EntityUtils.toString(httpEntity, "UTF-8");

    }

    /**
     * 可重试的，非双向认证的请求
     * 
     * @param url
     * @param uuid
     * @param data
     * @return
     * @throws Exception
     */
    public String requestWithoutCert(String url, String uuid, String data) throws Exception {
        return requestOnce(url, uuid, data, config.getHttpConnectTimeoutMs(), config.getHttpReadTimeoutMs(), false);
    }

    /**
     * 可重试的，非双向认证的请求
     * 
     * @param url
     * @param uuid
     * @param data
     * @param connectTimeoutMs
     * @param readTimeoutMs
     * @return
     * @throws Exception
     */
    public String requestWithoutCert(String url, String uuid, String data, int connectTimeoutMs, int readTimeoutMs)
            throws Exception {
        return requestOnce(url, uuid, data, connectTimeoutMs, readTimeoutMs, false);
    }

    /**
     * 可重试的，双向认证的请求
     * 
     * @param url
     * @param uuid
     * @param data
     * @return
     * @throws Exception
     */
    public String requestWithCert(String url, String uuid, String data) throws Exception {
        return requestOnce(url, uuid, data, config.getHttpConnectTimeoutMs(), config.getHttpReadTimeoutMs(), true);
    }

    /**
     * 可重试的，双向认证的请求
     * 
     * @param url
     * @param uuid
     * @param data
     * @param connectTimeoutMs
     * @param readTimeoutMs
     * @return
     * @throws Exception
     */
    public String requestWithCert(String url, String uuid, String data, int connectTimeoutMs, int readTimeoutMs)
            throws Exception {
        return requestOnce(url, uuid, data, connectTimeoutMs, readTimeoutMs, true);
    }
}
