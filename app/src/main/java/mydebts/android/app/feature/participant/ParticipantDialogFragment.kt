package mydebts.android.app.feature.participant

import android.app.Dialog
import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.design.widget.TextInputLayout
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.EditorInfo
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.EditText
import dagger.android.support.AndroidSupportInjection
import mydebts.android.app.R
import mydebts.android.app.data.model.Participant
import mydebts.android.app.extention.addSimpleOnTextChangeListener
import mydebts.android.app.extention.setDoubleText
import javax.inject.Inject

class ParticipantDialogFragment : DialogFragment() {

    @Inject lateinit var viewModel: ParticipantViewModel

    private lateinit var nameTextInputLayout: TextInputLayout
    private lateinit var nameEditText: AutoCompleteTextView
    private lateinit var amountTextInputLayout: TextInputLayout
    private lateinit var amountEditText: EditText

    private lateinit var suggestionsAdapter: ArrayAdapter<String>

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialogView = View.inflate(activity, R.layout.fragment_participant, null)

        bindViews(dialogView)

        AndroidSupportInjection.inject(this)

        bindViewModel()

        val dialog = AlertDialog.Builder(activity)
                .setView(dialogView)
                .setPositiveButton(android.R.string.ok, null)
                .setNegativeButton(android.R.string.cancel, null)
                .create()

        dialog.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)
        return dialog
    }

    override fun onResume() {
        super.onResume()

        (dialog as? AlertDialog)?.getButton(AlertDialog.BUTTON_POSITIVE)
                ?.setOnClickListener { viewModel.onDoneClick() }
    }

    private fun bindViews(dialogView: View) {
        nameTextInputLayout = dialogView.findViewById(R.id.text_input_layout_name)
        nameEditText = dialogView.findViewById(R.id.name)
        suggestionsAdapter = ArrayAdapter(activity, android.R.layout.simple_dropdown_item_1line)
        nameEditText.setAdapter(suggestionsAdapter)
        nameEditText.setOnItemClickListener { _, _, position, _ -> viewModel.onSuggestionItemClick(position) }
        nameEditText.addSimpleOnTextChangeListener {
            it?.let { viewModel.onNameChanged(it) }
        }

        amountTextInputLayout = dialogView.findViewById(R.id.text_input_layout_amount)
        amountEditText = dialogView.findViewById(R.id.amount)
        amountEditText.addSimpleOnTextChangeListener {
            it?.let { viewModel.onDebtChanged(it) }
        }
        amountEditText.setOnEditorActionListener { _, actionId, _ ->
            actionId.takeIf { it == EditorInfo.IME_ACTION_DONE }
                    ?.let { viewModel.onDoneClick(); true } == true
        }
    }

    private fun bindViewModel() {
        viewModel.name.observe(this, Observer { nameEditText.setText(it) })
        viewModel.nameEditEnabled.observe(this, Observer {
            it.let { it == null || !it }
                    .let { setNameEditEnabled(it) }
        })
        viewModel.isNameValid.observe(this, Observer {
            nameTextInputLayout.error = if (it == null || !it)
                getString(R.string.error_no_name) else null
        })
        viewModel.nameSuggestions.observe(this, Observer { suggestionsAdapter.addAll(it) })
        viewModel.debt.observe(this, Observer {
            it?.let { amountEditText.setDoubleText(it) } }
        )
        viewModel.isDebtValid.observe(this, Observer {
            amountTextInputLayout.error = if (it == null || !it)
                getString(R.string.error_no_amount) else null
        })
        viewModel.backNavigation.observe(this, Observer { it?.let { dismiss() } })
    }

    private fun setNameEditEnabled(enabled: Boolean) {
        nameEditText.isEnabled = enabled
        if (!enabled) {
            amountEditText.requestFocus()
        }
    }

    companion object {
        internal const val ARG_PARTICIPANT = "ARG_PARTICIPANT"

        fun newInstance() : ParticipantDialogFragment = ParticipantDialogFragment()

        fun newInstance(participant: Participant) : ParticipantDialogFragment {
            val  fragment = ParticipantDialogFragment()

            fragment.arguments = Bundle()
            fragment.arguments.putParcelable(ARG_PARTICIPANT, participant)

            return fragment
        }
    }
}