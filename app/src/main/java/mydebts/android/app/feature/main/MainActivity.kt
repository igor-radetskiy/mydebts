package mydebts.android.app.feature.main

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem

import mydebts.android.app.data.model.Event
import mydebts.android.app.data.model.Person
import mydebts.android.app.feature.event.EventFragment
import mydebts.android.app.feature.events.EventsFragment
import mydebts.android.app.feature.person.PersonFragment
import mydebts.android.app.feature.persons.PersonsFragment

class MainActivity : AppCompatActivity(), MainRouter {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportFragmentManager.addOnBackStackChangedListener {
            supportActionBar?.setDisplayHomeAsUpEnabled(supportFragmentManager.backStackEntryCount > 0)
        }
        navigateToEvents()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
            else -> return false
        }
    }

    override fun navigateToEvents() {
        replaceFragment(EventsFragment(), false)
    }

    override fun navigateToPersons() {
        replaceFragment(PersonsFragment(), false)
    }

    override fun navigateToPerson(person: Person) {
        replaceFragment(PersonFragment.newInstance(person), true)
    }

    override fun navigateToNewEvent() {
        replaceFragment(EventFragment.newInstance(), true)
    }

    override fun navigateToEvent(event: Event) {
        replaceFragment(EventFragment.newInstance(event), true)
    }

    override fun navigateBack() {
        onBackPressed()
    }

    private fun replaceFragment(fragment: Fragment, addToBackStack: Boolean) {
        val transaction = supportFragmentManager.beginTransaction()
                .replace(android.R.id.content, fragment)

        if (addToBackStack) {
            transaction.addToBackStack(null)
        }

        transaction.commit()
    }
}
