package es.uam.eps.dadm.cards.viewModel

import android.annotation.SuppressLint
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.google.firebase.database.FirebaseDatabase
import es.uam.eps.dadm.cards.ui.fragment.CardsApplication
import es.uam.eps.dadm.cards.ui.activity.SettingsActivity
import es.uam.eps.dadm.cards.model.Card
import es.uam.eps.dadm.cards.model.Deck
import timber.log.Timber

@SuppressLint("StaticFieldLeak")
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