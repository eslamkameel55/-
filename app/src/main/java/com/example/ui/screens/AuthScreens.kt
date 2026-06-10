package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.AppViewModel
import com.example.ui.Screen
import com.example.ui.StudyTextInput
import com.example.ui.theme.StudyBluePrimary
import com.example.ui.theme.StudyOrangeAccent
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(viewModel: AppViewModel, onTimeout: () -> Unit) {
    var animateIn by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        animateIn = true
        delay(2500)
        onTimeout()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        AnimatedVisibility(
            visible = animateIn,
            enter = fadeIn(tween(1200)) + expandVertically(tween(1200)),
            exit = fadeOut(tween(800))
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.padding(24.dp)
            ) {
                // Large smart logo emblem
                Box(
                    modifier = Modifier
                        .size(130.dp)
                        .clip(RoundedCornerShape(32.dp))
                        .background(StudyBluePrimary),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Star, // Emblem
                        contentDescription = null,
                        tint = StudyOrangeAccent,
                        modifier = Modifier.size(72.dp)
                    )
                }
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = "ذاكر بذكاء",
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Black,
                    color = MaterialTheme.colorScheme.primary,
                    letterSpacing = 1.sp
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "تعلم أسرع... وذاكر بذكاء",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.height(48.dp))
                CircularProgressIndicator(
                    color = StudyOrangeAccent,
                    strokeWidth = 3.dp,
                    modifier = Modifier.size(36.dp)
                )
            }
        }
    }
}

@Composable
fun WelcomeScreen(viewModel: AppViewModel) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(24.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
                .background(StudyBluePrimary.copy(alpha = 0.15f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Share,
                contentDescription = null,
                tint = StudyBluePrimary,
                modifier = Modifier.size(48.dp)
            )
        }
        Spacer(modifier = Modifier.height(32.dp))
        Text(
            text = "أهلاً بك في ذاكر بذكاء!",
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = "مساعدك الأكاديمي الرقمي لتلخيص الكتب وتحرير الفيديوهات التعليمية وحل الأسئلة فوراً بالذكاء الاصطناعي.",
            fontSize = 15.sp,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.65f),
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        Spacer(modifier = Modifier.height(48.dp))
        Button(
            onClick = { viewModel.navigateTo(Screen.Login) },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(containerColor = StudyBluePrimary),
            shape = RoundedCornerShape(16.dp)
        ) {
            Text("تسجيل الدخول", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
        }
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedButton(
            onClick = { viewModel.navigateTo(Screen.AccountTypeSelector) },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(16.dp),
            border = ButtonDefaults.outlinedButtonBorder.copy(width = 1.5.dp)
        ) {
            Text("إنشاء حساب جديد", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = StudyBluePrimary)
        }
        Spacer(modifier = Modifier.height(32.dp))
        Text(
            text = "تجرّب حساب تجريبي سريع؟\nاضغط تسجيل الدخول واستخدم الحسابات الجاهزة المكتوبة بالداخل.",
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.4f),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun AccountTypeSelectorScreen(viewModel: AppViewModel) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "اختر نوع الحساب",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = "حدد هويتك الأكاديمية للدخول للوحة التحكم المخصصة لك داخل منصة ذاكر بذكاء.",
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(32.dp))

        // Student Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
                .clickable {
                    viewModel.regAccountType.value = "STUDENT"
                    viewModel.navigateTo(Screen.SignUp)
                },
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = StudyBluePrimary.copy(alpha = 0.08f))
        ) {
            Row(
                modifier = Modifier
                    .padding(20.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(horizontalAlignment = Alignment.End, modifier = Modifier.weight(1f)) {
                    Text("بوابة الطالب", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = StudyBluePrimary)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("أدرس بذكاء، لخص ملفاتي، صمم خرائطي الذهنية واجتاز اختباراتي الذاتية.", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f), textAlign = TextAlign.Right)
                }
                Spacer(modifier = Modifier.width(16.dp))
                Icon(Icons.Default.Person, contentDescription = null, tint = StudyBluePrimary, modifier = Modifier.size(36.dp))
            }
        }

        // Teacher Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
                .clickable {
                    viewModel.regAccountType.value = "TEACHER"
                    viewModel.navigateTo(Screen.SignUp)
                },
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = StudyOrangeAccent.copy(alpha = 0.08f))
        ) {
            Row(
                modifier = Modifier
                    .padding(20.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(horizontalAlignment = Alignment.End, modifier = Modifier.weight(1f)) {
                    Text("بوابة المعلم الموجه", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = StudyOrangeAccent)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("ارفع ملفاتك لطلابك، اكتب الاختبارات، وتابع مستويات طلاب فصولك.", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f), textAlign = TextAlign.Right)
                }
                Spacer(modifier = Modifier.width(16.dp))
                Icon(Icons.Default.Star, contentDescription = null, tint = StudyOrangeAccent, modifier = Modifier.size(36.dp))
            }
        }

        // Admin Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
                .clickable {
                    viewModel.regAccountType.value = "ADMIN"
                    viewModel.navigateTo(Screen.SignUp)
                },
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.05f))
        ) {
            Row(
                modifier = Modifier
                    .padding(20.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(horizontalAlignment = Alignment.End, modifier = Modifier.weight(1f)) {
                    Text("بوابة الإدارة", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = MaterialTheme.colorScheme.onSurface)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("أدير بيانات المدرسة، راجع إحصائيات الاشتراكات، واضبط مواد المنهج الدراسي.", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f), textAlign = TextAlign.Right)
                }
                Spacer(modifier = Modifier.width(16.dp))
                Icon(Icons.Default.Settings, contentDescription = null, tint = MaterialTheme.colorScheme.onSurface, modifier = Modifier.size(36.dp))
            }
        }
    }
}

