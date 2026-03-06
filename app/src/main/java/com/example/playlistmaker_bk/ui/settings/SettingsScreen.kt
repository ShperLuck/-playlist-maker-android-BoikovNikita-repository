package com.example.playlistmaker_bk.ui.settings

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.playlistmaker_bk.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onBack: () -> Unit // 1. Добавил параметр onBack
) {
    val context = LocalContext.current

    val shareText = stringResource(R.string.share_text)
    val emailAddress = stringResource(R.string.email_address)
    val emailSubject = stringResource(R.string.email_subject)
    val emailBody = stringResource(R.string.email_body)
    val agreementUrl = stringResource(R.string.agreement_url)

    Scaffold(
        topBar = {
            // 2. Добавил TopAppBar с кнопкой назад
            TopAppBar(
                title = { Text(stringResource(R.string.settings_title)) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Назад"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Кнопки настроек
                SettingsButton(
                    title = stringResource(R.string.share_app),
                    onClick = { shareApp(context, shareText) }
                )

                SettingsButton(
                    title = stringResource(R.string.write_to_developers),
                    onClick = { writeToDevelopers(context, emailAddress, emailSubject, emailBody) }
                )

                SettingsButton(
                    title = stringResource(R.string.user_agreement),
                    onClick = { openUserAgreement(context, agreementUrl) }
                )
            }
        }
    }
}

@Composable
private fun SettingsButton(
    title: String,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        shape = MaterialTheme.shapes.medium
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium
        )
    }
}

/* ---------- Logic (Интенты остаются без изменений) ---------- */

private fun shareApp(context: Context, shareText: String) {
    val shareIntent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_TEXT, shareText)
    }
    context.startActivity(Intent.createChooser(shareIntent, null))
}

private fun writeToDevelopers(context: Context, email: String, subject: String, body: String) {
    val emailIntent = Intent(Intent.ACTION_SENDTO).apply {
        data = Uri.parse("mailto:")
        putExtra(Intent.EXTRA_EMAIL, arrayOf(email))
        putExtra(Intent.EXTRA_SUBJECT, subject)
        putExtra(Intent.EXTRA_TEXT, body)
    }
    context.startActivity(Intent.createChooser(emailIntent, null))
}

private fun openUserAgreement(context: Context, url: String) {
    val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
    context.startActivity(Intent.createChooser(browserIntent, null))
}