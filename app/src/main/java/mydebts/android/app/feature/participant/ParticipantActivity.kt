package mydebts.android.app.feature.participant

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.TextInputLayout
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.inputmethod.EditorInfo
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.EditText
import mydebts.android.app.R
import mydebts.android.app.data.model.Participant
import mydebts.android.app.di.SubcomponentBuilderResolver
import mydebts.android.app.extention.addSimpleOnTextChangeListener
import mydebts.android.app.extention.setDoubleText
import javax.inject.Inject

class ParticipantActivity : AppCompatActivity(), ParticipantScreen {

    @Inject lateinit var presenter: ParticipantPresenter

    private lateinit var nameTextInputLayout: TextInputLayout
    private lateinit var nameEditText: AutoCompleteTextView
    private lateinit var amountTextInputLayout: TextInputLayout
    private lateinit var amountEditText: EditText

    private lateinit var suggestionsAdapter: ArrayAdapter<String>

    @SuppressLint("PrivateResource")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_participant)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.abc_ic_clear_material)

        nameTextInputLayout = findViewById(R.id.text_input_layout_name) as TextInputLayout
        nameEditText = findViewById(R.id.name) as AutoCompleteTextView
        suggestionsAdapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line)
        nameEditText.setAdapter(suggestionsAdapter)
        nameEditText.setOnItemClickListener { _, _, position, _ -> presenter.onSuggestionItemClick(position) }
        nameEditText.addSimpleOnTextChangeListener { text -> presenter.onNameChanged(text) }

        amountTextInputLayout = findViewById(R.id.text_input_layout_amount) as TextInputLayout
        amountEditText = findViewById(R.id.amount) as EditText
        amountEditText.addSimpleOnTextChangeListener { text -> presenter.onDebtChanged(text) }
        amountEditText.setOnEditorActionListener { _, actionId, _ ->
            actionId.takeIf { it == EditorInfo.IME_ACTION_DONE }?.let { presenter.onDoneClick(); true } ?: false
        }

        (SubcomponentBuilderResolver.resolve(this) as ParticipantSubcomponent.Builder)
                .participant(intent.getParcelableExtra(EXTRA_PARTICIPANT))
                .activity(this)
                .build().inject(this)

        presenter.onCreate()
    }

    override fun onDestroy() {
        presenter.onDestroy()
        super.onDestroy()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_participant, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        item?.let {
            when (item.itemId) {
                android.R.id.home -> {
                    onBackPressed()
                }
                R.id.action_save -> {
                    presenter.onDoneClick()
                }
            }
        }
        return false
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
        setResult(Activity.RESULT_OK, Intent().putExtra(EXTRA_PARTICIPANT, result))
    }

    companion object {
        val EXTRA_PARTICIPANT = "EXTRA_PARTICIPANT"

        fun newIntent(context: Context) : Intent =
                Intent(context, ParticipantActivity::class.java)

        fun newIntent(context: Context, participant: Participant) : Intent =
                Intent(context, ParticipantActivity::class.java)
                    .putExtra(EXTRA_PARTICIPANT, participant)
    }
}