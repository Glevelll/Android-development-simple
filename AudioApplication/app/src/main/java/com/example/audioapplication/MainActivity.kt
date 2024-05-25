package com.example.audioapplication

import android.os.Bundle
import com.example.audioapplication.databinding.ActivityMainBinding
import androidx.appcompat.app.AppCompatActivity
import com.example.audioapplication.adapters.TabPagerAdapter
import com.google.android.material.tabs.TabLayoutMediator
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        startKoin {
            androidContext(this@MainActivity)
            modules(appModule)
        }

        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        setupUI()
    }

    private fun setupUI() = with(binding) {
        viewPager.adapter = TabPagerAdapter(this@MainActivity)

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            when (position) {
                0 -> tab.text = "Запись"
                1 -> tab.text = "Записи"
            }
        }.attach()
    }
}