package com.eweise.domain.repository

import com.eweise.domain.model.User

import scala.util.{Failure, Success, Try}


class UserRepository extends CrudRepository[User] {

    def findByEmailAndPassword(email: String, password: String): Option[User] =
        findAll().find(user => user.email == email && user.password == password)
}
