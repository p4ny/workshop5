package com.example.expensetracker.routes

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import com.example.expensetracker.data.CategoryRequest
import com.example.expensetracker.services.CategoryService // Import service

fun Route.categoryRouting(categoryService: CategoryService) {
    route("/categories") {
        // สร้าง (CREATE) หมวดหมู่ใหม่
        post {
            val categoryRequest = call.receive<CategoryRequest>()
            try {
                val newCategory = categoryService.createCategory(categoryRequest)
                call.respond(HttpStatusCode.Created, newCategory)
            } catch (e: IllegalArgumentException) {
                call.respond(HttpStatusCode.BadRequest, e.message ?: "Invalid request data")
            }
        }

        // อ่าน (READ) หมวดหมู่ทั้งหมด
        get {
            val categories = categoryService.getAllCategories()
            if (categories.isNotEmpty()) {
                call.respond(categories)
            } else {
                call.respond(HttpStatusCode.NoContent)
            }
        }

        // อ่าน (READ) หมวดหมู่เดียวด้วย ID
        get("/{id}") {
            val id = call.parameters["id"] ?: return@get call.respondText(
                "Missing ID",
                status = HttpStatusCode.BadRequest
            )
            val category = categoryService.getCategoryById(id)
            if (category != null) {
                call.respond(category)
            } else {
                call.respond(HttpStatusCode.NotFound)
            }
        }

        // อัปเดต (UPDATE) หมวดหมู่
        put("/{id}") {
            val id = call.parameters["id"] ?: return@put call.respondText(
                "Missing ID",
                status = HttpStatusCode.BadRequest
            )
            val categoryRequest = call.receive<CategoryRequest>()
            try {
                val updated = categoryService.updateCategory(id, categoryRequest)
                if (updated) {
                    call.respondText("Category updated successfully", status = HttpStatusCode.OK)
                } else {
                    call.respond(HttpStatusCode.NotFound, "Category not found or could not be updated")
                }
            } catch (e: IllegalArgumentException) {
                call.respond(HttpStatusCode.BadRequest, e.message ?: "Invalid request data")
            }
        }

        // ลบ (DELETE) หมวดหมู่
        delete("/{id}") {
            val id = call.parameters["id"] ?: return@delete call.respondText(
                "Missing ID",
                status = HttpStatusCode.BadRequest
            )
            val deleted = categoryService.deleteCategory(id)
            if (deleted) {
                call.respondText("Category deleted successfully", status = HttpStatusCode.NoContent)
            } else {
                call.respond(HttpStatusCode.NotFound, "Category not found")
            }
        }
    }
}