package es.uam.eps.dadm.cards

import android.app.Application
import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class StatisticsViewModel(application: Application): AndroidViewModel(application) {

    private val context = getApplication<Application>().applicationContext

    var decksWithCards: LiveData<List<DeckWithCards>> = CardsApplication.getDecksWithCards(context)

    //var cards: List<Card>? = deckWithCards.value.

    /*
    private val deckSelected = MutableLiveData<Long>()

    val deckWithCards: LiveData<List<DeckWithCards>> = deckSelected.switchMap() {
        CardDatabase.getInstance(context).cardDao.getDecksWithCards()
    }

    fun loadDeckId(id: Long) {
        deckSelected.value = id
    }

    private val _nDueCardsWeek = MutableLiveData<Int>()
    val nDueCardsWeek: LiveData<Int>
        get() = _nDueCardsWeek

    private val _nDueCardsMoreWeek = MutableLiveData<Int>()
    val nDueCardsMoreWeek: LiveData<Int>
        get() = _nDueCardsMoreWeek

    private val _nDecks = MutableLiveData<Int>()
    val nDecks: LiveData<Int>
        get() = _nDecks

    private val _nCards = MutableLiveData<Int>()
    val nCards: LiveData<Int>
        get() = _nCards

    private val _nRepetitions = MutableLiveData<Int>()
    val nRepetitions: LiveData<Int>
        get() = _nRepetitions

    private val _nGoodCards = MutableLiveData<Int>()
    val nGoodCards: LiveData<Int>
        get() = _nGoodCards

    private val _nDoubtCards = MutableLiveData<Int>()
    val nDoubtCards: LiveData<Int>
        get() = _nDoubtCards

    private val _nBadCards = MutableLiveData<Int>()
    val nBadCards: LiveData<Int>
        get() = _nBadCards

    private val _nDueCardsToday = MutableLiveData<Int>()

    init {
        Timber.i("StatisticsViewModel Init")
        updateLiveData()
    }

    fun updateLiveData() {
        Timber.i("StatViewmodel update livedata")
        _nCards.value = cards?.count()
        //_nDecks.value = decks.count()
        _nBadCards.value = badCards()?.size
        _nDoubtCards.value = doubtCards()?.size
        _nGoodCards.value = goodCards()?.size
        _nRepetitions.value = repetitions()
        _nDueCardsToday.value = dueToday()?.size
        _nDueCardsWeek.value = dueWeek()?.size
        _nDueCardsMoreWeek.value = dueWeekMore()?.size
    }

    private fun badCards() = cards?.filter { it.quality == 0 }

    private fun doubtCards() = cards?.filter { it.quality == 3 }

    private fun goodCards() = cards?.filter { it.quality == 5 }

    private fun repetitions() = cards?.count { it.answered }

    private fun dueToday() = cards?.filter { it.isDue(LocalDateTime.now())}

    private fun dueWeek() = cards?.filter {
        LocalDateTime.parse(it.nextPracticeDate).isBefore(LocalDateTime.now().plusWeeks(1))
    }

    private fun dueWeekMore() = cards?.filter {
        LocalDateTime.parse(it.nextPracticeDate).isAfter(LocalDateTime.now().plusWeeks(1))
    }

     */

}