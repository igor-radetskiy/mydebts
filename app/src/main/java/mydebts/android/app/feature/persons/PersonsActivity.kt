package mydebts.android.app.feature.persons

import android.arch.lifecycle.Observer
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.*
import dagger.android.AndroidInjection
import mydebts.android.app.R
import mydebts.android.app.data.model.Person
import mydebts.android.app.feature.person.PersonActivity
import javax.inject.Inject

class PersonsActivity : AppCompatActivity() {

    @Inject lateinit var viewModel: PersonsViewModel

    private val adapter: PersonsAdapter = PersonsAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initUi()
        AndroidInjection.inject(this)
        initViewModel()
    }

    private fun initUi() {
        setContentView(R.layout.activity_persons)

        supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(true)
            it.setHomeButtonEnabled(true)
        }

        val recyclerView: RecyclerView = findViewById(R.id.list_persons)
        recyclerView.layoutManager = LinearLayoutManager(applicationContext)
        recyclerView.adapter = adapter
    }

    private fun initViewModel() {
        viewModel.persons.observe(this, Observer<List<Person>> { it -> adapter.persons = it })
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

        fun newIntent(context: Context): Intent = Intent(context, PersonsActivity::class.java)
    }
}
