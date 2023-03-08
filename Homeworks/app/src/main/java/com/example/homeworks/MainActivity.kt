package com.example.homeworks

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.homeworks.databinding.ActivityMainBinding
import com.example.homeworks.fragments.MainFragment

class MainActivity : AppCompatActivity() {
    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    private val fragmentsContainerId: Int = R.id.activity_fragments_container

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
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