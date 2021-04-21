package com.kotlintoni.product.service

import com.kotlintoni.product.domain.Product

interface BasicCrud<T,P> {
    fun findAll():List<T>
    fun findById(id:P):T?
    fun create(t:T):Boolean
    fun update(t:T):Boolean
    fun delete(id:P):Boolean
}
