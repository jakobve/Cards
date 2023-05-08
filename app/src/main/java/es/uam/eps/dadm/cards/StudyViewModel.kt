package es.uam.eps.dadm.cards

import android.app.Application
import android.content.Context
import androidx.lifecycle.*
import es.uam.eps.dadm.cards.database.CardDatabase
import java.time.LocalDateTime
import java.util.concurrent.Executors

class StudyViewModel(application: Application): AndroidViewModel(application) {
    private val executor = Executors.newSingleThreadExecutor()
    private val context = getApplication<Application>().applicationContext
    private var deckId = MutableLiveData<Long>()

    var cards: LiveData<List<Card>> = deckId.switchMap {
        if(it != -1L) {
            CardsApplication.getCardsOfDeck(context, it)
        } else {
            CardsApplication.getCards(context)
        }
    }

    var dueCard: LiveData<Card?> = cards.map {
        try {
            it.filter { card -> card.isDue(LocalDateTime.now()) }.run {
                if(any()) random() else null
            }
        } catch (e: Exception) {
            null
        }
    }

    val nDueCards: LiveData<Int> = cards.map() {
        it.filter { card -> card.isDue((LocalDateTime.now())) }.size
    }

    private val _nRepetitions = MutableLiveData<Int>()

    init {
        //card = randomCard()
        //_nDueCards.value = dueCards().size
        //_nRepetitions.value = repetitions()
    }

    fun loadDeckId(id: Long) {
        deckId.value = id
    }

    fun update(quality: Int) {
        dueCard.value?.quality = quality
        dueCard.value?.update(LocalDateTime.now())
        //card = randomCard()
        executor.execute {
            dueCard.value?.let { CardsApplication.updateCard(context, it) }
        }
    }

    fun getMaxNumberOfCardsSetting(): Int {
        return SettingsActivity.getMaximumNumberOfCards(context)?.toInt() ?: 20
    }

    fun getBoardSetting(): Boolean {
        return SettingsActivity.getBoardPreference(context)
    }
}