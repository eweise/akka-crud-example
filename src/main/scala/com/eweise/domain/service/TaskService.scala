package com.eweise.domain.service

import java.time.OffsetDateTime

import com.eweise.domain.model.{FormValidatorNel, ID, Task}
import com.eweise.domain.payload.{TaskRequest, TaskResponse}
import com.eweise.domain.repository.TaskRepository

class TaskService(implicit taskRepository: TaskRepository) {

    def create(userId: ID, req: TaskRequest): TaskResponse = {
//        for {
//            tr:TaskRequest <- FormValidatorNel.validateTaskRequest(req)
//        } yield {
//            val task = Task(
//                title = tr.title,
//                userId = userId,
//                details = tr.details,
//                dueDate = tr.dueDate.getOrElse(OffsetDateTime.now),
//                complete = tr.complete.getOrElse(false)
//            )
//            taskRepository.create(task).toTaskResponse
//        }
        ???

    }

    def findAll(userId: ID):List[TaskResponse] = taskRepository.findAll().map(_.toTaskResponse)

    //    def update(task: Task): Task = taskRepository.update(task)
}