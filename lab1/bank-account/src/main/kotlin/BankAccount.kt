import java.lang.Exception

class BankAccount {
    private var status: Boolean = true

    var balance: Long = 0
        get() {
            synchronized(this) {
                if (status) return field else throw IllegalStateException()
            }
        }
        private set

    fun adjustBalance(amount: Long){
        synchronized (this) {
            balance += amount
        }
    }

    fun close() {
        status = false
    }
}
