package es.uam.eps.dadm.cards.viewModel

import android.app.Application
import androidx.lifecycle.*
import es.uam.eps.dadm.cards.ui.fragment.CardsApplication
import es.uam.eps.dadm.cards.ui.activity.SettingsActivity
import es.uam.eps.dadm.cards.model.Card
import timber.log.Timber
import java.time.LocalDateTime
import java.util.concurrent.Executors

class StudyViewModel(application: Application): AndroidViewModel(application) {

    private val executor = Executors.newSingleThreadExecutor()
    private val context = getApplication<Application>().applicationContext
    private var deckId = MutableLiveData<Long>()

    private var repetitions =  MutableLiveData<Int>()

    private var maxRepetitions = 20


    var cards: LiveData<List<Card>> = deckId.switchMap {
        Timber.i("Switchmap")
        if(it != -1L) {
            Timber.i("Specific deck")
            CardsApplication.getCardsOfDeck(context, it)
        } else {
            Timber.i("All decks")
            CardsApplication.getCards(context)
        }
    }


    var dueCard: LiveData<Card?> = cards.map {
        if(repetitions.value!! <= maxRepetitions) {
            try {
                it.filter { card -> card.isDue(LocalDateTime.now()) }.run {
                    if(any()) {
                        Timber.i("Studymodel_if")
                        random()
                    } else
                        null
                }
            } catch (e: Exception) {
                null
            }
        } else {
            null
        }
    }

    val nDueCards: LiveData<Int> = cards.map() {
        it.filter { card -> card.isDue((LocalDateTime.now())) }.size + 1
    }

    init {
        repetitions.value = if(getLastStudySession().isBefore(LocalDateTime.now().minusDays(1))){
            getRepetitionsToday()
        } else {
            0
        }
        setRepetitionsToday()

        maxRepetitions = getMaxNumberOfCardsSetting()
    }

    fun loadDeckId(id: Long) {
        deckId.value = id
    }

    fun update(quality: Int) {
        dueCard.value?.quality = quality
        dueCard.value?.answered = true
        dueCard.value?.update(LocalDateTime.now())
        executor.execute {
            dueCard.value?.let { CardsApplication.updateCard(context, it) }
        }
        repetitions.value = repetitions.value?.plus(1)
    }

    private fun getMaxNumberOfCardsSetting(): Int {
        return SettingsActivity.getMaximumNumberOfCards(context)?.toInt() ?: 20
    }

    fun getBoardSetting(): Boolean {
        return SettingsActivity.getBoardPreference(context)
    }

    private fun getRepetitionsToday(): Int {
        return SettingsActivity.getRepetitions(context)
    }

    fun setRepetitionsToday() {
        SettingsActivity.setRepetitions(context, repetitions.value ?: 0)
    }

    private fun getLastStudySession(): LocalDateTime {
        return LocalDateTime.parse(SettingsActivity.getLastStudySession(context))
    }

    fun setLastStudySession() {
        SettingsActivity.setLastStudySession(context, LocalDateTime.now().toString())
    }
}