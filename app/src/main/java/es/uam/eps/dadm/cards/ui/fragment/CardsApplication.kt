package es.uam.eps.dadm.cards.ui.fragment

import android.app.Application
import android.content.Context
import androidx.lifecycle.LiveData
import es.uam.eps.dadm.cards.model.Card
import es.uam.eps.dadm.cards.model.Deck
import es.uam.eps.dadm.cards.database.CardDatabase
import es.uam.eps.dadm.cards.model.DeckWithCards
import timber.log.Timber
import java.util.concurrent.Executors

class CardsApplication: Application() {

    @Override
    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
    }

    companion object {
        private val executor = Executors.newSingleThreadExecutor()
        private var cardDatabase: CardDatabase? = null
        val cards: MutableList<Card> = mutableListOf()

        private fun initializeDatabase(context: Context): CardDatabase {
            return CardDatabase.getInstance(context)
        }

        fun addCard(context: Context, card: Card) {
            cardDatabase = initializeDatabase(context)
            executor.execute {
                cardDatabase!!.cardDao.addCard(card)
            }
        }

        fun updateCard(context: Context, card: Card) {
            cardDatabase = initializeDatabase(context)
            executor.execute {
                cardDatabase!!.cardDao.updateCard(card)
            }
        }

        fun removeCard(context: Context, card: Card) {
            cardDatabase = initializeDatabase(context)
            executor.execute {
                cardDatabase!!.cardDao.updateCard(card)
                cardDatabase!!.cardDao.deleteCard(card)
            }
        }

        fun getCard(context: Context, id: String): LiveData<Card> {
            cardDatabase = initializeDatabase(context)
            return cardDatabase!!.cardDao.getCard(id)
        }

        fun getCards(context: Context): LiveData<List<Card>> {
            cardDatabase = initializeDatabase(context)
            return cardDatabase!!.cardDao.getCards()
        }

        fun addDeck(context: Context, deck: Deck) {
            cardDatabase = initializeDatabase(context)
            executor.execute {
                deck.deckId = cardDatabase!!.cardDao.getMaxDeckId() + 1
                cardDatabase!!.cardDao.addDeck(deck)
            }
        }

        fun saveDeckLocally(context: Context, deck: Deck) {
            cardDatabase = initializeDatabase(context)
            executor.execute {
                cardDatabase!!.cardDao.addDeck(deck)
            }
        }

        fun removeDeck(context: Context, deckId: Long) {
            cardDatabase = initializeDatabase(context)
            executor.execute {
                cardDatabase!!.cardDao.deleteCardsForDeck(deckId)
                cardDatabase!!.cardDao.deleteDeck(deckId)
            }
        }
        fun getDecksWithCardsForUser(context: Context, userId: String): LiveData<List<DeckWithCards>> {
            cardDatabase = initializeDatabase(context)
            return cardDatabase!!.cardDao.getDecksWithCardsForUser(userId)
        }

        fun getDeckWithCards(context: Context, deckId: Long): LiveData<DeckWithCards> {
            cardDatabase = initializeDatabase(context)
            return cardDatabase!!.cardDao.getDeckWithCards(deckId)
        }

        fun updateDeck(context: Context, deck: Deck) {
            cardDatabase = initializeDatabase(context)
            executor.execute {
                cardDatabase!!.cardDao.updateDeck(deck)
            }
        }

        fun getCardsOfDeck(context: Context, deckId: Long): LiveData<List<Card>> {
            cardDatabase = initializeDatabase(context)
            return cardDatabase!!.cardDao.getCardsOfDeck(deckId)
        }
    }
}