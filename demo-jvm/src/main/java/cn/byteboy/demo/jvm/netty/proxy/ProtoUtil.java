package cn.byteboy.demo.jvm.netty.proxy;

import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpRequest;
import lombok.Getter;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * @author hongshaochuan
 */
public class ProtoUtil {

    public static RequestProto getRequestProto(HttpRequest httpRequest) {
        String uri = httpRequest.uri().toLowerCase();

        if (!uri.startsWith("http://")) {
            uri = "http://" + uri;
        }
        URL url;
        try {
            url = new URL(uri);
        } catch (MalformedURLException e) {
            return null;
        }

        String host = url.getHost().isEmpty() ? httpRequest.headers().get(HttpHeaderNames.HOST) : url.getHost();
        int port = url.getPort() != -1 ? url.getPort() : url.getDefaultPort();
        boolean proxy = httpRequest.headers().contains(HttpHeaderNames.PROXY_CONNECTION);
        return new RequestProto(proxy, host, port, false);
    }



    @Getter
    public static class RequestProto {

        /**
         * 请求是否来源于http代理，用于区分是通过代理服务访问的还是直接通过http访问的代理服务器
         */
        private boolean proxy;

        private String host;

        private int port;

        private boolean ssl;

        public RequestProto(boolean proxy, String host, int port, boolean ssl) {
            this.proxy = proxy;
            this.host = host;
            this.port = port;
            this.ssl = ssl;
        }
    }
}
