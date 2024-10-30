data class Customer(val name: String, val bggUserName: String?, val email: String, val address: AddressInfo) {
    fun getBggCollection() {
        throw NotImplementedError("not yet implemented")
    }
}