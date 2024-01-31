package br.com.emersonfiwre.drawteam.features.home.view.fragment

import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import br.com.emersonfiwre.drawteam.DrawTeamSession
import br.com.emersonfiwre.drawteam.databinding.DrawTeamFragmentHomeBinding
import br.com.emersonfiwre.drawteam.commons.model.TeamModel
import br.com.emersonfiwre.drawteam.features.home.model.TeamViewType
import br.com.emersonfiwre.drawteam.features.home.view.adapter.TeamsAdapter
import br.com.emersonfiwre.drawteam.features.drawplayers.view.dialog.DrawPlayersBottomSheetDialog
import br.com.emersonfiwre.drawteam.features.home.view.dialog.SettingBottomSheet
import br.com.emersonfiwre.drawteam.features.home.view.provider.CountDownTimerImpl
import br.com.emersonfiwre.drawteam.features.home.viewmodel.HomeViewModel
import br.com.emersonfiwre.drawteam.features.home.viewmodel.factory.HomeViewModelFactory
import br.com.emersonfiwre.drawteam.features.home.viewmodel.viewstate.HomeViewState

class HomeFragment: Fragment() {

    private lateinit var binding: DrawTeamFragmentHomeBinding

    private lateinit var viewModel: HomeViewModel
    private var teamAdapter: TeamsAdapter? = null

    private val timer by lazy { DrawTeamSession.timerInMilliseconds }
    private var countDown: CountDownTimer? = null

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

    private fun setupClick() {
        binding.idDrawButton.setOnClickListener {
            DrawPlayersBottomSheetDialog.newInstance(childFragmentManager, setupOnDraw())
        }

        binding.idDrawStartGame.setOnClickListener {
            countDown = CountDownTimerImpl(binding.idDrawTimer, timer).start()
        }

        binding.idDrawCancelGame.setOnClickListener {
            countDown?.cancel()
        }

        binding.idDrawConfig.setOnClickListener {
            SettingBottomSheet.newInstance(childFragmentManager)
        }
    }

    private fun setupOnDraw(): (teams: List<TeamModel>) -> Unit = { teamList ->
        viewModel.setupListTeams(teamList)
    }

    companion object {

        @JvmStatic
        fun newInstance() = HomeFragment()
    }
}