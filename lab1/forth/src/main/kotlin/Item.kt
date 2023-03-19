//Item<T> class implement the interface IItem<T>, needed to have the constructor defined
abstract class Item<T>(private val _value: T, private val _last: Item<T>?)
    :IItem<T>{
}