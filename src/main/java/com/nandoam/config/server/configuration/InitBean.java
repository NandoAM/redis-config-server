package com.nandoam.config.server.configuration;

import com.nandoam.config.server.postgresql.PostgresqlProperty;
import io.quarkus.runtime.StartupEvent;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.transaction.Transactional;

@ApplicationScoped
public class InitBean {


    @Transactional
    void onStart(@Observes StartupEvent event) {

        initPostgresData();
    }


    private static void initPostgresData() {


        PostgresqlProperty property;

        for (int i = 1; i <= 1000; i++) {
            property = new PostgresqlProperty("clave_" + i, "valor_" + i);
            property.persist();
        }
    }

}
