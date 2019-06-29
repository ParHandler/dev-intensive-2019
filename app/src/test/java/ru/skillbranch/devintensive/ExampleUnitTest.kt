package ru.skillbranch.devintensive

import ru.skillbranch.devintensive.models.User
import org.junit.Test

import org.junit.Assert.*
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
        val user = User.makeUser("John Cena")
        val user2 = User.makeUser("John Wick")
        val user3 = User.makeUser("John Silverhand")
        print(user3)
    }

//    @Test
//    fun test_copy(){
//        val user = User.makeUser("John Wick")
//        var user2 = user.copy()
//    }

}
