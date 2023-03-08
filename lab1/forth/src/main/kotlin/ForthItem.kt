class ForthItem(private val _value: Int, private val _last: Item<Int>?)
    : Item<Int>(_value, _last) {
    override fun last(): Item<Int>? {
        return _last
    }

    override fun getValue(): Int {
        return _value
    }

}