package com.nandoam.config.server.redis;

import com.nandoam.config.server.model.Property;
import com.nandoam.config.server.postgresql.PostgresqlProperty;
import com.nandoam.config.server.postgresql.PostgresqlService;
import io.quarkus.redis.datasource.ReactiveRedisDataSource;
import io.quarkus.redis.datasource.RedisDataSource;
import io.quarkus.redis.datasource.keys.ReactiveKeyCommands;
import io.quarkus.redis.datasource.list.ReactiveListCommands;
import io.quarkus.redis.datasource.value.SetArgs;
import io.quarkus.redis.datasource.value.ValueCommands;
import io.smallrye.mutiny.Uni;
import lombok.extern.slf4j.Slf4j;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;


@ApplicationScoped
@Slf4j
public class RedisReactiveService {

    @Inject
    PostgresqlService postgresqlService;


    private final ReactiveKeyCommands<String> reactiveKeyCommands;

    private final ValueCommands<String, String> propertyCommands;
    private final ReactiveListCommands<String, Property> reactiveListCommands;


    public RedisReactiveService(RedisDataSource ds, ReactiveRedisDataSource reactive) {

        propertyCommands = ds.value(String.class);
        reactiveListCommands = reactive.list(String.class, Property.class);
        reactiveKeyCommands = reactive.key();
    }

    public Uni<List<String>> keys() {
        return reactiveKeyCommands.keys("*");
    }
    public Uni<List<String>> keysByUser(String user) {
        return reactiveKeyCommands.keys(user + "*");
    }


    public void addProperty(String key, Property property){
        List<Property> list = Arrays.asList(property);
        Uni<Long> result = reactiveListCommands.rpush(key, list.toArray(new Property[0]));
        result.subscribe().with(value -> log.debug("Resultado {}", value));
    }

    public Uni<List<Property>> getPropertiesList(String key){
        return reactiveListCommands.lrange(key, 0,-1);
    }


    public Property getProperty(String key) {

        String value = propertyCommands.get(key);

        if (value != null) {
            log.info("Key in redis: {} with value: {}", key, value);
            return new Property(key, value);
        }

        log.info("Key {} not in redis. Let's try to retrieve it from postgresql", key);
        Optional<PostgresqlProperty> postgresqlProperty = postgresqlService.findByKey(key);

        if (postgresqlProperty.isPresent()) {
            log.info("Key: {}, in postgresql", key);
            propertyCommands.set(key, postgresqlProperty.get().value, new SetArgs().ex(Duration.ofMinutes(1)));
            log.info("Key saved in redis: {} with value: {}", key, postgresqlProperty.get().value);
            return new Property(key, postgresqlProperty.get().value);
        }

        log.info("Key: {}, neither in redis nor in postgresql", key);
        return new Property("no_exist", "property");


    }

    public void set(String key, String value) {
        propertyCommands.set(key, value);
    }


}
