package com.smoke.st2.database.migrations

import com.smoke.st2.entities.Tasks
import dev.alpas.ozone.migration.Migration

class CreateTasksTable : Migration() {
    override var name = "2020_02_18_173210_create_tasks_table"
    
    override fun up() {
        createTable(Tasks)
    }
    
    override fun down() {
        dropTable(Tasks)
    }
}