@Composable
fun LoginScreen(viewModel: AppViewModel) {
    val scroll = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
            .verticalScroll(scroll),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "مرحباً بعودتك!",
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = "سجل دخولك لمتابعة خططك الدراسية ومناقشة مساعدك الذكي.",
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.55f),
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(32.dp))

        // Preseeded reminder
        Card(
            colors = CardDefaults.cardColors(containerColor = StudyOrangeAccent.copy(alpha = 0.1f)),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = "💡 حسابات سريعة للتجربة الفورية:",
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    color = StudyOrangeAccent,
                    textAlign = TextAlign.Right,
                    modifier = Modifier.fillMaxWidth()
                )
                Text(
                    text = "• طالب: student@study.com  (كلمة السر: 123456)\n• معلم: teacher@study.com  (كلمة السر: 123456)\n• مدير: admin@study.com  (كلمة السر: 123456)",
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                    textAlign = TextAlign.Right,
                    modifier = Modifier.fillMaxWidth(),
                    lineHeight = 16.sp
                )
            }
        }

        StudyTextInput(
            value = viewModel.loginEmail.value,
            onValueChange = { viewModel.loginEmail.value = it },
            label = "البريد الإلكتروني",
            placeholder = "مثال: student@study.com"
        )
        Spacer(modifier = Modifier.height(16.dp))
        StudyTextInput(
            value = viewModel.loginPassword.value,
            onValueChange = { viewModel.loginPassword.value = it },
            label = "كلمة المرور",
            placeholder = "أدخل كلمة المرور"
        )
        Spacer(modifier = Modifier.height(12.dp))

        if (viewModel.isLoginError.value) {
            Text(
                text = "البريد الإلكتروني أو كلمة المرور غير صحيحة!",
                color = Color.Red,
                fontSize = 12.sp,
                textAlign = TextAlign.Right,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(12.dp))
        }

        Button(
            onClick = {
                viewModel.performLogin {
                    viewModel.navigateTo(Screen.Home)
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = StudyBluePrimary)
        ) {
            Text("دخول آمن", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
        }
        Spacer(modifier = Modifier.height(24.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "سجل هنا",
                color = StudyBluePrimary,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.clickable { viewModel.navigateTo(Screen.AccountTypeSelector) }
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(text = "ليس لديك حساب؟", color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f))
        }
    }
}

