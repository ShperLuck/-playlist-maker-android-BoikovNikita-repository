package com.example.playlistmaker_bk.ui.examples

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.playlistmaker_bk.ui.theme.PlaylistMaker_bkTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheetWithFabScreen(
    modifier: Modifier = Modifier
) {
    // BottomSheet
    var showBottomSheet by remember { mutableStateOf(false) }

    // Состояние панели (для управления анимацией, свайпами, частичным раскрытием)
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = false  // можно раскрывать не полностью
    )

    Box(
        modifier = modifier.fillMaxSize()
    ) {
        // Основной контент экрана (фон + пояснение)
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Нажми на кнопку + внизу справа,\nчтобы открыть панель",
                style = MaterialTheme.typography.headlineMedium,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onBackground
            )
        }

        // Плавающая кнопка FAB
        FloatingActionButton(
            onClick = { showBottomSheet = true },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(24.dp),
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = Color.White,
            shape = CircleShape,
            elevation = FloatingActionButtonDefaults.elevation(
                defaultElevation = 8.dp,
                pressedElevation = 12.dp
            )
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Добавить плейлист / трек"
            )
        }

        // ModalBottomSheet — появляется только когда showBottomSheet = true
        if (showBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = { showBottomSheet = false },
                sheetState = sheetState,
                containerColor = MaterialTheme.colorScheme.surface,
                tonalElevation = 8.dp,
                dragHandle = {  // стандартная полоска для свайпа
                    BottomSheetDefaults.DragHandle()
                }
            ) {
                // Содержимое панели (пример для будущего создания плейлиста)
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Создать новый плейлист",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Text(
                        text = "Здесь будет форма:\n" +
                                "• Название плейлиста\n" +
                                "• Описание\n" +
                                "• Обложка\n" +
                                "• Кнопка «Создать»",
                        style = MaterialTheme.typography.bodyLarge,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    // Пример кнопки действия внутри панели
                    Button(
                        onClick = { showBottomSheet = false },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Создать (закрыть для примера)")
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    TextButton(onClick = { showBottomSheet = false }) {
                        Text("Отмена")
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BottomSheetWithFabScreenPreview() {
    PlaylistMaker_bkTheme {
        BottomSheetWithFabScreen()
    }
}