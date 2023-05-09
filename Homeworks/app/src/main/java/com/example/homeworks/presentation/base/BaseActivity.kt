package com.example.homeworks.presentation.base

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import java.util.Locale

open class BaseActivity(
    layout: Int,
    protected val fragmentsContainerId: Int
) : AppCompatActivity(layout) {

    private var requestPermissionsLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted)
                Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show()
            else if (!shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION))
                showPermsOnSetting()
        }

    private var settingsLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { _ ->
            if (checkSelfPermission(
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ) Toast.makeText(this, "Permission is granted", Toast.LENGTH_SHORT).show()
            else Toast.makeText(this, "Permission isn't granted, try again", Toast.LENGTH_SHORT).show()
        }

    fun requestPermissionWithRationale(permission: String) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
            Snackbar.make(
                findViewById(android.R.id.content),
                "Allow access to $permission",
                Snackbar.LENGTH_INDEFINITE
            ).setAction("Allow".uppercase())
            {
                requestPermission(permission)
            }.show()
        } else requestPermission(permission)
    }

    fun requestPermission(permission: String) {
        requestPermissionsLauncher.launch(permission)
    }

    private fun showPermsOnSetting() {
        Snackbar.make(
            findViewById(android.R.id.content),
            "Give permission manually", Snackbar.LENGTH_LONG
        )
            .setAction(
                "settings".uppercase(Locale.getDefault())
            ) { openApplicationSettings() }
            .show()
    }

    private fun openApplicationSettings() {
        val appSettingsIntent = Intent(
            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
            Uri.parse("package:$packageName")
        )
        settingsLauncher.launch(appSettingsIntent)
    }

    fun replaceFragment(fragment: Fragment, tag: String) {
        supportFragmentManager.beginTransaction().replace(
            fragmentsContainerId,
            fragment,
            tag
        ).commit()
    }
}