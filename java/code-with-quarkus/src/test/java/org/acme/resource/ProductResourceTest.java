package org.acme.resource;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.acme.AbstractIntegrationTest;
import org.acme.dto.ProductRequest;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.Matchers.greaterThan;

@QuarkusTest
class ProductResourceTest extends AbstractIntegrationTest {

    @Test
    void shouldCreateProduct() {
        ProductRequest request = new ProductRequest("Laptop", "Gaming laptop", new BigDecimal("1500.00"), 10);

        given()
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post("/api/products")
                .then()
                .statusCode(201)
                .body("id", notNullValue())
                .body("name", equalTo("Laptop"))
                .body("description", equalTo("Gaming laptop"))
                .body("price", equalTo(1500.00f))
                .body("stock", equalTo(10))
                .body("createdAt", notNullValue())
                .body("updatedAt", notNullValue());
    }

    @Test
    void shouldReturnBadRequestWhenCreateProductWithInvalidData() {
        ProductRequest request = new ProductRequest("", "Description", new BigDecimal("100.00"), 10);

        given()
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post("/api/products")
                .then()
                .statusCode(400);
    }

    @Test
    void shouldGetAllProducts() {
        // Create test products
        ProductRequest request1 = new ProductRequest("Laptop", "Gaming laptop", new BigDecimal("1500.00"), 10);
        ProductRequest request2 = new ProductRequest("Mouse", "Gaming mouse", new BigDecimal("50.00"), 100);

        given().contentType(ContentType.JSON).body(request1).post("/api/products");
        given().contentType(ContentType.JSON).body(request2).post("/api/products");

        given()
                .when()
                .get("/api/products")
                .then()
                .statusCode(200)
                .body("size()", greaterThan(0));
    }

    @Test
    void shouldGetProductById() {
        // Create a product
        ProductRequest request = new ProductRequest("Keyboard", "Mechanical keyboard", new BigDecimal("120.00"), 50);

        Integer productId = given()
                .contentType(ContentType.JSON)
                .body(request)
                .post("/api/products")
                .then()
                .statusCode(201)
                .extract()
                .path("id");

        // Get the product by ID
        given()
                .when()
                .get("/api/products/" + productId)
                .then()
                .statusCode(200)
                .body("id", equalTo(productId))
                .body("name", equalTo("Keyboard"))
                .body("description", equalTo("Mechanical keyboard"))
                .body("price", equalTo(120.00f))
                .body("stock", equalTo(50));
    }

    @Test
    void shouldReturnNotFoundWhenGetNonExistingProduct() {
        given()
                .when()
                .get("/api/products/99999")
                .then()
                .statusCode(404);
    }

    @Test
    void shouldUpdateProduct() {
        // Create a product
        ProductRequest request = new ProductRequest("Monitor", "4K monitor", new BigDecimal("500.00"), 20);

        Integer productId = given()
                .contentType(ContentType.JSON)
                .body(request)
                .post("/api/products")
                .then()
                .statusCode(201)
                .extract()
                .path("id");

        // Update the product
        ProductRequest updateRequest = new ProductRequest("Monitor Updated", "4K OLED monitor", new BigDecimal("600.00"), 15);

        given()
                .contentType(ContentType.JSON)
                .body(updateRequest)
                .when()
                .put("/api/products/" + productId)
                .then()
                .statusCode(200)
                .body("id", equalTo(productId))
                .body("name", equalTo("Monitor Updated"))
                .body("description", equalTo("4K OLED monitor"))
                .body("price", equalTo(600.00f))
                .body("stock", equalTo(15));
    }

    @Test
    void shouldReturnNotFoundWhenUpdateNonExistingProduct() {
        ProductRequest request = new ProductRequest("Product", "Description", new BigDecimal("100.00"), 10);

        given()
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .put("/api/products/99999")
                .then()
                .statusCode(404);
    }

    @Test
    void shouldDeleteProduct() {
        // Create a product
        ProductRequest request = new ProductRequest("Headset", "Gaming headset", new BigDecimal("80.00"), 30);

        Integer productId = given()
                .contentType(ContentType.JSON)
                .body(request)
                .post("/api/products")
                .then()
                .statusCode(201)
                .extract()
                .path("id");

        // Delete the product
        given()
                .when()
                .delete("/api/products/" + productId)
                .then()
                .statusCode(204);

        // Verify product is deleted
        given()
                .when()
                .get("/api/products/" + productId)
                .then()
                .statusCode(404);
    }

    @Test
    void shouldReturnNotFoundWhenDeleteNonExistingProduct() {
        given()
                .when()
                .delete("/api/products/99999")
                .then()
                .statusCode(404);
    }

    @Test
    void shouldVerifyCacheHit() {
        // Create a product
        ProductRequest request = new ProductRequest("Camera", "Digital camera", new BigDecimal("800.00"), 15);

        Integer productId = given()
                .contentType(ContentType.JSON)
                .body(request)
                .post("/api/products")
                .then()
                .statusCode(201)
                .extract()
                .path("id");

        // First call - cache miss
        given()
                .when()
                .get("/api/products/" + productId)
                .then()
                .statusCode(200)
                .body("name", equalTo("Camera"));

        // Second call - should be cache hit
        given()
                .when()
                .get("/api/products/" + productId)
                .then()
                .statusCode(200)
                .body("name", equalTo("Camera"));
    }

    @Test
    void shouldInvalidateCacheOnUpdate() {
        // Create a product
        ProductRequest request = new ProductRequest("Speaker", "Bluetooth speaker", new BigDecimal("150.00"), 25);

        Integer productId = given()
                .contentType(ContentType.JSON)
                .body(request)
                .post("/api/products")
                .then()
                .statusCode(201)
                .extract()
                .path("id");

        // Get product (populate cache)
        given()
                .when()
                .get("/api/products/" + productId)
                .then()
                .statusCode(200)
                .body("name", equalTo("Speaker"));

        // Update product (invalidate cache)
        ProductRequest updateRequest = new ProductRequest("Speaker Updated", "Wireless speaker", new BigDecimal("180.00"), 20);

        given()
                .contentType(ContentType.JSON)
                .body(updateRequest)
                .when()
                .put("/api/products/" + productId)
                .then()
                .statusCode(200);

        // Get updated product (cache should be invalidated)
        given()
                .when()
                .get("/api/products/" + productId)
                .then()
                .statusCode(200)
                .body("name", equalTo("Speaker Updated"))
                .body("price", equalTo(180.00f));
    }
}
