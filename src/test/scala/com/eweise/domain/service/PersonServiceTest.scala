package com.eweise.domain.service

import com.eweise.domain.RegistrationRequest
import com.eweise.domain.repository.PersonRepository
import com.eweise.intf.DBTestSupport
import org.mockito.Mockito
import org.scalatest.{FlatSpec, Matchers}

class PersonServiceTest extends FlatSpec with Matchers with DBTestSupport {

    implicit val mockPersonRepo = Mockito.mock(classOf[PersonRepository])
    implicit val webToken = Mockito.mock(classOf[WebToken])

    val personService = new PersonService()

    "registration" should "not throw ValidationException" in {
        val response = personService.register(RegistrationRequest(
            username = "username",
            password = "P@ssw0rd",
            email = "e@mail.com"))
    }
}
