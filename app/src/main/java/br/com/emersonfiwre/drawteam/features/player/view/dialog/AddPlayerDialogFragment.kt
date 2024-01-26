package br.com.emersonfiwre.drawteam.features.player.view.dialog

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import br.com.emersonfiwre.drawteam.R
import br.com.emersonfiwre.drawteam.databinding.DrawTeamDialogAddPlayerBinding
import br.com.emersonfiwre.drawteam.features.player.viewmodel.PlayerViewModel
import br.com.emersonfiwre.drawteam.features.player.viewmodel.factory.PlayerViewModelFactory
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class AddPlayerDialogFragment: DialogFragment() {

    private lateinit var binding: DrawTeamDialogAddPlayerBinding

    private lateinit var viewModel: PlayerViewModel

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = MaterialAlertDialogBuilder(requireContext(), R.style.DrawTeamDialogTheme)
        setupViewModel()

        binding = DrawTeamDialogAddPlayerBinding.inflate(
            LayoutInflater.from(requireContext()),
            null,
            false
        )
        val dialog = binding.root
        builder.setView(dialog)

        builder
            .setNegativeButton(resources.getString(R.string.draw_team_cancel)) { _, _ ->
                // Respond to negative button press
            }
            .setPositiveButton(resources.getString(R.string.draw_team_save)) { _, _ ->
                setupSavePlayer()
            }

        return builder.create()
    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(
            requireParentFragment(), PlayerViewModelFactory(requireActivity().application)
        )[PlayerViewModel::class.java]
    }

    private fun setupSavePlayer() {
        if (!binding.idDialogEditName.text.isNullOrBlank()) {
            viewModel.setupAddPlayers(
                binding.idDialogEditName.text.toString(),
                binding.idRadioButtonGoalkeeper.isChecked
            )
        } else {
            Toast.makeText(context, R.string.draw_team_please_fill_name, Toast.LENGTH_SHORT).show()
        }
    }

    companion object {

        @JvmStatic
        fun newInstance(fragmentManager: FragmentManager) {
            AddPlayerDialogFragment().show(fragmentManager, AddPlayerDialogFragment::javaClass.name)
        }
    }
}