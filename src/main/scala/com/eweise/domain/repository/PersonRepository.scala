package com.eweise.domain.repository

import com.eweise.domain.model.Person


class PersonRepository {

    def findByEmailAndPassword(email: String, password: String): Option[Person] = ???
//        findAll().find(user => user.email == email && user.password == password)

    def create(p:Person):Person = ???
}
