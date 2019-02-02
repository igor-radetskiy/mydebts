package mydebts.android.app.feature.people

import androidx.lifecycle.Observer
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import android.view.*
import android.widget.TextView
import dagger.android.AndroidInjection
import mydebts.android.app.R
import mydebts.android.app.data.model.Person
import mydebts.android.app.feature.person.PersonActivity
import javax.inject.Inject

class PeopleActivity : AppCompatActivity() {

    @Inject lateinit var viewModel: PeopleViewModel

    private val adapter: PeopleAdapter = PeopleAdapter()

    private lateinit var peopleRecyclerView: RecyclerView
    private lateinit var emptyTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initUi()
        AndroidInjection.inject(this)
        initViewModel()
    }

    private fun initUi() {
        setContentView(R.layout.activity_people)

        supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(true)
            it.setHomeButtonEnabled(true)
        }

        peopleRecyclerView = findViewById(R.id.list_people)
        peopleRecyclerView.adapter = adapter

        emptyTextView = findViewById(R.id.text_empty)
    }

    private fun initViewModel() {
        viewModel.people.observe(this, Observer<List<Person>> { it ->
            when {
                it != null && it.isNotEmpty() -> {
                    peopleRecyclerView.visibility = View.VISIBLE
                    emptyTextView.visibility = View.GONE
                    adapter.people = it
                }
                else -> {
                    peopleRecyclerView.visibility = View.GONE
                    emptyTextView.visibility = View.VISIBLE
                }
            }
        })
        viewModel.selectedPerson.observe(this,
                Observer<Person> {
                    it?.let { startActivity(PersonActivity.newIntent(this, it)) }
                })
        adapter.setOnPersonClick { position -> viewModel.onPersonClick(position) }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean =
            when(item?.itemId) {
                android.R.id.home -> {
                    onBackPressed()
                    true
                }
                else -> false
            }

    companion object {

        fun newIntent(context: Context): Intent = Intent(context, PeopleActivity::class.java)
    }
}
