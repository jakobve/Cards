package es.uam.eps.dadm.cards.model

import androidx.room.ColumnInfo
import java.lang.Double.max
import java.time.LocalDateTime
//import java.time.format.DateTimeFormatter
import java.util.UUID
import kotlin.math.roundToLong
import androidx.room.Entity
import androidx.room.PrimaryKey


/**
 * Class represents a flashcard with its functions to show and update the learning algorithm
 * @author Jakob
 */
@Entity(tableName = "cards_table")
open class Card(
    @ColumnInfo(name="cards_question")
    var question: String,
    var answer: String,
    var date: String = LocalDateTime.now().toString(),
    @PrimaryKey
    var id: String = UUID.randomUUID().toString(),
    var deckId: Long = 0
    ) {

    var quality: Int = -1
    var repetitions: Int = 0
    var interval: Long = 1L
    var easiness: Double = 2.5
    var nextPracticeDate = date
    var answered = false
    var expanded = false

    constructor() : this(
        "question",
        "answer",
        LocalDateTime.now().toString(),
        UUID.randomUUID().toString(),
        0
    )

    /*
    /**
     * Shows the flashcard in the output, so that user must enter the right input
     */
    open fun show() {
        // User mus make an input until their input is ENTER
        do {
            print("$question (ENTER to see answer) ")
            val enter = readln()
        } while(enter != "")

        var userQuality: Int

        do {
            print("$answer (Type 0, 3 or 5): ")
            userQuality = readln().toIntOrNull() ?: -1
        } while((userQuality != 0) && (userQuality != 3) && (userQuality != 5))
        quality = userQuality
    }
*/
    /**
     * Updates the easiness, quality, repetitions, date property by leveraging the learning algorithm
     * @param currentDate The current date to update the date the card is shown the next time
     */
    fun update(currentDate: LocalDateTime) {

        easiness = max(1.3, easiness + 0.1 - (5-quality) * (0.08 + (5-quality) * 0.02))

        if(quality == 0) {
            repetitions = 0
        } else {
            repetitions += 1
        }

        if(repetitions in 0..1) {
            interval = 1
        } else if (repetitions == 2) {
            interval = 6
        } else if (repetitions > 2) {
            interval = (interval * easiness).roundToLong()
        }

        nextPracticeDate = currentDate.plusDays(interval).toString()
        answered = false
    }

    fun isDue(date: LocalDateTime): Boolean {
        return (LocalDateTime.parse(nextPracticeDate).isEqual(date)
                || LocalDateTime.parse(nextPracticeDate).isBefore(date))
    }


/*
    /**
     * Prints the details for a given card: Easiness, Repetitions, Interval, Next practicing date
     */
    fun details() {
        val f = DateTimeFormatter.ISO_LOCAL_DATE
        println("eas = ${String.format("%.2f", easiness)} " +
                "rep = $repetitions " +
                "int = $interval " +
                "next = ${f.format(LocalDateTime.parse(nextPracticeDate))}")
    }

    fun isExpanded(): Boolean {
        return this.expanded
    }

    /**
     * Convert the Card with all its properties into a String representation
     * @return String with all type of Card: card and all the properties of the card class
     */
    override fun toString() = "card | $question | $answer | $date | $id | $repetitions | $interval " +
            "| $easiness | $nextPracticeDate | $deckId"



    fun update_from_view(view: View) {
        quality = when(view.id) {
            R.id.easy_quality_button -> 5
            R.id.doubt_quality_button -> 3
            R.id.difficult_quality_button -> 0
            else -> throw java.lang.Exception("Unavailable quality")
        }
        update(LocalDateTime.now())
    }

    fun updateCard(quality: Int) {
        this.quality = quality
        update(LocalDateTime.now())
    }

    /**
     * Gets a String with all the properties of a Card and creates a Card instance from it
     * @return Card instance
     */
    companion object {
        fun fromString(cad: String): Card {
            val properties = cad.split(" | ")
            // Instantiate Card with primary constructor call
            // Index at properties start at 1, because index 0 specifies type of card (card or cloze)
            val card =  Card(
                properties[1],
                properties[2],
                properties[3],
                properties[4],
                properties[5]
            )

            // Set rest of the properties
            card.repetitions = properties[5].toIntOrNull() ?: 0
            card.interval = properties[7].toLongOrNull() ?: 1L
            card.easiness = properties[8].toDoubleOrNull() ?: 2.5
            card.nextPracticeDate = properties[9]

            // Return the Card instance
            return card
        }
    }


 */

}