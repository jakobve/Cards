package es.uam.eps.dadm.cards.viewModel

import android.app.Application
import androidx.lifecycle.*
import es.uam.eps.dadm.cards.ui.fragment.CardsApplication
import es.uam.eps.dadm.cards.model.DeckWithCards
import es.uam.eps.dadm.cards.ui.activity.SettingsActivity
import es.uam.eps.dadm.cards.model.Deck


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

    fun getUserId(): String? {
        return SettingsActivity.getUserID(context)
    }
}