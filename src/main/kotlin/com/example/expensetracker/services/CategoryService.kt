package com.example.expensetracker.services

import com.example.expensetracker.data.Category
import com.example.expensetracker.data.CategoryRequest
import com.example.expensetracker.data.InMemoryTransactionRepository // ใช้ Repository ตัวเดียวกัน

class CategoryService(private val categoryRepository: InMemoryTransactionRepository) {

    // สร้างหมวดหมู่ใหม่
    suspend fun createCategory(request: CategoryRequest): Category {
        // ตรรกะการตรวจสอบความถูกต้อง
        if (request.name.isBlank()) {
            throw IllegalArgumentException("Category name cannot be empty.")
        }
        // ในโปรเจกต์จริง คุณอาจจะตรวจสอบว่าชื่อหมวดหมู่ซ้ำกันหรือไม่
        // val existingCategory = categoryRepository.findCategoryByName(request.name)
        // if (existingCategory != null) {
        //     throw IllegalArgumentException("Category with name '${request.name}' already exists.")
        // }

        val newCategory = Category(id = "", name = request.name) // ID จะถูกสร้างโดย Repository
        return categoryRepository.createCategory(newCategory)
    }

    // ดึงหมวดหมู่ทั้งหมด
    suspend fun getAllCategories(): List<Category> {
        return categoryRepository.getAllCategories()
    }

    // ดึงหมวดหมู่ด้วย ID
    suspend fun getCategoryById(id: String): Category? {
        return categoryRepository.getCategoryById(id)
    }

    // อัปเดตหมวดหมู่
    suspend fun updateCategory(id: String, request: CategoryRequest): Boolean {
        if (request.name.isBlank()) {
            throw IllegalArgumentException("Category name cannot be empty.")
        }

        val existingCategory = categoryRepository.getCategoryById(id)
        if (existingCategory == null) {
            return false // ไม่พบหมวดหมู่ที่จะอัปเดต
        }

        val updatedCategory = existingCategory.copy(name = request.name)
        return categoryRepository.updateCategory(id, updatedCategory)
    }

    // ลบหมวดหมู่
    suspend fun deleteCategory(id: String): Boolean {
        // ตรรกะทางธุรกิจเพิ่มเติม: อาจจะตรวจสอบว่ามีธุรกรรมใดๆ ที่ใช้หมวดหมู่นี้อยู่หรือไม่ก่อนลบ
        // val transactionsUsingCategory = transactionService.getTransactionsByCategoryId(id)
        // if (transactionsUsingCategory.isNotEmpty()) {
        //     throw IllegalStateException("Cannot delete category as there are transactions associated with it.")
        // }
        return categoryRepository.deleteCategory(id)
    }
}