package com.kotlintoni.product.controller

import com.kotlintoni.product.domain.Product
import com.kotlintoni.product.service.BasicCrud
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

abstract class BasicController<T,P>(private val basicCrud: BasicCrud<T,P>) {

    @GetMapping
    fun findAll () = basicCrud.findAll()

    @GetMapping("/{id}")
    fun findById(@PathVariable id: P): ResponseEntity<T> {
        val entity = basicCrud.findById(id)
        return ResponseEntity.status(if(entity!=null) HttpStatus.OK else HttpStatus.NO_CONTENT).body(entity)
    }

    @PutMapping
    fun update(@RequestBody t:T): ResponseEntity<Boolean> {
        val entity = basicCrud.update(t)
        return ResponseEntity.status(if(entity) HttpStatus.OK else HttpStatus.CONFLICT).body(entity)
    }

    @PostMapping
    fun create(@RequestBody t:T): ResponseEntity<Boolean> {
        val entity = basicCrud.create(t)
        return ResponseEntity.status(if(entity) HttpStatus.OK else HttpStatus.CONFLICT).body(entity)

    }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id:P): ResponseEntity<Boolean> {
        val entity = basicCrud.delete(id)
        return ResponseEntity.status(if(entity) HttpStatus.OK else HttpStatus.NO_CONTENT).body(entity)
    }
}
