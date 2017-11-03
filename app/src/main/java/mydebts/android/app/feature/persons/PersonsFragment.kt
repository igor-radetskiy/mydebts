package mydebts.android.app.feature.persons

import android.support.v4.app.Fragment
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.*
import mydebts.android.app.R
import mydebts.android.app.data.PersonsSource
import mydebts.android.app.data.model.Person
import mydebts.android.app.di.SubcomponentBuilderResolver
import mydebts.android.app.feature.main.MainRouter
import mydebts.android.app.rx.RxUtil
import javax.inject.Inject

class PersonsFragment : Fragment() {

    @Inject lateinit var personsSource: PersonsSource
    @Inject lateinit var rxUtil: RxUtil

    private val adapter: PersonsAdapter = PersonsAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
        setHasOptionsMenu(true)

        (SubcomponentBuilderResolver.resolve(this) as PersonsSubcomponent.Builder)
                .build().inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater!!.inflate(R.layout.fragment_persons, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        activity.setTitle(R.string.title_people)

        view?.let {
            val recyclerView: RecyclerView = view.findViewById(R.id.list_persons)
            recyclerView.layoutManager = LinearLayoutManager(activity.applicationContext)
            adapter.setOnPersonClickListener(object: PersonsAdapter.OnPersonClickListener {
                override fun onPersonClick(person: Person) {
                    (activity as MainRouter).navigateToPerson(person)
                }
            })
            recyclerView.adapter = adapter
        }

        personsSource.getAll()
                .compose(rxUtil.singleSchedulersTransformer())
                .subscribe({ persons -> adapter.persons = persons})
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater!!.inflate(R.menu.menu_persons, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when(item!!.itemId) {
            R.id.action_switch_to_events -> {
                (activity as MainRouter).navigateToEvents()
                true
            }
            else -> false
        }
    }
}
