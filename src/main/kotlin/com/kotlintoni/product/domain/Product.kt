package com.kotlintoni.product.domain

data class Product(val name: String, val price: Double?) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as Product
        if (name != other.name) return false
        return true
    }

    override fun hashCode(): Int = name.hashCode()

}
