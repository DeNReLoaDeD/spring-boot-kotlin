package com.kotlintoni.product.utils

fun <E> MutableSet<E>.update(element:E): Boolean{
    return this.remove(element) && this.add(element);
}
