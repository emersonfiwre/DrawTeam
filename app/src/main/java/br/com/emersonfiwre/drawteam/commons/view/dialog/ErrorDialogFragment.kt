package br.com.emersonfiwre.drawteam.commons.view.dialog

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import br.com.emersonfiwre.drawteam.R
import br.com.emersonfiwre.drawteam.databinding.DrawTeamDialogErrorBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

private const val ARG_DESC = "description"

class ErrorDialogFragment: DialogFragment() {
    private var descriptionReceived: String? = null

    private lateinit var binding: DrawTeamDialogErrorBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            descriptionReceived = it.getString(ARG_DESC)
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder =
            MaterialAlertDialogBuilder(
                requireContext(),
                R.style.DrawTeamDialogTheme
            ).setPositiveButton(resources.getString(R.string.draw_team_ok)) { _, _ ->
                // not implementation yet
            }

        binding = DrawTeamDialogErrorBinding.inflate(
            LayoutInflater.from(requireContext()), null, false
        )
        val dialog = binding.root
        builder.setView(dialog)
        binding.idDialogErrorDescription.text = descriptionReceived
        return builder.create()
    }

    companion object {

        @JvmStatic
        fun newInstance(fragmentManager: FragmentManager, description: String) {
            ErrorDialogFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_DESC, description)
                }
            }.show(fragmentManager, ErrorDialogFragment::javaClass.name)
        }
    }
}