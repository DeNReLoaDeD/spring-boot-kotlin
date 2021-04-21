package com.kotlintoni.product

import com.fasterxml.jackson.databind.ObjectMapper
import com.kotlintoni.product.domain.Product
import com.kotlintoni.product.service.ProductService
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext
import java.util.*

@SpringBootTest
class ProductApplicationTests {

    @Autowired
    private lateinit var webApplicationContext: WebApplicationContext

    private val mockMvc: MockMvc by lazy {
        MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .alwaysDo<DefaultMockMvcBuilder>(MockMvcResultHandlers.print()).build()
    }

    @Autowired
    private lateinit var mapper: ObjectMapper

    @Autowired
    private lateinit var productService: ProductService

    private val path = "/api/v1/product"

    @Test
    fun contextLoads() {
    }

    @Test
    fun findAll() {
        val products: List<Product> = mockMvc.perform(MockMvcRequestBuilders.get(path))
                .andExpect(status().isOk)
                .bodyTo(mapper)

        val productsFromService: List<Product> = productService.findAll()

        assertThat(productsFromService, Matchers.`is`(Matchers.equalTo(products)))
    }

    @Test
    fun findById() {
        val productsFromService: List<Product> = productService.findAll()
        assert(!productsFromService.isEmpty()) { "Should not be empty" }
        val product: Product = productsFromService.first()
        mockMvc.perform(MockMvcRequestBuilders.get("$path/${product.name}"))
                .andExpect(status().isOk)
                .andExpect(jsonPath("$.name", Matchers.`is`(product.name)))
    }

    @Test
    fun findByIdEmpty() {
        mockMvc.perform(MockMvcRequestBuilders.get("$path/${UUID.randomUUID()}"))
                .andExpect(status().isNoContent)
                .andExpect(jsonPath("$").doesNotExist())
    }

    @Test
    fun saveSuccess() {
        val product = Product(name = "Watermelon", price = 35.98)

        val result: Boolean = mockMvc.perform(MockMvcRequestBuilders.post(path)
                .body(data = product, mapper = mapper))
                .andExpect(status().isOk)
                .bodyTo(mapper)

        assert(result)
    }

    @Test
    fun saveFail() {
        val productsFromService: List<Product> = productService.findAll()
        assert(!productsFromService.isEmpty()) { "Should not be empty" }

        val product = productsFromService.first()

        val result: Boolean = mockMvc.perform(MockMvcRequestBuilders.post(path)
                .body(data = product, mapper = mapper))
                .andExpect(status().isConflict)
                .bodyTo(mapper)

        assert(!result) { "Should be false" }
    }

    @Test
    fun updateSuccess(){
        val productsFromService: List<Product> = productService.findAll()
        assert(!productsFromService.isEmpty()) { "Should not be empty" }

        val product = productsFromService.first().copy(price = 44.23)
        val result: Boolean = mockMvc.perform(MockMvcRequestBuilders.put(path)
                .body(data = product, mapper = mapper))
                .andExpect(status().isOk)
                .bodyTo(mapper)

        assert(result)
    }

    @Test
    fun updateFail(){
        val result: Boolean = mockMvc.perform(MockMvcRequestBuilders.put(path)
                .body(data = Product(name="NotFruit", price=56.3), mapper = mapper))
                .andExpect(status().isConflict)
                .bodyTo(mapper)

        assert(!result){ "Should be false" }
    }

    @Test
    fun deleteSuccess(){
        val productsFromService: List<Product> = productService.findAll()
        assert(!productsFromService.isEmpty()) { "Should not be empty" }

        val product = productsFromService.first().copy(price = 44.23)
        val result: Boolean = mockMvc.perform(MockMvcRequestBuilders.delete(path+"/${product.name}")
                .body(data = product, mapper = mapper))
                .andExpect(status().isOk)
                .bodyTo(mapper)

        assert(result)
    }

    @Test
    fun deleteFail(){
        val result: Boolean = mockMvc.perform(MockMvcRequestBuilders.delete(path+ "/${UUID.randomUUID()}")
                .body(data = Product(name="NotFruit", price=56.3), mapper = mapper))
                .andExpect(status().isNoContent)
                .bodyTo(mapper)


        assert(!result){ "Should be false" }
    }

}
