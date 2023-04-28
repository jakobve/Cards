package es.uam.eps.dadm.cards.database

import androidx.lifecycle.LiveData
import androidx.room.*
import es.uam.eps.dadm.cards.Card
import es.uam.eps.dadm.cards.Deck
import es.uam.eps.dadm.cards.DeckWithCards

@Dao
interface CardDao {

    @Query("SELECT * FROM cards_table")
    fun getCards(): LiveData<List<Card>>

    @Query("SELECT * FROM cards_table WHERE id = :id")
    fun getCard(id: String): LiveData<Card>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addCard(card: Card)

    @Delete
    fun deleteCard(card: Card)

    @Update
    fun updateCard(card: Card)

    @Query("DELETE FROM cards_table")
    fun nukeTable()

    @Query("SELECT * FROM decks_table")
    fun getDecks(): LiveData<List<Deck>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addDeck(deck: Deck)

    @Update
    fun updateDeck(deck: Deck)

    @Transaction
    @Query("SELECT * FROM decks_table")
    fun getDecksWithCards(): LiveData<List<DeckWithCards>>

    @Transaction
    @Query("SELECT * FROM decks_table WHERE deckId = :deckId")
    fun getDeckWithCards(deckId: Long): LiveData<DeckWithCards>

    @Transaction
    @Query("DELETE FROM cards_table WHERE deckId = :deckId")
    fun deleteCardsForDeck(deckId: Long)

    @Query("DELETE FROM decks_table WHERE deckId = :deckId")
    fun deleteDeck(deckId: Long)

    @Query("SELECT * FROM cards_table WHERE deckId = :deckId ORDER BY repetitions DESC")
    fun getCardsOfDeck(deckId: Long): LiveData<List<Card>>

    @Query("SELECT MAX(deckId) FROM decks_table")
    fun getMaxDeckId(): Long

}