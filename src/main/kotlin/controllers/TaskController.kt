package com.smoke.st2.controllers

import dev.alpas.http.HttpCall
import dev.alpas.orAbort
import dev.alpas.ozone.create
import dev.alpas.routing.Controller
import com.smoke.st2.entities.Tasks
import me.liuwj.ktorm.dsl.desc
import me.liuwj.ktorm.dsl.orderBy
import me.liuwj.ktorm.dsl.select

class TaskController : Controller() {
    fun index(call: HttpCall) {

       // val tasks = Tasks.getTasks()
        val tasks = Tasks.getTasks()
        println(tasks)
        val total = 1
        call.render("welcome", mapOf("tasks" to tasks, "total" to total))
    }

    fun store(call: HttpCall) {
        println(call)
        val newTask = call.stringParam("newTask").orAbort()
        val now = call.nowInCurrentTimezone().toInstant()
        val post = Tasks.create() {
            it.name to newTask
            it.createdAt to now
            it.updatedAt to now
        }
    }
}