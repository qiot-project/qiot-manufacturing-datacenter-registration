package io.qiot.ubi.all.registration.util;

import java.io.StringReader;
import java.security.KeyPair;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.SecureRandom;
import java.security.Security;
import java.security.cert.Certificate;
import java.util.Enumeration;
import java.util.LinkedList;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;

import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.openssl.PEMKeyPair;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.slf4j.Logger;

@Singleton
public class PEMUtils {

    @Inject
    Logger LOGGER;

    /**
     * @param pemString
     *            (keyAndPub)
     * @param chainString (ca/crt)
     * @param password
     * @return
     */
    public KeyStore toPKCS12(final String pemString,
            final String chainString, final String password) {
        try {
            Security.addProvider(
                    new org.bouncycastle.jce.provider.BouncyCastleProvider());

            PrivateKey key;
            Certificate pubCert;

            try (StringReader reader = new StringReader(pemString);
                    PEMParser pem = new PEMParser(reader)) {
                PEMKeyPair pemKeyPair = ((PEMKeyPair) pem.readObject());
                JcaPEMKeyConverter jcaPEMKeyConverter = new JcaPEMKeyConverter()
                        .setProvider("BC");
                KeyPair keyPair = jcaPEMKeyConverter.getKeyPair(pemKeyPair);
                key = keyPair.getPrivate();

                X509CertificateHolder certHolder = (X509CertificateHolder) pem
                        .readObject();
                pubCert = new JcaX509CertificateConverter().setProvider("BC")
                        .getCertificate(certHolder);
            }

            // Get the certificates
            try (StringReader reader = new StringReader(chainString);
                    PEMParser pem = new PEMParser(reader)) {

                // load all certs
                LinkedList<Certificate> certsll = new LinkedList<>();
                X509CertificateHolder certHolder = (X509CertificateHolder) pem
                        .readObject();
                do {
                    Certificate X509Certificate = new JcaX509CertificateConverter()
                            .setProvider("BC").getCertificate(certHolder);
                    certsll.add(X509Certificate);
                } while ((certHolder = (X509CertificateHolder) pem
                        .readObject()) != null);

                Certificate[] chain = new Certificate[certsll.size() + 1];
                chain[0] = pubCert;

                KeyStore ks = KeyStore.getInstance("PKCS12");
                ks.load(null);

                int i = 1;
                for (Certificate cert : certsll) {
                    ks.setCertificateEntry("chain" + i, cert);
                    chain[i] = ks.getCertificate("chain" + i);
                    i++;
                }

                ks.setKeyEntry("cert", key, password.toCharArray(), chain);

                return ks;
            }
        } catch (Exception e) {
            LOGGER.error("", e);
        }
        return null;
    }

}