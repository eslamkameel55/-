package com.example.data

import android.content.Context
import androidx.room.*
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.flow.Flow

// --- Room Entities ---

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey val email: String,
    val username: String,
    val passwordHash: String,
    val accountType: String, // STUDENT, TEACHER, ADMIN
    val gradeLevel: String, // e.g. "الصف الثالث الثانوي"
    val subjectInterests: String = "الكل"
)

@Entity(tableName = "study_materials")
data class StudyMaterialEntity(
    @PrimaryKey val id: String,
    val title: String,
    val sourceText: String,
    val summary: String,
    val uploadType: String, // PDF, PPT, WORD, IMAGE, AUDIO, LINK
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "flashcards")
data class FlashcardEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val materialId: String,
    val front: String,
    val back: String,
    val isKnown: Boolean = false,
    val masteryScore: Int = 0 // scale of 1-5
)

@Entity(tableName = "quizzes")
data class LocalQuizEntity(
    @PrimaryKey val id: String,
    val materialId: String,
    val title: String,
    val score: Int,
    val totalQuestions: Int,
    val questionsJson: String, // List<QuizQuestion> serialized
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "videos")
data class LocalVideoEntity(
    @PrimaryKey val id: String,
    val materialId: String,
    val title: String,
    val scriptJson: String, // List<StudyScriptSlide> serialized
    val durationType: String, // SHORT, MEDIUM, DETAILED
    val voiceType: String, // MALE, FEMALE
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "mind_maps")
data class LocalMindMapEntity(
    @PrimaryKey val id: String,
    val materialId: String,
    val title: String,
    val nodesJson: String, // List<MindMapNode> serialized
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "study_plans")
data class StudyPlanEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val dayOfWeek: String, // "سبت", "أحد", "إثنين", etc.
    val durationMinutes: Int = 60,
    val isCompleted: Boolean = false,
    val timeLabel: String = "10:00 ص"
)

@Entity(tableName = "announcements")
data class TeacherAnnouncementEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val content: String,
    val senderName: String,
    val subject: String,
    val timestamp: Long = System.currentTimeMillis()
)

// --- Type Converters ---

class AppTypeConverters {
    private val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

    @TypeConverter
    fun fromStringList(value: List<String>?): String? {
        if (value == null) return null
        val type = Types.newParameterizedType(List::class.java, String::class.java)
        val adapter = moshi.adapter<List<String>>(type)
        return adapter.toJson(value)
    }

    @TypeConverter
    fun toStringList(value: String?): List<String>? {
        if (value == null) return null
        val type = Types.newParameterizedType(List::class.java, String::class.java)
        val adapter = moshi.adapter<List<String>>(type)
        return adapter.fromJson(value)
    }
}

// --- Combined DAO ---

@Dao
interface AppDao {
    // Users
    @Query("SELECT * FROM users WHERE email = :email LIMIT 1")
    suspend fun getUserByEmail(email: String): UserEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserEntity)

    @Query("SELECT * FROM users")
    fun getAllUsers(): Flow<List<UserEntity>>

    // Study Materials
    @Query("SELECT * FROM study_materials ORDER BY timestamp DESC")
    fun getAllStudyMaterials(): Flow<List<StudyMaterialEntity>>

    @Query("SELECT * FROM study_materials WHERE id = :id LIMIT 1")
    suspend fun getMaterialById(id: String): StudyMaterialEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMaterial(material: StudyMaterialEntity)

    @Query("DELETE FROM study_materials WHERE id = :id")
    suspend fun deleteMaterial(id: String)

    // Flashcards
    @Query("SELECT * FROM flashcards WHERE materialId = :materialId")
    fun getFlashcardsForMaterial(materialId: String): Flow<List<FlashcardEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFlashcard(flashcard: FlashcardEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFlashcards(flashcards: List<FlashcardEntity>)

    @Query("UPDATE flashcards SET isKnown = :isKnown, masteryScore = :score WHERE id = :id")
    suspend fun updateFlashcardProgress(id: Int, isKnown: Boolean, score: Int)

    // Quizzes
    @Query("SELECT * FROM quizzes ORDER BY timestamp DESC")
    fun getAllQuizzes(): Flow<List<LocalQuizEntity>>

    @Query("SELECT * FROM quizzes WHERE materialId = :materialId")
    fun getQuizzesForMaterial(materialId: String): Flow<List<LocalQuizEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuiz(quiz: LocalQuizEntity)

    // Videos
    @Query("SELECT * FROM videos ORDER BY timestamp DESC")
    fun getAllVideos(): Flow<List<LocalVideoEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVideo(video: LocalVideoEntity)

    // Mind Maps
    @Query("SELECT * FROM mind_maps WHERE materialId = :materialId LIMIT 1")
    suspend fun getMindMapForMaterial(materialId: String): LocalMindMapEntity?

    @Query("SELECT * FROM mind_maps ORDER BY timestamp DESC")
    fun getAllMindMaps(): Flow<List<LocalMindMapEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMindMap(mindMap: LocalMindMapEntity)

    // Study plans
    @Query("SELECT * FROM study_plans ORDER BY id ASC")
    fun getAllStudyPlans(): Flow<List<StudyPlanEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStudyPlan(plan: StudyPlanEntity)

    @Query("UPDATE study_plans SET isCompleted = :isCompleted WHERE id = :id")
    suspend fun updateStudyPlanStatus(id: Int, isCompleted: Boolean)

    @Query("DELETE FROM study_plans WHERE id = :id")
    suspend fun deleteStudyPlan(id: Int)

    // Announcements
    @Query("SELECT * FROM announcements ORDER BY timestamp DESC")
    fun getAllAnnouncements(): Flow<List<TeacherAnnouncementEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAnnouncement(announcement: TeacherAnnouncementEntity)
}

// --- App Database ---

@Database(
    entities = [
        UserEntity::class,
        StudyMaterialEntity::class,
        FlashcardEntity::class,
        LocalQuizEntity::class,
        LocalVideoEntity::class,
        LocalMindMapEntity::class,
        StudyPlanEntity::class,
        TeacherAnnouncementEntity::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(AppTypeConverters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun appDao(): AppDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "study_smart_db"
                )
                .fallbackToDestructiveMigration()
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
