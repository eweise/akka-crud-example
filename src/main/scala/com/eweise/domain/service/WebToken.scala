package com.eweise.domain.service

import java.time.Instant

import pdi.jwt.{JwtAlgorithm, JwtCirce, JwtClaim}

object WebToken {

    private val TEN_MINUTES = 10 * 60
    val key = "secret key"

    def create(email: String): String = {

        val claim = JwtClaim(
            expiration = Some(Instant.now.plusSeconds(TEN_MINUTES).getEpochSecond),
            issuedAt = Some(Instant.now.getEpochSecond)
        )


        JwtCirce.encode(claim, key, JwtAlgorithm.HS256)
    }

    def decode(token:String) = {
        val decoded = JwtCirce.decodeAll(token)
        decoded
    }

}
