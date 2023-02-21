package com.nandoam.config.server;

import com.nandoam.config.server.postgresql.PostgresqlProperty;
import com.nandoam.config.server.postgresql.PostgresqlService;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import java.util.List;
import java.util.Optional;

@Path("/postgresql")
@Slf4j
public class PostgreSqlResource {

    @Inject
    PostgresqlService postgresqlService;

    @GET
    public List<PostgresqlProperty> getPostgresSQLProperties() {

        return postgresqlService.getAllProperties();
    }

    @GET
    @Path("/{key}")
    public Optional<PostgresqlProperty> getPostgresSQLProperty(String key) {
        log.info("PostgresSQL Key value: {}", key);
        return postgresqlService.findByKey(key);
    }
}
