package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.horizontalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.LocalTextStyle
import com.example.ui.*
import com.example.ui.AppViewModel
import com.example.ui.Screen
import com.example.ui.theme.StudyBluePrimary
import com.example.ui.theme.StudyOrangeAccent

@Composable
fun ExamsScreen(viewModel: AppViewModel) {
    val questions = viewModel.activeQuizQuestions
    val selectedIdx = viewModel.quizSelectedQuestionIdx.value
    val isSubmitted = viewModel.isQuizSubmitted.value
    val isGenerating = viewModel.isQuizGenerating.value

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.End
    ) {
        // Headers
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { viewModel.navigateBack() }) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back")
            }
            Text(text = viewModel.activeQuizTitle.value, fontSize = 18.sp, fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (isGenerating) {
            Box(modifier = Modifier.fillMaxWidth().height(300.dp), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    CircularProgressIndicator(color = StudyOrangeAccent)
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("جاري توليد وتجهيز أسئلة الاختبار التفاعلية...", fontWeight = FontWeight.Bold)
                }
            }
            return
        }

        if (questions.isEmpty()) {
            Box(modifier = Modifier.fillMaxWidth().height(300.dp), contentAlignment = Alignment.Center) {
                Text("لا توجد أسئلة نشطة للحل.", fontWeight = FontWeight.Bold)
            }
            return
        }

        // Display results badge if submitted
        if (isSubmitted) {
            val correctCount = questions.count { it.selectedOptionIndex == it.correctOptionIndex }
            val percentage = (correctCount.toFloat() / questions.size * 100).toInt()

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                colors = CardDefaults.cardColors(containerColor = StudyBluePrimary),
                shape = RoundedCornerShape(20.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp), horizontalAlignment = Alignment.End) {
                    Text("تهانينا يا بطل! تم تقديم الاختبار بنجاح 🏆", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("النتيجة الإجمالية: $correctCount من أصل ${questions.size} بمعدل $percentage%", color = Color.White, fontSize = 14.sp)
                    Spacer(modifier = Modifier.height(12.dp))
                    Button(
                        onClick = { viewModel.navigateTo(Screen.Results) },
                        colors = ButtonDefaults.buttonColors(containerColor = StudyOrangeAccent),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text("عرض كشف تقارير الأداء 📊", fontWeight = FontWeight.Bold, color = Color.White)
                    }
                }
            }

            // AI Performance diagnosis and weaknesses profiling card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                colors = CardDefaults.cardColors(containerColor = StudyOrangeAccent.copy(alpha = 0.08f)),
                shape = RoundedCornerShape(20.dp),
                border = BorderStroke(1.dp, StudyOrangeAccent.copy(alpha = 0.3f))
            ) {
                Column(modifier = Modifier.padding(18.dp), horizontalAlignment = Alignment.End) {
                    Text("📊 التقرير التشخيصي وتحليل نقاط الضعف بالذكاء الاصطناعي:", fontWeight = FontWeight.Bold, color = StudyOrangeAccent, fontSize = 14.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    val wrongQuestions = questions.filter { it.selectedOptionIndex != it.correctOptionIndex }
                    if (wrongQuestions.isEmpty()) {
                        Text("مذهل للغاية! لقد حققت علامة كاملة في كافة محاور ومفاهيم هذا الاختبار. أداؤك يقارب 100%، مما يدل على استيعاب عبقري شامل للمحتوى والأركان الفرعية. استمر في المذاكرة الذكية والتقدم نحو الدرس الموالي! 🚀", fontSize = 11.sp, textAlign = TextAlign.Right, lineHeight = 18.sp)
                    } else {
                        Text("بناءً على إجاباتك، يوصي معلمو الذكاء الاصطناعي بالتركيز على مراجعة المحاور التالية التي تعثرت بها:", fontSize = 11.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.Right)
                        Spacer(modifier = Modifier.height(6.dp))
                        wrongQuestions.forEachIndexed { i, q ->
                            Text("← السؤال المتعثر [${i+1}]: \"${q.text.take(75)}...\"", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant, textAlign = TextAlign.Right, modifier = Modifier.padding(vertical = 2.dp))
                            if (q.explanation.isNotBlank()) {
                                Text("💡 التفسير النموذجي: ${q.explanation}", fontSize = 10.5.sp, color = StudyBluePrimary, fontWeight = FontWeight.Medium, textAlign = TextAlign.Right, modifier = Modifier.padding(bottom = 6.dp))
                            }
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("💡 نصيحتنا الأكاديمية: يرجى الانتقال إلى تبويب 'الملخص الفائق' واختيار النمط 'تلخيص الفصول منفصلة' أو مراجعة 'الخريطة الذهنية' للربط البصري وتفادي ذات العقبات في المسابقات القادمة.", fontSize = 11.sp, color = StudyBluePrimary, fontWeight = FontWeight.Bold, textAlign = TextAlign.Right, lineHeight = 18.sp)
                    }
                }
            }
        }

        // Progress bar indicators
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            questions.indices.forEach { index ->
                val isSelected = selectedIdx == index
                val isAnswered = questions[index].selectedOptionIndex != -1
                val isCorrect = isAnswered && questions[index].selectedOptionIndex == questions[index].correctOptionIndex
                
                val bulletColor = when {
                    isSubmitted -> if (isCorrect) Color.Green else Color.Red
                    isSelected -> StudyBluePrimary
                    isAnswered -> StudyBluePrimary.copy(alpha = 0.4f)
                    else -> MaterialTheme.colorScheme.surfaceVariant
                }

                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .padding(horizontal = 2.dp)
                        .clip(CircleShape)
                        .background(bulletColor)
                        .clickable { viewModel.quizSelectedQuestionIdx.value = index },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "${index + 1}",
                        color = if (isSelected || isSubmitted || isAnswered) Color.White else MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Question Details Card
        val q = questions[selectedIdx]

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp)
        ) {
            Column(modifier = Modifier.padding(20.dp), horizontalAlignment = Alignment.End) {
                Text(
                    text = "سؤال رقم ${selectedIdx + 1}:",
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    color = StudyBluePrimary
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = q.text,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    textAlign = TextAlign.Right,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Options List
        q.options.indices.forEach { optIdx ->
            val optText = q.options[optIdx]
            val isSelectedOpt = q.selectedOptionIndex == optIdx
            
            val isCorrectOpt = q.correctOptionIndex == optIdx
            val borderOptColor = when {
                isSubmitted && isCorrectOpt -> Color.Green
                isSubmitted && isSelectedOpt && !isCorrectOpt -> Color.Red
                isSelectedOpt -> StudyBluePrimary
                else -> MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)
            }

            val bgOptColor = when {
                isSubmitted && isCorrectOpt -> Color.Green.copy(alpha = 0.1f)
                isSubmitted && isSelectedOpt && !isCorrectOpt -> Color.Red.copy(alpha = 0.1f)
                isSelectedOpt -> StudyBluePrimary.copy(alpha = 0.08f)
                else -> MaterialTheme.colorScheme.surface
            }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .border(1.5.dp, borderOptColor, RoundedCornerShape(14.dp))
                    .clickable(enabled = !isSubmitted) {
                        q.selectedOptionIndex = optIdx
                        // trigger UI rebuild by updating active list hack
                        viewModel.activeQuizQuestions[selectedIdx] = q.copy()
                    },
                colors = CardDefaults.cardColors(containerColor = bgOptColor),
                shape = RoundedCornerShape(14.dp)
            ) {
                Row(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = optText,
                        fontWeight = FontWeight.Medium,
                        fontSize = 14.sp,
                        textAlign = TextAlign.Right,
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 12.dp)
                    )
                    RadioButton(
                        selected = isSelectedOpt,
                        onClick = {
                            if (!isSubmitted) {
                                q.selectedOptionIndex = optIdx
                                viewModel.activeQuizQuestions[selectedIdx] = q.copy()
                            }
                        },
                        enabled = !isSubmitted
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Explanation accordion
        if (isSubmitted && q.explanation.isNotEmpty()) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
            ) {
                Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.End) {
                    Text("💡 توضيح الإجابة الصحيحة وشرح الأستاذ:", fontWeight = FontWeight.Bold, color = StudyOrangeAccent, fontSize = 13.sp)
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(text = q.explanation, fontSize = 13.sp, lineHeight = 20.sp, textAlign = TextAlign.Right)
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        // Navigate question Buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                onClick = {
                    if (selectedIdx < questions.size - 1) {
                        viewModel.quizSelectedQuestionIdx.value = selectedIdx + 1
                    }
                },
                enabled = selectedIdx < questions.size - 1,
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.surfaceVariant, contentColor = MaterialTheme.colorScheme.onSurfaceVariant)
            ) {
                Text("التالي")
            }

            if (!isSubmitted) {
                Button(
                    onClick = { viewModel.submitQuiz() },
                    colors = ButtonDefaults.buttonColors(containerColor = StudyOrangeAccent)
                ) {
                    Text("تقديم الإجابات وتصحيح الاختبار 🎓", color = Color.White)
                }
            }

            Button(
                onClick = {
                    if (selectedIdx > 0) {
                        viewModel.quizSelectedQuestionIdx.value = selectedIdx - 1
                    }
                },
                enabled = selectedIdx > 0,
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.surfaceVariant, contentColor = MaterialTheme.colorScheme.onSurfaceVariant)
            ) {
                Text("السابق")
            }
        }
    }
}

