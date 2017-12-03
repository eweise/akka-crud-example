package com.eweise.domain.service

import java.util.UUID
import com.eweise.domain.service.UserClaim

import org.scalatest.{FlatSpec, Matchers}

class JwtTokenTest extends FlatSpec with Matchers {

    "JwtToken" should "create and find a token" in {

        val jwtToken = new JwtToken()
        val userId = UUID.randomUUID()
        val token = jwtToken.create(userId.toString)

        jwtToken.find(token) shouldEqual Right(UserClaim(userId))
    }
}
