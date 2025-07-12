package com.example.expensetracker.data

import java.util.UUID

// Interface สำหรับ Transaction Repository
interface TransactionRepository {
    suspend fun createTransaction(transaction: Transaction): Transaction
    suspend fun getAllTransactions(): List<Transaction>
    suspend fun getTransactionById(id: String): Transaction?
    suspend fun updateTransaction(id: String, updatedTransaction: Transaction): Boolean
    suspend fun deleteTransaction(id: String): Boolean
}

// การใช้งาน Transaction Repository แบบจำลอง (ในหน่วยความจำ)
class InMemoryTransactionRepository : TransactionRepository {
    private val transactions = mutableListOf<Transaction>()
    private val categories = mutableListOf( // เพิ่มหมวดหมู่เริ่มต้น
        Category(id = UUID.randomUUID().toString(), name = "Food"),
        Category(id = UUID.randomUUID().toString(), name = "Transport"),
        Category(id = UUID.randomUUID().toString(), name = "Salary"),
        Category(id = UUID.randomUUID().toString(), name = "Utilities")
    )

    override suspend fun createTransaction(transaction: Transaction): Transaction {
        val newTransaction = transaction.copy(id = UUID.randomUUID().toString())
        transactions.add(newTransaction)
        return newTransaction
    }

    override suspend fun getAllTransactions(): List<Transaction> {
        return transactions.toList()
    }

    override suspend fun getTransactionById(id: String): Transaction? {
        return transactions.find { it.id == id }
    }

    override suspend fun updateTransaction(id: String, updatedTransaction: Transaction): Boolean {
        val index = transactions.indexOfFirst { it.id == id }
        return if (index != -1) {
            transactions[index] = updatedTransaction.copy(id = id) // ตรวจสอบให้แน่ใจว่า ID ไม่เปลี่ยน
            true
        } else {
            false
        }
    }

    override suspend fun deleteTransaction(id: String): Boolean {
        return transactions.removeIf { it.id == id }
    }

    // ฟังก์ชันเพิ่มเติมสำหรับ Repository เพื่อให้ CategoryService เรียกใช้ได้
    suspend fun getAllCategories(): List<Category> {
        return categories.toList()
    }

    suspend fun getCategoryById(id: String): Category? {
        return categories.find { it.id == id }
    }

    suspend fun createCategory(category: Category): Category {
        val newCategory = category.copy(id = UUID.randomUUID().toString())
        categories.add(newCategory)
        return newCategory
    }

    suspend fun updateCategory(id: String, updatedCategory: Category): Boolean {
        val index = categories.indexOfFirst { it.id == id }
        return if (index != -1) {
            categories[index] = updatedCategory.copy(id = id)
            true
        } else {
            false
        }
    }

    suspend fun deleteCategory(id: String): Boolean {
        return categories.removeIf { it.id == id }
    }
}