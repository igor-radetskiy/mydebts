package mydebts.android.app.feature.participant

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
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

class ParticipantDialogFragment : DialogFragment(), ParticipantScreen {

    @Inject lateinit var presenter: ParticipantPresenter

    private lateinit var nameTextInputLayout: TextInputLayout
    private lateinit var nameEditText: AutoCompleteTextView
    private lateinit var amountTextInputLayout: TextInputLayout
    private lateinit var amountEditText: EditText

    private lateinit var suggestionsAdapter: ArrayAdapter<String>

    private var callback: Callback? = null

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        val parentFragment = parentFragment

        callback = when {
            context is Callback -> context
            parentFragment is Callback -> parentFragment
            else -> null
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialogView = View.inflate(activity, R.layout.fragment_participant, null)

        nameTextInputLayout = dialogView.findViewById(R.id.text_input_layout_name)
        nameEditText = dialogView.findViewById(R.id.name)
        suggestionsAdapter = ArrayAdapter(activity, android.R.layout.simple_dropdown_item_1line)
        nameEditText.setAdapter(suggestionsAdapter)
        nameEditText.setOnItemClickListener { _, _, position, _ -> presenter.onSuggestionItemClick(position) }
        nameEditText.addSimpleOnTextChangeListener { text -> presenter.onNameChanged(text) }

        amountTextInputLayout = dialogView.findViewById(R.id.text_input_layout_amount)
        amountEditText = dialogView.findViewById(R.id.amount)
        amountEditText.addSimpleOnTextChangeListener { text -> presenter.onDebtChanged(text) }
        amountEditText.setOnEditorActionListener { _, actionId, _ ->
            actionId.takeIf { it == EditorInfo.IME_ACTION_DONE }?.let { presenter.onDoneClick(); true } == true
        }

        AndroidSupportInjection.inject(this)

        presenter.onCreate()

        val dialog = AlertDialog.Builder(activity)
                .setView(dialogView)
                .setPositiveButton(android.R.string.ok, null)
                .setNegativeButton(android.R.string.cancel, null)
                .create()

        dialog.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)
        return dialog
    }

    override fun onDismiss(dialog: DialogInterface?) {
        presenter.onDestroy()
        super.onDismiss(dialog)
    }

    override fun onResume() {
        super.onResume()

        val dialog = dialog

        (dialog as? AlertDialog)?.getButton(AlertDialog.BUTTON_POSITIVE)?.setOnClickListener { presenter.onDoneClick() }
    }

    override fun showName(name: CharSequence) {
        nameEditText.setText(name)
    }

    override fun showNameError(error: CharSequence?) {
        nameTextInputLayout.error = error
    }

    override fun setNameEnabled(enabled: Boolean) {
        nameEditText.isEnabled = enabled
    }

    override fun requestFocusOnDebtInput() {
        amountEditText.requestFocus()
    }

    override fun showDebt(debt: Double) {
        amountEditText.setDoubleText(debt)
    }

    override fun showDebtError(error: CharSequence?) {
        amountTextInputLayout.error = error
    }

    override fun setPersonsSuggestions(persons: List<String>) {
        suggestionsAdapter.addAll(persons)
    }

    override fun setResult(result: Participant) {
        callback?.onParticipantResult(result)
    }

    override fun finish() {
        dismiss()
    }

    interface Callback {

        fun onParticipantResult(participant: Participant)
    }

    companion object {
        internal val ARG_PARTICIPANT = "ARG_PARTICIPANT"

        fun newInstance() : ParticipantDialogFragment = ParticipantDialogFragment()

        fun newInstance(participant: Participant) : ParticipantDialogFragment {
            val  fragment = ParticipantDialogFragment()

            fragment.arguments = Bundle()
            fragment.arguments.putParcelable(ARG_PARTICIPANT, participant)

            return fragment
        }
    }
}