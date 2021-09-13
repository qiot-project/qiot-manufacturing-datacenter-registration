package io.qiot.manufacturing.datacenter.registration.service.impl;

import io.quarkus.runtime.annotations.RegisterForReflection;

/**
 * @author andreabattaglia
 *
 */
@RegisterForReflection
public enum CertPathEnum {
    /**
     * 
     */
    BOOTSTRAP_FD_SERVER_KS("certs/bootstrap/factorydatacenter/broker.ks"),
    /**
     * 
     */
    BOOTSTRAP_FD_SERVER_TS("certs/bootstrap/factorydatacenter/broker.ts"),
    /**
    * 
    */
    BOOTSTRAP_FD_CLIENT_KS("certs/bootstrap/factorydatacenter/client.ks"),
    /**
    * 
    */
    BOOTSTRAP_FD_CLIENT_TS("certs/bootstrap/factorydatacenter/client.ts"),
    /**
    * 
    */
    RUNTIME_FD_SERVER_KS("certs/bootstrap/factorydatacenter/broker.ks"),
    /**
    * 
    */
    RUNTIME_FD_SERVER_TS("certs/runtime/factorydatacenter/broker.ts"),
    /**
    * 
    */
    RUNTIME_FD_CLIENT_KS("certs/runtime/factorydatacenter/client.ks"),
    /**
    * 
    */
    RUNTIME_FD_CLIENT_TS("certs/runtime/factorydatacenter/client.ts"),

    /**
     * 
     */
    BOOTSTRAP_MF_SERVER_KS("certs/bootstrap/machineryfactory/broker.ks"),
    /**
     * 
     */
    BOOTSTRAP_MF_SERVER_TS("certs/bootstrap/machineryfactory/broker.ts"),
    /**
    * 
    */
    BOOTSTRAP_MF_CLIENT_KS("certs/bootstrap/machineryfactory/client.ks"),
    /**
    * 
    */
    BOOTSTRAP_MF_CLIENT_TS("certs/bootstrap/machineryfactory/client.ts"),
    /**
    * 
    */
    RUNTIME_MF_SERVER_KS("certs/runtime/machineryfactory/broker.ks"),
    /**
    * 
    */
    RUNTIME_MF_SERVER_TS("certs/runtime/machineryfactory/broker.ts"),
    /**
    * 
    */
    RUNTIME_MF_CLIENT_KS("certs/runtime/machineryfactory/client.ks"),
    /**
    * 
    */
    RUNTIME_MF_CLIENT_TS("certs/runtime/machineryfactory/client.ts");

    private final String pathString;

    private CertPathEnum(String pathString) {
        this.pathString = pathString;
    }

    public String getPathString() {
        return pathString;
    }
}
