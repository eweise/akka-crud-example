package com.eweise.domain.repository

import java.time.OffsetDateTime

import com.eweise.domain.model.{ID, Task}
import scalikejdbc._

class TaskRepository extends CrudRepository[Task] {

    def find(taskId: ID)(implicit session: DBSession): Option[Task] = {
        val t = Task.syntax("t")

        sql"""select ${t.result.*} from ${Task.as(t)} where ${t.id} = ${taskId} """
                .map(Task(t.resultName)).single.apply()
    }

    def create(task: Task)(implicit session: DBSession): Task = {
        sql"""insert into task (id, user_id, title, details, due_date, complete, created_at, modified_at) values (
                 ${task.id},
                 ${task.userId},
                    ${task.title},
                    ${task.details},
                    ${task.dueDate},
                    ${task.complete},
                    ${task.createdAt},
                    ${task.modifiedAt})""".update.apply()
        task
    }

    def update(task: Task)(implicit session: DBSession): Int = {
        sql"""update task set
              title = ${task.title},
              details=${task.details},
              due_date=${task.dueDate},
              complete=${task.complete},
              modified_at=${OffsetDateTime.now}
              where id = ${task.id}""".update.apply()
    }

    def delete(taskId: ID)(implicit session: DBSession): Int =
        sql"delete from task where id = $taskId".update().apply()
}
