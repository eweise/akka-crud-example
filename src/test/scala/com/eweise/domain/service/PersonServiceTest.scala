package com.eweise.domain.service

import com.eweise.domain.RegistrationRequest
import com.eweise.domain.model.Person
import com.eweise.domain.repository.PersonRepository
import com.eweise.intf.DBTestSupport
import org.mockito.Matchers._
import org.mockito.Mockito
import org.scalatest.{FlatSpec, Matchers}

class PersonServiceTest extends DBTestSupport {

    implicit val mockPersonRepo = Mockito.mock(classOf[PersonRepository])
    implicit val webToken = Mockito.mock(classOf[JwtToken])

    val personService = new PersonService()

    "registration" should "not throw ValidationException" in {
        val person = Person(
            username = "username",
            email = "email",
            password = "password")

        Mockito.when(mockPersonRepo.create((any(classOf[Person])))(any())).thenReturn(person)
        val response = personService.register(RegistrationRequest(
            username = "username",
            password = "P@ssw0rd",
            email = "e@mail.com"))
    }
}
