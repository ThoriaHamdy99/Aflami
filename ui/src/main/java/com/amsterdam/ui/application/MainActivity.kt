package com.amsterdam.ui.application

import android.content.ComponentName
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            AflamiApp()
        }
    }


    override fun onDestroy() {
        super.onDestroy()

        val uiMode = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK

        val isDarkMode = when (uiMode) {
            Configuration.UI_MODE_NIGHT_YES   -> LauncherIcon.DARK
            Configuration.UI_MODE_NIGHT_NO    -> LauncherIcon.LIGHT
            else                              -> LauncherIcon.LIGHT
        }
        switchLauncherIcon(isDarkMode)
    }
    enum class LauncherIcon(val aliasName: String) {
        LIGHT(".LightLauncher"),
        DARK(".DarkLauncher"),
    }


    fun switchLauncherIcon(launcherIcon : LauncherIcon) {
        LauncherIcon.values().forEach {
            val state = if (launcherIcon == it) {
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED
            } else {
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED
            }
            packageManager.setComponentEnabledSetting(
                ComponentName(this, "$packageName${it.aliasName}"),
                state,
                PackageManager.DONT_KILL_APP
            )
        }
    }
}

