package com.example.ui

import androidx.activity.compose.BackHandler
import androidx.compose.animation.*
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.screens.*
import com.example.ui.theme.StudyBluePrimary
import com.example.ui.theme.StudyOrangeAccent

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun MainAppContent(viewModel: AppViewModel, isDarkMode: MutableState<Boolean>) {
    val currentScreen = viewModel.navigationStack.lastOrNull() ?: Screen.Splash
    val loggedUser by viewModel.loggedUser.collectAsState()

    // Handle Android physical back presses safely
    BackHandler(enabled = viewModel.navigationStack.size > 1) {
        viewModel.navigateBack()
    }

    // Determine if bottom navigation should be visible (only inside secure portals, not splash/login/signup)
    val showBottomBar = loggedUser != null &&
            currentScreen != Screen.Splash &&
            currentScreen != Screen.Welcome &&
            currentScreen != Screen.Login &&
            currentScreen != Screen.SignUp &&
            currentScreen != Screen.AccountTypeSelector

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                RtlBottomBar(viewModel = viewModel, currentScreen = currentScreen)
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Render active view screen elements
            when (currentScreen) {
                is Screen.Splash -> {
                    SplashScreen(viewModel) {
                        viewModel.navigationStack.clear()
                        viewModel.navigationStack.add(Screen.Home)
                    }
                }
                is Screen.Welcome -> WelcomeScreen(viewModel)
                is Screen.Login -> LoginScreen(viewModel)
                is Screen.SignUp -> SignUpScreen(viewModel)
                is Screen.AccountTypeSelector -> AccountTypeSelectorScreen(viewModel)
                
                // Student Portal Screen roots
                is Screen.Home -> StudentHomeScreen(viewModel)
                is Screen.Subjects -> SubjectsGridScreen(viewModel)
                is Screen.Lessons -> LessonsListScreen(viewModel)
                is Screen.FileUpload -> FileUploadScreen(viewModel)
                is Screen.SavedFiles -> SavedFilesScreen(viewModel)
                is Screen.SavedVideos -> SavedVideosScreen(viewModel)
                is Screen.StudyPlan -> StudyPlanScreen(viewModel)
                is Screen.Results -> ResultsTrackerScreen(viewModel)

                // Advanced AI Workspaces
                is Screen.AiAssistant -> AiAssistantScreen(viewModel)
                is Screen.PdfSummary -> PdfSummaryWorkspaceScreen(viewModel)
                is Screen.PdfToVideo -> PdfToVideoPlayerScreen(viewModel)
                is Screen.Exams -> ExamsScreen(viewModel)

                // Secondary dashboards and profiles
                is Screen.TeacherDashboard -> TeacherDashboardScreen(viewModel)
                is Screen.AdminDashboard -> AdminDashboardScreen(viewModel)
                is Screen.Subscriptions -> SubscriptionsScreen(viewModel)
                is Screen.Profile -> ProfileScreen(viewModel)
                is Screen.Settings -> SettingsScreen(viewModel, isDarkMode)

                else -> StudentHomeScreen(viewModel)
            }
        }
    }
}

