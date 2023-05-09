package es.uam.eps.dadm.cards

import androidx.room.Embedded
import androidx.room.Relation
import es.uam.eps.dadm.cards.Model.Card

data class DeckWithCards (
    @Embedded
    val deck: Deck,
    @Relation (
        parentColumn = "deckId",
        entityColumn = "deckId")
    val cards: List<Card>) {}