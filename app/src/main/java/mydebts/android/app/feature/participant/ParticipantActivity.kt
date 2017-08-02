package mydebts.android.app.feature.participant

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import io.reactivex.disposables.Disposable
import mydebts.android.app.R
import mydebts.android.app.data.model.Participant
import mydebts.android.app.di.SubcomponentBuilderResolver
import javax.inject.Inject

class ParticipantActivity : AppCompatActivity() {

    @Inject lateinit var viewModel: ParticipantViewModel

    private lateinit var nameEditText: EditText
    private lateinit var priceEditText: EditText

    private lateinit var participantDisposable: Disposable

    @SuppressLint("PrivateResource")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_participant)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.abc_ic_clear_material)

        nameEditText = findViewById(R.id.name) as EditText
        priceEditText = findViewById(R.id.price) as EditText

        (SubcomponentBuilderResolver.resolve(this) as ParticipantSubcomponent.Builder)
                .participant(intent.getParcelableExtra(EXTRA_PARTICIPANT))
                .build().inject(this)

        participantDisposable = viewModel.observeParticipant()
                .subscribe {
                    setResult(Activity.RESULT_OK, Intent()
                            .putExtra(EXTRA_PARTICIPANT, it))
                    finish()
                }
    }

    override fun onDestroy() {
        participantDisposable.dispose()
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
                    viewModel.setName(nameEditText.text)
                    viewModel.setDebt(priceEditText.text)
                    viewModel.onDoneClick()
                }
            }
        }
        return false
    }

    companion object {
        val EXTRA_PARTICIPANT = "EXTRA_PARTICIPANT"

        fun newIntent(context: Context) : Intent {
            return Intent(context, ParticipantActivity::class.java)
                    .putExtra(EXTRA_PARTICIPANT, Participant())
        }

        fun newIntent(context: Context, participant: Participant) : Intent {
            return Intent(context, ParticipantActivity::class.java)
                    .putExtra(EXTRA_PARTICIPANT, participant)
        }
    }
}