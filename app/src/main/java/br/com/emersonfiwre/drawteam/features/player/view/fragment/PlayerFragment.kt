package br.com.emersonfiwre.drawteam.features.player.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.ItemTouchHelper
import br.com.emersonfiwre.drawteam.R
import br.com.emersonfiwre.drawteam.databinding.DrawTeamFragmentPlayersBinding
import br.com.emersonfiwre.drawteam.features.player.model.PlayerViewType
import br.com.emersonfiwre.drawteam.features.player.adapter.PlayerAdapter
import br.com.emersonfiwre.drawteam.features.player.view.dialog.AddPlayerDialogFragment
import br.com.emersonfiwre.drawteam.commons.view.dialog.ErrorDialogFragment
import br.com.emersonfiwre.drawteam.features.player.view.provider.ItemTouchHelperProvider
import br.com.emersonfiwre.drawteam.features.player.viewmodel.PlayerViewModel
import br.com.emersonfiwre.drawteam.features.player.viewmodel.factory.PlayerViewModelFactory
import br.com.emersonfiwre.drawteam.features.player.viewmodel.viewstate.PlayerViewState
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class PlayerFragment: Fragment() {

    private lateinit var binding: DrawTeamFragmentPlayersBinding

    private lateinit var viewModel: PlayerViewModel
    private var playerAdapter: PlayerAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DrawTeamFragmentPlayersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViewModel()
        setupObservers()
        setupAddErrorObservers()
        setupItemTouchHelper()
        setupRecyclerView()
        viewModel.setupPlayers()
    }

    private fun setupItemTouchHelper() {
        val touchHelper: ItemTouchHelper =
            ItemTouchHelper(
                ItemTouchHelperProvider(
                    requireContext(),
                    setupRemovePlayer()
                )
            )
        touchHelper.attachToRecyclerView(binding.idFragmentPlayerRecycler)
    }

    private fun setupRemovePlayer(): (position: Int) -> Unit = { position ->
        MaterialAlertDialogBuilder(requireContext(), R.style.DrawTeamDialogTheme)
            .setMessage(resources.getString(R.string.draw_team_want_to_delete_player))
            .setNegativeButton(getString(R.string.draw_team_no)) { dialog, which ->
                playerAdapter?.notifyItemChanged(position)
            }
            .setPositiveButton(getString(R.string.draw_team_yes)) { dialog, which ->
                viewModel.setupDeletePlayer(playerAdapter?.getItemByPosition(position))
            }
            .setOnCancelListener { playerAdapter?.notifyItemChanged(position) }
            .show()
    }

    private fun setupObservers() {
        viewModel.playerViewState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is PlayerViewState.PlayerListViewState.ShowPlayers -> {
                    setupUpdateItems(state.players)
                }

                PlayerViewState.PlayerListViewState.ShowSuccessAdd -> {
                    viewModel.setupPlayers()
                }
            }
        }
    }

    private fun setupAddErrorObservers() {
        viewModel.playerErrorViewState.observe(viewLifecycleOwner) { state ->
            when (state) {
                PlayerViewState.PlayerError.ShowAddError -> {
                    setupErrorAdd()
                }

                PlayerViewState.PlayerError.ShowListError -> {
                    setupErrorList()
                }

                PlayerViewState.PlayerError.ShowDeleteError -> {
                    setupErrorDelete()
                }
            }
        }
    }

    private fun setupErrorList() {
        binding.idFragmentPlayerRecycler.visibility = View.GONE
        binding.idFragmentPlayerError.idViewPlayerListError.visibility = View.VISIBLE
    }

    private fun setupErrorAdd() {
        ErrorDialogFragment.newInstance(
            childFragmentManager,
            getString(R.string.draw_team_add_error)
        )
    }

    private fun setupErrorDelete() {
        viewModel.setupPlayers()
        ErrorDialogFragment.newInstance(
            childFragmentManager,
            getString(R.string.draw_team_delete_error)
        )
    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(
            this, PlayerViewModelFactory(requireActivity().application)
        )[PlayerViewModel::class.java]
    }

    private fun setupUpdateItems(playersViewType: List<PlayerViewType>) {
        playerAdapter?.notifyItems(playersViewType)
    }

    private fun setupRecyclerView() {
        playerAdapter =
            PlayerAdapter(
                mutableListOf(),
                ::setupAddPlayer
            )
        binding.idFragmentPlayerRecycler.run {
            itemAnimator = DefaultItemAnimator()
            adapter = playerAdapter
        }
    }

    private fun setupAddPlayer() {
        AddPlayerDialogFragment.newInstance(childFragmentManager)
    }

    companion object {

        @JvmStatic
        fun newInstance() = PlayerFragment()
    }
}
