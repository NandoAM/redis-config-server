package com.nandoam.config.server.redis;

import com.nandoam.config.server.model.Properties;
import com.nandoam.config.server.model.Property;
import io.quarkus.redis.datasource.RedisDataSource;
import io.quarkus.redis.datasource.keys.KeyCommands;
import io.quarkus.redis.datasource.value.ValueCommands;
import lombok.extern.slf4j.Slf4j;

import javax.enterprise.context.ApplicationScoped;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@ApplicationScoped
@Slf4j
public class RedisPropertiesService implements RedisService {


    private final KeyCommands<String> keyCommands;
    private final ValueCommands<String, Property> propertyValueCommands;
    private final ValueCommands<String, Properties> propertiesValueCommands;

    public RedisPropertiesService(final RedisDataSource ds) {
        keyCommands = ds.key();
        propertyValueCommands = ds.value(String.class, Property.class);
        propertiesValueCommands = ds.value(String.class, Properties.class);
    }

    @Override
    public void addProperty(final Property property) {
        propertyValueCommands.set(property.key, property);
    }

    @Override
    public Property getProperty(final String key) {
        return propertyValueCommands.get(key);
    }

    @Override
    public List<Property> getAllProperty() {
        List<Property> propertyList = new ArrayList<>();
        List<String> keys = keyCommands.keys("*");
        keys.forEach(key -> {
            propertyList.add(propertyValueCommands.get(key));
        });
        return propertyList;
    }

    @Override
    public void addProperties(final Properties properties) {
        propertiesValueCommands.set(properties.getId(), properties);
    }

    @Override
    public Properties getProperties(final String key) {
        return propertiesValueCommands.get(key);
    }

    @Override
    public List<Properties> getAllProperties() {
        List<Properties> propertiesList = new ArrayList<>();
        List<String> keys = keyCommands.keys("*");
        keys.forEach(key -> {
            propertiesList.add(propertiesValueCommands.get(key));
        });
        return propertiesList;
    }

    @Override
    public Integer countPropertyInProperties(String key) {

        AtomicInteger atomicInteger = new AtomicInteger(0);
        List<String> keys = keyCommands.keys(key + "*");
        keys.forEach(keyValue -> {
              Properties properties = propertiesValueCommands.get(keyValue);
              atomicInteger.addAndGet(properties.getPropertyList().size());
        });
       return atomicInteger.get();
    }
}
