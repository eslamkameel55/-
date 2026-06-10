package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
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
import com.example.ui.theme.StudyBluePrimary
import com.example.ui.theme.StudyOrangeAccent
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
    Box(
        modifier = modifier.padding(bottom = depthY, end = depthX)
    ) {
        // Soft outer Dark Shadow on bottom-right (Tactile deep recess)
        Box(
            modifier = Modifier
                .matchParentSize()
                .offset(y = depthY, x = depthX)
                .clip(RoundedCornerShape(22.dp))
                .background(
                    Brush.linearGradient(
                        colors = listOf(
                            Color.Black.copy(alpha = 0.12f),
                            Color.Black.copy(alpha = 0.06f)
                        )
                    )
                )
        )
        // Soft outer Light Highlight on top-left (Opposing bevel)
        Box(
            modifier = Modifier
                .matchParentSize()
                .offset(y = -depthY / 2, x = -depthX / 2)
                .clip(RoundedCornerShape(22.dp))
                .background(Color.White.copy(alpha = 0.72f))
        )
        // Main Content Board (Tactile Front Layer)
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .border(2.dp, borderColor, RoundedCornerShape(22.dp)),
            colors = CardDefaults.cardColors(containerColor = color),
            shape = RoundedCornerShape(22.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
        ) {
            content()
        }
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
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val animatedDepthY by animateDpAsState(targetValue = if (isPressed) 2.dp else 6.dp, label = "depthY")
    val animatedDepthX by animateDpAsState(targetValue = if (isPressed) 1.dp else 3.dp, label = "depthX")

    Box(
        modifier = modifier
            .padding(bottom = 6.dp, end = 3.dp)
            .clickable(
                interactionSource = interactionSource,
                indication = null, // Disable ripple for custom tactile movement
                onClick = onClick
            )
    ) {
        // Deep Soft Shadow
        Box(
            modifier = Modifier
                .matchParentSize()
                .offset(y = animatedDepthY, x = animatedDepthX)
                .clip(shape)
                .background(
                    Brush.linearGradient(
                        colors = listOf(
                            Color.Black.copy(alpha = 0.18f),
                            color.copy(alpha = 0.45f)
                        )
                    )
                )
        )
        // Glowing Soft Highlight on top-left
        Box(
            modifier = Modifier
                .matchParentSize()
                .offset(y = -animatedDepthY / 2, x = -animatedDepthX / 2)
                .clip(shape)
                .background(Color.White.copy(alpha = 0.75f))
        )
        // Top Surface Face
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .border(2.dp, Color.White.copy(alpha = 0.15f), shape),
            colors = CardDefaults.cardColors(containerColor = color),
            shape = shape,
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {
            Row(
                modifier = Modifier
                    .padding(horizontal = 14.dp, vertical = 11.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                content()
            }
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
        borderColor = color.copy(alpha = 0.35f),
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
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val depthY by animateDpAsState(targetValue = if (isPressed) 2.dp else 6.dp, label = "depthY")
    val depthX by animateDpAsState(targetValue = if (isPressed) 1.dp else 3.dp, label = "depthX")

    Box(
        modifier = Modifier
            .padding(6.dp)
            .height(130.dp)
            .fillMaxWidth()
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            )
    ) {
        // Shadow Layer (Depth)
        Box(
            modifier = Modifier
                .matchParentSize()
                .offset(y = depthY, x = depthX)
                .clip(RoundedCornerShape(22.dp))
                .background(
                    Brush.linearGradient(
                        colors = listOf(
                            Color.Black.copy(alpha = 0.12f),
                            color.copy(alpha = 0.35f)
                        )
                    )
                )
        )

        // Soft Highlight Bevel
        Box(
            modifier = Modifier
                .matchParentSize()
                .offset(y = -depthY / 2, x = -depthX / 2)
                .clip(RoundedCornerShape(22.dp))
                .background(Color.White.copy(alpha = 0.72f))
        )

        // Main Front Card Face
        Card(
            modifier = Modifier
                .fillMaxSize()
                .border(2.dp, color.copy(alpha = 0.25f), RoundedCornerShape(22.dp)),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
            shape = RoundedCornerShape(22.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(12.dp)
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
                                .clip(RoundedCornerShape(6.dp))
                                .background(StudyOrangeAccent.copy(alpha = 0.15f))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(badgeText, fontSize = 8.sp, color = StudyOrangeAccent, fontWeight = FontWeight.Bold)
                        }
                    } else {
                        Spacer(modifier = Modifier.width(1.dp))
                    }

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
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = title,
                        fontWeight = FontWeight.Black,
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurface,
                        textAlign = TextAlign.Right,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = subtitle,
                        fontSize = 9.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f),
                        textAlign = TextAlign.Right,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
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

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.End
        ) {
        // Welcoming
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(StudyOrangeAccent.copy(alpha = 0.15f))
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text(
                    text = "الأستوديو الذكي ثلاثي الأبعاد 🎨",
                    color = StudyOrangeAccent,
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
        
        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = "أهلاً بك يا ${user.username} 👋",
            fontSize = 24.sp,
            fontWeight = FontWeight.Black,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Right,
            modifier = Modifier.fillMaxWidth()
        )
        Text(
            text = "المستوى الدراسي: ${user.gradeLevel}",
            fontSize = 13.sp,
            color = StudyBluePrimary,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Right,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Large 3D Banner card
        ThreeDContainer(color = StudyBluePrimary) {
            Box(
                modifier = Modifier
                    .background(
                        Brush.linearGradient(
                            colors = listOf(StudyBluePrimary, Color(0xFF1E88E5), Color(0xFF1565C0))
                        )
                    )
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                Column(horizontalAlignment = Alignment.End, modifier = Modifier.fillMaxWidth()) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color.White.copy(alpha = 0.2f))
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Text("مُتاح بالكامل ⚡", fontSize = 10.sp, color = Color.White, fontWeight = FontWeight.Bold)
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "منصة المذاكرة التفاعلية ثلاثية الأبعاد 🎓",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.ExtraBold,
                        textAlign = TextAlign.Right,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "هنا جميع ميزات ذكاء جيميناي وسكريبتات الفيديو المسموعة في لوحة تحكم واحدة! اختر ميزة بالأسفل لبدء التعلّم الآن.",
                        color = Color.White.copy(alpha = 0.9f),
                        fontSize = 11.sp,
                        lineHeight = 16.sp,
                        textAlign = TextAlign.Right,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Study Tip Card in 3D
        ThreeDContainer(color = StudyOrangeAccent) {
            Row(
                modifier = Modifier
                    .padding(14.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(horizontalAlignment = Alignment.End, modifier = Modifier.weight(1f)) {
                    Text(
                        text = "نصيحة المذاكرة اليومية الذكية:",
                        color = StudyOrangeAccent,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        textAlign = TextAlign.Right
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "تقنية التكرار المتباعد تزيد التركيز وتحفظ المعلومة في الذاكرة طويلة المدى. اضغط على بطاقات الفلاش (Flashcards) لتجربتها بنفسك!",
                        fontSize = 10.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                        textAlign = TextAlign.Right,
                        lineHeight = 15.sp
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = null,
                    tint = StudyOrangeAccent,
                    modifier = Modifier.size(22.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(22.dp))

        // Progress Badges in 3D
        SectionHeader(title = "معدلات التفاعل والإنجاز ثلاثي الأبعاد 📊")
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Plan progress with 3D shadow container
            val planProgress3DColor = StudyBluePrimary
            Box(
                modifier = Modifier
                    .weight(1.2f)
                    .padding(horizontal = 4.dp)
            ) {
                ThreeDContainer(color = planProgress3DColor) {
                    Column(
                        modifier = Modifier.padding(12.dp),
                        horizontalAlignment = Alignment.End
                    ) {
                        Text("المهام الأسبوعية", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        Text("النسبة: ${(plansProgress * 100).toInt()}%", fontSize = 10.sp, color = Color.Gray)
                        Spacer(modifier = Modifier.height(8.dp))
                        Box(contentAlignment = Alignment.Center, modifier = Modifier.size(54.dp).align(Alignment.CenterHorizontally)) {
                            CircularProgressIndicator(
                                progress = { 1.0f },
                                modifier = Modifier.fillMaxSize(),
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f),
                                strokeWidth = 5.dp
                            )
                            CircularProgressIndicator(
                                progress = { plansProgress },
                                modifier = Modifier.fillMaxSize(),
                                color = StudyBluePrimary,
                                strokeWidth = 5.dp
                            )
                            Text("${(plansProgress * 100).toInt()}%", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = StudyBluePrimary)
                        }
                    }
                }
            }

            // Quick Stats with 3D shadow containers
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 4.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                ThreeDContainer(color = StudyOrangeAccent) {
                    Column(
                        modifier = Modifier.padding(10.dp).fillMaxWidth(),
                        horizontalAlignment = Alignment.End
                    ) {
                        Text("ملخصات نشطة", fontSize = 10.sp, color = Color.Gray)
                        Text("$totalMaterialCount مستندات", fontSize = 13.sp, fontWeight = FontWeight.Black, color = StudyOrangeAccent)
                    }
                }
                ThreeDContainer(color = StudyBluePrimary) {
                    Column(
                        modifier = Modifier.padding(10.dp).fillMaxWidth(),
                        horizontalAlignment = Alignment.End
                    ) {
                        Text("الاختبارات الذاتية", fontSize = 10.sp, color = Color.Gray)
                        Text("$totalQuizCount مُنقضية", fontSize = 13.sp, fontWeight = FontWeight.Black, color = StudyBluePrimary)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Announcements Header
        if (announcements.isNotEmpty()) {
            SectionHeader(title = "تنبيهات وتلوينات المدرسين النشطة 🔔")
            announcements.take(2).forEach { announce ->
                ThreeDContainer(color = StudyBluePrimary) {
                    Column(modifier = Modifier.padding(12.dp), horizontalAlignment = Alignment.End) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = "المادة: ${announce.subject}", fontSize = 10.sp, color = StudyBluePrimary, fontWeight = FontWeight.Bold)
                            Text(text = announce.title, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = announce.content, fontSize = 11.sp, textAlign = TextAlign.Right, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f))
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = "المرسل: ${announce.senderName}", fontSize = 9.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f))
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
            }
            Spacer(modifier = Modifier.height(10.dp))
        }

        // REDESIGNED 12 FEATURES IN A SPECTACULAR 3D GRID
        SectionHeader(title = "لوحة جميع الميزات والأقسام 🛠️🎨")

        // Pair 1: Video Generator & PDF Smart Reader
        Row(modifier = Modifier.fillMaxWidth()) {
            Box(modifier = Modifier.weight(1f)) {
                PortalWidget3D(
                    title = "ملخصاتي ومستنداتي",
                    subtitle = "تحميل وتلخيص ملفات PDF",
                    icon = Icons.Default.Info,
                    color = StudyBluePrimary,
                    badgeText = "ذكاء جيميناي ⚡"
                ) {
                    viewModel.navigateTo(Screen.SavedFiles)
                }
            }
            Box(modifier = Modifier.weight(1f)) {
                PortalWidget3D(
                    title = "فيديو شرح ذكي",
                    subtitle = "تحويل صوره أو pdf لشرح مرئي",
                    icon = Icons.Default.PlayArrow,
                    color = StudyOrangeAccent,
                    badgeText = "مميز وحصري 🎥"
                ) {
                    viewModel.navigateTo(Screen.PdfToVideo)
                }
            }
        }

        // Pair 2: AI Companion & Self Exams
        Row(modifier = Modifier.fillMaxWidth()) {
            Box(modifier = Modifier.weight(1f)) {
                PortalWidget3D(
                    title = "امتحانات وتحديات",
                    subtitle = "اختبارات تفاعلية ونتائج فورية",
                    icon = Icons.Default.Create,
                    color = StudyOrangeAccent,
                    badgeText = "مستمر 🥇"
                ) {
                    viewModel.navigateTo(Screen.Exams)
                }
            }
            Box(modifier = Modifier.weight(1f)) {
                PortalWidget3D(
                    title = "المساعد الدراسي",
                    subtitle = "مناقشة وتلخيص مع جيميناي",
                    icon = Icons.Default.Send,
                    color = StudyBluePrimary,
                    badgeText = "شات ذكي 💬"
                ) {
                    viewModel.navigateTo(Screen.AiAssistant)
                }
            }
        }

        // Pair 3: Study Schedule Planner & Memory Flashcards
        Row(modifier = Modifier.fillMaxWidth()) {
            Box(modifier = Modifier.weight(1f)) {
                PortalWidget3D(
                    title = "بطاقات الاستذكار",
                    subtitle = "فلاش كارد الحفظ السريع",
                    icon = Icons.Default.Refresh,
                    color = StudyBluePrimary,
                    badgeText = "تقوية الذاكرة 🧠"
                ) {
                    viewModel.navigateTo(Screen.Flashcards)
                }
            }
            Box(modifier = Modifier.weight(1f)) {
                PortalWidget3D(
                    title = "الخطة الدراسية",
                    subtitle = "مهام وجداول مذاكرة أسبوعية",
                    icon = Icons.Default.DateRange,
                    color = StudyOrangeAccent,
                    badgeText = "تنظيم 📅"
                ) {
                    viewModel.navigateTo(Screen.StudyPlan)
                }
            }
        }

        // Pair 4: Intelligent Mind Maps & Saved Video Archive
        Row(modifier = Modifier.fillMaxWidth()) {
            Box(modifier = Modifier.weight(1f)) {
                PortalWidget3D(
                    title = "الفيديوهات المنتجة",
                    subtitle = "تصفح الشروحات المصممة مسبقاً",
                    icon = Icons.Default.PlayArrow,
                    color = StudyOrangeAccent,
                    badgeText = "أفلام تعليمية 🎞️"
                ) {
                    viewModel.navigateTo(Screen.SavedVideos)
                }
            }
            Box(modifier = Modifier.weight(1f)) {
                PortalWidget3D(
                    title = "الخرائط الذهنية",
                    subtitle = "رسم مخططات مفاهيمية ذكية",
                    icon = Icons.Default.Share,
                    color = StudyBluePrimary,
                    badgeText = "تمثيل بصري 🗺️"
                ) {
                    viewModel.navigateTo(Screen.MindMaps)
                }
            }
        }

        // Pair 5: Subject Curriculum & Premium Subscriptions
        Row(modifier = Modifier.fillMaxWidth()) {
            Box(modifier = Modifier.weight(1f)) {
                PortalWidget3D(
                    title = "باقة عضوية العباقرة",
                    subtitle = "وصول كامل لكافة أدوات الذكاء",
                    icon = Icons.Default.Star,
                    color = StudyBluePrimary,
                    badgeText = "ترقية 💎"
                ) {
                    viewModel.navigateTo(Screen.Subscriptions)
                }
            }
            Box(modifier = Modifier.weight(1f)) {
                PortalWidget3D(
                    title = "مناهج ومواد القسم",
                    subtitle = "الكتب الدراسية والفصول المحاكاة",
                    icon = Icons.Default.AccountBox,
                    color = StudyOrangeAccent,
                    badgeText = "الدروس 🏛️"
                ) {
                    viewModel.navigateTo(Screen.Subjects)
                }
            }
        }

        // Pair 6: User Professional Profile & App Configuration
        Row(modifier = Modifier.fillMaxWidth()) {
            Box(modifier = Modifier.weight(1f)) {
                PortalWidget3D(
                    title = "إعدادات المنصة",
                    subtitle = "خيارات قارئ الصوت والتوجيه",
                    icon = Icons.Default.Settings,
                    color = StudyOrangeAccent
                ) {
                    viewModel.navigateTo(Screen.Settings)
                }
            }
            Box(modifier = Modifier.weight(1f)) {
                PortalWidget3D(
                    title = "الملف الشخصي",
                    subtitle = "سجل البيانات والتحصيل الأكاديمي",
                    icon = Icons.Default.Person,
                    color = StudyBluePrimary,
                    badgeText = "حسابي 👤"
                ) {
                    viewModel.navigateTo(Screen.Profile)
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
        Spacer(modifier = Modifier.height(120.dp))
    }

    // Dynamic 3D Neumorphic Floating Action Dock
    Box(
        modifier = Modifier
            .align(Alignment.BottomCenter)
            .fillMaxWidth()
            .padding(bottom = 12.dp, start = 16.dp, end = 16.dp)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .border(2.dp, MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f), RoundedCornerShape(26.dp)),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.95f)
            ),
            shape = RoundedCornerShape(26.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // PDF to Video Screen 3D FAB
                Box(modifier = Modifier.weight(1f)) {
                    NeumorphicButton3D(
                        onClick = { viewModel.navigateTo(Screen.PdfToVideo) },
                        color = StudyOrangeAccent,
                        shape = RoundedCornerShape(18.dp)
                    ) {
                        Icon(Icons.Default.PlayArrow, contentDescription = null, tint = Color.White)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            "شرح مرئي 🎥",
                            color = Color.White,
                            fontWeight = FontWeight.Black,
                            fontSize = 11.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }

                // AI Summary Screen 3D FAB with smart fallback/selection
                Box(modifier = Modifier.weight(1f)) {
                    NeumorphicButton3D(
                        onClick = {
                            val activeMat = viewModel.activeMaterial.value
                            if (activeMat != null) {
                                viewModel.navigateTo(Screen.PdfSummary)
                            } else {
                                val firstMat = materials.firstOrNull()
                                if (firstMat != null) {
                                    viewModel.selectActiveMaterial(firstMat)
                                    viewModel.navigateTo(Screen.PdfSummary)
                                } else {
                                    viewModel.navigateTo(Screen.SavedFiles)
                                }
                            }
                        },
                        color = StudyBluePrimary,
                        shape = RoundedCornerShape(18.dp)
                    ) {
                        Icon(Icons.Default.Info, contentDescription = null, tint = Color.White)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            "تلخيص ذكي ⚡",
                            color = Color.White,
                            fontWeight = FontWeight.Black,
                            fontSize = 11.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
        }
    }
}
}

@Composable
fun SubjectsGridScreen(viewModel: AppViewModel) {
    val subjects = listOf(
        Pair("علم الأحياء", "الأحياء والتركيب الجيني"),
        Pair("الفيزياء الحديثة", "الميكانيكا والطاقة الكونية"),
        Pair("الكيمياء العضوية", "العناصر والتفاعلات الحيوية"),
        Pair("الرياضيات واللغارتمات", "التفاضل وحلول الجبر المتقدم"),
        Pair("الغة العربية الفصحى", "النحو والصرف والبلاغة"),
        Pair("اللغويات الأجنبية (إنجليزي)", "القواعد والكتابة الإبداعية")
    )

    Column(modifier = Modifier.fillMaxSize().padding(16.dp), horizontalAlignment = Alignment.End) {
        Text("تصفح المناهج حسب المادة", fontSize = 22.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.Right)
        Text("اختر المادة التعليمية لاستقصاء المذكرات والملخصات المعتمدة.", fontSize = 13.sp, color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f), textAlign = TextAlign.Right)

        Spacer(modifier = Modifier.height(16.dp))

        LazyVerticalGrid(columns = GridCells.Fixed(2), modifier = Modifier.fillMaxSize()) {
            items(subjects) { sub ->
                Card(
                    modifier = Modifier
                        .padding(6.dp)
                        .height(130.dp)
                        .clickable {
                            viewModel.studentSelectedSubject.value = sub.first
                            viewModel.navigateTo(Screen.Lessons)
                        },
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp).fillMaxSize(), verticalArrangement = Arrangement.Bottom, horizontalAlignment = Alignment.End) {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(StudyBluePrimary.copy(alpha = 0.1f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Info, contentDescription = null, tint = StudyBluePrimary, modifier = Modifier.size(18.dp))
                        }
                        Spacer(modifier = Modifier.weight(1f))
                        Text(text = sub.first, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        Text(text = sub.second, fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f), maxLines = 1, overflow = TextOverflow.Ellipsis)
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
