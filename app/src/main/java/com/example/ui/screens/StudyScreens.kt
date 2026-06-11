package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.ui.res.painterResource
import com.example.R
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.horizontalScroll
import android.net.Uri
import android.provider.OpenableColumns
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.ui.platform.LocalContext
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.StudyMaterialEntity
import com.example.data.StudyPlanEntity
import com.example.ui.*
import com.example.ui.theme.*
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun NeumorphicCard3D(
    color: Color = MaterialTheme.colorScheme.surface,
    borderColor: Color = color.copy(alpha = 0.35f),
    depthY: androidx.compose.ui.unit.Dp = 6.dp,
    depthX: androidx.compose.ui.unit.Dp = 3.dp,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = color
        ),
        shape = RoundedCornerShape(20.dp),
        border = BorderStroke(1.dp, borderColor.copy(alpha = 0.25f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        content()
    }
}

@Composable
fun NeumorphicButton3D(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    color: Color = StudyBluePrimary,
    shape: RoundedCornerShape = RoundedCornerShape(18.dp),
    content: @Composable RowScope.() -> Unit
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(50.dp),
        shape = shape,
        colors = ButtonDefaults.buttonColors(
            containerColor = color
        ),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            content()
        }
    }
}

@Composable
fun ThreeDContainer(
    color: Color = StudyBluePrimary,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    NeumorphicCard3D(
        color = MaterialTheme.colorScheme.surface,
        borderColor = color,
        modifier = modifier,
        content = content
    )
}

