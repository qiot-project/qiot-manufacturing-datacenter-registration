package io.qiot.manufacturing.datacenter.registration.certmanager.client;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import io.fabric8.certmanager.api.model.v1.CertificateList;
import io.fabric8.certmanager.client.CertManagerClient;
import io.fabric8.certmanager.server.mock.EnableCertManagerMockClient;

@EnableCertManagerMockClient
public class CertificateOperationTest {

    CertManagerClient client;

    @Test
    public void shouldReturnEmptyList() {
        CertificateList certificateList = client.v1().certificates().inNamespace("ns1").list();
        assertNotNull(certificateList);
        assertTrue(certificateList.getItems().isEmpty());
    }

}
