package com.eweise.domain.service


import cats.data.Validated.{Invalid, Valid}
import cats.implicits._
import com.eweise.domain.model.{ID, Task}
import com.eweise.domain.repository.TaskRepository
import com.eweise.domain.service.Validator.{ValidationResult, notNull, success}
import com.eweise.domain.{TaskRequest, TaskResponse, ValidationFailedException}

import scala.util.{Failure, Try}


class TaskService(implicit taskRepository: TaskRepository) {


    def findAll(userId: ID): List[TaskResponse] = taskRepository.findAll().map(toTaskResponse)

    def create(userId: ID, req: TaskRequest): TaskResponse =
        validateForm(req) match {
            case Valid(_) => toTaskResponse(taskRepository.create(toTask(userId, req)))
            case Invalid(errors) => throw new ValidationFailedException(errors.toList)
        }

    def validateForm(req: TaskRequest): ValidationResult[TaskRequest] = (
            notNull("title", req.title),
            notNull("email", req.details),
            success(req.dueDate),
            success(req.complete)
    ).mapN(TaskRequest)


    def toTaskResponse(task: Task) =
        new TaskResponse(
            id = task.id,
            title = task.title,
            details = task.details,
            dueDate = task.dueDate,
            complete = task.complete)

    def toTask(userId: ID, req: TaskRequest): Task =
        new Task(
            userId = userId,
            title = req.title,
            details = req.details,
            dueDate = req.dueDate)
}