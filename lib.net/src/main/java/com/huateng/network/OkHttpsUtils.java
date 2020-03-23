package com.huateng.network;
import com.orhanobut.logger.Logger;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPublicKey;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import Decoder.BASE64Encoder;

/**
 * Created by shanyong on 2017/8/10.
 */

public class OkHttpsUtils {

    /**
     * 获取 SSLSocketFactory
     *
     * @param certificates 服务端证书
     * @param bksFile      客户端bks
     * @param password     客户端证书密码
     * @return
     */
    public static SSLSocketFactory getSSLSocketFactory(InputStream[] certificates, InputStream bksFile, String password) {
        // Create a trust manager that does not validate certificate chains
        final TrustManager[] trustManagers = prepareTrustManager(certificates);

        Logger.i("TrustManagers :" + trustManagers.length);

        KeyManager[] keyManagers = prepareKeyManager(bksFile, password);
        final TrustManager[] trustCerts = new TrustManager[]{new X509TrustManager() {
            @Override
            public void checkClientTrusted(
                    X509Certificate[] chain,
                    String authType) throws CertificateException {
            }

            //客户端并为对ssl证书的有效性进行校验
            @Override
            public void checkServerTrusted(
                    X509Certificate[] chain,
                    String authType) throws CertificateException {
                if (chain == null) {
                    throw new IllegalArgumentException("checkServerTrusted:x509Certificate array isnull");
                }

                if (!(chain.length > 0)) {
                    throw new IllegalArgumentException("checkServerTrusted: X509Certificate is empty");
                }

                //ECDHE_RSA  RSA
                Logger.i("authType :" + authType);
                if (null == authType || (!authType.equalsIgnoreCase("ECDHE_RSA") && !authType.equalsIgnoreCase("RSA"))) {
                    Logger.i(authType);
                    throw new CertificateException("checkServerTrusted: AuthType is not RSA");
                }

                // Perform customary SSL/TLS checks
                try {
                    for (TrustManager trustManager : trustManagers) {
                        ((X509TrustManager) trustManager).checkServerTrusted(chain, authType);
                    }

                } catch (Exception e) {
                    Logger.e(e.getMessage());
                    throw new CertificateException(e);
                }
                // Hack ahead: BigInteger and toString(). We know a DER encoded Public Key begins
                // with 0×30 (ASN.1 SEQUENCE and CONSTRUCTED), so there is no leading 0×00 to drop.
                RSAPublicKey pubkey = (RSAPublicKey) chain[0].getPublicKey();

                //String encoded = new BigInteger(1 /* positive */, pubkey.getEncoded()).toString(16);

                BASE64Encoder base64Encoder = new BASE64Encoder();
                String encoded = base64Encoder.encode(pubkey.getEncoded());

                //Logger.i(encoded);

                // Pin it!
                final boolean expected = NetworkConfig.PUB_KEY.equalsIgnoreCase(encoded);

                if (!expected) {
                    throw new CertificateException("checkServerTrusted: Expected public key: "
                            + NetworkConfig.PUB_KEY + ", got public key:" + encoded);
                }
            }

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[0];
            }
        }};

        // Install the trusting trust manager
        SSLContext sslContext = null;
        try {
            sslContext = SSLContext.getInstance("TLS");
            sslContext.init(keyManagers, trustCerts,
                    new SecureRandom());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        } finally {
            try {
                bksFile.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return sslContext.getSocketFactory();
    }


    /**
     * 准备所信任的服务器证书
     *
     * @param certificates
     * @return
     */
    private static TrustManager[] prepareTrustManager(InputStream... certificates) {
        if (certificates == null || certificates.length <= 0) {
            return null;
        }
        try {
            CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(null);
            int index = 0;
            for (InputStream certificate : certificates) {
                String certificateAlias = Integer.toString(index++);
                keyStore.setCertificateEntry(certificateAlias, certificateFactory.generateCertificate(certificate));
                try {
                    if (certificate != null) {
                        certificate.close();
                    }
                } catch (IOException e) {
                }
            }
            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init(keyStore);
            TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();
            return trustManagers;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }

    /**
     * 准备服务器信任的客户端的证书
     *
     * @param bksFile
     * @param password
     * @return
     */
    private static KeyManager[] prepareKeyManager(InputStream bksFile, String password) {
        try {
            if (bksFile == null || password == null) {
                return null;
            }
            // Android默认的是BKS格式的证书
            KeyStore clientKeyStore = KeyStore.getInstance("BKS");
            clientKeyStore.load(bksFile, password.toCharArray());
            KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            keyManagerFactory.init(clientKeyStore, password.toCharArray());
            return keyManagerFactory.getKeyManagers();

        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnrecoverableKeyException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //信任所有证书
    public static SSLSocketFactory getTrustAllSSLSocketFactory() {

        final X509TrustManager trustAllCerts = new X509TrustManager() {
            @Override
            public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {

            }

            @Override
            public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {

            }

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[0];
            }
        };

        SSLSocketFactory sSLSocketFactory = null;

        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, new TrustManager[]{trustAllCerts},
                    null);
            sSLSocketFactory = sc.getSocketFactory();
        } catch (Exception e) {

        }
        return sSLSocketFactory;
    }

    //信任所有域名
    public static HostnameVerifier getAllHostnameVerify() {
        HostnameVerifier hostnameVerifier = new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        };
        return hostnameVerifier;
    }

    /**
     * set HostnameVerifier
     * {@link HostnameVerifier}
     */
    protected static HostnameVerifier getHostnameVerifier(final String[] hostUrls) {
        HostnameVerifier TRUSTED_VERIFIER = new HostnameVerifier() {

            public boolean verify(String hostname, SSLSession session) {
                boolean ret = false;
                for (String host : hostUrls) {
                    if (host.equalsIgnoreCase(hostname)) {
                        ret = true;
                    }
                }
                return ret;
            }
        };
        return TRUSTED_VERIFIER;
    }
}
