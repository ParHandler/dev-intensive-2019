package ru.skillbranch.devintensive


import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Test
import ru.skillbranch.devintensive.extensions.*

import ru.skillbranch.devintensive.models.*
import ru.skillbranch.devintensive.utils.Utils
import java.util.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }
    @Test
    fun  test_instance(){
        val user = User("1")
        val user2 = User("2", "Jonh","Cena" )
        val user3 = User("3", "Jonh","Silverhand", null, lastVisit =  Date(), isOnline = true)
        user.printMe()
        user2.printMe()
        user3.printMe()
        println("$user $user2 $user3")
    }
    @Test
    fun test_factory(){
//        val user = User.makeUser("John Cena")
        val user = User.makeUser("John Wick")
//        val user3 = User.makeUser("John Silverhand")
        val user2 = user.copy(id = "2", lastName = "Cena", lastVisit = Date())
        print("$user \n$user2")
    }

    @Test
    fun test_decomposition (){
        val user = User.makeUser("John Wick")
        fun getUserInfo() = user
        val (id, firstName, lastName) = getUserInfo()

        println("$id, $firstName, $lastName")
        println("${user.component1()}, ${user.component2()}, ${user.component3()}")

    }

    @Test
    fun test_copy(){
        val user = User.makeUser("John Wick")
        val user2 = user.copy(id="0", lastVisit = Date().add(-2,TimeUnits.SECOND))

        if(user.equals(user2)) {
            println("equals \n${user.hashCode()} $user \n${user2.hashCode()} $user2")
        }else{
            println("not equals \n${user.hashCode()} $user \n${user2.hashCode()} $user2")
        }

        if(user===user2) {
            println("equals address \n${System.identityHashCode(user)}\n" +
                    "${System.identityHashCode(user2)}")
        }else{
            println("not equals address \n${System.identityHashCode(user)}\n" +
                    "${System.identityHashCode(user2)}")
        }

        println("""
            ${user.lastVisit?.format()}
            ${user2.lastVisit?.format()}
        """.trimIndent())
    }
    @Test
    fun test_dataq_maping(){
        val user = User.makeUser("Куниловский Евгений")
        val newUser = user.copy(lastVisit = Date().add(-4609*24*360, TimeUnits.SECOND) )
        println(user)
        val userView = newUser.toUserView()

        userView.printMe()

    }
    @Test
    fun test_abstract_factory(){
        val user = User.makeUser("Куниловский Евгений")
        val txtMessage = BaseMessage.makeMessage(user, Chat("0"), payload = "any text message", type = "text")
        val imgMessage = BaseMessage.makeMessage(user, Chat("0"), payload = "any image url", type = "image")

        when(imgMessage){
            is BaseMessage -> println("this is base message")
            is TextMessage -> println("this is text message")
            is ImageMessage -> println("this is image message")
        }
        println(txtMessage.formatMessage())
        println(imgMessage.formatMessage())

        //complete
    }

    @Test
    fun test_transliteration(){
        assertEquals( "Zh Zh", Utils.transliteration("Ж Ж") )
        assertEquals( "ZhZh", Utils.transliteration("ЖЖ") )
        assertEquals( "AbrAKadabra", Utils.transliteration("AbrAKadabra") )
        assertEquals( "StraNNIi NikVash'e", Utils.transliteration("СтраННЫй НикВаще") )
    }
    @Test
    fun test_Utils_toInitials() {
        var i = 0
        val criteriaMap = mapOf(
            Pair(null, null) to null,
            Pair(null, "") to null,
            Pair(null, " ") to null,
            Pair("", null) to null,
            Pair(" ", null) to null,
            Pair("", "") to null,
            Pair("", " ") to null,
            Pair(" ", "") to null,
            Pair("john", null) to "J",
            Pair(null, "doe") to "D",
            Pair("john", "doe") to "JD",
            Pair("Vasya", "Pupkin") to "VP"
        )
        criteriaMap.forEach { criteria ->
            i++
            println("Case $i: $criteria")
            val actualInitials = Utils.toInitials(criteria.key.first, criteria.key.second)
            assertEquals(criteria.value, actualInitials)
            println("Done")
        }
    }

    @Test
    fun humanizeDiffTest() {
        var messageDate = Date()
        var currDate = Date()
        resetDates(messageDate, currDate)

        assertEquals("2 часа назад", messageDate.add(-2, TimeUnits.HOUR).humanizeDiff(currDate))

        resetDates(messageDate, currDate)
        assertEquals("52 дня назад", messageDate.add(-52, TimeUnits.DAY).humanizeDiff(currDate))

        resetDates(messageDate, currDate)
        assertEquals("через 2 минуты", messageDate.add(2, TimeUnits.MINUTE).humanizeDiff(currDate))

        resetDates(messageDate, currDate)
        assertEquals("через 7 дней", messageDate.add(7, TimeUnits.DAY).humanizeDiff(currDate))

        resetDates(messageDate, currDate)
        assertEquals("более года назад", messageDate.add(-361, TimeUnits.DAY).humanizeDiff(currDate))

        resetDates(messageDate, currDate)
        assertEquals("более чем через год", messageDate.add(361, TimeUnits.DAY).humanizeDiff(currDate))

        resetDates(messageDate, currDate)

        assertEquals("только что", messageDate.add(-1, TimeUnits.SECOND).humanizeDiff(currDate))

        resetDates(messageDate, currDate)
        assertEquals("несколько секунд назад", messageDate.add(-45, TimeUnits.SECOND).humanizeDiff(currDate))

        resetDates(messageDate, currDate)
        assertEquals("минуту назад", messageDate.add(-46, TimeUnits.SECOND).humanizeDiff(currDate))

        resetDates(messageDate, currDate)
        assertEquals("10 минут назад", messageDate.add(-600, TimeUnits.SECOND).humanizeDiff(currDate))

        resetDates(messageDate, currDate)
        assertEquals("1 минуту назад", messageDate.add(-76, TimeUnits.SECOND).humanizeDiff(currDate))

        resetDates(messageDate, currDate)
        assertEquals("минуту назад", messageDate.add(-1, TimeUnits.MINUTE).humanizeDiff(currDate))

        resetDates(messageDate, currDate)
        assertEquals("2 минуты назад", messageDate.add(-2, TimeUnits.MINUTE).humanizeDiff(currDate))

        resetDates(messageDate, currDate)
        assertEquals("3 минуты назад", messageDate.add(-3, TimeUnits.MINUTE).humanizeDiff(currDate))

        resetDates(messageDate, currDate)
        assertEquals("45 минут назад", messageDate.add(-45, TimeUnits.MINUTE).humanizeDiff(currDate))

        resetDates(messageDate, currDate)
        assertEquals("час назад", messageDate.add(-1, TimeUnits.HOUR).humanizeDiff(currDate))

        resetDates(messageDate, currDate)
        assertEquals("1 час назад", messageDate.add(-76, TimeUnits.MINUTE).humanizeDiff(currDate))

        resetDates(messageDate, currDate)
        assertEquals("2 часа назад", messageDate.add(-120, TimeUnits.MINUTE).humanizeDiff(currDate))

        resetDates(messageDate, currDate)
        assertEquals("3 часа назад", messageDate.add(-3, TimeUnits.HOUR).humanizeDiff(currDate))

        resetDates(messageDate, currDate)
        assertEquals("4 часа назад", messageDate.add(-4, TimeUnits.HOUR).humanizeDiff(currDate))

        resetDates(messageDate, currDate)
        assertEquals("5 часов назад", messageDate.add(-5, TimeUnits.HOUR).humanizeDiff(currDate))

        resetDates(messageDate, currDate)

        assertEquals("день назад", messageDate.add(-26, TimeUnits.HOUR).humanizeDiff(currDate))

        resetDates(messageDate, currDate)
        assertEquals("1 день назад", messageDate.add(-27, TimeUnits.HOUR).humanizeDiff(currDate))

        resetDates(messageDate, currDate)
        assertEquals("4 дня назад", messageDate.add(-4, TimeUnits.DAY).humanizeDiff(currDate))

        resetDates(messageDate, currDate)
        assertEquals("5 дней назад", messageDate.add(-5, TimeUnits.DAY).humanizeDiff(currDate))

        resetDates(messageDate, currDate)
        assertEquals("360 дней назад", messageDate.add(-360, TimeUnits.DAY).humanizeDiff(currDate))

        resetDates(messageDate, currDate)
        assertEquals("более года назад", messageDate.add(-361, TimeUnits.DAY).humanizeDiff(currDate))
    }

    private fun resetDates(messageDate: Date, currDate: Date) {
        messageDate.time = currDate.time
    }
}
