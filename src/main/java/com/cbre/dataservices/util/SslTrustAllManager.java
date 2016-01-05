package com.cbre.dataservices.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.GeneralSecurityException;
import java.security.cert.X509Certificate;

@Component
public class SslTrustAllManager {

    @Value("${security.trustall}")
    private Boolean trustAllCerts;

    private SSLContext sslContext;

    public SslTrustAllManager(){

        trustAllCerts = new Boolean(true);

        if(trustAllCerts != null && trustAllCerts.booleanValue()){
            TrustManager[] trustAllCerts = new TrustManager[] {
                    new X509TrustManager() {
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return new X509Certificate[0];
                        }
                        public void checkClientTrusted(
                                java.security.cert.X509Certificate[] certs, String authType) {
                        }
                        public void checkServerTrusted(
                                java.security.cert.X509Certificate[] certs, String authType) {
                        }
                    }
            };

            try {
                SSLContext sc = SSLContext.getInstance("SSL");
                sc.init(null, trustAllCerts, new java.security.SecureRandom());
                HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

                this.sslContext = sc;
            }
            catch (GeneralSecurityException e) {
                System.out.println("[EXCEPTION] - Unable to trust all SSL certs");
                System.out.println(e.getMessage());
                System.out.println(e.getStackTrace());
            }
        }

    }

    public SSLContext getSSLContext(){
        return this.sslContext;
    }
}
