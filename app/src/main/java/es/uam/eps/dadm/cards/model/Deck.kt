package es.uam.eps.dadm.cards.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.io.File
import java.io.FileNotFoundException
import java.io.FileWriter
import java.lang.Exception

 */
@Entity(tableName = "decks_table")
data class Deck(
    @PrimaryKey var deckId: Long,
    var name: String,
    var userId: String
    ) {

    constructor(): this(
        -1,
        "testDeck",
        "user"
    )

}