@Composable
fun AiAssistantScreen(viewModel: AppViewModel) {
    val chatMessages = viewModel.chatHistory
    val isChatLoading = viewModel.isChatLoading.value
    var showImageSimulator by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxSize()) {
        // Headers
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .border(1.dp, StudyOrangeAccent, RoundedCornerShape(8.dp))
                    .clickable { showImageSimulator = !showImageSimulator }
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Text(
                    text = "📷 رفع صورة سؤال",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = StudyOrangeAccent
                )
            }
            Text("مساعد المذاكرة الذكي المباشر", fontSize = 18.sp, fontWeight = FontWeight.Bold)
        }

        // Image question simulator selector
        AnimatedVisibility(visible = showImageSimulator) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                colors = CardDefaults.cardColors(containerColor = StudyOrangeAccent.copy(alpha = 0.08f))
            ) {
                Column(modifier = Modifier.padding(12.dp), horizontalAlignment = Alignment.End) {
                    Text("💡 جرب إرسال صورة سؤال كرتوني لحله بالذكاء الاصطناعي:", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = StudyOrangeAccent)
                    Spacer(modifier = Modifier.height(8.dp))

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                        Button(
                            onClick = {
                                viewModel.submitImageQuestion("مسألة جبر: حل المعادلة 3x + 5 = 20 لإيجاد قيمة المتغير مجهول النسبة x.")
                                showImageSimulator = false
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = StudyBluePrimary),
                            modifier = Modifier.padding(horizontal = 4.dp),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text("📐 مسألة رياضيات", fontSize = 11.sp, color = Color.White)
                        }

                        Button(
                            onClick = {
                                viewModel.submitImageQuestion("صورة أحياء: صفحة توضح غشاء بلاسمي ومكونات الخلية وسائلا سبيس الميتوكوندريا.")
                                showImageSimulator = false
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = StudyBluePrimary),
                            modifier = Modifier.padding(horizontal = 4.dp),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text("🧬 علم الأحياء", fontSize = 11.sp, color = Color.White)
                        }
                    }
                }
            }
        }

        // Messages List
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            reverseLayout = false
        ) {
            items(chatMessages) { msg ->
                val isUser = msg.sender == "USER"
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    horizontalArrangement = if (isUser) Arrangement.End else Arrangement.Start
                ) {
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = if (isUser) StudyBluePrimary else MaterialTheme.colorScheme.surfaceVariant
                        ),
                        shape = RoundedCornerShape(
                            topStart = 16.dp,
                            topEnd = 16.dp,
                            bottomStart = if (isUser) 16.dp else 0.dp,
                            bottomEnd = if (isUser) 0.dp else 16.dp
                        ),
                        modifier = Modifier.widthIn(max = 280.dp)
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(
                                text = msg.text,
                                color = if (isUser) Color.White else MaterialTheme.colorScheme.onSurface,
                                fontSize = 13.sp,
                                textAlign = if (isUser) TextAlign.Right else TextAlign.Left,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }
            }
            if (isChatLoading) {
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        horizontalArrangement = Arrangement.Start
                    ) {
                        CircularProgressIndicator(
                            color = StudyOrangeAccent,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("جاري توليد الرد من المعلم المساعد الذكي...", fontSize = 12.sp, color = Color.Gray)
                    }
                }
            }
        }

        // Input bottom bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface)
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = { viewModel.sendChatQuery() },
                enabled = viewModel.chatbotQuery.value.isNotBlank() && !isChatLoading,
                colors = IconButtonDefaults.iconButtonColors(containerColor = StudyBluePrimary, contentColor = Color.White)
            ) {
                Icon(Icons.Default.Send, contentDescription = "Send", modifier = Modifier.size(18.dp))
            }

            Spacer(modifier = Modifier.width(8.dp))

            OutlinedTextField(
                value = viewModel.chatbotQuery.value,
                onValueChange = { viewModel.chatbotQuery.value = it },
                placeholder = { Text("اطرح أي سؤال دراسي، أو لخص قاعدة نحو معينة...", modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Right) },
                modifier = Modifier.weight(1f),
                textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.Right),
                shape = RoundedCornerShape(24.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = StudyBluePrimary
                )
            )
        }
    }
}

