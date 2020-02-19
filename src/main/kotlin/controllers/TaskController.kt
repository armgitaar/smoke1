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
        val tasks = Tasks.latest().toList()
        val total = tasks.size
        val completed = tasks.count { it.completed }

        call.render("welcome", mapOf("tasks" to tasks, "total" to total, "completed" to completed ))
    }

    fun store(call: HttpCall) {
        call.applyRules("newTask") {
            required()
            min(2)
        }.validate()

        Tasks.create() {
            it.name to call.stringParam("newTask")
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

        val markAsComplete = call.param("state") != null

        Tasks.update {
            it.completed to markAsComplete
            where {
                it.id eq id
            }
        }

        if (markAsComplete) {
            flash("success", "Successfully completed the to-do!")
        } else {
            flash("success", "Successfully updated the to-do")
        }

        call.redirect().back()

    }

}