package com.example.expensetracker

import io.ktor.server.application.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.plugins.contentnegotiation.*
import com.example.expensetracker.plugins.configureRouting
import com.example.expensetracker.data.InMemoryTransactionRepository // Import repository
import com.example.expensetracker.services.TransactionService // Import services
import com.example.expensetracker.services.CategoryService
import com.example.expensetracker.services.ReportService

// สร้าง instance ของ Repository และ Services ในระดับโมดูล
// ในโปรเจกต์จริง คุณอาจใช้ Dependency Injection Framework (เช่น Koin, Kodein) ที่นี่
val repository = InMemoryTransactionRepository() // <-- Repository instance
val transactionService = TransactionService(repository) // <-- Service instance
val categoryService = CategoryService(repository)
val reportService = ReportService(repository)

fun Application.module() {
    // ติดตั้ง ContentNegotiation เพื่อจัดการ JSON
    install(ContentNegotiation) {
        json()
    }

    // กำหนดเส้นทางของคุณ โดยส่ง service instances เข้าไป
    configureRouting(transactionService, categoryService, reportService)
}