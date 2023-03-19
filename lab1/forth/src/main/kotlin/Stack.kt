//Stack LIFO class that accept Item<P> item
//P is the generic for the value of the Item
//T is the generic for Item<P>
class Stack<P, T: Item<P>>(private val itemFactory: (P, Item<P>?) -> T){
    private var top: T? = null
    private var count: Int = 0

    //check if stack is empty
    fun isEmpty(): Boolean{
        return count == 0
    }

    //return the size of the stack
    fun size(): Int{
        return count
    }

    //return the value at the top of the stack
    fun peek(): P?{
        return top?.getValue()
    }

    //push an item at the top of the stack with value
    fun push(value: P){
        val item: T = itemFactory(value, top)   //instantiate the item
        top = item
        count++
    }

    //pop the item at the top of the stack and return is value
    fun pop(): P?{
        val result: P? = peek()
        if(isEmpty())
            return result
        top = top?.last() as T?
        count--
        return result
    }
}
