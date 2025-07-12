package com.example.expensetracker.services

import com.example.expensetracker.data.*
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

class ReportService(
    private val transactionRepository: InMemoryTransactionRepository
) {

    // สร้างรายงานสรุปรายเดือน
    suspend fun getMonthlySummary(year: Int, month: Int): ReportSummary {
        val allTransactions = transactionRepository.getAllTransactions()
        val categories = transactionRepository.getAllCategories().associateBy { it.id }

        var totalIncome = 0.0
        var totalExpense = 0.0
        val expenseByCategory = mutableMapOf<String, Double>()

        for (transaction in allTransactions) {
            try {
                val transactionDate = LocalDate.parse(transaction.date)
                if (transactionDate.year == year && transactionDate.monthValue == month) {
                    when (transaction.type) {
                        TransactionType.INCOME -> totalIncome += transaction.amount
                        TransactionType.EXPENSE -> {
                            totalExpense += transaction.amount
                            val categoryName = categories[transaction.categoryId]?.name ?: "Unknown"
                            expenseByCategory[categoryName] = expenseByCategory.getOrDefault(categoryName, 0.0) + transaction.amount
                        }
                    }
                }
            } catch (e: DateTimeParseException) {
                // Log warning or skip invalid date transactions
                println("Warning: Invalid date format for transaction ID ${transaction.id}: ${transaction.date}")
            }
        }

        val netBalance = totalIncome - totalExpense

        return ReportSummary(
            month = month,
            year = year,
            totalIncome = totalIncome,
            totalExpense = totalExpense,
            netBalance = netBalance,
            expenseByCategory = expenseByCategory
        )
    }

    // คุณสามารถเพิ่มฟังก์ชันสร้างรายงานอื่นๆ ได้ที่นี่
    // เช่น getAnnualSummary(year: Int), getExpenseBreakdown(startDate: String, endDate: String)
}