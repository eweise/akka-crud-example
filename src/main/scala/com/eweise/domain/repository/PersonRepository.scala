package com.eweise.domain.repository

import com.eweise.domain.model.{ID, Person}
import io.circe.generic.auto._
import io.circe.java8.time.TimeInstances
import io.circe.parser.decode
import io.circe.syntax._
import scalikejdbc._

class PersonRepository extends RepositoryHelper with TimeInstances {

    def findByEmailAndPassword(email: String, password: String): Option[Person] = ???

    def create(person: Person)(implicit session: DBSession): Person = {
        val data = person.asJson.noSpaces
        sql"""insert into person (id, data) values (${person.id}, $data)""".update.apply
        mustExist(find(person.id))
    }

    def findAll()(implicit session: DBSession): List[Person] =
        sql"select data from person".map(rs => handleResult(decode[Person](rs.string("data")))
        ).collection.apply()

    def find(userId: ID)(implicit session: DBSession): Option[Person] =
        sql"select data from person where id = $userId".map(rs =>
            handleResult(decode[Person](rs.string("data")))).single.apply()
}
