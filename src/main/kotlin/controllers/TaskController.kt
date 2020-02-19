package com.smoke.st2.controllers

import dev.alpas.http.HttpCall
import dev.alpas.orAbort
import dev.alpas.ozone.create
import dev.alpas.routing.Controller
import com.smoke.st2.entities.Tasks
import dev.alpas.ozone.latest
import me.liuwj.ktorm.dsl.count
import me.liuwj.ktorm.dsl.delete
import me.liuwj.ktorm.dsl.eq
import me.liuwj.ktorm.dsl.update
import me.liuwj.ktorm.entity.toList


class TaskController : Controller() {
    fun index(call: HttpCall) {
        val tasks = Tasks.latest().toList()
        val total = Tasks.count()
        call.render("welcome", mapOf("tasks" to tasks, "total" to total))
    }

    fun store(call: HttpCall) {
        val newTask = call.stringParam("newTask").orAbort()
        val now = call.nowInCurrentTimezone().toInstant()
        val post = Tasks.create() {
            it.name to newTask
            it.createdAt to now
            it.updatedAt to now
        }
        flash("success", "Successfully added task")
        call.redirect().back()
    }

    fun delete(call: HttpCall) {
        val id = call.longParam("id").orAbort()
        Tasks.delete { it.id eq id }
        flash("success", "Successfully deleted the project!")
        call.redirect().back()
    }

    fun update(call: HttpCall) {
        println("hey")
        val id = call.longParam("id").orAbort()
        println(id)
        Tasks
            .update { it.completed to 1
                where {
                    Tasks.id eq id
                }
            }
        flash("success", "Successfully completed the task!")
        call.redirect().back()
    }

}