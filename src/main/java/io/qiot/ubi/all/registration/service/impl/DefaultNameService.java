package io.qiot.ubi.all.registration.service.impl;

import java.util.UUID;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Typed;

import io.qiot.ubi.all.registration.service.NameService;

/**
 * @author mmascia
 */
@ApplicationScoped
@Typed(DefaultNameService.class)
public class DefaultNameService implements NameService {

    @Override
    public String getName(UUID id) {
        return "factory01";
    }

}
