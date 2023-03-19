//ForthItem class implement the Item<T> abstract class
class ForthItem(private val _value: Int, private val _last: Item<Int>?)
    : Item<Int>(_value, _last) {

    //return the value contained in the _last variable defined in the constructor
    override fun last(): Item<Int>? {
        return _last
    }

    //return the value contained in the _value variable defined in the constructor
    override fun getValue(): Int {
        return _value
    }

}