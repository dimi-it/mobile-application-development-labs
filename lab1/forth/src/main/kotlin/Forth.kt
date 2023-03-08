class Forth {
    val stack: Stack<Int, ForthItem> = Stack(::ForthItem)
    val operationMap: MutableMap<String, () -> Unit> = mutableMapOf(
        "+" to ::plus,
        "-" to ::minus,
        "*" to ::multiplication,
        "/" to ::division,
        "dup" to ::dup,
        "drop" to ::drop,
        "swap" to ::swap,
        "over" to ::over
    )
    var isDefiningNewKeyword: Boolean = false
    var newKeyword: String = ""
    var newOperation: () -> Unit = {}

    fun evaluate(vararg lines: String): List<Int> {
        for(line in lines){
            val items = line.split(" ")
            val evaluationProcess: (String) -> Unit =
                if(items[0] == ":") ::evaluateNewCommand else ::evaluateItem
            for(item in items)
                evaluationProcess(item.lowercase())
        }
        return getStackContent()
    }

    fun getStackContent(): List<Int>{
        val result: MutableList<Int> = mutableListOf()
        while (!stack.isEmpty()){
            stack.pop()?.let { result.add(it) }
        }
        return result.reversed()
    }

    fun evaluateItem(value: String){
        when{
            operationMap.containsKey(value) -> operationMap[value]?.let { it() }
            value.isNumeric() -> stack.push(value.toInt())
            else -> throw Exception("undefined operation")
        }
    }

    fun evaluateNewCommand(value: String){
        when(value){
            ":" -> isDefiningNewKeyword = true
            ";" ->{
                operationMap[newKeyword] = newOperation
                newKeyword = ""
                newOperation = { }
            }
            else -> {
                if(isDefiningNewKeyword){
                    if(value.isNumeric()){
                        throw Exception("illegal operation")
                    }
                    newKeyword = value
                    isDefiningNewKeyword = false
                }
                else{
                    newOperation = when{
                        operationMap.containsKey(value) -> compose(newOperation, operationMap[value]!!)
                        value.isNumeric() -> compose(newOperation) { stack.push(value.toInt()) }
                        else -> throw Exception("illegal operation")
                    }
                }
            }
        }
    }

    fun evaluateDoubleValueOperation(operation: (Int, Int) -> Unit,
                                     customException: (Int, Int) -> Unit = { _, _ -> }){
        val y: Int = stack.pop() ?: throw Exception("empty stack")
        val x: Int = stack.pop() ?: throw Exception("only one value on the stack")
        customException(x, y)
        operation(x, y)
    }

    fun evaluateSingleValueOperation(operation: (Int) -> Unit){
        val x: Int = stack.pop() ?: throw Exception("empty stack")
        operation(x)
    }

    fun plus(){
        evaluateDoubleValueOperation({ x, y -> stack.push(x + y) })
    }
    fun minus(){
        evaluateDoubleValueOperation({ x, y -> stack.push(x - y) })
    }
    fun multiplication(){
        evaluateDoubleValueOperation({ x, y -> stack.push(x * y) })
    }
    fun division(){
        evaluateDoubleValueOperation(({ x, y -> stack.push(x / y) }))
            {_, y -> if(y == 0) throw Exception("divide by zero")}
    }
    fun dup(){
        evaluateSingleValueOperation { x ->
            run {
                stack.push(x)
                stack.push(x)
            }
        }
    }
    fun drop(){
        evaluateSingleValueOperation { _ -> }
    }
    fun swap(){
        evaluateDoubleValueOperation({ x, y ->
            run{
                stack.push(y)
                stack.push(x)
            }
        })
    }
    fun over(){
        evaluateDoubleValueOperation({ x, y ->
            run{
                stack.push(x)
                stack.push(y)
                stack.push(x)
            }
        })
    }

    fun compose(f: () -> Unit, g: () -> Unit): () -> Unit = {
        f()
        g()
    }

    fun String.isNumeric(): Boolean{
        return this.toIntOrNull() != null
    }
}



