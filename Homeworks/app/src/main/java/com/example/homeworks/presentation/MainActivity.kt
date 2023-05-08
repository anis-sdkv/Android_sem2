package com.example.homeworks.presentation

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.homeworks.R
import com.example.homeworks.data.room.RoomWeatherRepository
import com.example.homeworks.databinding.ActivityMainBinding
import com.example.homeworks.presentation.fragments.MainFragment
import com.google.android.material.snackbar.Snackbar
import java.util.Locale

class MainActivity : AppCompatActivity(R.layout.activity_main) {
    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    private val fragmentsContainerId: Int = R.id.activity_fragments_container

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        repository = RoomWeatherRepository(this)
        initFragments()
    }

    private fun initFragments() {
        val fragment = supportFragmentManager.findFragmentByTag(MainFragment.FRAGMENT_TAG)
        if (fragment != null) return
        supportFragmentManager.beginTransaction().add(
            fragmentsContainerId,
            MainFragment.getInstance(),
            MainFragment.FRAGMENT_TAG
        ).commit()
    }

    var requestPermissions =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted)
                Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show()
            else if (!shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION))
                showPermsOnSetting()
        }

    private var settings =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { _ ->
            if (checkSelfPermission(
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ) Toast.makeText(this, "Permission is granted", Toast.LENGTH_SHORT).show()
            else Toast.makeText(this, "Permission isn't granted, try again", Toast.LENGTH_SHORT).show()
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
        settings.launch(appSettingsIntent)
    }

    fun replaceFragment(fragment: Fragment, tag: String) {
        supportFragmentManager.beginTransaction().replace(
            fragmentsContainerId,
            fragment,
            tag
        ).commit()
    }


    override fun onDestroy() {
        _binding = null
        repository = null
        super.onDestroy()
    }

    companion object{
        var repository: RoomWeatherRepository? = null
    }
}