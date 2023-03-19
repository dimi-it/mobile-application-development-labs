//Interface class for the Item
interface IItem<T>{

    //return the value contained in the _last variable defined in the constructor of the implementation
    fun last(): IItem<T>?

    //return the value contained in the _value variable defined in the constructor of the implementation
    fun getValue(): T?
}
