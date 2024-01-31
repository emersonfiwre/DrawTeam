package br.com.emersonfiwre.drawteam.features.home.view.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import br.com.emersonfiwre.drawteam.DrawTeamSession.numberOfPlayers
import br.com.emersonfiwre.drawteam.DrawTeamSession.numberOfTeams
import br.com.emersonfiwre.drawteam.DrawTeamSession.timerInMilliseconds
import br.com.emersonfiwre.drawteam.commons.extensions.millisecondsToInt
import br.com.emersonfiwre.drawteam.commons.extensions.minutesToMilliseconds
import br.com.emersonfiwre.drawteam.databinding.DrawTeamDialogSettingsBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class SettingBottomSheet: BottomSheetDialogFragment() {

    private lateinit var binding: DrawTeamDialogSettingsBinding

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
        setupViews()
    }

    private fun setupViews() {
        setupTimerMinuteValues()
        setupTeamNumberValues()
        setupPlayersNumberValues()
    }

    private fun setupTeamNumberValues() {
        binding.idDialogSettingNumberPickerTeam.maxValue = FIFTY_INT
        binding.idDialogSettingNumberPickerTeam.minValue = TWO_INT
        binding.idDialogSettingNumberPickerTeam.value = numberOfTeams

        binding.idDialogSettingNumberPickerTeam.setOnValueChangedListener { picker, oldVal, newVal ->
            numberOfTeams = picker.value
        }
    }

    private fun setupTimerMinuteValues() {
        binding.idDialogSettingNumberPickerTimerMinute.maxValue = FIFTY_NINE_INT
        binding.idDialogSettingNumberPickerTimerMinute.minValue = ONE_INT
        binding.idDialogSettingNumberPickerTimerMinute.millisecondsToInt(timerInMilliseconds)

        binding.idDialogSettingNumberPickerTimerMinute.setOnValueChangedListener { picker, oldVal, newVal ->
            timerInMilliseconds = picker.minutesToMilliseconds()
        }
    }

    private fun setupPlayersNumberValues() {
        binding.idDialogSettingNumberPickerPlayers.maxValue = HUNDRED_INT
        binding.idDialogSettingNumberPickerPlayers.minValue = TWO_INT
        binding.idDialogSettingNumberPickerPlayers.value = numberOfPlayers

        binding.idDialogSettingNumberPickerPlayers.setOnValueChangedListener { picker, oldVal, newVal ->
            numberOfPlayers = picker.value
        }
    }

    companion object {
        private const val FIFTY_NINE_INT = 59
        private const val ONE_INT = 1
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