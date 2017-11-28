package com.eweise.domain.service

import cats.data.Validated.{Invalid, Valid}
import cats.implicits._
import com.eweise.domain.model.{Person}
import com.eweise.domain.repository.PersonRepository
import com.eweise.domain.service.Validator.FieldValue
import com.eweise.domain.{LoginRequest, RegistrationRequest, RegistrationResponse, ValidationFailedException}
import scalikejdbc.DB


case class PersonService(implicit userRepo: PersonRepository) {

    def validateForm(req: RegistrationRequest): Validator.ValidationResult[RegistrationRequest] = {
        def validateUserName(implicit fieldValue: FieldValue[String]) = Validator.notNull *> Validator.length(6)

        def validateEmail(implicit fieldValue: FieldValue[String]) = Validator.notNull *> Validator.validateEmail

        def validatePassword(implicit fieldValue: FieldValue[String]) = Validator.notNull *> Validator.validatePassword

        (validateUserName(("username", req.username)),
                validateEmail(("email", req.email)),
                validatePassword(("password", req.password))
        ).mapN(RegistrationRequest)
    }

    def doLogin(user: LoginRequest): Option[Person] = userRepo.findByEmailAndPassword(user.email, user.password)

    def register(request: RegistrationRequest): RegistrationResponse = {
        validateForm(request) match {
            case Valid(value) => {

                DB localTx { implicit session =>
                    userRepo.create(createPerson(value))
                }

                RegistrationResponse(WebToken.create(request.email))
            }
            case Invalid(errors) => throw new ValidationFailedException(errors.toList)
        }
    }

    def createPerson(req: RegistrationRequest): Person = new Person(
        username = req.username,
        email = req.email,
        password = req.password)
}
