package es.uam.eps.dadm.cards

class MultipleChoiceCard(
    question: String,
    var answers: MutableMap<String, Boolean>
) : Card(question, "") {

    fun checkAnswer(answer: String): Boolean {
        return this.answers.getValue(answer)
    }
}