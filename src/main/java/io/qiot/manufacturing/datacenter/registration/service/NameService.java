package io.qiot.manufacturing.datacenter.registration.service;

import java.util.UUID;

/**
 * @author mmascia
 */
public interface NameService {
    
    String getName(UUID id);
}
