package myanalogcodegenerator.test

class TestClass {
    private val name = "John"
    private var age: String? = null
    private lateinit var address: String

    fun greet(name: String, age: Int): List<String> {
        return listOf("$name is $age")
    }
}