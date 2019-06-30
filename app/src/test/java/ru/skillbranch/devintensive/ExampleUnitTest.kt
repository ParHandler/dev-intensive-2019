package ru.skillbranch.devintensive

import org.junit.Test

import org.junit.Assert.*
import org.w3c.dom.Text
import ru.skillbranch.devintensive.extensions.TimeUnits
import ru.skillbranch.devintensive.extensions.add
import ru.skillbranch.devintensive.extensions.format
import ru.skillbranch.devintensive.extensions.toUserView
import ru.skillbranch.devintensive.models.*
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
        var user2 = user.copy(id="0", lastVisit = Date().add(-2,TimeUnits.SECOND))

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
        val newUser = user.copy(lastVisit = Date().add(-7, TimeUnits.SECOND) )
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
    }

}
