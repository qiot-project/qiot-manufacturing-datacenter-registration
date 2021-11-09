package io.qiot.ubi.all.registration.util;

import java.io.ByteArrayOutputStream;
import java.io.StringReader;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.Security;
import java.security.spec.PKCS8EncodedKeySpec;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Singleton;

import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.PEMKeyPair;
import org.bouncycastle.openssl.PEMParser;
import org.slf4j.Logger;

import io.quarkus.runtime.annotations.RegisterForReflection;

@Singleton
@RegisterForReflection(targets = { BouncyCastleProvider.class,
        PrivateKeyInfo.class, X509CertificateHolder.class,
        JcaX509CertificateConverter.class, PEMKeyPair.class, PEMParser.class })
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

    @PostConstruct
    public void init() {
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
    }

    public byte[] toPKCS12(final String keyFile, final String cerFile, final String password) throws Exception {
        {
                // Get the private key
                StringReader reader = new StringReader(keyFile);
        
                PEMParser pem = new PEMParser(reader);
                Object parsedObject = pem.readObject();
                PrivateKeyInfo privateKeyInfo = parsedObject instanceof PEMKeyPair ? ((PEMKeyPair)parsedObject).getPrivateKeyInfo() : (PrivateKeyInfo)parsedObject;
                PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(privateKeyInfo.getEncoded());
                KeyFactory factory = KeyFactory.getInstance("RSA");
                PrivateKey key = factory.generatePrivate(privateKeySpec);
        
                pem.close();
                reader.close();
        
                // Get the certificate
                reader = new StringReader(cerFile);
                pem = new PEMParser(reader);
        
                X509CertificateHolder certHolder = (X509CertificateHolder) pem.readObject();
                java.security.cert.Certificate X509Certificate =
                    new JcaX509CertificateConverter().setProvider("BC")
                        .getCertificate(certHolder);
        
                pem.close();
                reader.close();
        
                // Put them into a PKCS12 keystore and write it to a byte[]
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                KeyStore ks = KeyStore.getInstance("PKCS12");
                ks.load(null);
                ks.setKeyEntry("alias", (Key) key, password.toCharArray(),
                    new java.security.cert.Certificate[]{X509Certificate});
                ks.store(bos, password.toCharArray());
                bos.close();
                return bos.toByteArray();
            }
    }

    public byte[] toPKCS12(final String cerFile, final String password) throws Exception {
        {
                // Get the certificate
                StringReader reader = new StringReader(cerFile);
                PEMParser pem = new PEMParser(reader);
        
                X509CertificateHolder certHolder = (X509CertificateHolder) pem.readObject();
                java.security.cert.Certificate X509Certificate =
                    new JcaX509CertificateConverter().setProvider("BC")
                        .getCertificate(certHolder);
        
                pem.close();
                reader.close();
        
                // Put them into a PKCS12 keystore and write it to a byte[]
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                KeyStore ks = KeyStore.getInstance("PKCS12");
                ks.load(null);
                ks.setCertificateEntry("root", X509Certificate);
                ks.store(bos, password.toCharArray());
                bos.close();
                return bos.toByteArray();
            }
    }
}