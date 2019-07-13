package ru.skillbranch.devintensive.models

class Bender(var staus:Status = Status.NORMAL, var question: Question = Question.NAME){
    enum class Status {
        NORMAL,
        WARNING,
        DANGER,
        CRITICAL
    }
    enum class Question{
        NAME,
        PROFESSION,
        MATERIAL,
        BDAY,
        SERIAL,
        IDLE
    }
}