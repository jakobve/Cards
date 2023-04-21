package es.uam.eps.dadm.cards

import android.app.Application
import androidx.lifecycle.*


class DeckEditViewModel(application: Application): AndroidViewModel(application) {

    private val context = getApplication<Application>().applicationContext

    private val deckId = MutableLiveData<Long>()

    val deckWithCards: LiveData<DeckWithCards> = deckId.switchMap() {
        CardsApplication.getDeckWithCards(context, it)
    }

    fun loadDeckId(id: Long) {
        deckId.value = id
    }

    fun updateDeck(deck: Deck) {
        CardsApplication.updateDeck(context, deck)
    }

    fun removeDeck(deckId: Long) {
        CardsApplication.removeDeck(context, deckId)
    }

    fun addDeck(deckWithCards: DeckWithCards) {
        CardsApplication.addDeck(context, deckWithCards.deck)
    }
}