class Forth {
    val stack: Stack<Int, ForthItem> = Stack(::ForthItem)   //create a new stack, and pass the constructor of the Item
    val operationMap: MutableMap<String, () -> Unit> = mutableMapOf(  //Dictionary of the various function, to have O(1) access by the string
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
                if(items[0] == ":") ::evaluateNewCommand else ::evaluateItem    //based on the first item of the line, assign to evaluationProcess the correct evaluation function
            for(item in items)
                evaluationProcess(item.lowercase())
        }
        return getStackContent()
    }

    //return the stack content in reversed order ("FIFO order")
    fun getStackContent(): List<Int>{
        val result: MutableList<Int> = mutableListOf()
        while (!stack.isEmpty()){
            stack.pop()?.let { result.add(it) }
        }
        return result.reversed()
    }

    //evaluate the string value in a switch case based on the requirement
    fun evaluateItem(value: String){
        when{
            operationMap.containsKey(value) -> operationMap[value]?.let { it() }    //call the correct function inside the dictionary
            value.isNumeric() -> stack.push(value.toInt())
            else -> throw Exception("undefined operation")
        }
    }

    //evaluate the string value in a switch case to create a new command
    fun evaluateNewCommand(value: String){
        when(value){
            ":" -> isDefiningNewKeyword = true      //start of the new command
            ";" ->{                                 //end of the new command
                operationMap[newKeyword] = newOperation     //add the command to the dictionary
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
                    newOperation = when{            //define the new operation
                        operationMap.containsKey(value) -> compose(newOperation, operationMap[value]!!)     //join the current saved operation with the new one
                        value.isNumeric() -> compose(newOperation) { stack.push(value.toInt()) }            //join the current saved operation with the push operation
                        else -> throw Exception("illegal operation")
                    }
                }
            }
        }
    }

    //evaluate operation with two operand by pop the two values from the stack
    fun evaluateDoubleValueOperation(operation: (Int, Int) -> Unit,
                                     customException: (Int, Int) -> Unit = { _, _ -> }){
        val y: Int = stack.pop() ?: throw Exception("empty stack")
        val x: Int = stack.pop() ?: throw Exception("only one value on the stack")
        customException(x, y)   //check the custom exception if passed
        operation(x, y)
    }

    //evaluate operation with one operand by pop the value from the stack
    fun evaluateSingleValueOperation(operation: (Int) -> Unit){
        val x: Int = stack.pop() ?: throw Exception("empty stack")
        operation(x)
    }

    //call the evaluation function, by passing the stack push with the correct operation
    fun plus(){
        evaluateDoubleValueOperation({ x, y -> stack.push(x + y) })
    }

    //call the evaluation function, by passing the stack push with the correct operation
    fun minus(){
        evaluateDoubleValueOperation({ x, y -> stack.push(x - y) })
    }

    //call the evaluation function, by passing the stack push with the correct operation
    fun multiplication(){
        evaluateDoubleValueOperation({ x, y -> stack.push(x * y) })
    }

    //call the evaluation function, by passing the stack push with the correct operation and the custom exception
    fun division(){
        evaluateDoubleValueOperation(({ x, y -> stack.push(x / y) }))
            {_, y -> if(y == 0) throw Exception("divide by zero")}
    }

    //call the evaluation function, by passing a function containing the two stack push with the correct operation
    fun dup(){
        evaluateSingleValueOperation { x ->
            run {
                stack.push(x)
                stack.push(x)
            }
        }
    }

    //call the evaluation function, by passing the correct operation
    fun drop(){
        evaluateSingleValueOperation { _ -> }
    }

    //call the evaluation function, by passing a function containing the two stack push with the correct operation
    fun swap(){
        evaluateDoubleValueOperation({ x, y ->
            run{
                stack.push(y)
                stack.push(x)
            }
        })
    }

    //call the evaluation function, by passing a function containing the two stack push with the correct operation
    fun over(){
        evaluateDoubleValueOperation({ x, y ->
            run{
                stack.push(x)
                stack.push(y)
                stack.push(x)
            }
        })
    }

    //join multiple functions
    fun compose(f: () -> Unit, g: () -> Unit): () -> Unit = {
        f()
        g()
    }

    //extension function for string type
    fun String.isNumeric(): Boolean{
        return this.toIntOrNull() != null
    }
}



