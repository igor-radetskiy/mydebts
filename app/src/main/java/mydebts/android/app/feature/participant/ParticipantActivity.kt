package mydebts.android.app.feature.participant

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.EditText
import mydebts.android.app.R
import mydebts.android.app.data.model.Participant
import mydebts.android.app.di.SubcomponentBuilderResolver
import mydebts.android.app.extention.addSimpleOnTextChangeListener
import mydebts.android.app.extention.setCurrencyText
import mydebts.android.app.extention.setDoubleText
import javax.inject.Inject

class ParticipantActivity : AppCompatActivity(), ParticipantScreen {

    @Inject lateinit var viewModel: ParticipantViewModel

    private lateinit var nameEditText: AutoCompleteTextView
    private lateinit var priceEditText: EditText

    private lateinit var suggestionsAdapter: ArrayAdapter<String>

    @SuppressLint("PrivateResource")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_participant)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.abc_ic_clear_material)

        nameEditText = findViewById(R.id.name) as AutoCompleteTextView
        suggestionsAdapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line)
        nameEditText.setAdapter(suggestionsAdapter)
        nameEditText.setOnItemClickListener { _, _, position, _ -> viewModel.onSuggestionItemClick(position) }
        nameEditText.addSimpleOnTextChangeListener { text -> viewModel.onNameChanged(text) }

        priceEditText = findViewById(R.id.price) as EditText
        priceEditText.addSimpleOnTextChangeListener { text -> viewModel.onDebtChanged(text) }

        (SubcomponentBuilderResolver.resolve(this) as ParticipantSubcomponent.Builder)
                .participant(intent.getParcelableExtra(EXTRA_PARTICIPANT))
                .activity(this)
                .build().inject(this)

        viewModel.onCreate()
    }

    override fun onDestroy() {
        viewModel.onDestroy()
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
                    viewModel.onDoneClick()
                }
            }
        }
        return false
    }

    override fun showName(name: CharSequence) {
        nameEditText.setText(name)
    }

    override fun setNameEnabled(enabled: Boolean) {
        nameEditText.isEnabled = enabled
    }

    override fun showDebt(debt: Double) {
        priceEditText.setDoubleText(debt)
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