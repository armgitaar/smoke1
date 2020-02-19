package com.smoke.st2.entities

import dev.alpas.ozone.*
import me.liuwj.ktorm.dsl.desc
import me.liuwj.ktorm.dsl.orderBy
import me.liuwj.ktorm.dsl.select
import me.liuwj.ktorm.schema.boolean
import java.time.Instant

interface Task : OzoneEntity<Task> {
    val id: Long
    val name: String
    val completed: Boolean
    val createdAt: Instant?
    val updatedAt: Instant?

    companion object : OzoneEntity.Of<Task>()
}

object Tasks : OzoneTable<Task>("tasks") {
    val id by bigIncrements()
    val name by string("name").size(150).nullable().bindTo { it.name }
    val completed by boolean("completed").default(false).bindTo { it.completed }
    val createdAt by createdAt()
    val updatedAt by updatedAt()


    fun getTasks(): List<Task> {
        return select()
            .orderBy(createdAt.desc())
            .map { row -> createEntity(row) }
    }


}