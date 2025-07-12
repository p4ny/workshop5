package com.example.expensetracker.routes

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import com.example.expensetracker.services.ReportService // Import service

fun Route.reportRouting(reportService: ReportService) {
    route("/reports") {
        get("/monthly") {
            val year = call.request.queryParameters["year"]?.toIntOrNull()
            val month = call.request.queryParameters["month"]?.toIntOrNull()

            if (year == null || month == null) {
                return@get call.respondText("Missing 'year' or 'month' query parameters", status = HttpStatusCode.BadRequest)
            }
            if (month < 1 || month > 12 || year < 1900) { // เพิ่มการตรวจสอบความถูกต้องของ input
                return@get call.respondText("Invalid 'month' or 'year' value", status = HttpStatusCode.BadRequest)
            }

            val monthlySummary = reportService.getMonthlySummary(year, month)
            call.respond(monthlySummary)
        }
    }
}