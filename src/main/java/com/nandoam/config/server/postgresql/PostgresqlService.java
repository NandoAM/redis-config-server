package com.nandoam.config.server.postgresql;


import io.quarkus.cache.CacheResult;
import lombok.extern.slf4j.Slf4j;

import javax.enterprise.context.ApplicationScoped;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
@Slf4j
public class PostgresqlService {


    public Optional<PostgresqlProperty> findByKey(String key) {
        return PostgresqlProperty.find("key", key).firstResultOptional();
    }

    @CacheResult(cacheName = "postgresql-all-properties")
    public List<PostgresqlProperty> getAllProperties() {
        log.info("Getting all properties from postgresql");
        return PostgresqlProperty.listAll();

    }

}
