package com.kotlintoni.product.controller

import com.kotlintoni.product.domain.Product
import com.kotlintoni.product.service.ProductService
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/product")
class ProductController(productService: ProductService):BasicController<Product, String>(productService)
