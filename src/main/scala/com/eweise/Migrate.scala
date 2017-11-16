package com.eweise

import org.flywaydb.core.Flyway


object Migrate {
    val flyway = new Flyway()
    flyway.migrate()
}
