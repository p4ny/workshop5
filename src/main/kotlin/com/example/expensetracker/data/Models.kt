package com.example.expensetracker.data

import kotlinx.serialization.Serializable
import java.time.LocalDate

@Serializable
data class Transaction(
    val id: String, // ID ควรเป็น val และไม่สามารถเป็น null ได้เมื่อถูกสร้างแล้ว
    val description: String,
    val amount: Double,
    val type: TransactionType,
    val date: String, // หรือ LocalDate หากคุณใช้ serializer ที่กำหนดเองสำหรับมัน
    val categoryId: String // ID ของหมวดหมู่ที่เกี่ยวข้อง
)

@Serializable
enum class TransactionType {
    INCOME,
    EXPENSE
}

@Serializable
data class Category(
    val id: String,
    val name: String
)

// DTOs สำหรับ request bodies (เมื่อรับข้อมูลจาก client)
@Serializable
data class TransactionRequest(
    val description: String,
    val amount: Double,
    val type: TransactionType,
    val date: String,
    val categoryId: String
)

@Serializable
data class CategoryRequest(
    val name: String
)

// สำหรับ Output (ถ้าจำเป็นต้องซ่อนบางฟิลด์ หรือรวมข้อมูล)
@Serializable
data class TransactionResponse(
    val id: String,
    val description: String,
    val amount: Double,
    val type: TransactionType,
    val date: String,
    val category: Category // สามารถรวมข้อมูลหมวดหมู่ได้ที่นี่
)

@Serializable
data class ReportSummary(
    val month: Int,
    val year: Int,
    val totalIncome: Double,
    val totalExpense: Double,
    val netBalance: Double,
    val expenseByCategory: Map<String, Double> // Category Name to Amount
)