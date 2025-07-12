package com.example.expensetracker.routes

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import com.example.expensetracker.data.TransactionRequest
import com.example.expensetracker.services.TransactionService // Import service

// เปลี่ยน route function ให้รับ service instance
fun Route.transactionRouting(transactionService: TransactionService) {
    route("/transactions") {
        // สร้าง (CREATE) ธุรกรรมใหม่
        post {
            val transactionRequest = call.receive<TransactionRequest>()
            try {
                val newTransaction = transactionService.createTransaction(transactionRequest)
                call.respond(HttpStatusCode.Created, newTransaction)
            } catch (e: IllegalArgumentException) {
                call.respond(HttpStatusCode.BadRequest, e.message ?: "Invalid request data")
            }
        }

        // อ่าน (READ) ธุรกรรมทั้งหมด
        get {
            val transactions = transactionService.getAllTransactionResponses() // ใช้ Response DTO
            if (transactions.isNotEmpty()) {
                call.respond(transactions)
            } else {
                call.respond(HttpStatusCode.NoContent) // ไม่มีข้อมูล
            }
        }

        // อ่าน (READ) ธุรกรรมเดียวด้วย ID
        get("/{id}") {
            val id = call.parameters["id"] ?: return@get call.respondText(
                "Missing ID",
                status = HttpStatusCode.BadRequest
            )
            val transaction = transactionService.getTransactionResponseById(id)
            if (transaction != null) {
                call.respond(transaction)
            } else {
                call.respond(HttpStatusCode.NotFound)
            }
        }

        // อัปเดต (UPDATE) ธุรกรรม
        put("/{id}") {
            val id = call.parameters["id"] ?: return@put call.respondText(
                "Missing ID",
                status = HttpStatusCode.BadRequest
            )
            val transactionRequest = call.receive<TransactionRequest>()
            try {
                val updated = transactionService.updateTransaction(id, transactionRequest)
                if (updated) {
                    call.respondText("Transaction updated successfully", status = HttpStatusCode.OK)
                } else {
                    call.respond(HttpStatusCode.NotFound, "Transaction not found or could not be updated")
                }
            } catch (e: IllegalArgumentException) {
                call.respond(HttpStatusCode.BadRequest, e.message ?: "Invalid request data")
            }
        }

        // ลบ (DELETE) ธุรกรรม
        delete("/{id}") {
            val id = call.parameters["id"] ?: return@delete call.respondText(
                "Missing ID",
                status = HttpStatusCode.BadRequest
            )
            val deleted = transactionService.deleteTransaction(id)
            if (deleted) {
                call.respondText("Transaction deleted successfully", status = HttpStatusCode.NoContent)
            } else {
                call.respond(HttpStatusCode.NotFound, "Transaction not found")
            }
        }
    }
}