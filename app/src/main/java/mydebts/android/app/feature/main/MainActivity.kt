package mydebts.android.app.feature.main

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector

import mydebts.android.app.data.model.Event
import mydebts.android.app.data.model.Person
import mydebts.android.app.feature.event.EventFragment
import mydebts.android.app.feature.events.EventsFragment
import mydebts.android.app.feature.person.PersonActivity
import mydebts.android.app.feature.persons.PersonsFragment
import javax.inject.Inject

class MainActivity : AppCompatActivity(), MainRouter, HasSupportFragmentInjector {

    @Inject lateinit var fragmentInjector: DispatchingAndroidInjector<Fragment>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        AndroidInjection.inject(this)

        supportFragmentManager.addOnBackStackChangedListener {
            supportActionBar?.setDisplayHomeAsUpEnabled(supportFragmentManager.backStackEntryCount > 0)
        }
        navigateToEvents()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when(item?.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> false
        }
    }

    override fun navigateToEvents() {
        replaceFragment(EventsFragment(), false)
    }

    override fun navigateToPersons() {
        replaceFragment(PersonsFragment(), false)
    }

    override fun navigateToPerson(person: Person) {
        startActivity(PersonActivity.newIntent(this, person))
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

    override fun supportFragmentInjector(): AndroidInjector<Fragment> = fragmentInjector

    private fun replaceFragment(fragment: Fragment, addToBackStack: Boolean) {
        val transaction = supportFragmentManager.beginTransaction()
                .replace(android.R.id.content, fragment)

        if (addToBackStack) {
            transaction.addToBackStack(null)
        }

        transaction.commit()
    }
}
