//Item<T> class implement the interface IItem<T>
abstract class Item<T>(private val _value: T, private val _last: Item<T>?)
    :IItem<T>{

    //return the value contained in the _last variable defined in the constructor
    override fun last(): Item<T>? {
        return _last
    }
}