package com.eweise.domain.service

import java.time.Instant

import pdi.jwt.{JwtAlgorithm, JwtCirce, JwtClaim}

object WebToken {

    private val tokens = collection.mutable.Map[String, String]()
    private val TEN_MINUTES = 10 * 60

    def create(email: String): String = {

        val claim = JwtClaim(
            expiration = Some(Instant.now.plusSeconds(TEN_MINUTES).getEpochSecond),
            issuedAt = Some(Instant.now.getEpochSecond)
        )

        val key = "secret key"

        val algo = JwtAlgorithm.HS256

        val token = JwtCirce.encode(claim, key, algo)
        tokens.put(email, token)
        token
    }

    def find(email: String): Option[String] = tokens.get(email)

}
