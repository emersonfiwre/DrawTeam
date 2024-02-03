package br.com.emersonfiwre.drawteam.features.drawplayers.view.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import br.com.emersonfiwre.drawteam.DrawTeamSession
import br.com.emersonfiwre.drawteam.R
import br.com.emersonfiwre.drawteam.commons.extensions.disableDontKeepActivities
import br.com.emersonfiwre.drawteam.commons.model.TeamModel
import br.com.emersonfiwre.drawteam.commons.view.listener.DrawTeamListener
import br.com.emersonfiwre.drawteam.databinding.DrawTeamActivityDrawPlayersBinding
import br.com.emersonfiwre.drawteam.features.drawplayers.view.fragment.DrawPlayersFragment

class DrawPlayersActivity: AppCompatActivity(), DrawTeamListener {

    private lateinit var binding: DrawTeamActivityDrawPlayersBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DrawTeamActivityDrawPlayersBinding.inflate(layoutInflater)
        setContentView(binding.root)
        replaceFragment(DrawPlayersFragment.newInstance())
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(
                R.id.id_draw_players_container,
                fragment
            ).commit()
    }

    override fun onDrawClick(teams: List<TeamModel>) {
        if (this.disableDontKeepActivities()) {
            DrawTeamSession.teamsShuffledSession = teams
            finish()
        } else {
            setResult(
                Activity.RESULT_OK,
                Intent().apply { putExtra(LIST_TEAMS_RESULT, ArrayList(teams)) })
            finish()
        }
    }

    companion object {

        const val LIST_TEAMS_RESULT = "ListTeamsResult"

        @JvmStatic
        fun newInstance(context: Context) {
            context.startActivity(Intent(context, DrawPlayersActivity::class.java))
        }

        @JvmStatic
        fun newInstance(context: Context?, startForResult: ActivityResultLauncher<Intent>) {
            startForResult.launch(Intent(context, DrawPlayersActivity::class.java))
        }
    }
}
