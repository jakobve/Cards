package es.uam.eps.dadm.cards.model

import androidx.room.Embedded
import androidx.room.Relation

data class DeckWithCards (
    @Embedded
    val deck: Deck,
    @Relation (
        parentColumn = "deckId",
        entityColumn = "deckId")
    val cards: List<Card>)