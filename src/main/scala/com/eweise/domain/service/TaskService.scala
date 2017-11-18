package com.eweise.domain.service


import cats.data.Validated.{Invalid, Valid}
import cats.implicits._
import com.eweise.domain.model.{ID, Task}
import com.eweise.domain.repository.TaskRepository
import com.eweise.domain.service.Validator.{ValidationResult, notNull, success}
import com.eweise.domain.{TaskRequest, TaskResponse, ValidationFailedException}
import scalikejdbc.DB


class TaskService(implicit taskRepository: TaskRepository) {


    def findAll(userId: ID): List[TaskResponse] = taskRepository.findAll().map(toTaskResponse)

    def find(userId: ID, taskId: ID): Option[TaskResponse] = DB readOnly { implicit session =>
        taskRepository.find(taskId).map(toTaskResponse)
    }

    def create(userId: ID, req: TaskRequest): TaskResponse =
        validateForm(req) match {
            case Valid(_) => toTaskResponse(DB localTx { implicit session =>
                taskRepository.create(toTask(userId, req))
            })
            case Invalid(errors) => throw new ValidationFailedException(errors.toList)
        }

    def update(userId: ID, taskId: ID, req: TaskRequest): TaskResponse =
        validateForm(req) match {
            case Valid(_) =>
                DB localTx { implicit session =>
                    taskRepository.find(taskId) match {
                        case Some(existingTask) =>
                            toTaskResponse(taskRepository.update(updateFields(existingTask, req)))
                        case _ => throw new RuntimeException("not found")
                    }
                }
            case Invalid(errors) => throw new ValidationFailedException(errors.toList)
        }

    def validateForm(req: TaskRequest): ValidationResult[TaskRequest] = (
            notNull("title", req.title),
            notNull("email", req.details),
            success(req.dueDate),
            success(req.complete)
    ).mapN(TaskRequest)

    def updateFields(task: Task, req: TaskRequest): Task = {
        task.copy(title = req.title,
            details = req.details,
            dueDate = req.dueDate,
            complete = req.complete.getOrElse(task.complete)
        )
    }

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