package com.eweise.domain

class ValidationFailedException(val errors: List[String]) extends RuntimeException