@Composable
fun SignUpScreen(viewModel: AppViewModel) {
    val scroll = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
            .verticalScroll(scroll),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        val typeLabel = when (viewModel.regAccountType.value) {
            "TEACHER" -> "المعلم المشرف"
            "ADMIN" -> "مدير المنصة"
            else -> "طالب ذكي"
        }

        Text(
            text = "إنشاء حساب جديد ($typeLabel)",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = "ابدأ فوراً واستفد من خدمات المساعد الأكاديمي والتلخيص.",
            fontSize = 13.sp,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(24.dp))

        StudyTextInput(
            value = viewModel.regUsername.value,
            onValueChange = { viewModel.regUsername.value = it },
            label = "الاسم الكامل",
            placeholder = "الاسم بالكامل"
        )
        Spacer(modifier = Modifier.height(16.dp))
        StudyTextInput(
            value = viewModel.regEmail.value,
            onValueChange = { viewModel.regEmail.value = it },
            label = "البريد الإلكتروني",
            placeholder = "example@domain.com"
        )
        Spacer(modifier = Modifier.height(16.dp))
        StudyTextInput(
            value = viewModel.regPassword.value,
            onValueChange = { viewModel.regPassword.value = it },
            label = "كلمة المرور",
            placeholder = "على الأقل 6 خانات"
        )
        Spacer(modifier = Modifier.height(16.dp))

        if (viewModel.regAccountType.value == "STUDENT") {
            Text(
                text = "اختر المرحلة والصف الدراسي:",
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                textAlign = TextAlign.Right,
                modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
            )
            val grades = listOf("الصف الأول الثانوي", "الصف الثاني الثانوي", "الصف الثالث الثانوي")
            grades.forEach { grade ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { viewModel.regGrade.value = grade }
                        .padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = grade, modifier = Modifier.padding(end = 8.dp), fontSize = 14.sp)
                    RadioButton(
                        selected = viewModel.regGrade.value == grade,
                        onClick = { viewModel.regGrade.value = grade }
                    )
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
        }

        Button(
            onClick = {
                viewModel.performRegistration {
                    viewModel.navigateTo(Screen.Home)
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = StudyBluePrimary)
        ) {
            Text("تسجيل وحفظ البيانات", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
        }
        Spacer(modifier = Modifier.height(24.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "سجل دخولك",
                color = StudyBluePrimary,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.clickable { viewModel.navigateTo(Screen.Login) }
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(text = "لديك حساب بالفعل؟", color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f))
        }
    }
}

@Composable
fun ProfileScreen(viewModel: AppViewModel) {
    val user = viewModel.loggedUser.collectAsState().value ?: return
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(90.dp)
                .clip(CircleShape)
                .background(StudyBluePrimary),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = user.username.take(1),
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = user.username, fontSize = 22.sp, fontWeight = FontWeight.Bold)
        Text(text = user.email, fontSize = 14.sp, color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f))
        Spacer(modifier = Modifier.height(8.dp))
        AssistChip(
            onClick = {},
            label = {
                val label = when (user.accountType) {
                    "TEACHER" -> "موجه تعليمي"
                    "ADMIN" -> "رئيس الإدارة"
                    else -> "طالب مستذكر"
                }
                Text(label, fontWeight = FontWeight.Bold)
            }
        )

        Spacer(modifier = Modifier.height(24.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.End) {
                Text("تفاصيل الحساب الأكاديمي", fontWeight = FontWeight.Bold, fontSize = 16.sp, modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Right)
                HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f))

                Row(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp), horizontalArrangement = Arrangement.End) {
                    Text(text = user.gradeLevel, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(text = "الصف الدراسي:", color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f))
                }

                Row(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp), horizontalArrangement = Arrangement.End) {
                    Text(text = user.subjectInterests, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(text = "المواد المفضلة:", color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f))
                }

                Row(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp), horizontalArrangement = Arrangement.End) {
                    val subType = if (viewModel.subscriptionPlan.value == "FREE") "باقة مجانية (محدودة)" else "باقة العباقرة الممتازة (كاملة 👑)"
                    Text(text = subType, fontWeight = FontWeight.Bold, color = if (viewModel.subscriptionPlan.value != "FREE") StudyOrangeAccent else MaterialTheme.colorScheme.onSurface)
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(text = "نوع الاشتراك الحالي:", color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f))
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = { viewModel.navigateTo(Screen.Subscriptions) },
            colors = ButtonDefaults.buttonColors(containerColor = StudyOrangeAccent),
            shape = RoundedCornerShape(14.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("ترقية الحساب إلى مميز 👑", fontWeight = FontWeight.Bold, color = Color.White)
        }

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedButton(
            onClick = { viewModel.logout() },
            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Red),
            border = ButtonDefaults.outlinedButtonBorder.copy(width = 1.dp),
            shape = RoundedCornerShape(14.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("إعادة ضبط الجلسة والرجوع للرئيسية", fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun SettingsScreen(viewModel: AppViewModel, isDarkMode: MutableState<Boolean>) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.End
    ) {
        Text("ضبط إعدادات التطبيق", fontSize = 22.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.Right)
        Spacer(modifier = Modifier.height(16.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                // Dark mode toggle
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Switch(
                        checked = isDarkMode.value,
                        onCheckedChange = { isDarkMode.value = it }
                    )
                    Column(horizontalAlignment = Alignment.End) {
                        Text("الوضع الليلي (الظلام)", fontWeight = FontWeight.Bold)
                        Text("يريح العين أثناء المذاكرة الليلية", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f))
                    }
                }

                HorizontalDivider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f), modifier = Modifier.padding(vertical = 8.dp))

                // Text To Speech status
                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val statusText = if (viewModel.isTtsReady.value) "جاهز في جهازك" else "غير متاح"
                    Text(statusText, color = StudyBluePrimary, fontWeight = FontWeight.Bold)
                    Column(horizontalAlignment = Alignment.End) {
                        Text("النطق الصوتي للملخصات (TTS)", fontWeight = FontWeight.Bold)
                        Text("شرح نصوص الل دروس بالصوت تلقائياً", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f))
                    }
                }

                HorizontalDivider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f), modifier = Modifier.padding(vertical = 8.dp))

                // API Key configured status
                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val keyStatus = if (com.example.data.GeminiService.isApiKeyAvailable()) "متصل 🟢" else "غير مهيأ ⚠️"
                    Text(keyStatus, color = if (com.example.data.GeminiService.isApiKeyAvailable()) Color.Green else Color.Red, fontWeight = FontWeight.Bold)
                    Column(horizontalAlignment = Alignment.End) {
                        Text("اتصال الذكاء الاصطناعي (Gemini Key)", fontWeight = FontWeight.Bold)
                        Text("المفتاح السري المؤمن للدردشة والتلخيص الفوري", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f))
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
        Text("عن التطبيق", fontWeight = FontWeight.Bold, fontSize = 16.sp, modifier = Modifier.padding(bottom = 8.dp))
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
            shape = RoundedCornerShape(12.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.End) {
                Text("اسم البرنامج: ذاكر بذكاء (Study Smart)", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                Text("الإصدار الحالي: v1.0.2 (مستقر)", fontSize = 12.sp)
                Text("التقنيات: Kotlin, Jetpack Compose, Room SQLite, OkHttp, Gemini AI Engine.", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f), textAlign = TextAlign.Right)
            }
        }
    }
}

