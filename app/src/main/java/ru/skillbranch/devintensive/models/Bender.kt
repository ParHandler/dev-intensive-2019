package ru.skillbranch.devintensive.models

class Bender(var status: Status = Status.NORMAL, var question: Question = Question.NAME) {
    fun askQuestions(): String = when (question) {
        Question.NAME -> Question.NAME.question
        Question.PROFESSION -> Question.PROFESSION.question
        Question.MATERIAL -> Question.MATERIAL.question
        Question.BDAY -> Question.BDAY.question
        Question.SERIAL -> Question.SERIAL.question
        Question.IDLE -> Question.IDLE.question
    }

    /*
    При вводе неверного ответа более 3 раз сбросить состояние сущности Bender на значение по умолчанию
     (status = Status.NORMAL, question = Question.NAME) и вернуть
     "Это неправильный ответ. Давай все по новой\n${question.question}" to status.color и изменить
     цвет ImageView (iv_bender) на цвет status.color
     Необходимо сохранять состояние экземпляра класса Bender при пересоздании Activity
     (достаточно сохранить Status, Question)
     */
    fun listenAnswer(answer: String): Pair<String, Triple<Int, Int, Int>> {
        return when(question){
            Question.IDLE -> question.question to status.color
            else -> {
                if (question.answers.contains(answer)) {
                        question = question.nextQuestion()
                        "Отлично - ты справился\n${question.question}" to status.color
                } else {
                    if (status == Status.CRITICAL) {
                        status = Status.NORMAL
                        question = Question.NAME
                        "Это неправильный ответ. Давай все по новой\n${question.question}" to status.color
                    } else {
                        status = status.nextStatus()
                        "Это неправильный ответ\n${question.question}" to status.color
                    }
                }
            }
        }
    }
    enum class Status(val color: Triple<Int, Int, Int>) {
        NORMAL(Triple(255, 255, 255)),
        WARNING(Triple(255, 120, 0)),
        DANGER(Triple(255, 60, 60)),
        CRITICAL(Triple(255, 0, 0));

        fun nextStatus(): Status {
            return if (this.ordinal < values().lastIndex) {
                values()[this.ordinal + 1]
            } else {
                values()[0]
            }

        }
    }

    enum class Question(val question: String, val answers: List<String>) {
        NAME("Как меня зовут?", listOf("бендер", "bender")) {
            override fun nextQuestion(): Question = PROFESSION
            override fun validateAnswer(answer: String): Boolean = true
        },
        PROFESSION("Назови мою профессию?", listOf("сгибальщик", "bender")) {
            override fun nextQuestion(): Question = MATERIAL
            override fun validateAnswer(answer: String): Boolean = true
        },
        MATERIAL("Из чего я сделан?", listOf("металл", "дерево", "metal", "iron", "wood")) {
            override fun nextQuestion(): Question = BDAY
            override fun validateAnswer(answer: String): Boolean = true
        },
        BDAY("Когда меня создали?", listOf("2993")) {
            override fun nextQuestion(): Question = SERIAL
            override fun validateAnswer(answer: String): Boolean = true
        },
        SERIAL("Мой серийный номер?", listOf("2716057")) {
            override fun nextQuestion(): Question = IDLE
            override fun validateAnswer(answer: String): Boolean = true
        },
        IDLE("На этом все, вопросов больше нет", listOf()) {
            override fun nextQuestion(): Question = IDLE
            override fun validateAnswer(answer: String): Boolean = true
        };

        abstract fun nextQuestion(): Question
        abstract fun validateAnswer(answer: String):Boolean
    }
}