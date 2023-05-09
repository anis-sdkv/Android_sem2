package com.example.homeworks.presentation

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.homeworks.R
import com.example.homeworks.databinding.ActivityMainBinding
import com.example.homeworks.di.DataDependency
import com.example.homeworks.presentation.base.BaseActivity
import com.example.homeworks.presentation.fragments.main.MainFragment
import com.google.android.material.snackbar.Snackbar
import java.util.Locale

class MainActivity : BaseActivity(R.layout.activity_main, R.id.activity_fragments_container) {
    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        DataDependency.init(this)
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


    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }
}