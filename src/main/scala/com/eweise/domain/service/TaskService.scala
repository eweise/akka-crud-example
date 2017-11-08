package com.eweise.domain.service

import java.time.OffsetDateTime

import com.eweise.domain.model.{ID, Task}
import com.eweise.domain.repository.TaskRepository
import com.eweise.domain.{TaskRequest, TaskResponse}

class TaskService(implicit taskRepository: TaskRepository) {

    def create(userId: ID, req: TaskRequest): TaskResponse = {
        val task = Task(
            title = req.title,
            userId = userId,
            details = req.details,
            dueDate = req.dueDate.getOrElse(OffsetDateTime.now),
            complete = req.complete.getOrElse(false)
        )

        taskRepository.create(task).toTaskResponse
    }

    def update(task: Task): Task = taskRepository.update(task)

}
