package es.uam.eps.dadm.cards.model

import androidx.room.ColumnInfo
import java.lang.Double.max
import java.time.LocalDateTime
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

}