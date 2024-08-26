package com.example.cuddle

import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.example.cuddle.databinding.ActivityMainBinding
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private lateinit var binding: ActivityMainBinding
    private lateinit var actionBarDrawerToggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbar = binding.toolbar
        setSupportActionBar(toolbar)

        // Set up the ActionBarDrawerToggle
        actionBarDrawerToggle = ActionBarDrawerToggle(
            this,
            binding.drawerLayout,
            toolbar,
            R.string.open,  // String resource for open drawer description
            R.string.close  // String resource for close drawer description
        )

        // Attach the ActionBarDrawerToggle to the DrawerLayout
        binding.drawerLayout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()

        // Enable the hamburger icon by setting it as the default
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(actionBarDrawerToggle.drawerArrowDrawable)


        binding.navigationView.setNavigationItemSelectedListener(this)

        val navController = findNavController(R.id.navHostFragment)
        NavigationUI.setupWithNavController(binding.bottomNavigationView, navController)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.favorite -> Toast.makeText(this, "Favorite", Toast.LENGTH_SHORT).show()
            R.id.rateUs -> Toast.makeText(this, "Rate Us", Toast.LENGTH_SHORT).show()
            R.id.shareApp -> Toast.makeText(this, "Share App", Toast.LENGTH_SHORT).show()
            R.id.termsCondition -> Toast.makeText(this, "Terms and Conditions", Toast.LENGTH_SHORT).show()
            R.id.privacyPolicy -> Toast.makeText(this, "Privacy Policy", Toast.LENGTH_SHORT).show()
            R.id.developerInfo -> Toast.makeText(this, "Developer Info", Toast.LENGTH_SHORT).show()
        }
        return true
    }
}
