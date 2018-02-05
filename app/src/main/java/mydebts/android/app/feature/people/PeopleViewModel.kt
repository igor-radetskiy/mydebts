package mydebts.android.app.feature.people

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import io.reactivex.disposables.CompositeDisposable
import mydebts.android.app.data.PersonsSource
import mydebts.android.app.data.model.Person
import mydebts.android.app.rx.RxUtil
import javax.inject.Inject

class PeopleViewModel constructor(
        private val personsSource: PersonsSource,
        private val rxUtil: RxUtil) : ViewModel()
{
    private val disposables = CompositeDisposable()

    private var _people: MutableLiveData<List<Person>>? = null
    internal val people: LiveData<List<Person>>
        get() {
            if (_people == null) {
                _people = MutableLiveData()
                loadPersons()
            }
            return _people!!
        }

    private val _selectedPerson = MutableLiveData<Person>()
    internal val selectedPerson: LiveData<Person>
        get() = _selectedPerson

    private fun loadPersons() {
        disposables.add(personsSource.getAll()
                .compose(rxUtil.singleSchedulersTransformer())
                .subscribe { persons -> _people?.value = persons})
    }

    internal fun onPersonClick(position: Int) {
        _selectedPerson.value = _people?.value?.get(position)
        // Prevent handling of selected person after Activity re-creation
        _selectedPerson.value = null
    }

    override fun onCleared() {
        disposables.clear()
    }

    class Factory @Inject constructor(
            private val personsSource: PersonsSource,
            private val rxUtil: RxUtil) : ViewModelProvider.Factory
    {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST")
            return PeopleViewModel(personsSource, rxUtil) as T
        }
    }
}
