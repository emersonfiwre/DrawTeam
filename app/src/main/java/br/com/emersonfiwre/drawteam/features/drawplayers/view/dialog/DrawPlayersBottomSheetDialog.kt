package br.com.emersonfiwre.drawteam.features.drawplayers.view.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import br.com.emersonfiwre.drawteam.R
import br.com.emersonfiwre.drawteam.commons.constants.DrawTeamConstants.ZERO_INT
import br.com.emersonfiwre.drawteam.commons.view.dialog.ErrorDialogFragment
import br.com.emersonfiwre.drawteam.databinding.DrawTeamDialogDrawBinding
import br.com.emersonfiwre.drawteam.features.drawplayers.view.adapter.PlayerSelectionsAdapter
import br.com.emersonfiwre.drawteam.features.drawplayers.viewmodel.DrawPlayersViewModel
import br.com.emersonfiwre.drawteam.features.drawplayers.viewmodel.factory.DrawPlayersViewModelFactory
import br.com.emersonfiwre.drawteam.features.drawplayers.viewmodel.viewstate.DrawPlayersViewState
import br.com.emersonfiwre.drawteam.commons.model.TeamModel
import br.com.emersonfiwre.drawteam.features.drawplayers.view.listener.TextWatchImpl
import br.com.emersonfiwre.drawteam.commons.model.PlayerModel
import br.com.emersonfiwre.drawteam.features.player.model.PlayerStateEnum
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class DrawPlayersBottomSheetDialog: BottomSheetDialogFragment() {

    private lateinit var binding: DrawTeamDialogDrawBinding

    private lateinit var viewModel: DrawPlayersViewModel
    private var drawPlayerAdapter: PlayerSelectionsAdapter? = null

    private val playerList = mutableListOf<PlayerModel>()

    var onDrawClickReceived: (teams: List<TeamModel>) -> Unit = {}

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DrawTeamDialogDrawBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (dialog as? BottomSheetDialog)?.behavior?.state = BottomSheetBehavior.STATE_EXPANDED
        setupViewModel()
        setupPlayerListObservers()
        setupPlayerFoundedObservers()
        setupTeamObservers()
        setupErrorObservers()
        setupViews()
        setupRecyclerView()
        viewModel.getPlayers()
    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(
            this,
            DrawPlayersViewModelFactory(requireActivity().application)
        )[DrawPlayersViewModel::class.java]
    }

    private fun setupPlayerListObservers() {
        viewModel.playerListViewState.observe(viewLifecycleOwner) { state ->
            when (state) {
                DrawPlayersViewState.PlayerListViewState.ShowEmptyState -> {
                    setupEmptyPlayers()
                }

                is DrawPlayersViewState.PlayerListViewState.ShowPlayers -> {
                    setupPlayers(state.players)
                }
            }
        }
    }

    private fun setupPlayerFoundedObservers() {
        viewModel.playerListFoundedViewState.observe(viewLifecycleOwner) { state ->
            when (state) {
                DrawPlayersViewState.PlayerListFoundedViewState.ShowEmptyState -> {
                    setupEmptyPlayers()
                }

                is DrawPlayersViewState.PlayerListFoundedViewState.ShowPlayers -> {
                    setupPlayersFounded(state.players)
                }
            }
        }
    }

    private fun setupTeamObservers() {
        viewModel.shuffledPlayersViewState.observe(viewLifecycleOwner) { state ->
            when (state) {
                DrawPlayersViewState.ShuffledPlayersViewState.ShowNoPlayersSelected -> {
                    setupDialogUnselectedPlayers()
                }

                is DrawPlayersViewState.ShuffledPlayersViewState.ShowTeams -> {
                    setupShuffleClick(state.teams)
                }

                DrawPlayersViewState.ShuffledPlayersViewState.ShowWithoutEnoughPlayers -> {
                    setupDialogWithoutEnoughPlayer()
                }
            }
        }
    }

    private fun setupErrorObservers() {
        viewModel.drawErrorViewState.observe(viewLifecycleOwner) { state ->
            when (state) {
                DrawPlayersViewState.DrawPlayersError.ShowDrawError -> {
                    setupDialogErrorDraw()
                }

                DrawPlayersViewState.DrawPlayersError.ShowPlayerListError -> {
                    setupErrorGetPlayers()
                }

                DrawPlayersViewState.DrawPlayersError.ShowSearchError -> {
                    setupErrorSearchPlayers()
                }
            }
        }
    }

    private fun setupShuffleClick(teams: List<TeamModel>) {
        onDrawClickReceived(teams)
        dismiss()
    }

    private fun setupDialogWithoutEnoughPlayer() {
        ErrorDialogFragment.newInstance(
            childFragmentManager,
            getString(R.string.draw_team_fuck_off)
        )
    }

    private fun setupDialogUnselectedPlayers() {
        ErrorDialogFragment.newInstance(
            childFragmentManager,
            getString(R.string.draw_team_unselected_players)
        )
    }

    private fun setupDialogErrorDraw() {
        ErrorDialogFragment.newInstance(
            childFragmentManager,
            getString(R.string.draw_team_draw_error)
        )
    }

    private fun setupErrorSearchPlayers() {
        ErrorDialogFragment.newInstance(
            childFragmentManager,
            getString(R.string.draw_team_error_search_players)
        )
    }

    private fun setupEmptyPlayers() {
        binding.idDialogDrawRecycler.visibility = View.GONE
        binding.idDialogDrawTxtInputSearch.visibility = View.GONE
        binding.idDialogDrawTxtCountPlayers.visibility = View.GONE
        binding.idEmptyPlayerContainer.idViewEmptyPlayer.visibility = View.VISIBLE
        binding.idDialogDrawButton.visibility = View.GONE
    }

    private fun setupErrorGetPlayers() {
        binding.idDialogDrawRecycler.visibility = View.GONE
        binding.idDialogDrawTxtInputSearch.visibility = View.GONE
        binding.idDialogDrawTxtCountPlayers.visibility = View.GONE
        binding.idErrorGetPlayersContainer.idViewErrorGetPlayers.visibility = View.VISIBLE
        binding.idDialogDrawButton.visibility = View.GONE
    }

    private fun setupPlayers(players: List<PlayerModel>) {
        playerList.clear()
        playerList.addAll(players)
        drawPlayerAdapter?.notifyItems(playerList)
    }

    private fun setupPlayersFounded(players: List<PlayerModel>) {
        drawPlayerAdapter?.notifyItems(players)
    }

    private fun setupUpdateListCheck(): (playerModel: PlayerModel) -> Unit = { player ->
        playerList.find { it.uid == player.uid }?.playerState = player.playerState
        setupCountPlayersSelected(
            playerList.count { it.playerState == PlayerStateEnum.CHECKED }
        )
    }

    private fun setupRecyclerView() {
        drawPlayerAdapter =
            PlayerSelectionsAdapter(
                mutableListOf(),
                setupUpdateListCheck()
            )
        binding.idDialogDrawRecycler.run {
            itemAnimator = DefaultItemAnimator()
            adapter = drawPlayerAdapter
        }
    }

    private fun setupViews() {
        binding.idDialogDrawButton.setOnClickListener {
            viewModel.setupDrawPlayers(playerList)
        }
        setupCountPlayersSelected()
        setupSearch()
    }

    private fun setupCountPlayersSelected(selected: Int? = null) {
        binding.idDialogDrawTxtCountPlayers.text =
            getString(R.string.draw_team_players_count, selected ?: ZERO_INT)
    }

    private fun setupSearch() {
        binding.idDialogDrawEditSearch.addTextChangedListener(TextWatchImpl {
            viewModel.filterByKeywords(it.toString(), playerList)
        })
    }

    companion object {

        @JvmStatic
        fun newInstance(
            fragmentManager: FragmentManager,
            onDrawClick: (teams: List<TeamModel>) -> Unit
        ) {
            DrawPlayersBottomSheetDialog().apply {
                onDrawClickReceived = onDrawClick
            }.show(fragmentManager, DrawPlayersBottomSheetDialog::javaClass.name)
        }
    }
}
