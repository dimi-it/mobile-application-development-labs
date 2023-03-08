interface IItem<T>{
    fun last(): IItem<T>?
    fun getValue(): T?
}
