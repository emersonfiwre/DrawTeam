package br.com.emersonfiwre.drawteam.commons.view.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import br.com.emersonfiwre.drawteam.R
import br.com.emersonfiwre.drawteam.commons.extensions.disableDontKeepActivities
import br.com.emersonfiwre.drawteam.databinding.DrawTeamActivityMainBinding
import br.com.emersonfiwre.drawteam.features.home.view.fragment.HomeFragment
import br.com.emersonfiwre.drawteam.features.player.view.fragment.PlayerFragment

class MainActivity: AppCompatActivity() {

    private lateinit var binding: DrawTeamActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DrawTeamActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        replaceFragment(HomeFragment())
        setupBottomNavigation()
    }

    private fun setupBottomNavigation() {
        binding.bottomNavigation.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.page_1 -> {
                    replaceFragment(HomeFragment.newInstance())
                    true
                }

                R.id.page_2 -> {
                    replaceFragment(PlayerFragment.newInstance())
                    true
                }

                else -> {
                    false
                }
            }
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.id_container_main, fragment).commit()
    }
}