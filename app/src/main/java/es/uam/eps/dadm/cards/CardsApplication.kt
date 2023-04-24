package es.uam.eps.dadm.cards

import android.app.Application
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import es.uam.eps.dadm.cards.database.CardDatabase
import timber.log.Timber
import java.time.LocalDateTime
import java.util.concurrent.Executors

class CardsApplication: Application() {


    @Override
    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
    }

    companion object {
        val executor = Executors.newSingleThreadExecutor()
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

        fun getNDueCards(context: Context): LiveData<Int> {
            cardDatabase = initializeDatabase(context)
            val cards = getCards(context)
            Timber.i(cards.value?.size.toString())
            return MutableLiveData(cards.value?.filter { it.isDue(LocalDateTime.now()) }?.size ?: 0)
        }

        fun addDeck(context: Context, deck: Deck) {
            cardDatabase = initializeDatabase(context)
            executor.execute {
                deck.deckId = cardDatabase!!.cardDao.getMaxDeckId() + 1
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

        fun getDecksWithCards(context: Context): LiveData<List<DeckWithCards>> {
            cardDatabase = initializeDatabase(context)
           return cardDatabase!!.cardDao.getDecksWithCards()
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

        /*
        fun generateTestDeck() {
            if(cards.any { it.deckId == "test" } || decks.any { it.id == "test" }) {
                cards.removeAll { it.deckId == "test" }
                decks.removeAll { it.id  == "test" }
            } else {
                decks.add(Deck("Test deck", "test"))
                cards.add(Card("To drink", "beber", deckId = "test"))
                cards.add(Card("Table", "La mesa", deckId = "test"))
                cards.add(Card("The lamp", "La lampara", deckId = "test"))
                cards.add(Card("To eat", "Comer", deckId = "test"))
                cards.add(Card("To read", "leer", deckId = "test"))
            }
        }

         */
    }
}