@Composable
fun RtlBottomBar(viewModel: AppViewModel, currentScreen: Screen) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp, vertical = 10.dp)
            .height(68.dp),
        shape = RoundedCornerShape(22.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f))
    ) {
        Row(
            modifier = Modifier.fillMaxSize().padding(horizontal = 4.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Tab 7: Settings / Profile
            RtlBottomBar3DItem(
                selected = currentScreen == Screen.Settings || currentScreen == Screen.Profile,
                onClick = {
                    viewModel.navigationStack.clear()
                    viewModel.navigationStack.add(Screen.Home)
                    viewModel.navigateTo(Screen.Settings)
                },
                icon = { Icon(Icons.Default.Settings, contentDescription = "الإعدادات") },
                label = "الإعدادات",
                accentColor = StudyOrangeAccent
            )

            // Tab 6: Weekly study organizer
            RtlBottomBar3DItem(
                selected = currentScreen == Screen.StudyPlan,
                onClick = {
                    viewModel.navigationStack.clear()
                    viewModel.navigationStack.add(Screen.Home)
                    viewModel.navigateTo(Screen.StudyPlan)
                },
                icon = { Icon(Icons.Default.Check, contentDescription = "المنظم") },
                label = "المنظم",
                accentColor = StudyBluePrimary
            )

            // Tab 5: Exams / Quizzes
            RtlBottomBar3DItem(
                selected = currentScreen == Screen.Exams || currentScreen == Screen.Results,
                onClick = {
                    viewModel.navigationStack.clear()
                    viewModel.navigationStack.add(Screen.Home)
                    viewModel.navigateTo(Screen.Exams)
                },
                icon = { Icon(Icons.Default.Star, contentDescription = "الاختبارات") },
                label = "الاختبارات",
                accentColor = StudyOrangeAccent
            )

            // Tab 4: Image/PDF to video converter (Requested: "تحويل صوره او pdf الي فيديو شرح")
            RtlBottomBar3DItem(
                selected = currentScreen == Screen.PdfToVideo,
                onClick = {
                    viewModel.navigationStack.clear()
                    viewModel.navigationStack.add(Screen.Home)
                    viewModel.navigateTo(Screen.PdfToVideo)
                },
                icon = { Icon(Icons.Default.PlayArrow, contentDescription = "فيديو شرح") },
                label = "فيديو شرح",
                accentColor = StudyBluePrimary
            )

            // Tab 3: Upload files & summarize
            RtlBottomBar3DItem(
                selected = currentScreen == Screen.SavedFiles || currentScreen == Screen.FileUpload || currentScreen == Screen.PdfSummary,
                onClick = {
                    viewModel.navigationStack.clear()
                    viewModel.navigationStack.add(Screen.Home)
                    viewModel.navigateTo(Screen.SavedFiles)
                },
                icon = { Icon(Icons.Default.Info, contentDescription = "ملخصاتي") },
                label = "ملخصاتي",
                accentColor = StudyOrangeAccent
            )

            // Tab 2: Conversations AI workspace
            RtlBottomBar3DItem(
                selected = currentScreen == Screen.AiAssistant,
                onClick = {
                    viewModel.navigationStack.clear()
                    viewModel.navigationStack.add(Screen.Home)
                    viewModel.navigateTo(Screen.AiAssistant)
                },
                icon = { Icon(Icons.Default.Send, contentDescription = "المساعد") },
                label = "المساعد",
                accentColor = StudyBluePrimary
            )

            // Tab 1: Home Dashboard
            RtlBottomBar3DItem(
                selected = currentScreen == Screen.Home,
                onClick = {
                    viewModel.navigationStack.clear()
                    viewModel.navigationStack.add(Screen.Home)
                },
                icon = { Icon(Icons.Default.Home, contentDescription = "الرئيسية") },
                label = "الرئيسية",
                accentColor = StudyOrangeAccent
            )
        }
    }
}

@Composable
fun RowScope.RtlBottomBar3DItem(
    selected: Boolean,
    onClick: () -> Unit,
    icon: @Composable () -> Unit,
    label: String,
    accentColor: Color
) {
    val scale by animateFloatAsState(targetValue = if (selected) 1.12f else 1.0f)
    val translationY by animateDpAsState(targetValue = if (selected) (-2).dp else 0.dp)

    Column(
        modifier = Modifier
            .weight(1f)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            )
            .padding(vertical = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .graphicsLayer {
                    scaleX = scale
                    scaleY = scale
                    this.translationY = translationY.toPx()
                }
                .size(24.dp),
            contentAlignment = Alignment.Center
        ) {
            // Shadow Layer for 3D effect
            Box(
                modifier = Modifier
                    .offset(x = 1.dp, y = 1.dp)
                    .graphicsLayer { alpha = if (selected) 0.5f else 0.15f }
            ) {
                CompositionLocalProvider(
                    LocalContentColor provides Color.Black
                ) {
                    icon()
                }
            }

            // Foreground Layer
            Box {
                CompositionLocalProvider(
                    LocalContentColor provides if (selected) accentColor else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                ) {
                    icon()
                }
            }
        }

        Spacer(modifier = Modifier.height(2.dp))

        Text(
            text = label,
            fontSize = 9.sp,
            fontWeight = if (selected) FontWeight.ExtraBold else FontWeight.Bold,
            color = if (selected) accentColor else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
        )
    }
}
