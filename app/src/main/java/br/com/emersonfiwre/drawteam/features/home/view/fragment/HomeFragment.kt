package br.com.emersonfiwre.drawteam.features.home.view.fragment

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import br.com.emersonfiwre.drawteam.DrawTeamSession
import br.com.emersonfiwre.drawteam.R
import br.com.emersonfiwre.drawteam.commons.extensions.disableDontKeepActivities
import br.com.emersonfiwre.drawteam.commons.extensions.serializable
import br.com.emersonfiwre.drawteam.commons.model.TeamModel
import br.com.emersonfiwre.drawteam.databinding.DrawTeamFragmentHomeBinding
import br.com.emersonfiwre.drawteam.features.drawplayers.view.activity.DrawPlayersActivity
import br.com.emersonfiwre.drawteam.features.drawplayers.view.activity.DrawPlayersActivity.Companion.LIST_TEAMS_RESULT
import br.com.emersonfiwre.drawteam.features.home.model.TeamViewType
import br.com.emersonfiwre.drawteam.features.home.view.adapter.TeamsAdapter
import br.com.emersonfiwre.drawteam.features.home.view.dialog.SettingBottomSheet
import br.com.emersonfiwre.drawteam.features.home.viewmodel.HomeViewModel
import br.com.emersonfiwre.drawteam.features.home.viewmodel.factory.HomeViewModelFactory
import br.com.emersonfiwre.drawteam.features.home.viewmodel.viewstate.HomeViewState

class HomeFragment: Fragment() {

    private lateinit var binding: DrawTeamFragmentHomeBinding

    private lateinit var viewModel: HomeViewModel
    private var teamAdapter: TeamsAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DrawTeamFragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViewModel()
        setupObservers()
        setupTimerObserver()
        setupErrorObservers()
        setupRecyclerView()
        setupClick()
    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(
            this,
            HomeViewModelFactory()
        )[HomeViewModel::class.java]
    }

    private fun setupObservers() {
        viewModel.homeListOfTeamViewState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is HomeViewState.HomeListOfTeamViewState.ShowTeams -> {
                    setupTeamsShuffled(state.players)
                }

                is HomeViewState.HomeListOfTeamViewState.ShowTwoTeams -> {
                    setupTwoTeamsShuffled(state.firstTeam, state.secondTeam)
                }
            }
        }
    }

    private fun setupTimerObserver() {
        viewModel.formattedTime.observe(viewLifecycleOwner) { state ->
            binding.idDrawTimer.text = state
        }
        viewModel.isRunning.observe(viewLifecycleOwner) { state ->
            updateButtonIcon(state, viewModel.timeMillis.value ?: 0L)
            binding.idDrawStopGame.isEnabled = state
        }

        viewModel.timeMillis.observe(viewLifecycleOwner) { state ->
            updateButtonIcon(viewModel.isRunning.value ?: false, state)
        }
    }

    private fun updateButtonIcon(running: Boolean, currentTimeMillis: Long) {
        if (running || (currentTimeMillis > 0L && !running)) {
            binding.idDrawStartGame.run {
                setImageResource(R.drawable.draw_team_ic_refresh)
                setOnClickListener {
                    viewModel.toggleOrReset()
                }
            }
        } else {
            binding.idDrawStartGame.run {
                setImageResource(R.drawable.draw_team_ic_play)
                setOnClickListener {
                    viewModel.startTimer()
                }
            }
        }
    }

    private fun setupErrorObservers() {
        viewModel.playerErrorViewState.observe(viewLifecycleOwner) { state ->
            when (state) {
                HomeViewState.HomeError.ShowPresentationError -> {
                    setupError()
                }
            }
        }
    }

    private fun setupRecyclerView() {
        teamAdapter = TeamsAdapter(
            mutableListOf()
        )
        binding.idDrawRecyclerTeam.run {
            itemAnimator = DefaultItemAnimator()
            adapter = teamAdapter
        }
    }

    private fun setupError() {
        binding.idDrawRecyclerTeam.visibility = View.GONE
        binding.idDrawRecyclerTeam2.visibility = View.GONE
        binding.idErrorHomeContainer.idViewErrorHomePresentation.visibility = View.VISIBLE
    }

    private fun setupTeamsShuffled(players: List<TeamViewType>) {
        binding.idDrawRecyclerTeam2.visibility = View.GONE
        teamAdapter?.notifyItems(players)
    }

    private fun setupTwoTeamsShuffled(
        firstTeam: List<TeamViewType>,
        secondTeam: List<TeamViewType>
    ) {
        teamAdapter?.notifyItems(firstTeam)
        setupSecondRecyclerView(secondTeam)
    }

    private fun setupSecondRecyclerView(secondTeam: List<TeamViewType>) {
        binding.idDrawRecyclerTeam2.run {
            visibility = View.VISIBLE
            itemAnimator = DefaultItemAnimator()
            adapter = TeamsAdapter(
                secondTeam.toMutableList()
            )
        }
    }

    private val startForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                val teams = result.data?.serializable<ArrayList<TeamModel>>(LIST_TEAMS_RESULT)
                viewModel.setupListTeams(teams)
            }
        }

    private fun setupClick() {
        binding.idDrawButton.setOnClickListener {
            DrawPlayersActivity.newInstance(context, startForResult)
        }

        binding.idDrawStartGame.setOnClickListener {
            viewModel.startTimer()
        }

        binding.idDrawStopGame.setOnClickListener {
            viewModel.stopTimer()
        }

        binding.idDrawConfig.setOnClickListener {
            SettingBottomSheet.newInstance(childFragmentManager)
        }
    }

    override fun onResume() {
        super.onResume()
        if (context.disableDontKeepActivities()) {
            DrawTeamSession.teamsShuffledSession?.let { viewModel.setupListTeams(it) }
        }
    }

    companion object {

        @JvmStatic
        fun newInstance() = HomeFragment()
    }
}