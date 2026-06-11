package com.example.ui

import android.app.Application
import android.speech.tts.TextToSpeech
import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.*
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Locale
import java.util.UUID

sealed interface Screen {
    object Splash : Screen
    object Welcome : Screen
    object Login : Screen
    object SignUp : Screen
    object AccountTypeSelector : Screen
    object Home : Screen
    object Subjects : Screen
    object Lessons : Screen
    object FileUpload : Screen
    object PdfSummary : Screen
    object VideoSummary : Screen
    object PdfToVideo : Screen
    object AiAssistant : Screen
    object Exams : Screen
    object Results : Screen
    object StudyPlan : Screen
    object Flashcards : Screen
    object MindMaps : Screen
    object SavedFiles : Screen
    object SavedVideos : Screen
    object TeacherDashboard : Screen
    object AdminDashboard : Screen
    object Subscriptions : Screen
    object Profile : Screen
    object Settings : Screen
}

data class ChatMessage(
    val sender: String, // "USER" or "AI"
    val text: String,
    val isImageQuestion: Boolean = false,
    val timestamp: Long = System.currentTimeMillis()
)

class AppViewModel(application: Application) : AndroidViewModel(application), TextToSpeech.OnInitListener {

    private val db = AppDatabase.getDatabase(application)
    private val dao = db.appDao()
    private val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

    // --- Navigation Backstack ---
    val navigationStack = mutableStateListOf<Screen>(Screen.Splash)

    fun navigateTo(screen: Screen) {
        if (navigationStack.lastOrNull() != screen) {
            navigationStack.add(screen)
        }
    }

    fun navigateBack() {
        if (navigationStack.size > 1) {
            navigationStack.removeLast()
        }
    }

    // --- TTS (Text To Speech) engine ---
    private var tts: TextToSpeech? = null
    val isTtsReady = MutableStateFlow(false)
    val isSpeaking = MutableStateFlow(false)