@Composable
fun PdfSummaryWorkspaceScreen(viewModel: AppViewModel) {
    val mat = viewModel.activeMaterial.value ?: return
    var selectedTab by remember { mutableStateOf(0) }

    Column(modifier = Modifier.fillMaxSize()) {
        // Workspace headers
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { viewModel.navigateBack() }) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back")
            }
            Text("مساعد المذاكرة: ${mat.title}", fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }

        // Tab Selector Row
        TabRow(selectedTabIndex = selectedTab, containerColor = MaterialTheme.colorScheme.background) {
            Tab(selected = selectedTab == 3, onClick = { selectedTab = 3 }) {
                Text("برزنتيشن 📊", modifier = Modifier.padding(10.dp), fontWeight = FontWeight.Bold, fontSize = 12.sp)
            }
            Tab(selected = selectedTab == 2, onClick = { selectedTab = 2 }) {
                Text("الخريطة 🪵", modifier = Modifier.padding(10.dp), fontWeight = FontWeight.Bold, fontSize = 12.sp)
            }
            Tab(selected = selectedTab == 1, onClick = { selectedTab = 1 }) {
                Text("البطاقات 🗃️", modifier = Modifier.padding(10.dp), fontWeight = FontWeight.Bold, fontSize = 12.sp)
            }
            Tab(selected = selectedTab == 0, onClick = { selectedTab = 0 }) {
                Text("الملخص الفائق 📝", modifier = Modifier.padding(10.dp), fontWeight = FontWeight.Bold, fontSize = 12.sp)
            }
        }

        // Render Tabs
        Box(modifier = Modifier.weight(1f).fillMaxWidth()) {
            when (selectedTab) {
                0 -> SummaryTab(viewModel, mat)
                1 -> FlashcardsTab(viewModel, mat)
                2 -> MindmapTab(viewModel, mat)
                3 -> PresentationTab(viewModel, mat)
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SummaryTab(viewModel: AppViewModel, mat: com.example.data.StudyMaterialEntity) {
    val isSpeaking by viewModel.isSpeaking.collectAsState()
    val isSummaryGenerating = viewModel.isSummaryGenerating.value
    val summaryContent = viewModel.customSummaryContent.value

    // Auto-sync local summary to custom on first launch
    LaunchedEffect(mat.id) {
        if (viewModel.customSummaryContent.value.isEmpty()) {
            viewModel.customSummaryContent.value = mat.summary
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.End
    ) {
        // AI Customizers panel
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = StudyBluePrimary.copy(alpha = 0.05f)),
            border = BorderStroke(1.dp, StudyBluePrimary.copy(alpha = 0.2f))
        ) {
            Column(modifier = Modifier.padding(14.dp), horizontalAlignment = Alignment.End) {
                Text("🪄 تخصيص التلخيص بالذكاء الاصطناعي:", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = StudyBluePrimary)
                Spacer(modifier = Modifier.height(10.dp))

                // Summary types
                Text("نمط التلخيص ومحاور العرض:", fontWeight = FontWeight.Bold, fontSize = 11.sp, color = Color.Gray)
                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    val types = listOf(
                        Triple("GENERAL", "ملخص شامل", Icons.Default.Info),
                        Triple("CHAPTERS", "الفصول منفصلة", Icons.Default.List),
                        Triple("EXAM_REVIEW", "مراجعة الامتحان", Icons.Default.Star)
                    )
                    types.forEach { (typeCode, typeLabel, icon) ->
                        val isSelected = viewModel.activeSummaryType.value == typeCode
                        Box(
                            modifier = Modifier
                                .padding(horizontal = 3.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (isSelected) StudyBluePrimary else MaterialTheme.colorScheme.surfaceVariant)
                                .clickable { viewModel.regenerateSummary(mat, typeCode, viewModel.activeSummaryLength.value) }
                                .padding(horizontal = 8.dp, vertical = 6.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(typeLabel, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface)
                                Spacer(modifier = Modifier.width(3.dp))
                                Icon(icon, contentDescription = null, modifier = Modifier.size(12.dp), tint = if (isSelected) Color.White else Color.Gray)
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Summary lengths
                Text("عمق وحجم التلخيص المقترح:", fontWeight = FontWeight.Bold, fontSize = 11.sp, color = Color.Gray)
                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    val lengths = listOf(
                        Pair("SHORT", "قصير وموجز"),
                        Pair("MEDIUM", "متوسط متوازن"),
                        Pair("DETAILED", "شامل ومفصل")
                    )
                    lengths.forEach { (lengthCode, lengthLabel) ->
                        val isSelected = viewModel.activeSummaryLength.value == lengthCode
                        Box(
                            modifier = Modifier
                                .padding(horizontal = 3.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (isSelected) StudyOrangeAccent else MaterialTheme.colorScheme.surfaceVariant)
                                .clickable { viewModel.regenerateSummary(mat, viewModel.activeSummaryType.value, lengthCode) }
                                .padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Text(lengthLabel, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface)
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedButton(
                onClick = {
                    if (isSpeaking) {
                        viewModel.stopSpeaking()
                    } else {
                        viewModel.speak(summaryContent)
                    }
                }
            ) {
                Text(if (isSpeaking) "إيقاف القراء ⏹️" else "استماع ناطق 🔊")
            }
            Text("محتوى الملخص التفاعلي", fontWeight = FontWeight.Bold, fontSize = 15.sp)
        }

        Spacer(modifier = Modifier.height(8.dp))

        if (isSummaryGenerating) {
            Card(
                modifier = Modifier.fillMaxWidth().height(180.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        CircularProgressIndicator(color = StudyOrangeAccent)
                        Spacer(modifier = Modifier.height(12.dp))
                        Text("جاري إعادة صياغة الملخص بالذكاء الاصطناعي...", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        } else {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f))
            ) {
                Text(
                    text = summaryContent,
                    modifier = Modifier.padding(16.dp).fillMaxWidth(),
                    fontSize = 14.sp,
                    lineHeight = 22.sp,
                    textAlign = TextAlign.Right
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
        SectionHeader(title = "أدوات بناء الاختبارات التوجيهية 🎯")

        // Interactive Quiz Generator Configurator Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.End) {
                Text("صمم اختبارك الذكي المدار بضمير المعلم:", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = StudyBluePrimary)
                Spacer(modifier = Modifier.height(12.dp))

                // Quiz Type Radio Selector
                Text("نوع الأسئلة للامتحان التقييمي:", fontWeight = FontWeight.Bold, fontSize = 11.sp, color = Color.Gray)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val types = listOf(
                        Pair("MCQ", "اختيارات"),
                        Pair("TRUE_FALSE", "صح وخطأ"),
                        Pair("FILL_BLANKS", "أكمل فراغات"),
                        Pair("ESSAY", "حل مقالي")
                    )
                    types.forEach { (typeKey, label) ->
                        val isSelected = viewModel.selectedQuizType.value == typeKey
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(label, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                            RadioButton(
                                selected = isSelected,
                                onClick = { viewModel.selectedQuizType.value = typeKey }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Quiz Difficulty Radio Selector
                Text("مستوى صعوبة الأسئلة التفاعلية:", fontWeight = FontWeight.Bold, fontSize = 11.sp, color = Color.Gray)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val difficulties = listOf(
                        Pair("EASY", "سهل مباشر 🥬"),
                        Pair("MEDIUM", "متوسط معقد 🧠"),
                        Pair("HARD", "صعب للعباقرة 🔥")
                    )
                    difficulties.forEach { (diffKey, label) ->
                        val isSelected = viewModel.selectedQuizDifficulty.value == diffKey
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(label, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                            RadioButton(
                                selected = isSelected,
                                onClick = { viewModel.selectedQuizDifficulty.value = diffKey }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Action launch button
                Button(
                    onClick = {
                        viewModel.fetchOrGenerateQuizForMaterial(
                            material = mat,
                            numQ = viewModel.selectedQuizNumQ.value,
                            type = viewModel.selectedQuizType.value,
                            difficulty = viewModel.selectedQuizDifficulty.value
                        ) {
                            viewModel.navigateTo(Screen.Exams)
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = StudyBluePrimary),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth().height(48.dp)
                ) {
                    Text("انطلاق ومباشرة الاختبار التفاعلي 🚀", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color.White)
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Large Video player widget
        Button(
            onClick = {
                viewModel.runPdfToVideoConversion(mat) {
                    viewModel.navigateTo(Screen.PdfToVideo)
                }
            },
            colors = ButtonDefaults.buttonColors(containerColor = StudyOrangeAccent),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth().height(48.dp)
        ) {
            Text("🎥 إنتاج فيديو شرح ناطق بالذكاء الاصطناعي", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color.White)
        }
    }
}

@Composable
fun FlashcardsTab(viewModel: AppViewModel, mat: com.example.data.StudyMaterialEntity) {
    val deck = viewModel.activeFlashcardDeck
    val isGen = viewModel.isGeneratingFlashcards.value

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (deck.isEmpty()) {
            if (isGen) {
                CircularProgressIndicator(color = StudyOrangeAccent)
                Spacer(modifier = Modifier.height(16.dp))
                Text("جاري استخراج البطاقة الذكية والحلول من الدرس...")
            } else {
                EmptyStateView(
                    title = "مذكرات فلاش كاردز غير نشطة",
                    description = "استخرج بطاقات الملاحظات والاختبار المتبادل تلقائياً من هذا الدرس بالكامل!"
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = { viewModel.triggerGenerateFlashcards(mat) },
                    colors = ButtonDefaults.buttonColors(containerColor = StudyBluePrimary),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("استخراج بطاقات فلاش ذكية 🎃", fontWeight = FontWeight.Bold)
                }
            }
        } else {
            val selectedIdx = viewModel.flashcardSelectedIdx.value
            val isFlipped = viewModel.isFlashcardFlipped.value

            if (selectedIdx < deck.size) {
                val card = deck[selectedIdx]
                val rotation by androidx.compose.animation.core.animateFloatAsState(
                    targetValue = if (isFlipped) 180f else 0f,
                    animationSpec = androidx.compose.animation.core.tween(500)
                )

                Text(
                    text = "بطاقة ${selectedIdx + 1} من ${deck.size}",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
                    modifier = Modifier.padding(bottom = 12.dp)
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(240.dp)
                        .graphicsLayer {
                            rotationY = rotation
                            cameraDistance = 12f * density
                        }
                        .clip(RoundedCornerShape(24.dp))
                        .background(if (isFlipped) StudyOrangeAccent.copy(alpha = 0.08f) else StudyBluePrimary.copy(alpha = 0.08f))
                        .border(1.5.dp, if (isFlipped) StudyOrangeAccent else StudyBluePrimary, RoundedCornerShape(24.dp))
                        .clickable { viewModel.isFlashcardFlipped.value = !isFlipped }
                        .padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Text(
                            text = if (isFlipped) card.back else card.front,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = if (isFlipped) "📲 انقر فوق البطاقة لإخفاء التعريف" else "📲 انقر فوق البطاقة للكشف عن التعريف",
                            fontSize = 10.sp,
                            color = Color.Gray,
                            textAlign = TextAlign.Center
                        )
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                    Button(
                        onClick = {
                            viewModel.markFlashcardKnown(card, false)
                            viewModel.isFlashcardFlipped.value = false
                            if (selectedIdx < deck.size - 1) {
                                viewModel.flashcardSelectedIdx.value = selectedIdx + 1
                            } else {
                                viewModel.flashcardSelectedIdx.value = 0
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Red.copy(alpha = 0.1f)),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("أحتاج لمراجعته ⚠️", color = Color.Red, fontWeight = FontWeight.Bold)
                    }

                    Button(
                        onClick = {
                            viewModel.markFlashcardKnown(card, true)
                            viewModel.isFlashcardFlipped.value = false
                            if (selectedIdx < deck.size - 1) {
                                viewModel.flashcardSelectedIdx.value = selectedIdx + 1
                            } else {
                                viewModel.flashcardSelectedIdx.value = 0
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Green.copy(alpha = 0.15f)),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("عرفته وحفظته ✓", color = Color.Green, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
fun MindmapTab(viewModel: AppViewModel, mat: com.example.data.StudyMaterialEntity) {
    val nodes = viewModel.activeMindMapNodes
    val isGen = viewModel.isGeneratingMindMap.value
    var selectedNodeForDetail by remember { mutableStateOf<com.example.data.MindMapNode?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (nodes.isEmpty()) {
            if (isGen) {
                CircularProgressIndicator(color = StudyOrangeAccent)
                Spacer(modifier = Modifier.height(16.dp))
                Text("جارت تهيئة وهيكلة الخريطة الهرمية للشرح البصري...")
            } else {
                EmptyStateView(
                    title = "الخريطة الذهنية البصرية غير نشطة",
                    description = "اضغط على البدء للحصول على ربط بائن ذكي لكافة الأركان الهامة بمخطط شجري تفاعلي!"
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = { viewModel.fetchOrGenerateMindMap(mat) {} },
                    colors = ButtonDefaults.buttonColors(containerColor = StudyOrangeAccent),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("انشاء مخطط هرمي فوري 🗺️", fontWeight = FontWeight.Bold)
                }
            }
        } else {
            Text("الخريطة الشجرية الهيكلية للدرس 🪵", fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 6.dp))
            Text("💡 انقر على أي مفهوم لمطالعة الشرح التفصيلي عبر الذكاء الاصطناعي", fontSize = 11.sp, color = Color.Gray, modifier = Modifier.padding(bottom = 12.dp))
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(nodes) { node ->
                    val isRoot = node.parentId == null
                    val paddingLeft = if (isRoot) 0.dp else 24.dp
                    val containerColor = if (isRoot) StudyOrangeAccent.copy(alpha = 0.08f) else StudyBluePrimary.copy(alpha = 0.06f)
                    val borderColor = if (isRoot) StudyOrangeAccent else StudyBluePrimary

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = paddingLeft, top = 4.dp, bottom = 4.dp)
                            .clickable {
                                viewModel.fetchDeepConceptExplanation(node, mat)
                                selectedNodeForDetail = node
                            },
                        colors = CardDefaults.cardColors(containerColor = containerColor),
                        shape = RoundedCornerShape(12.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, borderColor)
                    ) {
                        Row(
                            modifier = Modifier
                                .padding(12.dp)
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = if (isRoot) Icons.Default.Star else Icons.Default.Info,
                                contentDescription = null,
                                tint = borderColor,
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                text = node.label,
                                fontWeight = if (isRoot) FontWeight.Bold else FontWeight.Medium,
                                fontSize = if (isRoot) 14.sp else 13.sp,
                                color = MaterialTheme.colorScheme.onSurface,
                                textAlign = TextAlign.Right,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
            }
        }
    }

    val currentNodeForDetail = selectedNodeForDetail
    if (currentNodeForDetail != null) {
        AlertDialog(
            onDismissRequest = { selectedNodeForDetail = null },
            title = {
                Text(
                    text = "شرح مفهوم: ${currentNodeForDetail.label}",
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Right,
                    modifier = Modifier.fillMaxWidth()
                )
            },
            text = {
                Column(horizontalAlignment = Alignment.End, modifier = Modifier.fillMaxWidth()) {
                    if (viewModel.isFetchingNodeExplanation.value) {
                        CircularProgressIndicator(color = StudyOrangeAccent)
                        Spacer(modifier = Modifier.height(10.dp))
                        Text("جاري الاتصال بمعلم الذكاء الاصطناعي لتفسير المفهوم...", textAlign = TextAlign.Right)
                    } else {
                        Text(
                            text = viewModel.activeNodeDetailedExplanation.value,
                            textAlign = TextAlign.Right,
                            modifier = Modifier.fillMaxWidth(),
                            fontSize = 13.sp
                        )
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = { viewModel.speak(viewModel.activeNodeDetailedExplanation.value) },
                    colors = ButtonDefaults.buttonColors(containerColor = StudyBluePrimary)
                ) {
                    Text("🔊 قراءة صوتية", color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = { selectedNodeForDetail = null }) {
                    Text("إغلاق")
                }
            }
        )
    }
}

@Composable
fun PresentationTab(viewModel: AppViewModel, mat: com.example.data.StudyMaterialEntity) {
    val slides = viewModel.activePresentationSlides
    val isGen = viewModel.isGeneratingPresentation.value
    var selectedSlideForEdit by remember { mutableStateOf<Int?>(null) }
    var slideEditTitle by remember { mutableStateOf("") }
    var slideEditContent by remember { mutableStateOf("") }

    LaunchedEffect(mat.id) {
        if (slides.isEmpty()) {
            viewModel.generateOrFetchPresentation(mat) {}
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (isGen) {
            Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    CircularProgressIndicator(color = StudyOrangeAccent)
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("جاري تخطيط وصياغة شرائح العرض التقديمي بالذكاء الاصطناعي...", textAlign = TextAlign.Center)
                }
            }
        } else if (slides.isEmpty()) {
            Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                Text("المعذرة، لم نتمكن من العثور على شرائح جاهزة.")
            }
        } else {
            // Header Action Buttons for Exporter
            Row(
                modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                var exportStatusMessage by remember { mutableStateOf("") }
                var isExporting by remember { mutableStateOf(false) }

                LaunchedEffect(isExporting) {
                    if (isExporting) {
                        kotlinx.coroutines.delay(1500)
                        exportStatusMessage = "تم حفظ مستند العرض بنجاح وبصيغة المذاكرة المفضلة! 📤"
                        isExporting = false
                    }
                }

                if (exportStatusMessage.isNotEmpty()) {
                    LaunchedEffect(exportStatusMessage) {
                        kotlinx.coroutines.delay(4000)
                        exportStatusMessage = ""
                    }
                    Text(
                        text = exportStatusMessage,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = StudyOrangeAccent,
                        modifier = Modifier.weight(1f).padding(end = 6.dp),
                        textAlign = TextAlign.Right
                    )
                }

                Row {
                    Button(
                        onClick = { isExporting = true },
                        colors = ButtonDefaults.buttonColors(containerColor = StudyBluePrimary),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.padding(horizontal = 4.dp)
                    ) {
                        Text("📊 تصدير PPTX", fontSize = 11.sp, color = Color.White)
                    }
                    Button(
                        onClick = { isExporting = true },
                        colors = ButtonDefaults.buttonColors(containerColor = StudyOrangeAccent),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.padding(horizontal = 4.dp)
                    ) {
                        Text("📄 تصدير PDF", fontSize = 11.sp, color = Color.White)
                    }
                }
            }

            // Presentation list scroll container
            LazyColumn(modifier = Modifier.weight(1f).fillMaxWidth()) {
                itemsIndexed(slides) { index, slide ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        border = BorderStroke(1.dp, StudyBluePrimary.copy(alpha = 0.2f)),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                    ) {
                        Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.End) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Button(
                                    onClick = {
                                        selectedSlideForEdit = index
                                        slideEditTitle = slide.title
                                        slideEditContent = slide.narration
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                                    shape = RoundedCornerShape(8.dp),
                                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
                                ) {
                                    Text("تعديل الشريحة ✏️", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurface)
                                }
                                Text("الشريحة ${index + 1}", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = StudyOrangeAccent)
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            // Graphic presentation layout simulation
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(StudyBluePrimary.copy(alpha = 0.04f))
                                    .border(1.dp, StudyBluePrimary.copy(alpha = 0.1f))
                                    .padding(12.dp)
                            ) {
                                Column(horizontalAlignment = Alignment.End, modifier = Modifier.fillMaxWidth()) {
                                    Text(slide.title, fontWeight = FontWeight.ExtraBold, fontSize = 14.sp, color = StudyBluePrimary, textAlign = TextAlign.Right)
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text(slide.slideVisuals, fontSize = 11.sp, color = Color.Gray, textAlign = TextAlign.Right)
                                }
                            }

                            Spacer(modifier = Modifier.height(10.dp))
                            Text("محتوى الشريحة والسيناريو:", fontWeight = FontWeight.Bold, fontSize = 11.sp, color = Color.Gray)
                            Text(slide.narration, fontSize = 12.sp, textAlign = TextAlign.Right, modifier = Modifier.fillMaxWidth())
                        }
                    }
                }
            }
        }
    }

    if (selectedSlideForEdit != null) {
        val idx = selectedSlideForEdit!!
        AlertDialog(
            onDismissRequest = { selectedSlideForEdit = null },
            title = { Text("تعديل الشريحة رقم ${idx + 1}", modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Right) },
            text = {
                Column(horizontalAlignment = Alignment.End, modifier = Modifier.fillMaxWidth()) {
                    OutlinedTextField(
                        value = slideEditTitle,
                        onValueChange = { slideEditTitle = it },
                        label = { Text("عنوان الشريحة") },
                        modifier = Modifier.fillMaxWidth(),
                        textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.Right)
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    OutlinedTextField(
                        value = slideEditContent,
                        onValueChange = { slideEditContent = it },
                        label = { Text("نص الشريحة") },
                        modifier = Modifier.fillMaxWidth().height(120.dp),
                        textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.Right)
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val original = slides[idx]
                        viewModel.updatePresentationSlide(idx, original.copy(title = slideEditTitle, narration = slideEditContent))
                        selectedSlideForEdit = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = StudyBluePrimary)
                ) {
                    Text("حفظ التعديل 💾", color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = { selectedSlideForEdit = null }) {
                    Text("إلغاء")
                }
            }
        )
    }
}

@Composable
fun SavedVideosScreen(viewModel: AppViewModel) {
    val list by viewModel.allSavedVideos.collectAsState()

    Column(modifier = Modifier.fillMaxSize().padding(16.dp), horizontalAlignment = Alignment.End) {
        Text("أشرطة الفيديو الشارحة المنتجة 🎥", fontSize = 20.sp, fontWeight = FontWeight.Bold)
        Text("شاهد ملخصات شرائح المذاكرة التفاعلية الناطقة بمختلف اللغات والموجهات.", fontSize = 12.sp, color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f), textAlign = TextAlign.Right)

        Spacer(modifier = Modifier.height(16.dp))

        if (list.isEmpty()) {
            EmptyStateView(
                title = "لم تنتج أي مواد فيديو شرح بعد",
                description = "افتح أي تلخيص محفوظ ثم انقر على زر 'فيديو شرح ناطق' ليقوم مساعدك الذكي ببرمجة سكريبت صوتي وسيناريو بستاني رائع."
            )
        } else {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(list) { vid ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp)
                            .clickable {
                                viewModel.selectActiveVideo(vid)
                                viewModel.navigateTo(Screen.PdfToVideo)
                            },
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .padding(16.dp)
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.End,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(horizontalAlignment = Alignment.End, modifier = Modifier.weight(1f)) {
                                Text(vid.title, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                Spacer(modifier = Modifier.height(4.dp))
                                Text("المدة: ${vid.durationType} | الراوي: ${if (vid.voiceType == "FEMALE") "بنت (آلاء)" else "ولد (مازن)"}", fontSize = 11.sp, color = Color.Gray)
                            }
                            Spacer(modifier = Modifier.width(16.dp))
                            Box(
                                modifier = Modifier
                                    .size(44.dp)
                                    .clip(CircleShape)
                                    .background(StudyOrangeAccent),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.PlayArrow, contentDescription = null, tint = Color.White)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PdfToVideoPlayerScreen(viewModel: AppViewModel) {
    val slides = viewModel.activeVideoSlides
    val currentIdx = viewModel.activeVideoSlideIdx.value
    val isPlaying = viewModel.isVideoPlaying.value

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Headers
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = {
                viewModel.stopSpeaking()
                viewModel.navigateBack()
            }) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back")
            }
            Text(text = "فيديو شرح ذكي تفاعلي", fontSize = 18.sp, fontWeight = FontWeight.Bold)

            if (slides.isNotEmpty()) {
                IconButton(onClick = {
                    viewModel.stopSpeaking()
                    viewModel.activeVideoSlides.clear()
                }) {
                    Icon(Icons.Default.Add, contentDescription = "New Video")
                }
            } else {
                Spacer(modifier = Modifier.width(48.dp))
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (slides.isEmpty()) {
            val fileList by viewModel.filesList.collectAsState()
            val isConverting = viewModel.isConvertingVideo.value
            
            var localTitle by remember { mutableStateOf("") }
            var localContent by remember { mutableStateOf("") }
            var ocrStatusMsg by remember { mutableStateOf("") }
            val scrollState = rememberScrollState()

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState),
                horizontalAlignment = Alignment.End
            ) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = StudyBluePrimary.copy(alpha = 0.05f)),
                    shape = RoundedCornerShape(16.dp),
                    border = BorderStroke(1.dp, StudyBluePrimary.copy(alpha = 0.15f))
                ) {
                    Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.End) {
                        Text("🎥 صانع ومثبت الفيديوهات التعليمية بالذكاء الاصطناعي", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = StudyBluePrimary, textAlign = TextAlign.Right)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            "حوّل أي مستند (PDF) أو صورة شرح أو موضوع دراسي معقد إلى فيديو تفاعلي مسموع ومرئي بالثواني! قم باختيار ملف، أو الصق محتواه تحت، مع اختيار مدة الفيديو المفضلة ونبرة المعلق.",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                            lineHeight = 18.sp,
                            textAlign = TextAlign.Right
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                if (isConverting) {
                    Card(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 32.dp),
                        colors = CardDefaults.cardColors(containerColor = StudyOrangeAccent.copy(alpha = 0.05f)),
                        shape = RoundedCornerShape(16.dp),
                        border = BorderStroke(1.dp, StudyOrangeAccent.copy(alpha = 0.2f))
                    ) {
                        Column(
                            modifier = Modifier.padding(24.dp).fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            CircularProgressIndicator(color = StudyOrangeAccent)
                            Spacer(modifier = Modifier.height(16.dp))
                            Text("جاري توليد وإنتاج فيديو الشرح التفاعلي ⚡🎥", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("يقوم المساعد بقراءة واستكشاف نصوص المستند وتفكيكها إلى سيناريو شرائح كامل مدعوم برسومات متحركة ووصف بصري متكامل...", fontSize = 11.sp, color = Color.Gray, textAlign = TextAlign.Center)
                        }
                    }
                } else {
                    // Part 1: Select source
                    Text("1️⃣ اختر ملفاً دراسياً أو مصدراً للشرح:", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    Spacer(modifier = Modifier.height(8.dp))

                    if (fileList.isNotEmpty()) {
                        Text("اختر ملف سريع من ملخصاتك المحفوظة:", fontSize = 11.sp, color = Color.Gray)
                        Spacer(modifier = Modifier.height(4.dp))
                        
                        // Horizontal selection chips
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .horizontalScroll(rememberScrollState()),
                            horizontalArrangement = Arrangement.End
                        ) {
                            fileList.forEach { mat ->
                                val isSelected = localTitle == mat.title
                                Box(
                                    modifier = Modifier
                                        .padding(4.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(if (isSelected) StudyOrangeAccent else MaterialTheme.colorScheme.surfaceVariant)
                                        .clickable {
                                            localTitle = mat.title
                                            localContent = mat.sourceText
                                        }
                                        .padding(horizontal = 10.dp, vertical = 6.dp)
                                ) {
                                    Text(
                                        text = "📄 ${mat.title}",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface
                                    )
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                    }

                    // Demo topics chips for simulated camera image OCR
                    Text("أو امسح صورة / مستند (نماذج مسح حية 📸):", fontSize = 11.sp, color = Color.Gray)
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.End
                    ) {
                        val demos = listOf(
                            "خلية الأحياء" to "تركيب الخلية الحية والنواة وجدار الخلية وغشائها، والفرق الواضح برسم كارتوني بين الخلايا النباتية والحيوانية والدور الحيوي للميتوكوندريا في صنع وإنتاج طاقة الخلايا والكائن البشري والحيوان.",
                            "تفاعلات الكيمياء" to "ملخص يتكلم عن تفاعلات الأكسدة والارجاع، والعوامل المؤكسدة والمختزلة مع معادلات كيمياء السوائل والغازات بشكل رصين مع دور الروابط الهيدروجينية والتساهمية في السوائل.",
                            "قوانين الحركة" to "تقرير عن قوانين الحركة الثلاثة لإسحاق نيوتن والتطبيقات الحية والتجريبية عليها في الطيران والسيارات والفضاء الكوني وقوة الاحتكاك والتسارع وقصور الأجسام الذاتي والفعلي."
                        )

                        demos.forEach { (topicName, topicContent) ->
                            Box(
                                modifier = Modifier
                                    .padding(4.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(MaterialTheme.colorScheme.surfaceVariant)
                                    .clickable {
                                        ocrStatusMsg = "📸 جاري استخراج النص وتصفيته بالـ OCR..."
                                        localTitle = topicName
                                        localContent = topicContent
                                        ocrStatusMsg = "✅ تم استخراج النصوص بنجاح والتعرف عليها!"
                                    }
                                    .padding(horizontal = 10.dp, vertical = 6.dp)
                            ) {
                                Text(
                                    text = "⚡ $topicName",
                                    fontSize = 11.sp,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    }

                    if (ocrStatusMsg.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(ocrStatusMsg, fontSize = 11.sp, color = StudyBluePrimary, fontWeight = FontWeight.Bold, textAlign = TextAlign.Right)
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Text fields
                    OutlinedTextField(
                        value = localTitle,
                        onValueChange = { localTitle = it },
                        label = { Text("عنوان الفيديو التعليمي", modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Right) },
                        placeholder = { Text("مثال: شرح تركيب الحمض النووي (DNA)", modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Right) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.Right),
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = StudyBluePrimary)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = localContent,
                        onValueChange = { localContent = it },
                        label = { Text("أدخل أو الصق نص الشرح بالتفصيل", modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Right) },
                        placeholder = { Text("الصق هنا نصوص مستخرج PDF أو صور الشروحات ليعيد صياغتها المعلق الصوتي...", modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Right) },
                        modifier = Modifier.fillMaxWidth().height(140.dp),
                        shape = RoundedCornerShape(12.dp),
                        textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.Right),
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = StudyBluePrimary)
                    )

                    Spacer(modifier = Modifier.height(18.dp))

                    // Part 2: Video settings
                    Text("2️⃣ حدد إعدادات ونمط الإلقاء:", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    Spacer(modifier = Modifier.height(10.dp))

                    // Video Duration Selection
                    Text("طول الفيديو وعدد شاشات العرض:", fontSize = 11.sp, color = Color.Gray)
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        val durations = listOf(
                            "مفصل (8 شرائح)" to "DETAILED",
                            "متوسط (5 شرائح)" to "MEDIUM",
                            "سريع (3 شرائح)" to "SHORT"
                        )
                        durations.forEach { (label, key) ->
                            val isSelected = viewModel.selectedVideoDuration.value == key
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(horizontal = 4.dp)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(if (isSelected) StudyBluePrimary else MaterialTheme.colorScheme.surfaceVariant)
                                    .clickable { viewModel.selectedVideoDuration.value = key }
                                    .padding(vertical = 10.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = label,
                                    fontSize = 10.sp,
                                    color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Voice Selection
                    Text("نبرة صوت المعلق للسكريبت الناطق:", fontSize = 11.sp, color = Color.Gray)
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        val voices = listOf(
                            "آلاء (صوت بنت 👩‍🏫)" to "FEMALE",
                            "مازن (صوت ولد 👨‍🏫)" to "MALE"
                        )
                        voices.forEach { (label, key) ->
                            val isSelected = viewModel.selectedVoiceGender.value == key
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(horizontal = 4.dp)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(if (isSelected) StudyOrangeAccent else MaterialTheme.colorScheme.surfaceVariant)
                                    .clickable { viewModel.selectedVoiceGender.value = key }
                                    .padding(vertical = 10.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = label,
                                    fontSize = 11.sp,
                                    color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Sparking Generate Button
                    Button(
                        onClick = {
                            if (localTitle.isNotBlank() && localContent.isNotBlank()) {
                                viewModel.runPdfToVideoConversionWithText(localTitle, localContent) {
                                    viewModel.isVideoPlaying.value = true
                                }
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(54.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = StudyBluePrimary),
                        shape = RoundedCornerShape(14.dp),
                        enabled = localTitle.isNotBlank() && localContent.isNotBlank()
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.PlayArrow, contentDescription = null, tint = Color.White)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("توليد وإنتاج فيديو الشرح التفاعلي ⚡🎥", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color.White)
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Show Saved Videos section underneath!
                    Text("أشرطة الفيديو المنتجة سابقاً 🎥:", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    val savedVideos by viewModel.allSavedVideos.collectAsState(initial = emptyList())
                    if (savedVideos.isEmpty()) {
                        Text("لا توجد مقاطع منتجة ومحفوظة حالياً. جرب كتابة أو رفع ملف لإنتاج سكريبتك الأول!", fontSize = 11.sp, color = Color.Gray, textAlign = TextAlign.Right, modifier = Modifier.fillMaxWidth())
                    } else {
                        savedVideos.forEach { vid ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                                    .clickable {
                                        viewModel.selectActiveVideo(vid)
                                    },
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Row(
                                    modifier = Modifier.padding(12.dp).fillMaxWidth(),
                                    horizontalArrangement = Arrangement.End,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column(horizontalAlignment = Alignment.End, modifier = Modifier.weight(1f)) {
                                        Text(vid.title, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                        Text("المدة: ${vid.durationType} | الراوي: ${if (vid.voiceType == "FEMALE") "آلاء 🕵️‍♀️" else "مازن 🕵️‍♂️"}", fontSize = 10.sp, color = Color.Gray)
                                    }
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Box(
                                        modifier = Modifier
                                            .size(32.dp)
                                            .clip(CircleShape)
                                            .background(StudyOrangeAccent.copy(alpha = 0.15f)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(Icons.Default.PlayArrow, contentDescription = null, tint = StudyOrangeAccent, modifier = Modifier.size(16.dp))
                                    }
                                }
                            }
                        }
                    }
                }
            }
            return
        }

        val activeSlide = slides[currentIdx]

        // Virtual Screen Canvas (Player)
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(280.dp),
            colors = CardDefaults.cardColors(containerColor = StudyBluePrimary),
            shape = RoundedCornerShape(24.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Box(
                        modifier = Modifier
                            .background(Color.White.copy(alpha = 0.15f), RoundedCornerShape(8.dp))
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = "شريحة ${currentIdx + 1} من ${slides.size}",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = activeSlide.title,
                        color = Color.White,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = activeSlide.slideVisuals,
                        color = Color.White.copy(alpha = 0.8f),
                        fontSize = 13.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    Text(
                        text = if (isPlaying) "🔊 المعلم يقرأ السكريبت الصوتي الآن..." else "⏸️ تشغيل الصوتيات",
                        color = StudyOrangeAccent,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Slide script textual translation
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.End) {
                Text("سكريبت الشرح الصوتي 🎙️", fontWeight = FontWeight.Bold, color = StudyBluePrimary, fontSize = 12.sp)
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = activeSlide.narration,
                    fontSize = 14.sp,
                    lineHeight = 20.sp,
                    textAlign = TextAlign.Right,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        // Player Controls Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Next
            IconButton(
                onClick = {
                    if (currentIdx < slides.size - 1) {
                        viewModel.activeVideoSlideIdx.value = currentIdx + 1
                    }
                },
                enabled = currentIdx < slides.size - 1
            ) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Next")
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Play/Pause Action
            IconButton(
                onClick = {
                    val status = !isPlaying
                    viewModel.isVideoPlaying.value = status
                    if (status) {
                        viewModel.speak(activeSlide.narration)
                    } else {
                        viewModel.stopSpeaking()
                    }
                },
                modifier = Modifier
                    .size(70.dp)
                    .clip(CircleShape)
                    .background(StudyOrangeAccent)
            ) {
                Icon(
                    imageVector = if (isPlaying) Icons.Default.Close else Icons.Default.PlayArrow,
                    contentDescription = "Play/Pause",
                    tint = Color.White,
                    modifier = Modifier.size(36.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Prev
            IconButton(
                onClick = {
                    if (currentIdx > 0) {
                        viewModel.activeVideoSlideIdx.value = currentIdx - 1
                    }
                },
                enabled = currentIdx > 0
            ) {
                Icon(Icons.Default.ArrowForward, contentDescription = "Prev")
            }
        }
    }
}
