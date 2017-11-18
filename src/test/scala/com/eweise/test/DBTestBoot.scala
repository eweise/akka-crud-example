package com.eweise.test

import com.eweise.Migrator
import com.eweise.intf.Database
import com.typesafe.config.ConfigFactory

trait DBTestBoot {

    val config = ConfigFactory.load()
    val dbConfig = config.getConfig("database")
    val dbPoolConfiguration = new Database(dbConfig)

    println("migrating...")
    new Migrator(dbConfig).flyway
    new Database(dbConfig)
}
