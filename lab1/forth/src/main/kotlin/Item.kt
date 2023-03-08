abstract class Item<T>(private val _value: T, private val _last: Item<T>?)
    :IItem<T>{
    override fun last(): Item<T>? {
        return _last
    }
}