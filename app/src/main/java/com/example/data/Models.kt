package com.example.data

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class User(
    val id: String,
    val username: String,
    val email: String,
    val accountType: String, // STUDENT, TEACHER, ADMIN
    val gradeLevel: String, // e.g. "الصف الثالث الثانوي"
    val subjectInterests: String = "الكل"
)

@JsonClass(generateAdapter = true)
data class QuizQuestion(
    val text: String,
    val options: List<String>,
    val correctOptionIndex: Int,
    val explanation: String,
    var selectedOptionIndex: Int = -1 // -1 means unanswered
)

@JsonClass(generateAdapter = true)
data class StudyScriptSlide(
    val title: String,
    val narration: String,
    val slideVisuals: String
)

@JsonClass(generateAdapter = true)
data class MindMapNode(
    val id: String,
    val label: String,
    val parentId: String?,
    val colorHex: String = "#0288D1",
    val x: Float = 0f,
    val y: Float = 0f
)

@JsonClass(generateAdapter = true)
data class LearningVideo(
    val id: String,
    val materialId: String,
    val title: String,
    val script: List<StudyScriptSlide>,
    val durationType: String, // SHORT, MEDIUM, DETAILED
    val voiceType: String, // MALE, FEMALE
    val timestamp: Long = System.currentTimeMillis()
)
