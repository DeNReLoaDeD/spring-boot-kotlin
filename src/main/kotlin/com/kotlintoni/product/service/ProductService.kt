package com.kotlintoni.product.service

import com.kotlintoni.product.domain.Product
import com.kotlintoni.product.utils.update
import org.springframework.stereotype.Service

@Service
class ProductService:BasicCrud<Product, String> {
    private val products:MutableSet<Product> = mutableSetOf(Product("Nombre", null), Product("Apellido", 2.3));

    override fun findAll():List<Product> = products.toList();

    override fun findById(id: String): Product? = this.products.find { it.name == id }

    override fun create(t: Product): Boolean = products.add(t)

    override fun update(t: Product): Boolean = this.products.update(t)

    override fun delete(id: String): Boolean = this.products.remove(this.findById(id))
}
