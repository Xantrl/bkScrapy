package nn.pager.events

import android.util.Log
import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

class EventVM (
    private val repository: EventRepository
) : ViewModel() {
    var code = 200

    private val _allEvents = MutableLiveData<List<EventEntity>>()
    val allEvents: LiveData<List<EventEntity>>
        get() = _allEvents

    fun insert(event: EventEntity) = viewModelScope.launch {
        repository.insert(event)
    }

    fun getSaved() = repository.getSaved().asLiveData()

    fun deleteAll() = viewModelScope.launch {
        repository.deleteAll()
    }

    fun crawl(user: String, pass: String) {
        viewModelScope.launch(IO) {
            _allEvents.postValue(repository.crawl(user, pass))
        }
    }
    fun recrawl(user: String, pass: String) {
        deleteAll()
        viewModelScope.launch(IO) {
            _allEvents.postValue(repository.crawl(user, pass))
        }
    }


    class EventVMFactory(private val repository: EventRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(EventVM::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return EventVM(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")

        }
    }
}