@Composable
fun SubscriptionsScreen(viewModel: AppViewModel) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("باقات الاشتراك وعضوية العباقرة 👑", fontSize = 22.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(6.dp))
        Text("اختر باقتك وفك القيود أمام تلخيص الملفات ومقاطع فيديو الشرح والخرائط اللانهائية.", fontSize = 13.sp, color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f), textAlign = TextAlign.Center)
        Spacer(modifier = Modifier.height(24.dp))

        // FREE
        SubscriptionPlanCard(
            title = "الخطة المجانية",
            price = "0 ج.م / شهرياً",
            features = listOf(
                "الوصول للمساعد الذكي AI للدردشة الأساسية",
                "تلخيص ما يصل إلى 5 ملفات شهرياً",
                "إنشاء اختبارين لكل موضوع كحد أقصى"
            ),
            isActive = viewModel.subscriptionPlan.value == "FREE",
            onSelect = { viewModel.upgradeSubscription("FREE") },
            primaryColor = MaterialTheme.colorScheme.secondary
        )

        Spacer(modifier = Modifier.height(16.dp))

        // MONTHLY
        SubscriptionPlanCard(
            title = "باقة العباقرة الشهرية",
            price = "50 ج.م / شهرياً",
            features = listOf(
                "دردشة وبحث بامتياز مع المساعد الذكي AI",
                "رفع وتلخيص غير محدود لملفات PDF, Word, PowerPoint",
                "تحويل PDF إلى فيديو شرح ناطق (ولد / بنت)",
                "إنشاء خرائط ذهنية وبطاقات فلاش غير محدودة",
                "كافة تقارير الأداء الفوري لشهادة الوالدين"
            ),
            isActive = viewModel.subscriptionPlan.value == "MONTHLY",
            onSelect = { viewModel.upgradeSubscription("MONTHLY") },
            primaryColor = StudyBluePrimary
        )

        Spacer(modifier = Modifier.height(16.dp))

        // ANNUAL
        SubscriptionPlanCard(
            title = "باقة العباقرة السنوية الممتازة",
            price = "350 ج.م / سنوياً",
            features = listOf(
                "خصم رائع 40% مقارنة بالدفع الشهري",
                "كافة ميزات باقة العباقرة الشهرية كاملة",
                "أولوية اتصال فائقة السرعة مع المودل",
                "دعم غرف التدريس الخاصة وإرسال التقارير المباشرة"
            ),
            isActive = viewModel.subscriptionPlan.value == "SANNUAL",
            onSelect = { viewModel.upgradeSubscription("SANNUAL") },
            primaryColor = StudyOrangeAccent
        )
    }
}

