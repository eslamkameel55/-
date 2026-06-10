package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.*
import com.example.ui.theme.StudyBluePrimary
import com.example.ui.theme.StudyOrangeAccent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TeacherDashboardScreen(viewModel: AppViewModel) {
    val scroll = rememberScrollState()
    val user = viewModel.loggedUser.collectAsState().value ?: return

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(scroll),
        horizontalAlignment = Alignment.End
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { viewModel.navigateBack() }) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back")
            }
            Text("لوحة تحكم الأستاذ: ${user.username} 🎓", fontSize = 18.sp, fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Large Banner card
        GradientHeaderCard(
            title = "أهلاً ومرحباً يا موجه المادة!",
            subtitle = "من هنا يمكنك النشر الفوري للدروس والمذكرات لطلاب فصولك وبث الأخبار الهامة للمراجعات ونموذج الامتحانات.",
            colors = listOf(StudyOrangeAccent, Color(0xFFD84315))
        )

        Spacer(modifier = Modifier.height(16.dp))

        // KPI Dashboard Row
        SectionHeader(title = "أرقام الفصل الدراسي الحالي")
        Row(modifier = Modifier.fillMaxWidth()) {
            StatCard(
                title = "المشتركين بالفصل",
                value = "128 طالب",
                icon = Icons.Default.Person,
                iconColor = StudyBluePrimary,
                modifier = Modifier.weight(1f)
            )
            StatCard(
                title = "الملخصات المنشورة",
                value = "14 ملف دراسي",
                icon = Icons.Default.Share,
                iconColor = StudyOrangeAccent,
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Broadcast announcements form
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f))
        ) {
            Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.End) {
                Text("بث إعلان / تنبيه عاجل للفصل الدراسي", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                Text("سيظهر هذا التنبيه لجميع طلابك فوراً في لوحة التحكم الرئيسية الخاصة بهم.", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
                Spacer(modifier = Modifier.height(12.dp))

                StudyTextInput(
                    value = viewModel.instructorAnnounceTitle.value,
                    onValueChange = { viewModel.instructorAnnounceTitle.value = it },
                    label = "المفتاح / موضوع التنبيه",
                    placeholder = "مثال: مراجعة الخلية الحية يوم السبت"
                )

                Spacer(modifier = Modifier.height(8.dp))

                StudyTextInput(
                    value = viewModel.instructorAnnounceSubject.value,
                    onValueChange = { viewModel.instructorAnnounceSubject.value = it },
                    label = "اسم المادة الدراسية",
                    placeholder = "مثال: أحياء، فيزياء، رياضيات"
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = viewModel.instructorAnnounceBody.value,
                    onValueChange = { viewModel.instructorAnnounceBody.value = it },
                    label = { Text("نص التنبيه والتوجيهات الكاملة", modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Right) },
                    placeholder = { Text("اكتب هنا التعليمات، والواجبات المنزلية، وسيدرسها الطلاب فوراً...", modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Right) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp),
                    shape = RoundedCornerShape(12.dp),
                    textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.Right)
                )

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = { viewModel.submitTeacherAnnouncement() },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = StudyOrangeAccent)
                ) {
                    Text("بث التنبيه لجميع الطلاب الآن 🔔", fontWeight = FontWeight.Bold, color = Color.White)
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Student Analytics section
        SectionHeader(title = "تقارير حضور وأداء الطلاب (مكثف)")
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.End) {
                listOf(
                    Triple("أحمد الطالب", "الصف الثالث الثانوي", "94% (ممتاز)"),
                    Triple("سارة عمر", "الصف الثالث الثانوي", "88% (ممتاز)"),
                    Triple("عمرو دياب", "الصف الثالث الثانوي", "64% (يحتاج مراجعة!)")
                ).forEach { stu ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = stu.third, color = StudyBluePrimary, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        Column(horizontalAlignment = Alignment.End) {
                            Text(text = stu.first, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                            Text(text = stu.second, fontSize = 10.sp, color = Color.Gray)
                        }
                    }
                    HorizontalDivider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.05f))
                }
            }
        }
    }
}

@Composable
fun AdminDashboardScreen(viewModel: AppViewModel) {
    val scroll = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(scroll),
        horizontalAlignment = Alignment.End
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { viewModel.navigateBack() }) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back")
            }
            Text("لوحة إدارة النظام والإحصاءات ⚙️", fontSize = 18.sp, fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Large Banner card
        GradientHeaderCard(
            title = "بوابة الإدارة الشاملة للوصول",
            subtitle = "أبرز أرقام منصة ذاكر بذكاء وتحكم كامل في اشتراكات الطلاب والمدرسين والمناهج.",
            colors = listOf(Color(0xFF37474F), Color(0xFF212121))
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Key stats grid
        SectionHeader(title = "نظرة عامة على السيرفر")
        Row(modifier = Modifier.fillMaxWidth()) {
            StatCard(
                title = "إجمالي الحسابات",
                value = "1,420 حساب",
                icon = Icons.Default.Person,
                iconColor = StudyBluePrimary,
                modifier = Modifier.weight(1f)
            )
            StatCard(
                title = "المشتركين ببريميوم",
                value = "388 عبقري",
                icon = Icons.Default.Star,
                iconColor = StudyOrangeAccent,
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.End) {
                Text("لوحة التحكم بالاشتراكات والتراخيص", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                Spacer(modifier = Modifier.height(12.dp))

                listOf(
                    Pair("student@study.com (باقة مجانية)", "FREE"),
                    Pair("teacher@study.com (الموجه)", "PREMIUM"),
                    Pair("eslamkameel55@gmail.com", "PREMIUM")
                ).forEach { item ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (item.second == "FREE") Color.LightGray else StudyOrangeAccent)
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(text = item.second, fontWeight = FontWeight.Bold, fontSize = 11.sp, color = Color.White)
                        }
                        Text(text = item.first, fontSize = 13.sp, fontWeight = FontWeight.Medium)
                    }
                    HorizontalDivider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.05f))
                }
            }
        }
    }
}
