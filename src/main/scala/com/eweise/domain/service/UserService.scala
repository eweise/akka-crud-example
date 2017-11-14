package com.eweise.domain.service

import cats.data.Validated.{Invalid, Valid}
import cats.implicits._
import com.eweise.domain.model.User
import com.eweise.domain.repository.UserRepository
import com.eweise.domain.service.Validator.FieldValue
import com.eweise.domain.{LoginRequest, RegistrationRequest, RegistrationResponse}

import scala.util.Try


case class UserService(implicit userRepo: UserRepository) {

    def validateForm(req: RegistrationRequest): Validator.ValidationResult[RegistrationRequest] = {
        def validateUserName(implicit fieldValue: FieldValue[String]) = Validator.notNull *> Validator.length(6)

        def validateEmail(implicit fieldValue: FieldValue[String]) = Validator.notNull *> Validator.validateEmail

        def validatePassword(implicit fieldValue: FieldValue[String]) = Validator.notNull *> Validator.validatePassword

        (
                validateUserName(("username", req.username)),
                validateEmail(("email", req.email)),
                validatePassword(("password", req.password))
        ).mapN(RegistrationRequest)
    }

    def doLogin(user: LoginRequest): Try[Option[User]] = userRepo.findByEmailAndPassword(user.email, user.password)

    def register(request: RegistrationRequest): Either[List[String], RegistrationResponse] = {
        validateForm(request) match {
            case Valid(value) => {
                userRepo.create(createUser(value))
                Right(RegistrationResponse(WebToken.create(request.email)))
            }
            case Invalid(errors) => {
                Left(errors.toList)
            }
        }
    }

    def createUser(req: RegistrationRequest): User = new User(
        username = req.username,
        email = req.email,
        password = req.password)
}