@Composable
fun SubscriptionPlanCard(
    title: String,
    price: String,
    features: List<String>,
    isActive: Boolean,
    onSelect: () -> Unit,
    primaryColor: Color
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = if (isActive) 2.5.dp else 1.dp,
                color = if (isActive) primaryColor else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f),
                shape = RoundedCornerShape(20.dp)
            ),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            horizontalAlignment = Alignment.End
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (isActive) {
                    AssistChip(
                        onClick = {},
                        label = { Text("نشط حالياً ⚡", fontWeight = FontWeight.Bold) }
                    )
                } else {
                    Spacer(modifier = Modifier.width(1.dp))
                }
                Text(title, fontWeight = FontWeight.Bold, fontSize = 18.sp, color = primaryColor)
            }
            Text(price, fontSize = 24.sp, fontWeight = FontWeight.Black, color = MaterialTheme.colorScheme.onSurface, modifier = Modifier.padding(vertical = 4.dp))
            Spacer(modifier = Modifier.height(12.dp))

            features.forEach { feat ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.Top
                ) {
                    Text(feat, modifier = Modifier.padding(end = 8.dp), fontSize = 13.sp, textAlign = TextAlign.Right)
                    Icon(Icons.Default.Check, contentDescription = null, tint = primaryColor, modifier = Modifier.size(16.dp))
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = onSelect,
                colors = ButtonDefaults.buttonColors(containerColor = if (isActive) primaryColor.copy(alpha = 0.2f) else primaryColor),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                val btnText = if (isActive) "أنت مشترك في هذه الخطة" else "الاشتراك والبدء الآن"
                val btnColor = if (isActive) primaryColor else Color.White
                Text(btnText, fontWeight = FontWeight.Bold, color = btnColor)
            }
        }
    }
}
