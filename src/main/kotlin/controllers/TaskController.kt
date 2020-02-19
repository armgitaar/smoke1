package com.smoke.st2.controllers

import dev.alpas.http.HttpCall
import dev.alpas.orAbort
import dev.alpas.ozone.create
import dev.alpas.routing.Controller
import com.smoke.st2.entities.Tasks
import dev.alpas.ozone.latest

import me.liuwj.ktorm.dsl.*
import me.liuwj.ktorm.entity.toList


class TaskController : Controller() {
    fun index(call: HttpCall) {
        val tasks = Tasks.latest().toList()
        val total = Tasks.count()
        val completed = Tasks.count { it.completed }

        call.render("welcome", mapOf("tasks" to tasks, "total" to total, "completed" to completed ))
    }

    fun store(call: HttpCall) {
        val newTask = call.stringParam("newTask").orAbort()
        val now = call.nowInCurrentTimezone().toInstant()
        val post = Tasks.create() {
            it.name to newTask
            it.createdAt to now
            it.updatedAt to now
        }
        flash("success", "Successfully added to-do")
        call.redirect().back()
    }

    fun delete(call: HttpCall) {
        val id = call.longParam("id").orAbort()
        Tasks.delete { it.id eq id }
        flash("success", "Successfully removed to-do")
        call.redirect().back()
    }

    fun update(call: HttpCall) {
        val id = call.longParam("id").orAbort()
        val currentState = Tasks.select().where { Tasks.id eq id }.map { row -> row[Tasks.completed] }

         if (currentState.first()!!) {
            Tasks
                .update {
                    it.completed to false
                    where {
                        Tasks.id eq id
                    }
                }
             flash("success", "Successfully updated to-do")
             call.redirect().back()
        } else {
            Tasks
                .update {
                    it.completed to true
                    where {
                        Tasks.id eq id
                    }
                }
             flash("success", "Successfully completed to-do")
             call.redirect().back()
        }

    }


}