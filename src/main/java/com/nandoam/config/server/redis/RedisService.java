package com.nandoam.config.server.redis;

import com.nandoam.config.server.postgresql.PostgresqlProperty;
import com.nandoam.config.server.postgresql.PostgresqlService;
import io.quarkus.redis.datasource.ReactiveRedisDataSource;
import io.quarkus.redis.datasource.RedisDataSource;
import io.quarkus.redis.datasource.keys.ReactiveKeyCommands;
import io.quarkus.redis.datasource.value.SetArgs;
import io.quarkus.redis.datasource.value.ValueCommands;
import io.smallrye.mutiny.Uni;
import lombok.extern.slf4j.Slf4j;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.time.Duration;
import java.util.List;
import java.util.Optional;


@ApplicationScoped
@Slf4j
public class RedisService {

    @Inject
    PostgresqlService postgresqlService;


    private final ReactiveKeyCommands<String> keyCommands;

    private final ValueCommands<String, String> propertyCommands;


    public RedisService(RedisDataSource ds, ReactiveRedisDataSource reactive) {

        propertyCommands = ds.value(String.class);
        keyCommands = reactive.key();
    }

    public Uni<List<String>> keys() {
        return keyCommands.keys("*");
    }


    public Property getProperty(String key) {

        String value = propertyCommands.get(key);

        if (value != null) {
            log.info("Key in redis: {}", key);
            return new Property(key, value);
        }

        Optional<PostgresqlProperty> postgresqlProperty = postgresqlService.findByKey(key);

        if (postgresqlProperty.isPresent()) {
            log.info("Key in postgresql");
            //set(key,postgresqlProperty.get().value);
            propertyCommands.set(key, postgresqlProperty.get().value, new SetArgs().ex(Duration.ofMinutes(1)));
            log.info("Key saved in redis: {}", key);
            return new Property(key, postgresqlProperty.get().value);
        }

        return new Property("no_exist", "property");


    }

    public void set(String key, String value) {
        propertyCommands.set(key, value);
    }


}
