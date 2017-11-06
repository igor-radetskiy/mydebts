package mydebts.android.app.feature.persons

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import io.reactivex.disposables.CompositeDisposable
import mydebts.android.app.data.PersonsSource
import mydebts.android.app.data.model.Person
import mydebts.android.app.rx.RxUtil
import javax.inject.Inject

class PersonsViewModel constructor(
        private val personsSource: PersonsSource,
        private val rxUtil: RxUtil) : ViewModel()
{
    private val disposables = CompositeDisposable()

    private var _persons: MutableLiveData<List<Person>>? = null
    internal val persons: LiveData<List<Person>>
        get() {
            if (_persons == null) {
                _persons = MutableLiveData()
                loadPersons()
            }
            return _persons!!
        }

    private val _selectedPerson = MutableLiveData<Person>()
    internal val selectedPerson: LiveData<Person>
        get() = _selectedPerson

    private fun loadPersons() {
        disposables.add(personsSource.getAll()
                .compose(rxUtil.singleSchedulersTransformer())
                .subscribe { persons -> _persons?.value = persons})
    }

    internal fun onPersonClick(position: Int) {
        _selectedPerson.value = _persons?.value?.get(position)
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
            return PersonsViewModel(personsSource, rxUtil) as T
        }
    }
}
