package com.smoke.st2

import com.smoke.st2.controllers.TaskController
import dev.alpas.routing.RouteGroup
import dev.alpas.routing.Router


// https://alpas.dev/docs/routing
fun Router.addRoutes() = apply {
    group {
        webRoutesGroup()
    }.middlewareGroup("web")


    apiRoutes()
}

private fun RouteGroup.webRoutesGroup() {
    get("/", TaskController::class).name("welcome")
    // register more web routes here
    post("/", TaskController::class).name("store")
    delete("/", TaskController::class).name("delete")
    update("/", TaskController::class).name("update")

   /* group("tasks") {
        addTaskRoutes()
    }.name("tasks")*/
}



/*private fun RouteGroup.addTaskRoutes() {
    get("/", TaskController::class).name("list")
    get("/", TaskController::class, "add").name("add")
    get("/", TaskController::class, "show").name("show")
    post("/", TaskController::class).name("store")
    delete("/", TaskController::class).name("delete")
}*/

private fun Router.apiRoutes() {
    // register API routes here
}
