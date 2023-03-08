class Stack<P, T: Item<P>>(private val itemFactory: (P, Item<P>?) -> T){
    private var top: T? = null
    private var count: Int = 0

    fun isEmpty(): Boolean{
        return count == 0
    }

    fun size(): Int{
        return count
    }

    fun peek(): P?{
        return top?.getValue()
    }

    fun push(value: P){
        val item: T = itemFactory(value, top)
        top = item
        count++
    }

    fun pop(): P?{
        val result: P? = peek()
        if(isEmpty())
            return result
        top = top?.last() as T?
        count--
        return result
    }
}
