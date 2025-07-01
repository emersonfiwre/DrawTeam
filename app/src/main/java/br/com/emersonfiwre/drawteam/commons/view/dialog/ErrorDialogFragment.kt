package br.com.emersonfiwre.drawteam.commons.view.dialog

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import br.com.emersonfiwre.drawteam.R
import br.com.emersonfiwre.drawteam.databinding.DrawTeamDialogErrorBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

private const val ARG_DESC = "description"
private const val ARG_WARNING = "warning"

class ErrorDialogFragment: DialogFragment() {
    private var descriptionReceived: String? = null
    private var isWarning: Boolean? = null

    private lateinit var binding: DrawTeamDialogErrorBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            descriptionReceived = it.getString(ARG_DESC)
            isWarning = it.getBoolean(ARG_WARNING)
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder =
            MaterialAlertDialogBuilder(
                requireContext(),
                R.style.DrawTeamDialogTheme
            ).setPositiveButton(resources.getString(R.string.draw_team_ok)) { _, _ ->
                // Not yet implemented
            }

        binding = DrawTeamDialogErrorBinding.inflate(
            LayoutInflater.from(requireContext()), null, false
        )
        val dialog = binding.root
        builder.setView(dialog)
        setupViews()

        return builder.create()
    }

    private fun setupViews() {
        binding.idDialogErrorDescription.text = descriptionReceived
        if (isWarning == true) {
            binding.idDialogErrorIcon.setImageDrawable(
                ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.draw_team_ic_pay_attention
                )
            )
            binding.idDialogErrorTitle.text = getString(R.string.draw_team_pay_attention)
        }
    }

    companion object {

        @JvmStatic
        fun newInstance(
            fragmentManager: FragmentManager,
            description: String,
            warning: Boolean = false
        ) {
            ErrorDialogFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_DESC, description)
                    putBoolean(ARG_WARNING, warning)
                }
            }.show(fragmentManager, ErrorDialogFragment::javaClass.name)
        }
    }
}
