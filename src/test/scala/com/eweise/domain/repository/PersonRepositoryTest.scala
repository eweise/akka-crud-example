package com.eweise.domain.repository

import com.eweise.domain.model.Person
import com.eweise.test.DBTestSupport
import org.scalatest.{FlatSpec, Matchers}

class PersonRepositoryTest extends FlatSpec with Matchers with DBTestSupport {

    val personRepo = new PersonRepository()

    "PersonRepository " should "create and retrieve Person" in {
        autoRollback { implicit session =>
            val person = Person(username = "A", email = "b@c.com", password = "D")
            val newPerson = personRepo.create(person)
            newPerson should not be None

            val maybePerson = personRepo.find(newPerson.id)
            maybePerson should not be None
        }
    }
}
