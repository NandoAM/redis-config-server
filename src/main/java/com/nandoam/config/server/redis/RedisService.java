package com.nandoam.config.server.redis;

import com.nandoam.config.server.model.Properties;
import com.nandoam.config.server.model.Property;

import java.util.List;

public interface RedisService {

    void addProperty(final Property property);
    Property getProperty(final String key);
    List<Property> getAllProperty();

    void addProperties(final Properties properties);
    Properties getProperties(final String key);
    List<Properties> getAllProperties();
    Integer countPropertyInProperties(final String key);

}
