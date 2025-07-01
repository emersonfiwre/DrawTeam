package br.com.emersonfiwre.drawteam.features.home.view.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import br.com.emersonfiwre.drawteam.DrawTeamSession
import br.com.emersonfiwre.drawteam.databinding.DrawTeamDialogSettingsBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class SettingBottomSheet: BottomSheetDialogFragment() {

    private lateinit var binding: DrawTeamDialogSettingsBinding

    private var teams = 0
    private var players = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DrawTeamDialogSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (dialog as? BottomSheetDialog)?.behavior?.state = BottomSheetBehavior.STATE_EXPANDED
        teams = DrawTeamSession.numberOfTeams
        players = DrawTeamSession.numberOfPlayers
        setupViews()
    }

    private fun setupViews() {
        setupTeamNumberValues()
        setupPlayersNumberValues()
        setupClick()
    }

    private fun setupClick() {
        binding.idDialogSettingBtnSave.setOnClickListener{
            DrawTeamSession.numberOfTeams = teams
            DrawTeamSession.numberOfPlayers = players
            dismiss()
        }

        binding.idDialogSettingBtnCancel.setOnClickListener {
            dismiss()
        }
    }

    private fun setupTeamNumberValues() {
        binding.idDialogSettingNumberPickerTeam.maxValue = FIFTY_INT
        binding.idDialogSettingNumberPickerTeam.minValue = TWO_INT
        binding.idDialogSettingNumberPickerTeam.value = teams

        binding.idDialogSettingNumberPickerTeam.setOnValueChangedListener { picker, oldVal, newVal ->
            teams = picker.value
        }
    }

    private fun setupPlayersNumberValues() {
        binding.idDialogSettingNumberPickerPlayers.maxValue = HUNDRED_INT
        binding.idDialogSettingNumberPickerPlayers.minValue = TWO_INT
        binding.idDialogSettingNumberPickerPlayers.value = players

        binding.idDialogSettingNumberPickerPlayers.setOnValueChangedListener { picker, oldVal, newVal ->
            players = picker.value
        }
    }

    companion object {
        private const val HUNDRED_INT = 100
        private const val FIFTY_INT = 50
        private const val TWO_INT = 2

        @JvmStatic
        fun newInstance(fragmentManager: FragmentManager) {
            SettingBottomSheet().show(
                fragmentManager,
                SettingBottomSheet::javaClass.name
            )
        }
    }
}