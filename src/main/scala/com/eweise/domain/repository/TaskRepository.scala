package com.eweise.domain.repository

import com.eweise.domain.model.{ID, Task}
import scalikejdbc._

class TaskRepository extends CrudRepository[Task] {

    def find(taskId: ID)(implicit session: DBSession): Option[Task] = {
        val t = Task.syntax("t")

        sql"""select ${t.result.*} from ${Task.as(t)} where ${t.id} = ${taskId} """
                .map(Task(t.resultName)).single.apply()
    }

    def create(task: Task)(implicit session: DBSession): Task = {
        sql"""insert into task (id, title, details, due_date, complete, created_at, modified_at) values (
                 ${task.id},
                    ${task.title},
                    ${task.details},
                    ${task.dueDate},
                    ${task.complete},
                    ${task.createdAt},
                    ${task.modifiedAt})""".update.apply()
        task
    }
}
