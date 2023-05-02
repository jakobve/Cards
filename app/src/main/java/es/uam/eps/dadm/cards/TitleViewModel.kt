package es.uam.eps.dadm.cards

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.concurrent.Executors

class TitleViewModel(application: Application): AndroidViewModel(application) {

    private val context = getApplication<Application>().applicationContext
    val executor = Executors.newSingleThreadExecutor()

    val ndueCards: LiveData<Int> = CardsApplication.getNDueCards(context)

    fun getUserLoggedIn(): Boolean {
        return SettingsActivity.getLoggedIn(context)
    }

    fun getUserId(): String? {
        return SettingsActivity.getUserID(context)
    }

    fun saveDecksLocally(deckList: List<Deck>) {
        Timber.i("Download decks")
        for(deck in deckList) {
            CardsApplication.addDeck(context, deck)
            Timber.i(deck.name)
        }
    }

    fun saveCardsLocally(cardList: List<Card>) {
        Timber.i("Download cards")
        for(card in cardList) {
            CardsApplication.addCard(context, card)
            Timber.i(card.question)
        }
    }
}