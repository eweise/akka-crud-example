package com.eweise.domain.model

import io.circe.generic.auto._
import io.circe.java8.time._
import io.circe.parser.decode
import org.scalatest.{FlatSpec, Matchers}

class PersonTest extends FlatSpec with Matchers with TimeInstances {

    "create Person from JSON " should "be successful" in {
        val json = "{\"id\":\"70093fe5-5ac2-422b-87d8-c49084a250a4\",\"username\":\"A\",\"email\":\"b@c.com\",\"password\":\"D\",\"primaryAddress\":null,\"primaryPhone\":null,\"primaryEmail\":null,\"createdAt\":\"2017-11-27T17:09:44.584-10:00\",\"modifiedAt\":\"2017-11-27T17:09:44.584-10:00\"}"
     val length = json.length
            val result = decode[Person](json).getOrElse(null)

       val s = ""
    }


}
