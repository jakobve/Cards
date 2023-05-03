package es.uam.eps.dadm.cards

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.google.firebase.database.FirebaseDatabase
import timber.log.Timber

class DeckListViewModel(application: Application): AndroidViewModel(application) {
    private val context = getApplication<Application>().applicationContext

    val decksWithCards = CardsApplication.getDecksWithCardsForUser(context, getUserId()!!)

    fun uploadDecks() {
        Timber.i("Upload decks")
        val deckReference = FirebaseDatabase.getInstance().getReference("decks")
        val deckList = mutableListOf<Deck>()
        for(deckWithCards in decksWithCards.value!!) {
            deckList.add(deckWithCards.deck)
        }
        deckReference.setValue(deckList)
    }

    fun uploadCards() {
        Timber.i("Upload cards")
        val cardReference = FirebaseDatabase.getInstance().getReference("cards")
        val cardList = mutableListOf<Card>()
        for(deckWithCards in decksWithCards.value!!) {
            for(card in deckWithCards.cards) {
                cardList.add(card)
            }
            cardReference.setValue(cardList)
        }
    }

    private fun getUserId(): String? {
        return SettingsActivity.getUserID(context)
    }
}