    init {
        tts = TextToSpeech(application, this)
        seedInitialAccountsAndData()
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            // Prefer Arabic if available, fallback to English
            val result = tts?.setLanguage(Locale("ar"))
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                tts?.setLanguage(Locale.ENGLISH)
            }
            isTtsReady.value = true
        }
    }

    fun speak(text: String) {
        if (isTtsReady.value) {
            tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, "StudySmartTTS")
            isSpeaking.value = true
        }
    }

    fun stopSpeaking() {
        tts?.stop()
        isSpeaking.value = false
    }

    override fun onCleared() {
        super.onCleared()
        tts?.shutdown()
    }

    // --- Seeding Data ---
    private fun seedInitialAccountsAndData() {
        viewModelScope.launch(Dispatchers.IO) {
            // Seed Default Accounts if count is zero
            if (dao.getUserByEmail("student@study.com") == null) {
                dao.insertUser(UserEntity("student@study.com", "أحمد الطالب", "123456", "STUDENT", "الصف الثالث الثانوي"))
                dao.insertUser(UserEntity("teacher@study.com", "أ. محمد المعلم", "123456", "TEACHER", "المرحلة الثانوية", "الفيزياء والرياضيات"))
                dao.insertUser(UserEntity("admin@study.com", "مدير النظام", "123456", "ADMIN", "برمجة السيرفرات"))
            }

            // Seed a sample study document if empty
            dao.getAllStudyMaterials().collect { materials ->
                if (materials.isEmpty()) {
                    viewModelScope.launch(Dispatchers.IO) {
                        val sampleText = """
                            تركيب الخلية الحية ووظائفها الأساسية:
                            تعتبر الخلية هي وحدة البناء والوظيفة لجميع الكائنات الحية. تنقسم الخلايا إلى خلايا بدائية النوى (مثل البكتيريا) وخلايا حقيقية النوى (مثل النباتات والحيوانات).
                            تحتوي الخلية حقيقية النواة على المكونات التالية:
                            1. الغشاء البلازمي: يحيط بالخلية ويتحكم في دخول وخروج المواد.
                            2. النواة: مركز التحكم بالخلية وتحتوي على المادة الوراثية DNA.
                            3. السيتوبلازم: سائل تسبح فيه العضيات.
                            4. الميتوكوندريا: مصانع إنتاج الطاقة في الخلية (ATP).
                            5. الريبوسومات: موقع تصنيع البروتينات الهام لنمو الخلايا وتجددها.
                        """.trimIndent()
                        
                        val materialId = "sample-cell-biology"
                        dao.insertMaterial(
                            StudyMaterialEntity(
                                id = materialId,
                                title = "الخلية الحية ووظائفها",
                                sourceText = sampleText,
                                summary = "ملخص تركيب الخلية: الخلايا نوعان: بدائية النواة وحقيقية النواة. والمكونات الكبرى تشمل الغشاء البلازمي لحماية الخلية، والنواة لحفظ الحمض النووي डीएनए، والميتوكوندريا لتوليد الطاقة وحرق السكريات.",
                                uploadType = "PDF"
                            )
                        )

                        // Seed study plan
                        val planDays = listOf("السبت", "الأحد", "الإثنين", "الثلاثاء", "الأربعاء", "الخميس", "الجمعة")
                        var i = 1
                        for (day in planDays) {
                            dao.insertStudyPlan(StudyPlanEntity(title = "مراجعة علم الأحياء والخلية", dayOfWeek = day, durationMinutes = 45, isCompleted = i % 3 == 0, timeLabel = "08:30 م"))
                            i++
                        }

                        // Seed Flashcards
                        dao.insertFlashcards(
                            listOf(
                                FlashcardEntity(materialId = materialId, front = "الميتوكوندريا", back = "عضية مسؤولة عن إنتاج الخلايا للطاقة (ATP) وحرق الأكسجين والمواد الغذائية", masteryScore = 4),
                                FlashcardEntity(materialId = materialId, front = "الغشاء البلازمي", back = "غشاء شبه منفذ يحيط بسيتوبلازم الخلية ويحميها ويتحكم بمرور المواد والمذيبات كالأكسجين والماء", masteryScore = 5),
                                FlashcardEntity(materialId = materialId, front = "النواة", back = "غرفة القيادة المركزية التي تضم كروموسومات الخلية والحمض النووي وتنظم عمليات الانقسام الخلوي والتزاوج", masteryScore = 3)
                            )
                        )

                        // Seed a sample quiz
                        val quizQuestions = listOf(
                            QuizQuestion("ما هي العضية المسؤولة عن توفير الطاقة (ATP) بالخلية؟", listOf("النواة", "الميتوكوندريا", "الغشاء البلازمي", "الريبوسومات"), 1, "الميتوكوندريا هي مصانع الطاقة للخلية حيث يتم التنفس الخلوي لإنتاج جزئيات الـ ATP."),
                            QuizQuestion("أي المكونات التالية يحوي المادة الوراثية (DNA)؟", listOf("النواة", "الميتوكوندريا", "السيتوبلازم", "الريبوسومات"), 0, "النواة هي مستودع المادة الوراثية والجينات التي تنظم عمل الخلية بالكامل.")
                        )
                        val type = Types.newParameterizedType(List::class.java, QuizQuestion::class.java)
                        val questionsJson = moshi.adapter<List<QuizQuestion>>(type).toJson(quizQuestions)
                        dao.insertQuiz(
                            LocalQuizEntity(
                                id = "sample-quiz-1",
                                materialId = materialId,
                                title = "اختبار الخلية الحية التمهيدي",
                                score = 2,
                                totalQuestions = 2,
                                questionsJson = questionsJson
                            )
                        )
                    }
                }
            }
        }
    }

    // --- Auth Management ---
    val loggedUser = MutableStateFlow<User?>(
        User(
            id = "student@study.com",
            username = "أحمد الطالب",
            email = "student@study.com",
            accountType = "STUDENT",
            gradeLevel = "الصف الثالث الثانوي",
            subjectInterests = "الكل"
        )
    )
    val loginEmail = mutableStateOf("")
    val loginPassword = mutableStateOf("")
    val isLoginError = mutableStateOf(false)

    // Registration properties
    val regUsername = mutableStateOf("")
    val regEmail = mutableStateOf("")
    val regPassword = mutableStateOf("")
    val regGrade = mutableStateOf("الصف الثالث الثانوي")
    val regAccountType = mutableStateOf("STUDENT") // STUDENT, TEACHER, ADMIN

    fun performLogin(onSuccess: () -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            val email = loginEmail.value.lowercase().trim()
            val pass = loginPassword.value
            val entity = dao.getUserByEmail(email)
            if (entity != null && entity.passwordHash == pass) {
                val user = User(entity.email, entity.username, entity.email, entity.accountType, entity.gradeLevel, entity.subjectInterests)
                loggedUser.value = user
                isLoginError.value = false
                
                // Clear fields
                loginEmail.value = ""
                loginPassword.value = ""

                // Set selected grade for student setup
                studentGradeSelection.value = user.gradeLevel

                viewModelScope.launch(Dispatchers.Main) {
                    onSuccess()
                }
            } else {
                isLoginError.value = true
            }
        }
    }

    fun performRegistration(onSuccess: () -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            val email = regEmail.value.lowercase().trim()
            val name = regUsername.value.trim()
            val pass = regPassword.value
            
            if (email.isNotEmpty() && name.isNotEmpty() && pass.isNotEmpty()) {
                val newUser = UserEntity(
                    email = email,
                    username = name,
                    passwordHash = pass,
                    accountType = regAccountType.value,
                    gradeLevel = regGrade.value
                )
                dao.insertUser(newUser)

                // Log them in
                val user = User(newUser.email, newUser.username, newUser.email, newUser.accountType, newUser.gradeLevel, newUser.subjectInterests)
                loggedUser.value = user

                // Clear fields
                regEmail.value = ""
                regUsername.value = ""
                regPassword.value = ""

                viewModelScope.launch(Dispatchers.Main) {
                    onSuccess()
                }
            }
        }
    }

    fun logout() {
        stopSpeaking()
        navigationStack.clear()
        navigationStack.add(Screen.Home)
    }

    // --- Student Grade / Stage ---
    val studentGradeSelection = mutableStateOf("الصف الثالث الثانوي")
    val studentSelectedSubject = mutableStateOf("أحياء")

    // --- AI Direct Assistant (Chat) ---
    val chatbotQuery = mutableStateOf("")
    val chatHistory = mutableStateListOf<ChatMessage>().apply {
        add(ChatMessage("AI", "مرحباً بك يا بطل! أنا مساعدك التعليمي الذكي. يمكنك شرح أي درس لي، وسؤالي عن أي شيء في الرياضيات، العلوم، اللغات، أو رفع صورة سؤال لحله فوراً خطوة بخطوة 🧠"))
    }
    val isChatLoading = mutableStateOf(false)

    fun sendChatQuery(imageBytes: ByteArray? = null) {
        val text = chatbotQuery.value.trim()
        if (text.isEmpty() && imageBytes == null) return

        val userMsg = ChatMessage("USER", text, isImageQuestion = imageBytes != null)
        chatHistory.add(userMsg)
        chatbotQuery.value = ""
        isChatLoading.value = true

        viewModelScope.launch(Dispatchers.IO) {
            var promptContext = text
            if (activeMaterial.value != null) {
                promptContext += "\n\nسياق الدرس النشط الذي يدرسه الطالب حالياً:\n${activeMaterial.value?.sourceText}"
            }

            val systemInstrInput = """
                أنت مساعد ذكي ومتفوق لشرح المواد الأكاديمية (مثل العلوم والرياضيات واللغات) بطريقة تفصيلية خطوة بخطوة مخصصة للطلاب.
                بسط المذاكرة ولا تعطي إجابات مقتضبة بل كن صبوراً ومعلماً مبدعاً مع إدخال تلميحات تشجيعية للطلاب باللغة العربية والإنجليزية.
                إذا سألك الطالب عن مسألة رياضية، قم بتحليلها وحلها خطوة بخطوة مع شرح القاعدة المستند عليها بوضوح.
            """.trimIndent()

            val response = GeminiService.generateContent(
                prompt = promptContext,
                systemInstruction = systemInstrInput
            )

            viewModelScope.launch(Dispatchers.Main) {
                isChatLoading.value = false
                val cleanResponse = if (response == "API_KEY_MISSING") {
                    // Try to generate an incredibly high-quality localized mock answer so the app doesn't feel broken
                    val q = text.lowercase()
                    when {
                        q.contains("خلية") || q.contains("سيتوبلازم") || q.contains("نواة") || q.contains("أحياء") || q.contains("أعضاء") -> {
                            """
                            🧬 **معلم الأحياء المساعد:** مرحباً بك يا بطل المذاكرة! بخصوص استفسارك حول علم الأحياء والخلية:
                            
                            * **تركيب النواة:** النواة هي مركز التحكم الأكبر والمسؤولة الكلية عن تخزين حمض الـ DNA وإدارة العمليات الحيوية والإنقسام والانقسام الميتوزي.
                            * **الغشاء البلاسمي:** يحمي محتويات الخلية وله خاصية النفاذية الاختيارية للتحكم بدقة في العناصر والأملاح والمياه الداخلة والخارجة.
                            * **الميتوكوندريا:** تعتبر بيوت إنتاج الطاقة للخلية حيث يتم تفكيك جزيئات الغلوكوز وإنتاج جزيئات الـ ATP الحيوية.
                            
                            *💡 تذكر دائماً: الفهم الدقيق للرسومات التوضيحية يسهل عليك حفظ وظائف كل عضية بسهولة تامة! هل ترغب في شرح مكون آخر؟*
                            
                            *(ملاحظة: مفتاح الذكاء الاصطناعي Gemini API Key غير نشط حالياً، تم تقديم هذه المساعدة الدراسية الفورية من المعلم المحلي الذكي)*
                            """.trimIndent()
                        }
                        q.contains("رياضيات") || q.contains("حساب") || q.contains("معادلة") || q.contains("جبر") || q.contains("x") || q.contains("جمع") || q.contains("ضرب") || q.contains("قسمة") -> {
                            """
                            📐 **معلم الرياضيات المساعد:** أهلاً بك يا مهندس المستقبل! لحل المعادلات الجبرية من الدرجة الأولى أو استفسارك الحسابي:
                            
                            1. **تحديد المجاهيل:** نضع المتغيرات (مثل س أو x) في طرف واحد من المعادلة.
                            2. **موازنة العمليات:** للتخلص من أي رقم ملاصق للمتغير بالجمع أو الطرح، نقوم بإجراء العملية العكسية على الطرف الآخر.
                            3. **القسمة النهائية:** للحصول على قيمة x المفردة، نقسم كفتي المعادلة على المعامل المضروب في x.
                            
                            مثال بسيط لطلبك: إذا كان السؤال معادلة جبرية، نتدرج بحذف الثوابت ثم قسمة معامل المتغير لكي نحصل على الحل السريع.
                            
                            *💡 التدريب المستمر على حل المسائل هو سر التفوق الرياضي المطلق!*
                            
                            *(ملاحظة: مفتاح الذكاء الاصطناعي Gemini API Key غير نشط حالياً، تم تقديم هذه المساعدة الدراسية الفورية من المعلم المحلي الذكي)*
                            """.trimIndent()
                        }
                        q.contains("فيزياء") || q.contains("حركة") || q.contains("سرعة") || q.contains("تسارع") || q.contains("قانون") -> {
                            """
                            ⚡ **معلم الفيزياء المساعد:** أهلاً بك يا عالم الفيزياء الصغير! بخصوص موضوع الحركة ومسائل الميكانيكا الممتعة:
                            
                            * **السرعة (V):** وتساوي التغير في المسافة مقسوماً على التغير في الزمن (V = d / t).
                            * **التسارع (a):** هو معدل تغير السرعة بالنسبة للزمن (a = delta V / t).
                            * **قوانين نيوتن:** القانون الأول (القصور الذاتي)، والثاني (F = m * a) يثبت أن القوة تساوي الكتلة في التسارع.
                            
                            هل تحتاج إلى مناقشة مسألة لحساب السرعة أو القوة بالتحديد؟ أرسلها لي لتبسيطها معاً!
                            
                            *(ملاحظة: مفتاح الذكاء الاصطناعي Gemini API Key غير نشط حالياً، تم تقديم هذه المساعدة الدراسية الفورية من المعلم المحلي الذكي)*
                            """.trimIndent()
                        }
                        q.contains("انجليزي") || q.contains("english") || q.contains("grammar") || q.contains("ترجمة") || q.contains("لغة") -> {
                            """
                            🇺🇸 **معلم اللغات المساعد (English Mentor):** Welcome my friend! Let's master the language together:
                            
                            * **Simple Present (المضارع البسيط):** يُسخدم للتعبير عن الحقائق والروتين اليومي (مثال: He plays tennis every Saturday).
                            * **Past Simple (الماضي البسيط):** للتعبير عن حدث اكتمل بالماضي (مثال: We studied biology yesterday).
                            
                            *💡 Tip: Practice speaking and writing 3 sentences daily using these tenses to improve your communication skills!*
                            
                            *(ملاحظة: مفتاح الذكاء الاصطناعي Gemini API Key غير نشط حالياً، تم تقديم هذه المساعدة الدراسية الفورية من المعلم المحلي الذكي)*
                            """.trimIndent()
                        }
                        else -> {
                            """
                            🌟 **المعلم الذكي المساعد:** أهلاً ومرحباً بك يا ذكي! كيف يمكنني مساعدتك في رحلتك الدراسية اليوم لصفك الحالي (${studentGradeSelection.value})؟
                            
                            أنا هنا لمساعدتك في:
                            1. تبسيط شرح الدروس ومقررات العلوم واللغات.
                            2. صياغة جداول وخطط مذاكرة يومية وأسبوعية تكرارية مريحة.
                            3. حل مسائل الرياضيات والفيزياء ومراجعة بطاقات الفلاش والخرائط الذهنية.
                            
                            اكتب لي أي موضوع أو قاعدة علمية تود تلخيصها أو اختبار معرفتك بها بانتظارك!
                            
                            *(ملاحظة: مفتاح الذكاء الاصطناعي Gemini API Key غير نشط حالياً، تم تقديم هذه المساعدة الدراسية الفورية من المعلم المحلي الذكي)*
                            """.trimIndent()
                        }
                    }
                } else {
                    response
                }
                chatHistory.add(ChatMessage("AI", cleanResponse))
            }
        }
    }

    fun submitImageQuestion(desc: String) {
        val userMsg = ChatMessage("USER", "📷 [تم إدراج صورة السؤال]\n$desc", isImageQuestion = true)
        chatHistory.add(userMsg)
        isChatLoading.value = true

        viewModelScope.launch(Dispatchers.IO) {
            val systemInstr = "أنت مدرس علوم ورياضيات محترف، تقوم بتحليل وحل صور الأسئلة ومسائل الواجبات المدرسية التي يرسلها الطالب بالكامل بلغة عربية مبسطة."
            val response = GeminiService.generateContent(
                prompt = "قم بحل وتحليل مسألة السؤال المصور التالي وشرح حلها خطوة بخطوة: $desc",
                systemInstruction = systemInstr
            )
            viewModelScope.launch(Dispatchers.Main) {
                isChatLoading.value = false
                val explanation = if (response == "API_KEY_MISSING") {
                    "📷 [صورة السؤال تم تحليلها محلياً]\nلتنشيط المحلل البصري المتطور للذكاء الاصطناعي، يرجى تهيئة مفتاح Gemini API في إعدادات التطبيق. محلياً، هذا الكارت يمثل مسألة أحياء أو رياضيات من منهج ${studentGradeSelection.value}. الإجابة النموذجية تعتمد على تفصيل القوانين الجبرية أو التراكيب الحيوية."
                } else {
                    response
                }
                chatHistory.add(ChatMessage("AI", explanation))
            }
        }
    }

    // --- Study Material Upload & Synthesis ---
    val filesList = dao.getAllStudyMaterials().stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList()
    )
    val inputMaterialTitle = mutableStateOf("")
    val inputMaterialText = mutableStateOf("")
    val selectedFileType = mutableStateOf("PDF") // PDF, PPT, WORD, IMAGE
    val isUploadLoading = mutableStateOf(false)
    val activeMaterial = mutableStateOf<StudyMaterialEntity?>(null)

    fun handleUploadMaterial(onComplete: () -> Unit) {
        val title = inputMaterialTitle.value.trim()
        val content = inputMaterialText.value.trim()
        val type = selectedFileType.value

        if (title.isEmpty() || content.isEmpty()) return
        isUploadLoading.value = true

        viewModelScope.launch(Dispatchers.IO) {
            val systemPrompt = "أنت معاون أكاديمي وتلخيصي فائق الذكاء، تلخص المستندات الدراسية بدقة وإتقان مع إبراز العناصر الهامة بلغة عربية سلسلة تناسب الفهم الفائق."
            val summarizePrompt = "قم بعمل تلخيص بليغ ومفصل ومنسق ومقسم إلى فقرات للمستند التعليمي المعنون بـ: \"$title\":\n\n$content"

            val summaryResult = GeminiService.generateContent(
                prompt = summarizePrompt,
                systemInstruction = systemPrompt
            )

            val cleanSummary = if (summaryResult == "API_KEY_MISSING") {
                "ملخص محلي سريع ومبسط:\nتم تلقي المستند بنجاح! للتلخيص الفائق والشامل عبر الذكاء الاصطناعي التوليدي، يرجى تهيئة مفتاح Gemini API Secure Key في خيارات المعاملات لـ AI Studio.\nموجز النص المرفوع:\n${content.take(150)}..."
            } else {
                summaryResult
            }

            val materialEntity = StudyMaterialEntity(
                id = UUID.randomUUID().toString(),
                title = title,
                sourceText = content,
                summary = cleanSummary,
                uploadType = type
            )

            dao.insertMaterial(materialEntity)
            activeMaterial.value = materialEntity

            // Clear inputs
            viewModelScope.launch(Dispatchers.Main) {
                inputMaterialTitle.value = ""
                inputMaterialText.value = ""
                isUploadLoading.value = false
                onComplete()
            }
        }
    }

    fun selectActiveMaterial(material: StudyMaterialEntity) {
        activeMaterial.value = material
        fetchFlashcardsForMaterial(material.id)
    }

    fun deleteMaterial(materialId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            dao.deleteMaterial(materialId)
            if (activeMaterial.value?.id == materialId) {
                activeMaterial.value = null
            }
        }
    }

    // --- Interactive Quiz Engine ---
    val allQuizzes = dao.getAllQuizzes().stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList()
    )
    val isQuizGenerating = mutableStateOf(false)
    val activeQuizQuestions = mutableStateListOf<QuizQuestion>()
    val activeQuizTitle = mutableStateOf("اختبار تقييمي للذكاء")
    val quizSelectedQuestionIdx = mutableStateOf(0)
    val isQuizSubmitted = mutableStateOf(false)
    val currentQuizMaterialId = mutableStateOf("")

    // Quiz options selection states
    val selectedQuizType = mutableStateOf("MCQ") // MCQ, TRUE_FALSE, FILL_BLANKS, ESSAY
    val selectedQuizDifficulty = mutableStateOf("MEDIUM") // EASY, MEDIUM, HARD
    val selectedQuizNumQ = mutableStateOf(5)

    fun fetchOrGenerateQuizForMaterial(
        material: StudyMaterialEntity,
        numQ: Int = 5,
        type: String = "MCQ",
        difficulty: String = "MEDIUM",
        onReady: () -> Unit
    ) {
        isQuizGenerating.value = true
        currentQuizMaterialId.value = material.id
        
        val typeLabel = when (type) {
            "TRUE_FALSE" -> "صح أم خطأ"
            "FILL_BLANKS" -> "أكمل الفراغات"
            "ESSAY" -> "أسئلة مقالية"
            else -> "اختيار من متعدد"
        }
        val diffLabel = when (difficulty) {
            "EASY" -> "سهل"
            "HARD" -> "صعب"
            else -> "متوسط"
        }
        
        activeQuizTitle.value = "اختبار ($typeLabel - مستوى $diffLabel): ${material.title}"
        quizSelectedQuestionIdx.value = 0
        isQuizSubmitted.value = false
        activeQuizQuestions.clear()

        viewModelScope.launch(Dispatchers.IO) {
            val questions = GeminiService.generateQuiz(material.title, material.sourceText, numQ, type, difficulty)
            viewModelScope.launch(Dispatchers.Main) {
                activeQuizQuestions.addAll(questions)
                isQuizGenerating.value = false
                onReady()
            }
        }
    }

    fun submitQuiz() {
        if (isQuizSubmitted.value) return
        isQuizSubmitted.value = true

        // Calculate score
        val correctCount = activeQuizQuestions.count { it.selectedOptionIndex == it.correctOptionIndex }
        
        viewModelScope.launch(Dispatchers.IO) {
            val type = Types.newParameterizedType(List::class.java, QuizQuestion::class.java)
            val questionsJson = moshi.adapter<List<QuizQuestion>>(type).toJson(activeQuizQuestions)
            
            val quizEntity = LocalQuizEntity(
                id = UUID.randomUUID().toString(),
                materialId = currentQuizMaterialId.value,
                title = activeQuizTitle.value,
                score = correctCount,
                totalQuestions = activeQuizQuestions.size,
                questionsJson = questionsJson
            )
            
            dao.insertQuiz(quizEntity)
        }
    }

    // --- Interactive Flashcards Deck ---
    val activeFlashcardDeck = mutableStateListOf<FlashcardEntity>()
    val isGeneratingFlashcards = mutableStateOf(false)
    val flashcardSelectedIdx = mutableStateOf(0)
    val isFlashcardFlipped = mutableStateOf(false)

    fun fetchFlashcardsForMaterial(materialId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            dao.getFlashcardsForMaterial(materialId).collect { list ->
                viewModelScope.launch(Dispatchers.Main) {
                    activeFlashcardDeck.clear()
                    activeFlashcardDeck.addAll(list)
                }
            }
        }
    }

    fun triggerGenerateFlashcards(material: StudyMaterialEntity) {
        isGeneratingFlashcards.value = true
        viewModelScope.launch(Dispatchers.IO) {
            val prompt = """
                من محتوى الدرس المعنون بـ "${material.title}"، استخرج أهم 4 مصطلحات أو معلومات هامة وصمم لها بطاقات مراجعة بصيغة فلاش كاردز (سؤال جواب / مصطلح تعريف).
                أرسل المخرجات حصرياً بتنسيق JSON على شكل مصفوفة (JSON Array) تحوي كائنات بهذا التنسيق:
                [
                  {
                    "front": "المصطلح البارز",
                    "back": "شرح المصطلح أو الإجابة المركزة"
                  }
                ]
                تنبيه هام للغاية: أعد فقط كود الـ JSON بموثوقية، لا تستخدم كود بايت ماركداون.
            """.trimIndent()

            val systemInstr = "أنت أستاذ ومصمم بطاقات استذكار تكرار متباعد ذكي ومحترف."
            val response = GeminiService.generateContent(prompt, systemInstruction = systemInstr, responseJsonSchema = true)

            viewModelScope.launch(Dispatchers.Main) {
                isGeneratingFlashcards.value = false
                val list = try {
                    val sanitized = GeminiService.sanitizeJsonString(response)
                    if (response == "API_KEY_MISSING") {
                        getFallbackFlashcards(material.id)
                    } else {
                        val cardType = Types.newParameterizedType(List::class.java, Map::class.java)
                        val adapter = moshi.adapter<List<Map<String, String>>>(cardType)
                        val mapList = adapter.fromJson(sanitized) ?: emptyList()
                        mapList.map {
                            FlashcardEntity(
                                materialId = material.id,
                                front = it["front"] ?: "الكلمة",
                                back = it["back"] ?: "التعريف"
                            )
                        }
                    }
                } catch (e: Exception) {
                    Log.e("Flashcards", "Json failure: $response", e)
                    getFallbackFlashcards(material.id)
                }

                if (list.isNotEmpty()) {
                    viewModelScope.launch(Dispatchers.IO) {
                        dao.insertFlashcards(list)
                    }
                }
            }
        }
    }

    private fun getFallbackFlashcards(mId: String): List<FlashcardEntity> {
        return listOf(
            FlashcardEntity(materialId = mId, front = "عضية الريبوسوم", back = "عضيات دقيقة متواجدة بالسيتوبلازم أو ملتصقة بالشبكة الإندوبلازمية الخشنة وهي مسؤولة كلياً عن ترجمة الحمض النووي وتركيب البروتينات لبناء أنسجة الجسم."),
            FlashcardEntity(materialId = mId, front = "السائل السيتوبلازمي", back = "سائل هلامي يشغل حيز الخلايا وتسبح فيه كل عضيات الخلية الحيوية ويحفز تنقل بروتينات النقل والأدوية والمواد داخل هيكل الخلية."),
            FlashcardEntity(materialId = mId, front = "الخلية النباتية", back = "متميزة بجدار خلوي سلولوزي قاسي لحمايتها وفجوة عصارية كبيرة مركزية لحفظ المياه والبلاستيدات الخضراء لعمليات البناء الضوئي الذكية.")
        )
    }

    fun markFlashcardKnown(card: FlashcardEntity, isKnown: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            val newScore = if (isKnown) 5 else 1
            dao.updateFlashcardProgress(card.id, isKnown, newScore)
        }
    }

    // --- PDF To Explanatory Lecture Video Script ---
    val allSavedVideos = dao.getAllVideos().stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList()
    )
    val isConvertingVideo = mutableStateOf(false)
    val selectedVideoDuration = mutableStateOf("SHORT") // SHORT (قصير), MEDIUM (متوسط), DETAILED (مفصل)
    val selectedVoiceGender = mutableStateOf("FEMALE") // MALE (ولد), FEMALE (بنت)
    val activeVideoSlides = mutableStateListOf<StudyScriptSlide>()
    val activeVideoSlideIdx = mutableStateOf(0)
    val isVideoPlaying = mutableStateOf(false)

    fun runPdfToVideoConversion(material: StudyMaterialEntity, onReady: () -> Unit) {
        isConvertingVideo.value = true
        activeVideoSlideIdx.value = 0
        activeVideoSlides.clear()
        isVideoPlaying.value = false

        viewModelScope.launch(Dispatchers.IO) {
            val slides = GeminiService.convertPdfToSlidesVideo(
                pdfTitle = material.title,
                pdfContent = material.sourceText,
                length = selectedVideoDuration.value,
                voiceType = selectedVoiceGender.value
            )

            // Save video metadata to room
            val slidesType = Types.newParameterizedType(List::class.java, StudyScriptSlide::class.java)
            val jsonSlides = moshi.adapter<List<StudyScriptSlide>>(slidesType).toJson(slides)
            
            val videoEntity = LocalVideoEntity(
                id = UUID.randomUUID().toString(),
                materialId = material.id,
                title = "فيديو شرح: ${material.title}",
                scriptJson = jsonSlides,
                durationType = selectedVideoDuration.value,
                voiceType = selectedVoiceGender.value
            )

            dao.insertVideo(videoEntity)

            viewModelScope.launch(Dispatchers.Main) {
                activeVideoSlides.addAll(slides)
                isConvertingVideo.value = false
                onReady()
            }
        }
    }

    fun runPdfToVideoConversionWithText(title: String, content: String, onReady: () -> Unit) {
        val mat = com.example.data.StudyMaterialEntity(
            id = UUID.randomUUID().toString(),
            title = title,
            sourceText = content,
            summary = "تم استخراج محتويات هذا الملف وصياغة فيديو ناطق مع تحريك شرائح عرض كرتونية.",
            uploadType = if (selectedFileType.value == "IMAGE") "IMAGE" else "PDF",
            timestamp = System.currentTimeMillis()
        )
        viewModelScope.launch(Dispatchers.IO) {
            dao.insertMaterial(mat)
            viewModelScope.launch(Dispatchers.Main) {
                runPdfToVideoConversion(mat, onReady)
            }
        }
    }

    fun selectActiveVideo(video: LocalVideoEntity) {
        try {
            val slidesType = Types.newParameterizedType(List::class.java, StudyScriptSlide::class.java)
            val list = moshi.adapter<List<StudyScriptSlide>>(slidesType).fromJson(video.scriptJson) ?: emptyList()
            activeVideoSlides.clear()
            activeVideoSlides.addAll(list)
            activeVideoSlideIdx.value = 0
            isVideoPlaying.value = true
        } catch (e: Exception) {
            Log.e("Video", "Slide parse failure", e)
        }
    }

    // --- Interactive Mind Map Engine ---
    val allSavedMindMaps = dao.getAllMindMaps().stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList()
    )
    val isGeneratingMindMap = mutableStateOf(false)
    val activeMindMapNodes = mutableStateListOf<MindMapNode>()
    val mindMapFocusedMaterial = mutableStateOf<StudyMaterialEntity?>(null)

    fun fetchOrGenerateMindMap(material: StudyMaterialEntity, onReady: () -> Unit) {
        isGeneratingMindMap.value = true
        mindMapFocusedMaterial.value = material
        activeMindMapNodes.clear()

        viewModelScope.launch(Dispatchers.IO) {
            val mapEntity = dao.getMindMapForMaterial(material.id)
            if (mapEntity != null) {
                // Read from DB
                viewModelScope.launch(Dispatchers.Main) {
                    try {
                        val type = Types.newParameterizedType(List::class.java, MindMapNode::class.java)
                        val list = moshi.adapter<List<MindMapNode>>(type).fromJson(mapEntity.nodesJson) ?: emptyList()
                        activeMindMapNodes.addAll(list)
                        isGeneratingMindMap.value = false
                        onReady()
                    } catch (e: Exception) {
                        generateNewMindMap(material, onReady)
                    }
                }
            } else {
                generateNewMindMap(material, onReady)
            }
        }
    }

    private suspend fun generateNewMindMap(material: StudyMaterialEntity, onReady: () -> Unit) {
        val nodes = GeminiService.generateMindMap(material.title, material.sourceText)
        
        val type = Types.newParameterizedType(List::class.java, MindMapNode::class.java)
        val jsonNodes = moshi.adapter<List<MindMapNode>>(type).toJson(nodes)
        
        dao.insertMindMap(
            LocalMindMapEntity(
                id = UUID.randomUUID().toString(),
                materialId = material.id,
                title = "خريطة تفاعلية: ${material.title}",
                nodesJson = jsonNodes
            )
        )

        viewModelScope.launch(Dispatchers.Main) {
            activeMindMapNodes.addAll(nodes)
            isGeneratingMindMap.value = false
            onReady()
        }
    }

    // --- Advanced Custom Summary & Chapters Engine ---
    val activeSummaryType = mutableStateOf("GENERAL") // GENERAL, CHAPTERS, EXAM_REVIEW
    val activeSummaryLength = mutableStateOf("MEDIUM") // SHORT, MEDIUM, DETAILED
    val isSummaryGenerating = mutableStateOf(false)
    val customSummaryContent = mutableStateOf("")

    fun regenerateSummary(material: StudyMaterialEntity, type: String, length: String) {
        activeSummaryType.value = type
        activeSummaryLength.value = length
        isSummaryGenerating.value = true
        
        viewModelScope.launch(Dispatchers.IO) {
            val lengthPrompt = when (length) {
                "SHORT" -> "مختصر وبليغ جداً بفقرات رئيسية سريعة ورؤوس أقلام"
                "DETAILED" -> "مفصل وشامل ودقيق جداً يغطي كافة التفاصيل الفرعية والتعاريف والمسائل والقوانين"
                else -> "متوسط الطول ومتوازن يغطي الهيكل الفكرة الرئيسية"
            }
            
            val typePrompt = when (type) {
                "CHAPTERS" -> "الرجاء تفصيل وتلخيص كل فصل من فصول هذا الكتاب أو الدرس بشكل منفصل مع عنونة واضحة ومرتبة (مثال: الفصل الأول: الخلاصة والأركان، الفصل الثاني: الأفكار والعمليات...) مع نقاط وافية لكل فصل."
                "EXAM_REVIEW" -> "صياغة المراجعة النهائية المركزة والملخصة لكتاب ليلة الامتحان قبل الاختبار، تحتوي كبسولة المفاهيم والتعاريف والقوانين والأسئلة الأكثر توقعاً وتكراراً بالامتحان في شكل نقاط وعلامات مراجعة ميسرة الاستذكار التلقائي السريع."
                else -> "تلخيص للدرس أو الكتاب تلخيصاً عاماً غنياً ومنسقاً يغطي صلب المحتوى."
            }
            
            val systemPrompt = "أنت موجّه أكاديمي ومعلّم معاون فائق الذكاء والخبرة، تلخّص المناهج لضمان أعلى درجات الاستعداد والاستذكار."
            val summarizePrompt = """
                تم تكليفك بتلخيص ومراجعة المادة التعليمية لـ "${material.title}".
                المطلوب بالتحديد: $typePrompt
                طبيعة وحجم المخرجات المقترحة: $lengthPrompt
                
                وهنا النص الكامل للمحتوى للمذاكرة الفائقة:
                ${material.sourceText}
            """.trimIndent()
            
            val result = GeminiService.generateContent(
                prompt = summarizePrompt,
                systemInstruction = systemPrompt
            )
            
            viewModelScope.launch(Dispatchers.Main) {
                isSummaryGenerating.value = false
                customSummaryContent.value = if (result == "API_KEY_MISSING" || result.startsWith("ERROR_")) {
                    "عذراً! لتوليد هذا الشكل المخصص من التلخيص الذاتي (كبسولة الفصول أو مراجعة ليلة الامتحان التلقائية) باستخدام الذكاء الاصطناعي، يرجى تزويد مفتاح جيميناي API Key.\n\nإليك الملخص التمهيدي المخزن محلياً للدرس:\n${material.summary}"
                } else {
                    result
                }
            }
        }
    }

    // --- Interactive Concept Explorer (Mind map navigation details) ---
    val activeNodeDetailedExplanation = mutableStateOf("")
    val isFetchingNodeExplanation = mutableStateOf(false)

    fun fetchDeepConceptExplanation(node: MindMapNode, material: StudyMaterialEntity) {
        activeNodeDetailedExplanation.value = "جاري الاتصال بمعلم الذكاء الاصطناعي لشرح مفهوم: ${node.label}..."
        isFetchingNodeExplanation.value = true
        
        viewModelScope.launch(Dispatchers.IO) {
            val systemPrompt = "أنت مرشد تربوي ومحلل خرائط تفاعلية ذكي وبسيط للطلاب."
            val prompt = """
                بناءً على درس "${material.title}"، اشرح لي المفهوم الفرعي "${node.label}" بشكل مسطح وواضح ومبسط للغاية، ووضح سياقه وطريقة حفظه السليمة وارتباطه الخرائطي بالدرس لمساعدتي كطالب على الترسيخ البصري والتنقل السلس للمفاهيم المعقدة.
            """.trimIndent()
            
            val response = GeminiService.generateContent(prompt, systemInstruction = systemPrompt)
            viewModelScope.launch(Dispatchers.Main) {
                isFetchingNodeExplanation.value = false
                activeNodeDetailedExplanation.value = if (response == "API_KEY_MISSING" || response.startsWith("ERROR_")) {
                    "مفهوم: ${node.label}\nهذا العنصر يعبر عن ركن أساسي في درس ${material.title}.\nلتوليد تفاسير وشروحات أعمق لهذا المفهوم من خريطنا التفاعلية الذكية، يرجى إعداد مفتاح API في تبويب الإعدادات."
                } else {
                    response
                }
            }
        }
    }

    // --- Editable Slides Presentation Engine ---
    val activePresentationSlides = mutableStateListOf<StudyScriptSlide>()
    val isGeneratingPresentation = mutableStateOf(false)

    fun generateOrFetchPresentation(material: StudyMaterialEntity, onReady: () -> Unit) {
        isGeneratingPresentation.value = true
        activePresentationSlides.clear()
        
        viewModelScope.launch(Dispatchers.IO) {
            val slides = GeminiService.convertPdfToSlidesVideo(material.title, material.sourceText, "SHORT", "FEMALE")
            viewModelScope.launch(Dispatchers.Main) {
                activePresentationSlides.addAll(slides)
                isGeneratingPresentation.value = false
                onReady()
            }
        }
    }

    fun updatePresentationSlide(index: Int, updatedSlide: StudyScriptSlide) {
        if (index in activePresentationSlides.indices) {
            activePresentationSlides[index] = updatedSlide
        }
    }

    // --- Weekly Study Planner ---
    val allPlanTasks = dao.getAllStudyPlans().stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList()
    )
    val planTaskInputTitle = mutableStateOf("")
    val planTaskInputDay = mutableStateOf("السبت")
    val planTaskInputDuration = mutableStateOf("45")

    fun addStudyPlanTask() {
        val title = planTaskInputTitle.value.trim()
        val day = planTaskInputDay.value
        val mins = planTaskInputDuration.value.toIntOrNull() ?: 45
        if (title.isEmpty()) return

        viewModelScope.launch(Dispatchers.IO) {
            dao.insertStudyPlan(
                StudyPlanEntity(
                    title = title,
                    dayOfWeek = day,
                    durationMinutes = mins,
                    isCompleted = false,
                    timeLabel = "04:30 م"
                )
            )
            viewModelScope.launch(Dispatchers.Main) {
                planTaskInputTitle.value = ""
            }
        }
    }

    fun toggleStudyPlanStatus(id: Int, currentStatus: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            dao.updateStudyPlanStatus(id, !currentStatus)
        }
    }

    fun deleteStudyPlanTask(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            dao.deleteStudyPlan(id)
        }
    }

    // --- Teacher / Coach Panel Controls ---
    val teacherSubjectInput = mutableStateOf("الكيمياء الحيوية")
    val teacherGradeInput = mutableStateOf("المرحلة الثانوية - الأول الثانوي")
    val teacherFilesCount = mutableStateOf(1)
    val teacherStudentNameInput = mutableStateOf("أحمد الطالب")
    val instructorAnnounceTitle = mutableStateOf("")
    val instructorAnnounceSubject = mutableStateOf("أحياء")
    val instructorAnnounceBody = mutableStateOf("")
    val announcementsList = dao.getAllAnnouncements().stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList()
    )

    fun submitTeacherAnnouncement() {
        val title = instructorAnnounceTitle.value.trim()
        val subject = instructorAnnounceSubject.value.trim()
        val body = instructorAnnounceBody.value.trim()

        if (title.isEmpty() || body.isEmpty()) return

        viewModelScope.launch(Dispatchers.IO) {
            dao.insertAnnouncement(
                TeacherAnnouncementEntity(
                    title = title,
                    content = body,
                    senderName = loggedUser.value?.username ?: "الأستاذ المشرف",
                    subject = subject
                )
            )
            viewModelScope.launch(Dispatchers.Main) {
                instructorAnnounceTitle.value = ""
                instructorAnnounceBody.value = ""
            }
        }
    }

    // --- Subscriptions ---
    val subscriptionPlan = mutableStateOf("FREE") // FREE, MONTHLY, SANNUAL
    val solvedSummariesCount = mutableStateOf(12)
    val remainingSummaries = mutableStateOf(3) // 3 left for free users

    fun upgradeSubscription(plan: String) {
        subscriptionPlan.value = plan
        if (plan == "MONTHLY") {
            remainingSummaries.value = 50
        } else if (plan == "SANNUAL") {
            remainingSummaries.value = 9999
        }
    }
}
