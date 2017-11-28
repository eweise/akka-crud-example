package com.eweise.domain.service

import cats.data.Validated.{Invalid, Valid}
import cats.implicits._
import com.eweise.domain.model.{Person}
import com.eweise.domain.repository.PersonRepository
import com.eweise.domain.service.Validator.FieldValue
import com.eweise.domain.{LoginRequest, RegistrationRequest, RegistrationResponse, ValidationFailedException}
import scalikejdbc.DB


class PersonService(implicit userRepo: PersonRepository, webToken: WebToken ) {

    def validateForm(req: RegistrationRequest): Validator.ValidationResult[RegistrationRequest] = {
        def validateUserName(implicit fieldValue: FieldValue[String]) = Validator.notNull *> Validator.length(6)

        def validateEmail(implicit fieldValue: FieldValue[String]) = Validator.notNull *> Validator.validateEmail

        def validatePassword(implicit fieldValue: FieldValue[String]) = Validator.notNull *> Validator.validatePassword

        (validateUserName(("username", req.username)),
                validateEmail(("email", req.email)),
                validatePassword(("password", req.password))
        ).mapN(RegistrationRequest)
    }

    def login(user: LoginRequest): Option[Person] =
        DB localTx { implicit session => userRepo.findByEmailAndPassword(user.email, user.password)}

    def register(request: RegistrationRequest): RegistrationResponse = {
        validateForm(request) match {
            case Valid(value) => {
                DB localTx { implicit session =>
                    userRepo.create(createPerson(value))
                }
                RegistrationResponse(webToken.create(request.email))
            }
            case Invalid(errors) => throw new ValidationFailedException(errors.toList)
        }
    }

    private[this] def createPerson(req: RegistrationRequest): Person = new Person(
        username = req.username,
        email = req.email,
        password = req.password)
}
