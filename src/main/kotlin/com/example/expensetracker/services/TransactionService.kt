package com.example.expensetracker.services

import com.example.expensetracker.data.* // Import all data classes
import java.time.LocalDate
import java.time.format.DateTimeParseException
import java.util.UUID

class TransactionService(private val transactionRepository: InMemoryTransactionRepository) {

    // สร้างธุรกรรมใหม่
    suspend fun createTransaction(request: TransactionRequest): Transaction {
        // ตรรกะการตรวจสอบความถูกต้อง (Business Logic)
        if (request.description.isBlank()) {
            throw IllegalArgumentException("Description cannot be empty.")
        }
        if (request.amount <= 0) {
            throw IllegalArgumentException("Amount must be positive.")
        }
        if (transactionRepository.getCategoryById(request.categoryId) == null) { // ตรวจสอบว่า CategoryId มีอยู่จริง
            throw IllegalArgumentException("Category with ID ${request.categoryId} does not exist.")
        }
        try {
            LocalDate.parse(request.date) // ตรวจสอบรูปแบบวันที่
        } catch (e: DateTimeParseException) {
            throw IllegalArgumentException("Invalid date format. Expected YYYY-MM-DD.")
        }

        // สร้าง Transaction object และเรียกใช้ Repository
        val newTransaction = Transaction(
            id = UUID.randomUUID().toString(), // ID จะถูกสร้างโดย Repository เมื่อบันทึกจริง
            description = request.description,
            amount = request.amount,
            type = request.type,
            date = request.date,
            categoryId = request.categoryId
        )
        return transactionRepository.createTransaction(newTransaction)
    }

    // ดึงธุรกรรมทั้งหมด
    suspend fun getAllTransactions(): List<Transaction> {
        return transactionRepository.getAllTransactions()
    }

    // ดึงธุรกรรมด้วย ID
    suspend fun getTransactionById(id: String): Transaction? {
        return transactionRepository.getTransactionById(id)
    }

    // อัปเดตธุรกรรม
    suspend fun updateTransaction(id: String, request: TransactionRequest): Boolean {
        // ตรรกะการตรวจสอบความถูกต้อง
        if (request.description.isBlank()) {
            throw IllegalArgumentException("Description cannot be empty.")
        }
        if (request.amount <= 0) {
            throw IllegalArgumentException("Amount must be positive.")
        }
        if (transactionRepository.getCategoryById(request.categoryId) == null) {
            throw IllegalArgumentException("Category with ID ${request.categoryId} does not exist.")
        }
        try {
            LocalDate.parse(request.date)
        } catch (e: DateTimeParseException) {
            throw IllegalArgumentException("Invalid date format. Expected YYYY-MM-DD.")
        }

        val existingTransaction = transactionRepository.getTransactionById(id)
        if (existingTransaction == null) {
            return false // ไม่พบธุรกรรมที่จะอัปเดต
        }

        val updatedTransaction = existingTransaction.copy(
            description = request.description,
            amount = request.amount,
            type = request.type,
            date = request.date,
            categoryId = request.categoryId
        )
        return transactionRepository.updateTransaction(id, updatedTransaction)
    }

    // ลบธุรกรรม
    suspend fun deleteTransaction(id: String): Boolean {
        return transactionRepository.deleteTransaction(id)
    }

    // ฟังก์ชันเพิ่มเติมสำหรับการแปลง Transaction ไปเป็น TransactionResponse (รวมข้อมูล Category)
    suspend fun getTransactionResponseById(id: String): TransactionResponse? {
        val transaction = getTransactionById(id) ?: return null
        val category = transactionRepository.getCategoryById(transaction.categoryId)
        return TransactionResponse(
            id = transaction.id,
            description = transaction.description,
            amount = transaction.amount,
            type = transaction.type,
            date = transaction.date,
            category = category ?: Category(transaction.categoryId, "Unknown Category") // กรณีไม่พบหมวดหมู่
        )
    }

    suspend fun getAllTransactionResponses(): List<TransactionResponse> {
        val transactions = getAllTransactions()
        val categories = transactionRepository.getAllCategories().associateBy { it.id } // สร้าง Map เพื่อค้นหาได้เร็ว

        return transactions.map { transaction ->
            val category = categories[transaction.categoryId]
            TransactionResponse(
                id = transaction.id,
                description = transaction.description,
                amount = transaction.amount,
                type = transaction.type,
                date = transaction.date,
                category = category ?: Category(transaction.categoryId, "Unknown Category")
            )
        }
    }
}