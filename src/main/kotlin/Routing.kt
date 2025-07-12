package com.example.expensetracker.plugins

import io.ktor.server.application.*
import io.ktor.server.routing.*
import com.example.expensetracker.routes.transactionRouting
import com.example.expensetracker.routes.categoryRouting
import com.example.expensetracker.routes.reportRouting
import com.example.expensetracker.services.TransactionService
import com.example.expensetracker.services.CategoryService
import com.example.expensetracker.services.ReportService
import io.ktor.server.response.respondText

// เปลี่ยน configureRouting ให้รับ service instances
fun Application.configureRouting(
    transactionService: TransactionService,
    categoryService: CategoryService,
    reportService: ReportService
) {
    routing {
        route("/api/v1") {
            transactionRouting(transactionService) // ส่ง service ให้ route function
            categoryRouting(categoryService)
            reportRouting(reportService)
        }

        get("/") {
            call.respondText("Expense Tracker API is running!")
        }
    }
}