data class AddressInfo(val streetName: String, val number: String, val postCode: String, val state: String?, val country: String) {
    constructor(streetName: String, number: String, postCode: String, country: String) : this(streetName, number, postCode, null, country)
}