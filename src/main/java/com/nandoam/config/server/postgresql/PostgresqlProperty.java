package com.nandoam.config.server.postgresql;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;

import javax.persistence.*;

@Entity
public class PostgresqlProperty extends PanacheEntityBase {

    @Id
    @Column(name = "KEY")
    public String key;
    @Column(name="VALUE")
    public String value;

    public PostgresqlProperty(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public PostgresqlProperty() {
    }
}
