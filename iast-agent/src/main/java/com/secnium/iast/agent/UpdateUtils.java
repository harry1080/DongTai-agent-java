package com.secnium.iast.agent;

import org.json.JSONObject;

import javax.net.ssl.*;
import java.io.BufferedInputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

/**
 * @author dongzhiyong@huoxian.cn
 */
public class UpdateUtils {
    private final static String UPDATE_URL = IASTProperties.getInstance().getBaseUrl() + "/api/v1/engine/update";
    private final static String AGENT_TOKEN = URLEncoder.encode(AgentRegister.getAgentToken());

    //负责发送http请求获取数据
    public static boolean checkForUpdate() {
        String respRaw = sendRequest(UPDATE_URL + "?agent_name=" + AGENT_TOKEN);
        System.out.println(respRaw);
        if (respRaw != null && !respRaw.isEmpty()) {
            JSONObject resp = new JSONObject(respRaw);
            return "1".equals(resp.get("data").toString());
        }
        return false;
    }

    public static void setUpdateSuccess() {
        sendRequest(UPDATE_URL + "/0?agent_name=" + AGENT_TOKEN);
    }

    public static void setUpdateFailure() {
        sendRequest(UPDATE_URL + "/1?agent_name=" + AGENT_TOKEN);
    }

    private static String sendRequest(String urlStr) {
        HttpURLConnection connection = null;
        try {
            trustAllHosts();
            URL url = new URL(urlStr);
            // 通过请求地址判断请求类型(http或者是https)

            if ("https".equals(url.getProtocol().toLowerCase())) {
                HttpsURLConnection https = (HttpsURLConnection) url.openConnection();
                https.setHostnameVerifier(DO_NOT_VERIFY);
                connection = https;
            } else {
                connection = (HttpURLConnection) url.openConnection();
            }
            connection.setRequestMethod("GET");
            //fixme:根据配置文件动态获取token和http请求头，用于后续自定义操作
            connection.setRequestProperty("User-Agent", "SecniumIast Java Agent ");
            connection.setRequestProperty("Authorization", "Token " + IASTProperties.getInstance().getIastServerToken());
            connection.setRequestProperty("Accept", "*/*");
            connection.setUseCaches(false);
            connection.setDoOutput(true);

            BufferedInputStream in = new BufferedInputStream(connection.getInputStream());

            byte[] dataBuffer = new byte[1024];
            int bytesRead;
            StringBuilder sb = new StringBuilder();
            while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
                sb.append(new String(dataBuffer, 0, bytesRead));
            }

            return sb.toString();
        } catch (Exception e) {
            return null;
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    public static void trustAllHosts() {
        // Create a trust manager that does not validate certificate chains
        TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
            @Override
            public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

            }

            @Override
            public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

            }

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[0];
            }
        }};
        // Install the all-trusting trust manager
        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        } catch (Exception e) {
        }
    }

    public final static HostnameVerifier DO_NOT_VERIFY = new HostnameVerifier() {
        @Override
        public boolean verify(String s, SSLSession sslSession) {
            return true;
        }
    };
}
