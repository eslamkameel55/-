package com.example.data

import android.util.Log
import com.example.BuildConfig
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.util.concurrent.TimeUnit

object GeminiService {
    private const val TAG = "GeminiService"
    private const val MODEL_NAME = "gemini-3.5-flash"
    private const val BASE_URL = "https://generativelanguage.googleapis.com/v1beta/models/$MODEL_NAME:generateContent"

    private val client = OkHttpClient.Builder()
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .build()

    private val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

    /**
     * Checks if the Gemini API Key is set in BuildConfig.
     */
    fun isApiKeyAvailable(): Boolean {
        // Checking for a placeholder as specified in .env.example
        val key = BuildConfig.GEMINI_API_KEY
        return key.isNotEmpty() && key != "MY_GEMINI_API_KEY" && !key.contains("PLACEHOLDER")
    }

    /**
     * Primary method to query Gemini with text prompt and optional system instructions.
     */
    suspend fun generateContent(
        prompt: String,
        systemInstruction: String? = null,
        responseJsonSchema: Boolean = false
    ): String = withContext(Dispatchers.IO) {
        val apiKey = BuildConfig.GEMINI_API_KEY
        if (!isApiKeyAvailable()) {
            return@withContext "API_KEY_MISSING"
        }

        try {
            // Build Request JSON body following Google's REST API Schema
            val requestJson = JSONObject()
            
            // Contents
            val contentsArray = JSONArray()
            val contentObj = JSONObject()
            val partsArray = JSONArray()
            val partObj = JSONObject()
            partObj.put("text", prompt)
            partsArray.put(partObj)
            contentObj.put("parts", partsArray)
            contentsArray.put(contentObj)
            requestJson.put("contents", contentsArray)

            // System Instruction
            if (systemInstruction != null) {
                val sysInstrObj = JSONObject()
                val sysPartsArray = JSONArray()
                val sysPartObj = JSONObject()
                sysPartObj.put("text", systemInstruction)
                sysPartsArray.put(sysPartObj)
                sysInstrObj.put("parts", sysPartsArray)
                requestJson.put("systemInstruction", sysInstrObj)
            }

            // Generation Config
            val generationConfig = JSONObject()
            if (responseJsonSchema) {
                val responseFormat = JSONObject()
                responseFormat.put("type", "application/json")
                generationConfig.put("responseMimeType", "application/json")
            }
            generationConfig.put("temperature", 0.7)
            requestJson.put("generationConfig", generationConfig)

            val mediaType = "application/json; charset=utf-8".toMediaType()
            val requestBody = requestJson.toString().toRequestBody(mediaType)

            val request = Request.Builder()
                .url("$BASE_URL?key=$apiKey")
                .post(requestBody)
                .addHeader("Content-Type", "application/json")
                .build()

            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) {
                    val errBody = response.body?.string() ?: ""
                    Log.e(TAG, "Gemini API Call unsuccessful: Code ${response.code}, Body: $errBody")
                    return@withContext "ERROR_CODE_${response.code}: $errBody"
                }

                val responseStr = response.body?.string() ?: ""
                val responseJson = JSONObject(responseStr)
                
                val candidatesArray = responseJson.optJSONArray("candidates")
                if (candidatesArray != null && candidatesArray.length() > 0) {
                    val candidate = candidatesArray.getJSONObject(0)
                    val content = candidate.optJSONObject("content")
                    val parts = content?.optJSONArray("parts")
                    if (parts != null && parts.length() > 0) {
                        return@withContext parts.getJSONObject(0).optString("text")
                    }
                }
                
                return@withContext "لم يتم تلقي استجابة ملائمة من الذكاء الاصطناعي."
            }
        } catch (e: Exception) {
            Log.e(TAG, "Exception during Gemini Call", e)
            return@withContext "عذراً، حدث خطأ في الشبكة أو الاتصال بالخادم المساعد: ${e.localizedMessage}"
        }
    }

    /**
     * Primary method to query Gemini in a multimodal fashion (text prompt + base64 file attachment)
     */
    suspend fun generateMultimodalContent(
        prompt: String,
        mimeType: String,
        base64Data: String,
        systemInstruction: String? = null,
        responseJsonSchema: Boolean = false
    ): String = withContext(Dispatchers.IO) {
        val apiKey = BuildConfig.GEMINI_API_KEY
        if (!isApiKeyAvailable()) {
            return@withContext "API_KEY_MISSING"
        }

        try {
            val requestJson = JSONObject()
            
            // Contents
            val contentsArray = JSONArray()
            val contentObj = JSONObject()
            val partsArray = JSONArray()
            
            // Text part
            val textPartObj = JSONObject()
            textPartObj.put("text", prompt)
            partsArray.put(textPartObj)
            
            // Inline data part
            val inlineDataPartObj = JSONObject()
            val inlineDataObj = JSONObject()
            inlineDataObj.put("mimeType", mimeType)
            inlineDataObj.put("data", base64Data)
            inlineDataPartObj.put("inlineData", inlineDataObj)
            partsArray.put(inlineDataPartObj)
            
            contentObj.put("parts", partsArray)
            contentsArray.put(contentObj)
            requestJson.put("contents", contentsArray)

            // System Instruction
            if (systemInstruction != null) {
                val sysInstrObj = JSONObject()
                val sysPartsArray = JSONArray()
                val sysPartObj = JSONObject()
                sysPartObj.put("text", systemInstruction)
                sysPartsArray.put(sysPartObj)
                sysInstrObj.put("parts", sysPartsArray)
                requestJson.put("systemInstruction", sysInstrObj)
            }

            // Generation Config
            val generationConfig = JSONObject()
            if (responseJsonSchema) {
                generationConfig.put("responseMimeType", "application/json")
            }
            generationConfig.put("temperature", 0.4)
            requestJson.put("generationConfig", generationConfig)

            val mediaType = "application/json; charset=utf-8".toMediaType()
            val requestBody = requestJson.toString().toRequestBody(mediaType)

            val request = Request.Builder()
                .url("$BASE_URL?key=$apiKey")
                .post(requestBody)
                .addHeader("Content-Type", "application/json")
                .build()

            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) {
                    val errBody = response.body?.string() ?: ""
                    Log.e(TAG, "Gemini API Multimodal unsuccessful: Code ${response.code}, Body: $errBody")
                    return@withContext "ERROR_CODE_${response.code}: $errBody"
                }

                val responseStr = response.body?.string() ?: ""
                val responseJson = JSONObject(responseStr)
                
                val candidatesArray = responseJson.optJSONArray("candidates")
                if (candidatesArray != null && candidatesArray.length() > 0) {
                    val candidate = candidatesArray.getJSONObject(0)
                    val content = candidate.optJSONObject("content")
                    val parts = content?.optJSONArray("parts")
                    if (parts != null && parts.length() > 0) {
                        return@withContext parts.getJSONObject(0).optString("text")
                    }
                }
                
                return@withContext "Failed to parse content from multimodal request."
            }
        } catch (e: Exception) {
            Log.e(TAG, "Exception during Gemini Multimodal Call", e)
            return@withContext "عذراً، حدث خطأ أثناء تحليل الملف: ${e.localizedMessage}"
        }
    }

    /**
     * UI Helper to clean JSON string returned from Gemini (sometimes gets wrapped in triple backticks)
     */
    fun sanitizeJsonString(raw: String): String {
        var clean = raw.trim()
        if (clean.startsWith("```json")) {
            clean = clean.substringAfter("```json")
        } else if (clean.startsWith("```")) {
            clean = clean.substringAfter("```")
        }
        if (clean.endsWith("```")) {
            clean = clean.substringBeforeLast("```")
        }
        return clean.trim()
    }

    /**
     * Synthesizes Mock Speech using Android Text-To-Speech (real framework API) in the UI,
     * but also generates a complete educational slide transcript.
     */
    suspend fun convertPdfToSlidesVideo(
        pdfTitle: String,
        pdfContent: String,
        length: String, // SHORT, MEDIUM, DETAILED
        voiceType: String // MALE (ولد), FEMALE (بنت)
    ): List<StudyScriptSlide> {
        val totalSlides = when (length) {
            "SHORT" -> 3
            "MEDIUM" -> 5
            else -> 8
        }
        
        val systemPrompt = """
            أنت خبير في تبسيط وعرض المناهج التعليمية وتحويل محتوى الكتب والملفات إلى سيناريو فيديو شرح تعليمي.
            يجب أن تكون نبرة الشرح مشوقة، واضحة ومناسبة لمستوى الطلاب.
            لغة الشرح: اللغة العربية الفصحى المبسطة أو اللهجة المناسبة للتعليم.
            
            مطلوب كود مخرجات بتنسيق JSON حصرياً على شكل مصفوفة من الكائنات (JSON Array of Objects).
            كل كائن يحتوي على المفاتيح التالية بالإنجليزية:
            1. "title": عنوان الشريحة أو النقطة التعليمية.
            2. "narration": النص التفصيلي الذي يقرؤه الشارح بصوته (ولد أو بنت).
            3. "slideVisuals": وصف دقيق ومبهر للرسومات، النصوص أو الرسوم البيانية التوضيحية والصور المتحركة المعروضة على الشاشة لدعم المعنى.
            
            مثال على هيكل JSON المطلوب:
            [
              {
                "title": "بداية الرحلة",
                "narration": "أهلاً ومرحباً بكم يا أذكياء! اليوم سنكتشف معاً سحر الجينات وكيف تصنع صفاتنا...",
                "slideVisuals": "رسمة كرتونية جذابة تظهر شريط الحمض النووي المفتول يلمع بألوان برتقالية وزرقاء"
              }
            ]
            
            تنبيه: لا تكتب أي نصوص التفافية خارج مصفوفة الـ JSON، لا تستخدم كود ماركداون للـ JSON، فقط أرسل المصفوفة مباشرةً لكي نتمكن من تحليلها برمجياً.
        """.trimIndent()

        val prompt = """
            قم بتحويل المحتوى التالي المأخوذ من ملف "${pdfTitle}" إلى سيناريو فيديو شرح تعليمي مقسم إلى $totalSlides شرائح تفصيلية.
            طول الشرح المطلوب: $length
            صوت الشارح المعني بالتسجيل: $voiceType
            
            نص المحتوى الأساسي للتلخيص وصياغة السيناريو:
            $pdfContent
        """.trimIndent()

        val response = generateContent(prompt, systemInstruction = systemPrompt, responseJsonSchema = true)
        
        if (response == "API_KEY_MISSING" || response.startsWith("ERROR_") || response.startsWith("عذراً")) {
            // Return placeholder slide sequence if API fails or is not ready
            return getFallbackSlides(pdfTitle, length)
        }

        return try {
            val sanitized = sanitizeJsonString(response)
            val type = Types.newParameterizedType(List::class.java, StudyScriptSlide::class.java)
            val adapter = moshi.adapter<List<StudyScriptSlide>>(type)
            adapter.fromJson(sanitized) ?: getFallbackSlides(pdfTitle, length)
        } catch (e: Exception) {
            Log.e(TAG, "Failed to parse slide JSON, response was: $response", e)
            getFallbackSlides(pdfTitle, length)
        }
    }

    private fun getFallbackSlides(title: String, length: String): List<StudyScriptSlide> {
        return listOf(
            StudyScriptSlide(
                title = "المقدمة والتمهيد لدرس: $title",
                narration = "مرحباً بكم يا أصدقاء في درس اليوم الشيق! سنبسط معاً المفاهيم الأساسية المستخرجة من ملف $title لتثبيتها في عقولنا بأبسط طريقة وبشكل ذكي.",
                slideVisuals = "أيقونة كتاب ذو صفحات مفتوحة وحولها لمبات مضيئة ذكية وعلامات استفهام متحركة"
            ),
            StudyScriptSlide(
                title = "العناصر والمفاهيم الرئيسية",
                narration = "في هذه المحطة، سنتعرف على الأفكار الجوهرية. الملف يحتوي على معلومات هامة ننصح بتلخيصها في مذكراتك ومراجعة النقاط الإرشادية بانتظام.",
                slideVisuals = "بطاقات معلومات منسقة تظهر تباعاً بألوان أزرق سماوي وأورانج مبهر لتسليط الضوء على الكلمات الدلالية"
            ),
            StudyScriptSlide(
                title = "الخلاصة والتطبيق العملي",
                narration = "تذكروا دائماً: المذاكرة الذكية لا تعني العمل الشاق، بل تعني التركيز والتكرار المتباعد. ندعوكم لتجربة الاختبار التفاعلي وحفظ البطاقات التعليمية المتاحة في التطبيق!",
                slideVisuals = "رسم كارتوني رائع لطالب يرتدي قبعة التخرج وبجانبه شاشة تفاعلية تعرض نتائج ممتازة"
            )
        )
    }

    /**
     * Generates a completely customized set of Quiz Questions based on lessons.
     */
    /**
     * Generates a completely customized set of Quiz Questions based on lessons.
     * Supports MCQ, True/False, Fill-in-the-blanks, and Essay question formats with varying difficulties.
     */
    suspend fun generateQuiz(
        materialTitle: String,
        materialContent: String,
        numQuestions: Int = 5,
        type: String = "MCQ", // MCQ, TRUE_FALSE, FILL_BLANKS, ESSAY
        difficulty: String = "MEDIUM" // EASY, MEDIUM, HARD
    ): List<QuizQuestion> {
        val typeArabic = when (type) {
            "TRUE_FALSE" -> "صح وخطأ (مثلاً: السؤال ثم الخيارات هي 'صح' و 'خطأ' فقط والطلب هو التصحيح)"
            "FILL_BLANKS" -> "أكمل الفراغات (السؤال يحتوي على فراغات متبوعة بأربعة خيارات يختار الطالب المكمل المناسب)"
            "ESSAY" -> "سؤال مقالي (أسئلة عامة مقالية تطلب الشرح والكتابة النصية، واجعل الخيارات تحتوي على خيار تأكيدي واحد فقط وهو 'عرض الإجابة النموذجية ومطابقتها ✔️' لتسهيل التصحيح التلقائي والمقارنة الاستيعابية)"
            else -> "اختيار من متعدد (أربعة خيارات ذكية متبوعة بالتفسيرات والعلل التفصيلية)"
        }
        val difficultyArabic = when (difficulty) {
            "EASY" -> "سهل ومباشر"
            "HARD" -> "صعب وعميق يتطلب مهارات تفكير نقدي وتحليلي للجامعات"
            else -> "متوسط الصعوبة"
        }

        val systemPrompt = """
            أنت أستاذ ومصمم امتحانات وتقييمات أكاديمية ومقررات دراسية محترف ومبدع للغاية.
            قم بإنشاء اختبار تفاعلي مكون من $numQuestions أسئلة من نوع: $typeArabic وبمستوى صعوبة: $difficultyArabic، بناءً على المادة الدراسية والمقالات المرجعية المرفقة.
            
            مطلوب كود مخرجات بتنسيق JSON حصرياً (على شكل JSON Array من الكائنات).
            كل كائن يحمل البنية والخصائص الدقيقة التالية بالإنجليزية:
            {
              "text": "نص السؤال بالتحديد والتشكيل وسياقه الكامل؟",
              "options": ["الخيار الأول", "الخيار الثاني", "الخيار الثالث", "الخيار الرابع"],
              "correctOptionIndex": 0, // دليل الخيار الأصح يبدأ من 0. للمقالي والإنشائي ضع القيمة 0 دائماً.
              "explanation": "شرح علمي بليغ ومفصل يفسر الإجابة الصحيحة أو الإجابة النموذجية المكتملة للمقالي لتصحيح وتوجيه أداء الطالب بالكامل."
            }
            
            تحذير هام للغاية: لا تكتب أي نصوص جانبية أو كلام التفافي خارج الـ JSON. لا تستخدم علامات ماركداون للـ JSON. أجب فقط كـ JSON خام.
        """.trimIndent()

        val prompt = """
            قم بإنشاء اختبار تفاعلي دقيق مكون من $numQuestions أسئلة للمادة التعليمية: "$materialTitle".
            
            نص المحتوى التعليمي المرجعي:
            $materialContent
        """.trimIndent()

        val response = generateContent(prompt, systemInstruction = systemPrompt, responseJsonSchema = true)

        if (response == "API_KEY_MISSING" || response.startsWith("ERROR_") || response.startsWith("عذراً")) {
            return getFallbackQuiz(materialTitle, numQuestions, type)
        }

        return try {
            val sanitized = sanitizeJsonString(response)
            val typeToken = Types.newParameterizedType(List::class.java, QuizQuestion::class.java)
            val adapter = moshi.adapter<List<QuizQuestion>>(typeToken)
            adapter.fromJson(sanitized) ?: getFallbackQuiz(materialTitle, numQuestions, type)
        } catch (e: Exception) {
            Log.e(TAG, "Failed to parse quiz JSON, response: $response", e)
            getFallbackQuiz(materialTitle, numQuestions, type)
        }
    }

    private fun getFallbackQuiz(title: String, numQuestions: Int, type: String): List<QuizQuestion> {
        val quiz = mutableListOf<QuizQuestion>()
        val options = when (type) {
            "TRUE_FALSE" -> listOf("صح ✅", "خطأ ❌")
            "ESSAY" -> listOf("عرض الإجابة النموذجية ومطابقتها ✔️")
            else -> listOf("الخيار أ - التفصيل السليم والنموذجي", "الخيار ب - تفصيل فرعي", "الخيار ج - فرضية مضللة", "الخيار د - مفهوم مقارب")
        }
        for (i in 1..numQuestions) {
            quiz.add(
                QuizQuestion(
                    text = "سؤال تفاعلي رقم $i ($type) حول موضوع: $title؟",
                    options = options,
                    correctOptionIndex = 0,
                    explanation = "هذا الشرح التوضيحي يوضح تفصيلياً النقاط الرئيسية المستهدفة في ملف المذاكرة ويصحح المفاهيم الخاطئة طبقاً للمنهج المعتمد."
                )
            )
        }
        return quiz
    }

    /**
     * Generates standard interactive Mind Map Nodes based on text content.
     */
    suspend fun generateMindMap(
        materialTitle: String,
        materialContent: String
    ): List<MindMapNode> {
        val systemPrompt = """
            أنت خبير خرائط المفاهيم والخرائط الذهنية وتصميم الأطر البصرية لتسهيل المذاكرة.
            قم بتحليل النص التعليمي واستخراج العناوين الرئيسية والفرعية وصياغتها كشجرة هرمية لخرائط المفاهيم.
            
            مطلوب كود مخرجات بتنسيق JSON حصرياً (JSON Array of Objects).
            كل عقدة تحتوي على:
            - "id": معرف فريد رقمي أو نصي (مثل "1", "2").
            - "label": عنوان العقدة بلغة عربية رصينة ومطوّرة (مثلاً: العنصر الأساسي، القسم الأول).
            - "parentId": معرف العقدة الأب (null للعقدة الجذرية، مثل "1" للعقد التابعة للعقدة 1).
            - "colorHex": لون العقدة بالهيكس ديسمل (مثلاً ألوان هادئة جذابة لتنسيق خريطة المذاكرة).
            - "x": إحداثي أفقي تقريبي لتوزيع العقد بذكاء (تتراوح بين -300 إلى 300).
            - "y": إحداثي رأسي تقريبي لتوزيع العقد بذكاء (تتراوح بين 0 إلى 600).
            
            تنبيه: لا تكتب أي مقدمات أو علامات ترميز. فقط أرسل مصفوفة الـ JSON مباشرةً.
        """.trimIndent()

        val prompt = """
            قم ببناء خريطة ذهنية لـ "$materialTitle" استناداً للمادة التالية:
            $materialContent
        """.trimIndent()

        val response = generateContent(prompt, systemInstruction = systemPrompt, responseJsonSchema = true)

        if (response == "API_KEY_MISSING" || response.startsWith("ERROR_") || response.startsWith("عذراً")) {
            return getFallbackMindMap(materialTitle)
        }

        return try {
            val sanitized = sanitizeJsonString(response)
            val type = Types.newParameterizedType(List::class.java, MindMapNode::class.java)
            val adapter = moshi.adapter<List<MindMapNode>>(type)
            adapter.fromJson(sanitized) ?: getFallbackMindMap(materialTitle)
        } catch (e: Exception) {
            Log.e(TAG, "Failed to parse mind map JSON, response: $response", e)
            getFallbackMindMap(materialTitle)
        }
    }

    private fun getFallbackMindMap(title: String): List<MindMapNode> {
        return listOf(
            MindMapNode("1", title, null, "#FF6D00", 0f, 50f),
            MindMapNode("2", "القسم الأول: المفاهيم الأساسية", "1", "#29B6F6", -150f, 180f),
            MindMapNode("3", "المصطلحات الرياضية واللغوية", "2", "#26A69A", -220f, 300f),
            MindMapNode("4", "طرق التلخيص والمذاكرة", "2", "#AB47BC", -80f, 300f),
            MindMapNode("5", "القسم الثاني: التطبيقات والتمارين", "1", "#66BB6A", 150f, 180f),
            MindMapNode("6", "الاختبارات الذاتية والواجبات", "5", "#FFA726", 80f, 300f),
            MindMapNode("7", "البطاقات التعليمية والتثبيت الذكي", "5", "#EC407A", 220f, 300f)
        )
    }
}
