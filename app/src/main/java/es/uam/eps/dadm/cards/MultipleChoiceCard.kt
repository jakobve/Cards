package es.uam.eps.dadm.cards

import java.time.LocalDateTime
import java.util.*

class MultipleChoiceCard(
    question: String,
    answer: String,
    date: String = LocalDateTime.now().toString(),
    id: String = UUID.randomUUID().toString(),
    deckId: Long = 0,
    val answerOptions: Map<String, Boolean>
) : Card(question, answer, date, id, deckId) {

    init {
        if (!answerOptions.containsValue(true)) {
            throw IllegalArgumentException("At least one answer option must be marked as correct")
        }
    }

    fun checkAnswer(answer: String): Boolean {
        return this.answerOptions.getValue(answer)
    }
}