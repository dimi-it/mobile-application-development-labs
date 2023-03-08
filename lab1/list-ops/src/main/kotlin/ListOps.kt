fun <T> List<T>.customAppend(list: List<T>): List<T> {
    return list.customFoldLeft(this) { acc, item -> acc + item }
}

fun List<Any>.customConcat(): List<Any> {
    return this.customFoldLeft(emptyList()) {
        acc, item ->
            if(item is List<*>) {
                acc.customAppend((item as List<Any>).customConcat())
            }
            else{
                acc + item
            }
    }
}

fun <T> List<T>.customFilter(predicate: (T) -> Boolean): List<T> {
    return this.customFoldLeft(emptyList()) {
        acc, item ->
            if(predicate(item)){
                acc + item
            }
            else{
                acc
            }
    }
}

val List<Any>.customSize: Int
    get() = this.customFoldLeft(0){acc, _ -> acc + 1}

fun <T, U> List<T>.customMap(transform: (T) -> U): List<U> {
    return this.customFoldLeft(emptyList()){acc, item -> acc + transform(item)}
}

fun <T, U> List<T>.customFoldLeft(initial: U, f: (U, T) -> U): U {
    if(this.isEmpty()){
        return initial
    }
    else{
        return this.drop(1).customFoldLeft(f(initial, this.first()), f)
    }
}

fun <T, U> List<T>.customFoldRight(initial: U, f: (T, U) -> U): U {
    return this.customReverse().customFoldLeft(initial){acc, item -> f(item, acc)}
}

fun <T> List<T>.customReverse(): List<T> {
    return this.customFoldLeft(listOf<T>()){acc, item -> listOf(item) + acc }
}
