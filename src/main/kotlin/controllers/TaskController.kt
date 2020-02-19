package com.smoke.st2.controllers

import dev.alpas.http.HttpCall
import dev.alpas.orAbort
import dev.alpas.ozone.create
import dev.alpas.routing.Controller
import com.smoke.st2.entities.Tasks
import dev.alpas.ozone.latest
import dev.alpas.validation.min
import dev.alpas.validation.required

import me.liuwj.ktorm.dsl.*
import me.liuwj.ktorm.entity.toList


class TaskController : Controller() {
    fun index(call: HttpCall) {
        // you are making 3 rounds to the database
        /*
        val tasks = Tasks.latest().toList()
        val total = Tasks.count()
        val completed = Tasks.count { it.completed }
         */
        val tasks = Tasks.latest().toList()
        val total = tasks.size
        val completed = tasks.count { it.completed }

        call.render("welcome", mapOf("tasks" to tasks, "total" to total, "completed" to completed ))
    }

    fun store(call: HttpCall) {
        // with this I can create a task with just an empty string. Let's actually validate it first

        call.applyRules("newTask") {
            required()
            min(2)
        }.validate()

        /* val newTask = call.stringParam("newTask").orAbort() */

        // With new createdAt and updatedAt bindings, you don't need this in most of the cases.
        // If you need more precise control over the timezone then you need it.
        /* val now = call.nowInCurrentTimezone().toInstant() */

        Tasks.create() {
            it.name to call.stringParam("newTask")
            /*
                it.createdAt to now
                it.updatedAt to now
             */
        }
        flash("success", "Successfully added the task!")
        call.redirect().back()
    }

    fun delete(call: HttpCall) {
        val id = call.longParam("id").orAbort()
        Tasks.delete { it.id eq id }
        flash("success", "Successfully removed the task!")
        call.redirect().back()
    }

    fun update(call: HttpCall) {
        val id = call.longParam("id").orAbort()
        // Set the name of the checkbox to something like "state" and use it.
        // I'll explain to you later why this is better.
        val markAsComplete = call.param("state") != null

        Tasks.update {
            it.completed to markAsComplete
            where {
                it.id eq id
            }
        }

        if (markAsComplete) {
            flash("success", "Successfully completed the task!")
        } else {
            flash("success", "Successfully updated the task!")
        }

        call.redirect().back()

        /*
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
         */
    }


}
