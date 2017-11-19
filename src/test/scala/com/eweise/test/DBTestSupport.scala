package com.eweise.test

import com.eweise.Migrator
import com.eweise.intf.Database
import com.typesafe.config.ConfigFactory
import scalikejdbc.{ConnectionPool, DB, DBSession, using}

trait DBTestSupport {

    val config = ConfigFactory.load()
    val dbConfig = config.getConfig("database")
    val dbPoolConfiguration = new Database(dbConfig)

    println("migrating...")
    new Migrator(dbConfig).flyway
    new Database(dbConfig)

    def autoRollback(f: DBSession => Unit): Unit = using(DB(ConnectionPool.borrow())) { db =>
        try {
            db.begin()
            db.withinTx { implicit session =>
                f(session)
            }
        } finally {
            db.rollbackIfActive()
        }
    }
}
