package es.uam.eps.dadm.cards

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import es.uam.eps.dadm.cards.Model.Card
import timber.log.Timber
class TitleViewModel(application: Application): AndroidViewModel(application) {

    private val context = getApplication<Application>().applicationContext

    fun getUserLoggedIn(): Boolean {
        return SettingsActivity.getLoggedIn(context)
    }

    fun getUserId(): String? {
        return SettingsActivity.getUserID(context)
    }

    private fun saveDecksLocally(deckList: List<Deck>) {
        Timber.i("Save decks")
        for(deck in deckList) {
            CardsApplication.saveDeckLocally(context, deck)
            Timber.i(deck.name)
        }
    }

    private fun saveCardsLocally(cardList: List<Card>) {
        Timber.i("Save cards")
        for(card in cardList) {
            CardsApplication.addCard(context, card)
            Timber.i(card.question)
        }
    }

    fun downloadDecks() {
        Timber.i("Download decks")
        val reference = FirebaseDatabase.getInstance().getReference("decks")

        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val listOfDecks = mutableListOf<Deck>()
                for (deckSnapshot in snapshot.children) {
                    deckSnapshot.getValue(Deck::class.java)?.let {
                        listOfDecks.add(it)
                    }
                }
                saveDecksLocally(listOfDecks)
            }
            override fun onCancelled(error: DatabaseError) {}
        })
    }

    fun downloadCards() {
        Timber.i("Download cards")
        val reference = FirebaseDatabase.getInstance().getReference("cards")

        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val listOfCards = mutableListOf<Card>()
                for (deckSnapshot in snapshot.children) {
                    deckSnapshot.getValue(Card::class.java)?.let {
                        listOfCards.add(it)
                    }
                }
                saveCardsLocally(listOfCards)
            }
            override fun onCancelled(error: DatabaseError) {}
        })
    }

    /*

    fun deleteAllCardsLocally() {
        CardsApplication.deleteAllCardsLocally(context)
    }

    fun deleteAllDecksLocally() {
        CardsApplication.deleteAllDecksLocally(context)
    }

     */
}