@Composable
fun PortalWidget3D(
    title: String,
    subtitle: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    color: Color,
    badgeText: String? = null,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .padding(6.dp)
            .height(115.dp)
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        border = BorderStroke(1.dp, color.copy(alpha = 0.25f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(14.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.End
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                if (badgeText != null) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(color.copy(alpha = 0.12f))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = badgeText,
                            fontSize = 8.sp,
                            color = color,
                            fontWeight = FontWeight.Bold
                        )
                    }
                } else {
                    Spacer(modifier = Modifier.width(1.dp))
                }

                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(color.copy(alpha = 0.1f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = color,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Right,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = subtitle,
                    fontSize = 10.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                    textAlign = TextAlign.Right,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Composable
fun ActionGridCard(
    title: String,
    subtitle: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    color: Color,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(130.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        border = BorderStroke(1.dp, color.copy(alpha = 0.25f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(14.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.End
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(color.copy(alpha = 0.12f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = color,
                    modifier = Modifier.size(18.dp)
                )
            }
            
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Right,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(3.dp))
                Text(
                    text = subtitle,
                    fontSize = 10.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                    textAlign = TextAlign.Right,
                    lineHeight = 13.sp,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Composable
fun StudentHomeScreen(viewModel: AppViewModel) {
    val user = viewModel.loggedUser.collectAsState().value ?: return
    val materials by viewModel.filesList.collectAsState()
    val plans by viewModel.allPlanTasks.collectAsState()
    val quizzes by viewModel.allQuizzes.collectAsState()
    val announcements by viewModel.announcementsList.collectAsState()

    val completedPlansCount = plans.count { it.isCompleted }
    val totalPlansCount = plans.size
    val plansProgress = if (totalPlansCount > 0) completedPlansCount.toFloat() / totalPlansCount else 0.82f

    val totalMaterialCount = materials.size
    val totalQuizCount = quizzes.size

    val dateStr = SimpleDateFormat("EEEE, d MMMM yyyy", Locale("ar")).format(Date())
    val context = LocalContext.current

    // Launcher for real PDF and image file picking
    val filePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            try {
                // Query system openable database for physical name
                var displayName = "مستند_مرفوع"
                context.contentResolver.query(uri, null, null, null, null)?.use { cursor ->
                    if (cursor.moveToFirst()) {
                        val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                        if (nameIndex != -1) {
                            displayName = cursor.getString(nameIndex)
                        }
                    }
                }
                
                // Clean the file name extension
                val cleanTitle = displayName.substringBeforeLast(".")
                viewModel.inputMaterialTitle.value = cleanTitle
                
                // Set high quality simulated text content depending on the type
                val isImage = displayName.lowercase().run { endsWith(".png") || endsWith(".jpg") || endsWith(".jpeg") || endsWith(".webp") }
                val mimeType = context.contentResolver.getType(uri) ?: (if (isImage) "image/jpeg" else "application/pdf")
                viewModel.selectedFileType.value = if (isImage) "IMAGE" else "PDF"

                // Read actual physical bytes of image/PDF and fill View Model Base64 state
                context.contentResolver.openInputStream(uri)?.use { inputStream ->
                    val bytes = inputStream.readBytes()
                    val base64Str = android.util.Base64.encodeToString(bytes, android.util.Base64.NO_WRAP)
                    viewModel.uploadedFileBase64.value = base64Str
                    viewModel.uploadedFileMimeType.value = mimeType
                }
                
                viewModel.inputMaterialText.value = if (isImage) {
                    "🎨 [تم تحميل صورة مستندك بنجاح: $displayName. اضغط على الزر أدناه ليقوم جيميناي بمسح الصورة ضوئياً واستنتاج الشرح والامتحان!]"
                } else {
                    "📂 [تم تحميل ملف مستندك بنجاح: $displayName. اضغط على الزر أدناه ليقوم جيميناي بقراءة وتلخيص محتويات الـ PDF وصياغة الامتحان!]"
                }
            } catch (e: Exception) {
                // Fallback
                viewModel.inputMaterialTitle.value = "ملف تعليمي مرفوع"
                viewModel.inputMaterialText.value = "محتوى المستند للتوضيح والدراسة."
            }
        }
    }

    // Helper target option
    var selectedTargetGoal by remember { mutableStateOf("SUMMARY_EXAM") } // SUMMARY_EXAM, FLASHCARDS, MINDMAP

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.End
        ) {
            // Header bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Online tag
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(StudyBluePrimary.copy(alpha = 0.15f))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "المعلم الذكي نشط ⚡",
                        color = StudyBluePrimary,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Text(
                    text = dateStr,
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
                    textAlign = TextAlign.Right
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // App Launcher Icon Showcase & Brand Title
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, StudyOrangeAccent.copy(alpha = 0.25f)),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        horizontalAlignment = Alignment.End,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = "استوديو المذاكرة الذكي 🎓✨",
                            fontWeight = FontWeight.Black,
                            fontSize = 15.sp,
                            color = MaterialTheme.colorScheme.onSurface,
                            textAlign = TextAlign.Right
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "أقوى أدوات التلخيص التفاعلي وصناعة الفيديوهات بالذكاء الاصطناعي",
                            fontSize = 10.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.75f),
                            textAlign = TextAlign.Right,
                            lineHeight = 13.sp
                        )
                    }
                    Spacer(modifier = Modifier.width(14.dp))
                    // Gorgeous system launcher icon preview
                    Box(
                        modifier = Modifier
                            .size(54.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(MaterialTheme.colorScheme.surfaceVariant)
                            .border(1.5.dp, StudyOrangeAccent, RoundedCornerShape(16.dp))
                            .padding(4.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.smart_study_icon_1781186424076),
                            contentDescription = "أيقونة التطبيق الرسمية",
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(RoundedCornerShape(12.dp))
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Professional Profile Header Display
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.25f))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        horizontalAlignment = Alignment.End,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = "أهلاً بك يا ${user.username} 👋",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Black,
                            color = MaterialTheme.colorScheme.onSurface,
                            textAlign = TextAlign.Right
                        )
                        Text(
                            text = "الصف الدراسي الحالي: ${user.gradeLevel}",
                            fontSize = 12.sp,
                            color = StudyBluePrimary,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Right
                        )
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    // Styled Avatar symbol
                    Box(
                        modifier = Modifier
                            .size(54.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(
                                Brush.linearGradient(
                                    colors = listOf(StudyBluePrimary, StudyOrangeAccent)
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = user.username.take(1).uppercase(),
                            color = Color.White,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // ==========================================
            // MANDATORY DIRECT PDF & IMAGE UPLOAD & INTERACT AREA
            // ==========================================
            Text(
                text = "بوابة الرفع والتحليل الذكي الفوري 📑✨",
                fontWeight = FontWeight.Black,
                fontSize = 15.sp,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 6.dp),
                textAlign = TextAlign.Right
            )

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(2.dp, StudyBluePrimary.copy(alpha = 0.7f)),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.End
                ) {
                    Text(
                        text = "ارفع كتاباً، ملف PDF أو صورة مستند مأخوذة بكاميرا هاتفك لتلخيصها فورًا وصناعة اختبار تفاعلي في ثوانٍ معدودة!",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                        textAlign = TextAlign.Right,
                        lineHeight = 16.sp,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Selector row for file type
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        listOf(
                            "PDF" to "📄 ملف PDF",
                            "IMAGE" to "🖼️ صورة مستند",
                            "TEXT" to "✍️ نص مباشر"
                        ).forEach { (typeKey, label) ->
                            val isSelected = viewModel.selectedFileType.value == typeKey
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(if (isSelected) StudyBluePrimary else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                                    .clickable { viewModel.selectedFileType.value = typeKey }
                                    .padding(vertical = 8.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = label,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Elegant drag-and-drop visual upload container card
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(112.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(StudyBluePrimary.copy(alpha = 0.05f))
                            .border(
                                width = 1.5.dp,
                                color = StudyBluePrimary.copy(alpha = 0.35f),
                                shape = RoundedCornerShape(16.dp)
                            )
                            .clickable {
                                val currentType = viewModel.selectedFileType.value
                                if (currentType == "PDF") {
                                    filePickerLauncher.launch("application/pdf")
                                } else if (currentType == "IMAGE") {
                                    filePickerLauncher.launch("image/*")
                                } else {
                                    // Default/Fallback
                                    viewModel.selectedFileType.value = "PDF"
                                    filePickerLauncher.launch("application/pdf")
                                }
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center,
                            modifier = Modifier.padding(12.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.AddCircle, 
                                contentDescription = null,
                                tint = StudyBluePrimary,
                                modifier = Modifier.size(32.dp)
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = if (viewModel.selectedFileType.value == "PDF") {
                                    "اضغط هنا لرفع واستيراد ملف الـ PDF 📂"
                                } else if (viewModel.selectedFileType.value == "IMAGE") {
                                    "اضغط هنا لتصوير أو رفع صورة المستند 📸"
                                } else {
                                    "اضغط هنا لرفع واستيراد كتاب أو مستند 📑"
                                },
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp,
                                color = StudyBluePrimary,
                                textAlign = TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "يدعم الكتب المدرسية والملخصات لتوليد شرح وامتحانات تفاعلية فورية مخصصة لك",
                                fontSize = 9.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                                textAlign = TextAlign.Center
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Prompt Presets section for quick testing
                    Text(
                        text = "💡 أمثلة سريعة جاهزة للشحن الفوري بنقرة واحدة:",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = StudyOrangeAccent,
                        textAlign = TextAlign.Right,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.End
                    ) {
                        listOf(
                            Triple(
                                "🧬 تركيب الخلية ووظائفها الفائقة (أحياء)",
                                "الخلية هي الوحدة البنائية الأساسية لجميع الكائنات الحية. تتكون الخلية من نواة مركزية تنظم الانقسامات الخلوية وتحافظ على الحمض النووي DNA، ويحيط بها سائل السيتوبلازم وتسبح فيه مكونات هامة مثل الميتوكوندريا المسؤولة عن إنتاج جزيئات الطاقة الكيميائية ATP، وجهاز جولجي لإفراز المواد والبروتينات، وغشاء بلاسمي خارجي يحميها ويملك نفاذية اختيارية دقيقة لتنظيم حركة المياه والأملاح.",
                                "🧬 الخلية (أحياء)"
                            ),
                            Triple(
                                "🧪 الروابط التساهمية وتكافؤ الإلكترونات (كيمياء)",
                                "تنشأ الروابط التساهمية بين ذرات العناصر اللافلزية عندما تتشارك الإلكترونات في غلافها الخارجي بدلاً من نقلها بالكامل، وذلك لكي تصل كل ذرة إلى الاستقرار الثماني المكتمل. جزيء الماء H2O هو أشهر الروابط التساهمية الأحادية حيث تشارك ذرة الأكسجين بالإلكترونات مع ذرتي هيدروجين. من أهم سماتها: درجات غليان منخفضة مقارنة بالأيونية، وهي عازلة تماماً للكهرباء.",
                                "🧪 الذرة (كيمياء)"
                            ),
                            Triple(
                                "⚡ قوانين نيوتن وميكانيكا الاحتكاك (فيزياء)",
                                "تنص قوانين نيوتن للحركة على ثلاثة مبادئ ثورية: أولا قانون القصور الذاتي حيث يبقى الجسم الساكن ساكناً والمتحرك متحركاً ما لم تؤثر عليه قوة خارجية. القانون الثاني يثبت رياضياً أن القوة تساوي الكتلة ضرب التسارع (F = m * a). القانون الثالث يقر بالمعادلة الطبيعية لكل فعل رد فعل مساوٍ له في المقدار ومعاكس له في الاتجاه، وتعمل قوى الاحتكاك دائماً كقوة مقاومة للحركة.",
                                "⚡ القوة (فيزياء)"
                            )
                        ).forEach { (pTitle, pText, label) ->
                            Box(
                                modifier = Modifier
                                    .padding(horizontal = 3.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(MaterialTheme.colorScheme.surfaceVariant)
                                    .clickable {
                                        viewModel.inputMaterialTitle.value = pTitle
                                        viewModel.inputMaterialText.value = pText
                                    }
                                    .padding(horizontal = 8.dp, vertical = 6.dp)
                            ) {
                                Text(text = label, fontSize = 9.sp, fontWeight = FontWeight.Bold, color = StudyOrangeAccent)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Text Input Title
                    StudyTextInput(
                        value = viewModel.inputMaterialTitle.value,
                        onValueChange = { viewModel.inputMaterialTitle.value = it },
                        label = "عنوان المستند أو اسم الدرس",
                        placeholder = "عنوان الدرس الدراسي للعمل عليه..."
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Text Input Main Content
                    OutlinedTextField(
                        value = viewModel.inputMaterialText.value,
                        onValueChange = { viewModel.inputMaterialText.value = it },
                        label = { Text("محتوى الدرس المستخرج", modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Right) },
                        placeholder = { Text("اكتب هنا أو الصق نصوص شرح الدرس أو الكتاب لتوليد ملخص أو امتحان تلقائي...", modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Right) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(120.dp),
                        shape = RoundedCornerShape(12.dp),
                        textStyle = LocalTextStyle.current.copy(fontSize = 11.sp, textAlign = TextAlign.Right),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = StudyBluePrimary,
                            unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant
                        )
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Target Outcome Customizer
                    Text(
                        text = "اختر الخدمة المستهدفة بالذكاء الاصطناعي 🎯:",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface,
                        textAlign = TextAlign.Right,
                        modifier = Modifier.padding(bottom = 6.dp)
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        listOf(
                            "SUMMARY_EXAM" to "📄 تلخيص + امتحان",
                            "FLASHCARDS" to "🧠 بطاقات استذكار",
                            "MINDMAP" to "🗺️ خريطة ذهنية"
                        ).forEach { (goalKey, label) ->
                            val isSelected = selectedTargetGoal == goalKey
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(if (isSelected) StudyOrangeAccent else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                                    .clickable { selectedTargetGoal = goalKey }
                                    .padding(vertical = 8.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = label,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    if (viewModel.isUploadLoading.value || viewModel.isQuizGenerating.value || viewModel.isGeneratingFlashcards.value) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            CircularProgressIndicator(color = StudyOrangeAccent, modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("جاري معالجة المستند وصياغة الذكاء الاصطناعي...", fontWeight = FontWeight.Bold, fontSize = 11.sp, color = StudyOrangeAccent)
                        }
                    } else {
                        Button(
                            onClick = {
                                if (viewModel.inputMaterialTitle.value.trim().isEmpty() || viewModel.inputMaterialText.value.trim().isEmpty()) {
                                    // Autoload the first preset to safeguard against empty triggers
                                    viewModel.inputMaterialTitle.value = "🧬 تركيب الخلية ووظائفها الفائقة (أحياء)"
                                    viewModel.inputMaterialText.value = "الخلية هي الوحدة البنائية الأساسية لجميع الكائنات الحية. تتكون الخلية من نواة مركزية تنظم الانقسامات الخلوية وتحافظ على الحمض النووي DNA، ويحيط بها سائل السيتوبلازم وتسبح فيه مكونات هامة مثل الميتوكوندريا المسؤولة عن إنتاج جزيئات الطاقة ATP، وغشاء بلاسمي خارجي يحميها ويملك نفاذية اختيارية."
                                }
                                
                                viewModel.handleUploadMaterial {
                                    // Callback once material is saved!
                                    val mat = viewModel.activeMaterial.value
                                    if (mat != null) {
                                        when (selectedTargetGoal) {
                                            "SUMMARY_EXAM" -> {
                                                // Pre-generate quiz & open exams
                                                viewModel.fetchOrGenerateQuizForMaterial(mat) {
                                                    viewModel.navigateTo(Screen.Exams)
                                                }
                                            }
                                            "FLASHCARDS" -> {
                                                // Trigger flashcards and open flashcards
                                                viewModel.triggerGenerateFlashcards(mat)
                                                viewModel.navigateTo(Screen.Flashcards)
                                            }
                                            "MINDMAP" -> {
                                                // Open mindmaps
                                                viewModel.fetchOrGenerateMindMap(mat) {}
                                                viewModel.navigateTo(Screen.MindMaps)
                                            }
                                        }
                                    }
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp),
                            shape = RoundedCornerShape(14.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = StudyBluePrimary)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
                                Icon(Icons.Default.Star, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("تحليل وتوليد الأدوات الذكية ⚡", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.White)
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Study organizers & Quick statistics in a beautiful balanced 3-column Grid
            SectionHeader(title = "معدلات التفاعل والإنجاز 📊")
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Stat 1: Study Progress
                Card(
                    modifier = Modifier.weight(1f).height(115.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = BorderStroke(1.dp, StudyBluePrimary.copy(alpha = 0.15f)),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(10.dp).fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text("المهام والجدول", fontWeight = FontWeight.Bold, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurface)
                        Spacer(modifier = Modifier.height(6.dp))
                        Box(contentAlignment = Alignment.Center, modifier = Modifier.size(42.dp)) {
                            CircularProgressIndicator(
                                progress = { 1.0f },
                                modifier = Modifier.fillMaxSize(),
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.05f),
                                strokeWidth = 3.5.dp
                            )
                            CircularProgressIndicator(
                                progress = { plansProgress },
                                modifier = Modifier.fillMaxSize(),
                                color = StudyBluePrimary,
                                strokeWidth = 3.5.dp
                            )
                            Text("${(plansProgress * 100).toInt()}%", fontSize = 9.sp, fontWeight = FontWeight.Black, color = StudyBluePrimary)
                        }
                    }
                }

                // Stat 2: Active Materials
                Card(
                    modifier = Modifier.weight(1f).height(115.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = BorderStroke(1.dp, StudyOrangeAccent.copy(alpha = 0.15f)),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(10.dp).fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(StudyOrangeAccent.copy(alpha = 0.1f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Info, contentDescription = null, tint = StudyOrangeAccent, modifier = Modifier.size(16.dp))
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("ملخصات نشطة", fontWeight = FontWeight.Bold, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurface)
                        Text("$totalMaterialCount مستندات", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = StudyOrangeAccent)
                    }
                }

                // Stat 3: Solved Quizzes
                Card(
                    modifier = Modifier.weight(1f).height(115.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = BorderStroke(1.dp, StudySky.copy(alpha = 0.15f)),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(10.dp).fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(StudySky.copy(alpha = 0.1f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Check, contentDescription = null, tint = StudySky, modifier = Modifier.size(16.dp))
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("الاختبارات", fontWeight = FontWeight.Bold, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurface)
                        Text("$totalQuizCount مُنقضية", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = StudySky)
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // ==========================================
            // SLEEK SECONDARY ADAPTIVE ACCESS GRID
            // ==========================================
            SectionHeader(title = "الأقسام والأدوات الذكية 🛠️✨")

            // Grid of 6 beautifully crafted quick action cards (2x3 Grid System with consistent tile ratios)
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                // Row 1
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Box(modifier = Modifier.weight(1f)) {
                        ActionGridCard(
                            title = "المعلم والأستاذ المساعد 💬",
                            subtitle = "اسأل، لخص وناقش المواد مع الذكاء الاصطناعي التوليدي",
                            icon = Icons.Default.Send,
                            color = StudyBluePrimary,
                            onClick = { viewModel.navigateTo(Screen.AiAssistant) }
                        )
                    }
                    Box(modifier = Modifier.weight(1f)) {
                        ActionGridCard(
                            title = "شرح مرئي بالفيديو 🎥",
                            subtitle = "عرض السيناريو وجداول الرسوم التلخيصية للفيديو",
                            icon = Icons.Default.PlayArrow,
                            color = StudyOrangeAccent,
                            onClick = { viewModel.navigateTo(Screen.PdfToVideo) }
                        )
                    }
                }

                // Row 2
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Box(modifier = Modifier.weight(1f)) {
                        ActionGridCard(
                            title = "بطاقات التلقين السريع 🧠",
                            subtitle = "احفظ جزيئات دروسك بالتكرار المتباعد الذكي",
                            icon = Icons.Default.Refresh,
                            color = StudyEmerald,
                            onClick = { viewModel.navigateTo(Screen.Flashcards) }
                        )
                    }
                    Box(modifier = Modifier.weight(1f)) {
                        ActionGridCard(
                            title = "الخرائط الذهنية الذكية 🗺️",
                            subtitle = "ربط المخططات والمفاهيم بشكل مرئي رائع",
                            icon = Icons.Default.Share,
                            color = StudySky,
                            onClick = { viewModel.navigateTo(Screen.MindMaps) }
                        )
                    }
                }

                // Row 3
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Box(modifier = Modifier.weight(1f)) {
                        ActionGridCard(
                            title = "الخطة والمنظم 📅",
                            subtitle = "جدول مذاكرتك المدرسية اليومية والأسبوعية",
                            icon = Icons.Default.DateRange,
                            color = StudyGold,
                            onClick = { viewModel.navigateTo(Screen.StudyPlan) }
                        )
                    }
                    Box(modifier = Modifier.weight(1f)) {
                        ActionGridCard(
                            title = "المستندات المحملة 📁",
                            subtitle = "تصفح الكتب ومستندات الـ PDF المرفوعة",
                            icon = Icons.Default.Build,
                            color = StudyOrangeLight,
                            onClick = { viewModel.navigateTo(Screen.SavedFiles) }
                        )
                    }
                }
            }

        // Navigation into Teacher/Admin dashboards
        if (user.accountType == "TEACHER" || user.accountType == "ADMIN") {
            Spacer(modifier = Modifier.height(24.dp))
            SectionHeader(title = "أقسام الإشراف الإداري والتعليمي 👨‍🏫")
            if (user.accountType == "TEACHER") {
                ThreeDContainer(color = StudyOrangeAccent) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { viewModel.navigateTo(Screen.TeacherDashboard) }
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.PlayArrow, contentDescription = null, tint = StudyOrangeAccent)
                        Text("الدخول إلى لوحة المعلم الموجه 🎓", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    }
                }
            }
            if (user.accountType == "ADMIN") {
                Spacer(modifier = Modifier.height(10.dp))
                ThreeDContainer(color = StudyBluePrimary) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { viewModel.navigateTo(Screen.AdminDashboard) }
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.Settings, contentDescription = null, tint = StudyBluePrimary)
                        Text("الدخول إلى لوحة إدارة النظام ⚙️", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(32.dp))
    }
}
}

@Composable
fun SubjectsGridScreen(viewModel: AppViewModel) {
    val subjects = listOf(
        Triple("علم الأحياء", "الأحياء والتركيب الجيني", StudyEmerald),
        Triple("الفيزياء الحديثة", "الميكانيكا والطاقة الكونية", StudyBlueDark),
        Triple("الكيمياء العضوية", "العناصر والتفاعلات الحيوية", StudyOrangeAccent),
        Triple("الرياضيات واللغارتمات", "التفاضل وحلول الجبر المتقدم", StudyGold),
        Triple("اللغة العربية الفصحى", "النحو والصرف والبلاغة", StudyOrangeLight),
        Triple("اللغويات الأجنبية (إنجليزي)", "القواعد والكتابة الإبداعية", StudySky)
    )

    Column(modifier = Modifier.fillMaxSize().padding(16.dp), horizontalAlignment = Alignment.End) {
        Text("تصفح المناهج حسب المادة", fontSize = 22.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.Right)
        Text("اختر المادة التعليمية لاستقصاء المذكرات والملخصات المعتمدة.", fontSize = 13.sp, color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f), textAlign = TextAlign.Right)

        Spacer(modifier = Modifier.height(16.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(subjects) { (name, desc, color) ->
                Card(
                     modifier = Modifier
                        .fillMaxWidth()
                        .height(135.dp)
                        .clickable {
                            viewModel.studentSelectedSubject.value = name
                            viewModel.navigateTo(Screen.Lessons)
                        },
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = BorderStroke(1.dp, color.copy(alpha = 0.22f)),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(14.dp).fillMaxSize(),
                        verticalArrangement = Arrangement.SpaceBetween,
                        horizontalAlignment = Alignment.End
                    ) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(color.copy(alpha = 0.12f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = when(name) {
                                    "علم الأحياء" -> Icons.Default.Info
                                    "الفيزياء الحديثة" -> Icons.Default.Star
                                    "الكيمياء العضوية" -> Icons.Default.Build
                                    "الرياضيات واللغارتمات" -> Icons.Default.Add
                                    "اللغة العربية الفصحى" -> Icons.Default.Menu
                                    else -> Icons.Default.Send
                                },
                                contentDescription = null,
                                tint = color,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                        
                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                text = name,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                color = MaterialTheme.colorScheme.onSurface,
                                textAlign = TextAlign.Right,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = desc,
                                fontSize = 10.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                                textAlign = TextAlign.Right,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis,
                                lineHeight = 13.sp
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun LessonsListScreen(viewModel: AppViewModel) {
    val subject = viewModel.studentSelectedSubject.value
    val materials by viewModel.filesList.collectAsState()

    // Filter materials for this subject simulation
    val subjectMaterials = materials.filter {
        it.title.contains(subject) || it.sourceText.contains(subject) || it.title.contains("الخلية")
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp), horizontalAlignment = Alignment.End) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { viewModel.navigateBack() }) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back")
            }
            Text("محتوى وتلخيص مادة: $subject", fontSize = 18.sp, fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(12.dp))

        if (subjectMaterials.isEmpty()) {
            EmptyStateView(
                title = "لا توجد ملخصات مضافة لهذه المادة",
                description = "يمكنك النقر فوق زر 'رفع ملف وملخص' بالأسفل لبدء تلخيص درس جديد في مادة $subject فوراً!"
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = { viewModel.navigateTo(Screen.FileUpload) },
                modifier = Modifier.fillMaxWidth().height(48.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("رفع وتلخيص درس الآن 🧠", fontWeight = FontWeight.Bold)
            }
        } else {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(subjectMaterials) { mat ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp)
                            .clickable {
                                viewModel.selectActiveMaterial(mat)
                                viewModel.navigateTo(Screen.PdfSummary)
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
                                Text(text = mat.title, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(text = "النوع: ${mat.uploadType} | تاريخ الرفع: ملخص ذكي", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f))
                            }
                            Spacer(modifier = Modifier.width(16.dp))
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(StudyBluePrimary.copy(alpha = 0.12f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.Info, contentDescription = null, tint = StudyBluePrimary)
                            }
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FileUploadScreen(viewModel: AppViewModel) {
    val scroll = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(scroll),
        horizontalAlignment = Alignment.End
    ) {
        Text("بوابة تلخيص الملفات والمستندات بذكاء", fontSize = 20.sp, fontWeight = FontWeight.Bold)
        Text("أدخل نصوص الكتب، ملفات PDF أو الواجبات المدرسية لتلخيصها وحلها بذكاء.", fontSize = 12.sp, color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f), textAlign = TextAlign.Right)

        Spacer(modifier = Modifier.height(16.dp))

        // Select type row
        Text("اختر نوع المستند المرفوع:", fontWeight = FontWeight.Bold, fontSize = 14.sp)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            val fileTypes = listOf("PDF", "PPT", "Word", "IMAGE", "LINK")
            fileTypes.forEach { type ->
                val isSelected = viewModel.selectedFileType.value == type
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 4.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(if (isSelected) StudyBluePrimary else MaterialTheme.colorScheme.surfaceVariant)
                        .clickable { viewModel.selectedFileType.value = type }
                        .padding(vertical = 10.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = type,
                        color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        StudyTextInput(
            value = viewModel.inputMaterialTitle.value,
            onValueChange = { viewModel.inputMaterialTitle.value = it },
            label = "عنوان الدرس أو الملف",
            placeholder = "مثال: الكيمياء العضوية - المحاضرة الأولى"
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = viewModel.inputMaterialText.value,
            onValueChange = { viewModel.inputMaterialText.value = it },
            label = { Text("محتوى الدرس أو النص المستخرج من PDF", modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Right) },
            placeholder = { Text("الصق هنا نصوص الكتاب، أو الأسئلة، أو المحتوى التعليمي الكامل للتلخيص والمذاكرة...", modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Right) },
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp),
            shape = RoundedCornerShape(16.dp),
            textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.Right),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = StudyBluePrimary
            )
        )

        Spacer(modifier = Modifier.height(24.dp))

        if (viewModel.isUploadLoading.value) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                CircularProgressIndicator(color = StudyOrangeAccent)
                Spacer(modifier = Modifier.width(16.dp))
                Text("جاري قراءة الملف وتلخيصه بالذكاء الاصطناعي...", fontWeight = FontWeight.Bold, fontSize = 13.sp)
            }
        } else {
            Button(
                onClick = {
                    viewModel.handleUploadMaterial {
                        viewModel.navigateTo(Screen.SavedFiles)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = StudyBluePrimary)
            ) {
                Text("تحليل، تلخيص وبناء الفلاش كاردز ⚡", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Color.White)
            }
        }
    }
}

@Composable
fun SavedFilesScreen(viewModel: AppViewModel) {
    val list by viewModel.filesList.collectAsState()

    Column(modifier = Modifier.fillMaxSize().padding(16.dp), horizontalAlignment = Alignment.End) {
        Text("مكتبة ملخصاتي الذكية المحفوظة", fontSize = 20.sp, fontWeight = FontWeight.Bold)
        Text("تصفح ملخصاتك وراجع بطاقات الفلاش والخرائط الذهنية المصممة خصيصاً لك.", fontSize = 12.sp, color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f), textAlign = TextAlign.Right)

        Spacer(modifier = Modifier.height(16.dp))

        if (list.isEmpty()) {
            EmptyStateView(
                title = "لم تقم برفع أي ملفات حتى الآن",
                description = "يرجى الذهاب لتبويب 'رفع' وإدخال نصوص دروسك وملفاتك وسيتولى المساعد الذكي كل شيء!"
            )
        } else {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(list) { mat ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp)
                            .clickable {
                                viewModel.selectActiveMaterial(mat)
                                viewModel.navigateTo(Screen.PdfSummary)
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
                            IconButton(onClick = { viewModel.deleteMaterial(mat.id) }) {
                                Icon(Icons.Default.Close, contentDescription = "Delete", tint = Color.LightGray)
                            }
                            Spacer(modifier = Modifier.weight(1f))
                            Column(horizontalAlignment = Alignment.End) {
                                Text(mat.title, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                                Spacer(modifier = Modifier.height(4.dp))
                                Text("النوع: ${mat.uploadType} | تم التلخيص بعبقرية ✨", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f))
                            }
                            Spacer(modifier = Modifier.width(16.dp))
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(StudyBluePrimary.copy(alpha = 0.12f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.Info, contentDescription = null, tint = StudyBluePrimary)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun StudyPlanScreen(viewModel: AppViewModel) {
    val plans by viewModel.allPlanTasks.collectAsState()
    val scroll = rememberScrollState()

    val days = listOf("السبت", "الأحد", "الإثنين", "الثلاثاء", "الأربعاء", "الخميس", "الجمعة")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(scroll),
        horizontalAlignment = Alignment.End
    ) {
        Text("الخطة الدراسية والمنظم الأسبوعي", fontSize = 20.sp, fontWeight = FontWeight.Bold)
        Text("خطط أوقات مذاكرتك والتزم بمراجعة تكرارية لتسجيل أحسن الدرجات.", fontSize = 12.sp, color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f), textAlign = TextAlign.Right)

        Spacer(modifier = Modifier.height(16.dp))

        // Create form
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f))
        ) {
            Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.End) {
                Text("إضافة مهمة جديدة لجدولك", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                Spacer(modifier = Modifier.height(12.dp))

                StudyTextInput(
                    value = viewModel.planTaskInputTitle.value,
                    onValueChange = { viewModel.planTaskInputTitle.value = it },
                    label = "اسم الدرس أو المهمة",
                    placeholder = "مثال: مراجعة الخلية وحل اختبار"
                )

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Day Selector
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        var expanded by remember { mutableStateOf(false) }
                        Box {
                            Button(
                                onClick = { expanded = true },
                                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                            ) {
                                Text(viewModel.planTaskInputDay.value, fontWeight = FontWeight.Bold, color = Color.White)
                            }
                            DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                                days.forEach { day ->
                                    DropdownMenuItem(
                                        text = { Text(day, textAlign = TextAlign.Right, modifier = Modifier.fillMaxWidth()) },
                                        onClick = {
                                            viewModel.planTaskInputDay.value = day
                                            expanded = false
                                        }
                                    )
                                }
                            }
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("اليوم الأسبوعي:", fontSize = 12.sp)
                    }

                    // Duration Input
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.width(120.dp)) {
                        StudyTextInput(
                            value = viewModel.planTaskInputDuration.value,
                            onValueChange = { viewModel.planTaskInputDuration.value = it },
                            label = "المدة (ق)",
                            placeholder = "45"
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = { viewModel.addStudyPlanTask() },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = StudyOrangeAccent),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text("إضافة المهمة للجدول الأسبوعي", fontWeight = FontWeight.Bold, color = Color.White)
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Render days
        days.forEach { day ->
            val dayPlans = plans.filter { it.dayOfWeek == day }
            if (dayPlans.isNotEmpty()) {
                Text(
                    text = "📅 يوم $day",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = StudyBluePrimary,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
                dayPlans.forEach { task ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .padding(12.dp)
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            IconButton(onClick = { viewModel.deleteStudyPlanTask(task.id) }) {
                                Icon(Icons.Default.Close, contentDescription = "Delete", tint = Color.LightGray)
                            }
                            Column(horizontalAlignment = Alignment.End, modifier = Modifier.weight(1f)) {
                                Text(
                                    text = task.title,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    style = LocalTextStyle.current.copy(
                                        textDecoration = if (task.isCompleted) androidx.compose.ui.text.style.TextDecoration.LineThrough else null
                                    )
                                )
                                Text("المدة المقترحة: ${task.durationMinutes} دقيقة | الساعة: ${task.timeLabel}", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f))
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Checkbox(
                                checked = task.isCompleted,
                                onCheckedChange = { viewModel.toggleStudyPlanStatus(task.id, task.isCompleted) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ResultsTrackerScreen(viewModel: AppViewModel) {
    val quizzes by viewModel.allQuizzes.collectAsState()

    Column(modifier = Modifier.fillMaxSize().padding(16.dp), horizontalAlignment = Alignment.End) {
        Text("تقارير وتحليلات الأداء الأكاديمي", fontSize = 20.sp, fontWeight = FontWeight.Bold)
        Text("راجع كشوف درجاتك التفاعلية واقضِ على نقاط الضعف بالاستذكار التكراري.", fontSize = 12.sp, color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f), textAlign = TextAlign.Right)

        Spacer(modifier = Modifier.height(16.dp))

        if (quizzes.isEmpty()) {
            EmptyStateView(
                title = "لم تجتز أي اختبارات بعد",
                description = "عند انتهائك من تلخيص أو قراءة أي درس، اضغط على تبويب 'بدء اختبار' ليقوم الذكاء الاصطناعي باختبارك وحفظ درجاتك هنا ومتابعة تطورك."
            )
        } else {
            val totalScore = quizzes.sumOf { it.score }
            val totalQuestions = quizzes.sumOf { it.totalQuestions }
            val avgPercentage = if (totalQuestions > 0) (totalScore.toFloat() / totalQuestions * 100).toInt() else 0

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = StudyBluePrimary)
            ) {
                Column(modifier = Modifier.padding(20.dp), horizontalAlignment = Alignment.End) {
                    Text("المعدل الدراسي العام للذكاء", color = Color.White, fontSize = 14.sp)
                    Text("$avgPercentage%", fontSize = 36.sp, fontWeight = FontWeight.Black, color = Color.White)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("إجمالي الأسئلة المستجابة: $totalQuestions سؤال | الإجابات الصحيحة: $totalScore", fontSize = 12.sp, color = Color.White.copy(alpha = 0.8f))
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
            SectionHeader(title = "سجل الاختبارات التفصيلي")

            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(quizzes) { q ->
                    val dateFormatted = SimpleDateFormat("yyyy/MM/dd", Locale.getDefault()).format(Date(q.timestamp))
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .padding(16.dp)
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "${q.score} / ${q.totalQuestions}",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (q.score >= q.totalQuestions / 2) StudyBluePrimary else Color.Red
                            )
                            Column(horizontalAlignment = Alignment.End, modifier = Modifier.weight(1f).padding(end = 12.dp)) {
                                Text(q.title, fontWeight = FontWeight.Bold, fontSize = 14.sp, textAlign = TextAlign.Right)
                                Text("تاريخ تقديم الامتحان: $dateFormatted", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f))
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun FlashcardsScreen(viewModel: AppViewModel) {
    val materials by viewModel.filesList.collectAsState()
    var selectedMat by remember { mutableStateOf<StudyMaterialEntity?>(null) }
    val horizontalScrollState = androidx.compose.foundation.rememberScrollState()

    // Auto-select first material if none is selected
    LaunchedEffect(materials) {
        if (selectedMat == null && materials.isNotEmpty()) {
            selectedMat = materials.first()
        }
    }

    LaunchedEffect(selectedMat) {
        selectedMat?.id?.let { matId ->
            viewModel.fetchFlashcardsForMaterial(matId)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.End
    ) {
        // Toolbar Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { viewModel.navigateBack() }) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back")
            }
            Text(
                text = "مراجعة بطاقات الاستذكار 🧠",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Right
            )
        }

        Text(
            text = "طريقة الحفظ الأسرع بالتكرار المتباعد والتلقين الذكي.",
            fontSize = 11.sp,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
            textAlign = TextAlign.Right,
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
        )

        if (materials.isEmpty()) {
            EmptyStateView(
                title = "لم يتم العثور على أي ملفات دراسية",
                description = "لالبدء، يرجى ملء مكتبتك عبر رفع ملف PDF أو موضوع علمي من لوحة التحكم الرئيسية."
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = { viewModel.navigateTo(Screen.SavedFiles) },
                colors = ButtonDefaults.buttonColors(containerColor = StudyBluePrimary),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text("المكتبة والرفع ⚡", fontWeight = FontWeight.Bold, color = Color.White)
            }
        } else {
            // Horizontal material chips selector
            Text(
                text = "اختر الدرس أو الموضوع للمذاكرة:",
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp,
                textAlign = TextAlign.Right,
                modifier = Modifier.fillMaxWidth().padding(bottom = 6.dp)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
                    .horizontalScroll(horizontalScrollState),
                horizontalArrangement = Arrangement.End
            ) {
                materials.forEach { mat ->
                    val isSelected = selectedMat?.id == mat.id
                    Box(
                        modifier = Modifier
                            .padding(horizontal = 4.dp, vertical = 2.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(if (isSelected) StudyOrangeAccent else MaterialTheme.colorScheme.surfaceVariant)
                            .clickable { selectedMat = mat }
                            .padding(horizontal = 12.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = mat.title,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }

            // Render Flashcards Interactive Deck View
            selectedMat?.let { mat ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.8f)),
                    shape = RoundedCornerShape(24.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.35f))
                ) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        FlashcardsTab(viewModel = viewModel, mat = mat)
                    }
                }
            }
        }
    }
}

@Composable
fun MindMapsScreen(viewModel: AppViewModel) {
    val materials by viewModel.filesList.collectAsState()
    var selectedMat by remember { mutableStateOf<StudyMaterialEntity?>(null) }
    val horizontalScrollState = androidx.compose.foundation.rememberScrollState()

    // Auto-select first material if none is selected
    LaunchedEffect(materials) {
        if (selectedMat == null && materials.isNotEmpty()) {
            selectedMat = materials.first()
        }
    }

    LaunchedEffect(selectedMat) {
        selectedMat?.let { mat ->
            viewModel.fetchOrGenerateMindMap(mat) {}
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.End
    ) {
        // Toolbar Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { viewModel.navigateBack() }) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back")
            }
            Text(
                text = "الخرائط الذهنية الذكية 🗺️",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Right
            )
        }

        Text(
            text = "مخططات مفاهيمية مرئية تربط جزيئات الدروس وتسهل الحفظ والتذكر.",
            fontSize = 11.sp,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
            textAlign = TextAlign.Right,
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
        )

        if (materials.isEmpty()) {
            EmptyStateView(
                title = "لم يتم العثور على أي ملفات دراسية",
                description = "للقيام برسم خرائط ذهنية تفاعلية، يرجى ملء مكتبتك أولاً عبر كتابة نصوص أو رفع مستند."
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = { viewModel.navigateTo(Screen.SavedFiles) },
                colors = ButtonDefaults.buttonColors(containerColor = StudyBluePrimary),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text("المكتبة والرفع ⚡", fontWeight = FontWeight.Bold, color = Color.White)
            }
        } else {
            // Horizontal material chips selector
            Text(
                text = "اختر الدرس لرسم خريطته الذهنية:",
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp,
                textAlign = TextAlign.Right,
                modifier = Modifier.fillMaxWidth().padding(bottom = 6.dp)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
                    .horizontalScroll(horizontalScrollState),
                horizontalArrangement = Arrangement.End
            ) {
                materials.forEach { mat ->
                    val isSelected = selectedMat?.id == mat.id
                    Box(
                        modifier = Modifier
                            .padding(horizontal = 4.dp, vertical = 2.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(if (isSelected) StudyBluePrimary else MaterialTheme.colorScheme.surfaceVariant)
                            .clickable { selectedMat = mat }
                            .padding(horizontal = 12.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = mat.title,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }

            // Render Mind Map Node View
            selectedMat?.let { mat ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.8f)),
                    shape = RoundedCornerShape(24.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.35f))
                ) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        MindmapTab(viewModel = viewModel, mat = mat)
                    }
                }
            }
        }
    }
}
