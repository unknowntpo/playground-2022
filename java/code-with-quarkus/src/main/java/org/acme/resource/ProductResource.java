package org.acme.resource;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.acme.domain.Product;
import org.acme.dto.ProductRequest;
import org.acme.dto.ProductResponse;
import org.acme.service.ProductService;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@Path("/api/products")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ProductResource {

    @Inject
    ProductService productService;

    @POST
    public Response createProduct(ProductRequest request) {
        try {
            Product product = new Product(
                    request.getName(),
                    request.getDescription(),
                    request.getPrice(),
                    request.getStock()
            );
            Product created = productService.createProduct(product);
            ProductResponse response = new ProductResponse(created);
            return Response.created(URI.create("/api/products/" + created.getId()))
                    .entity(response)
                    .build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(e.getMessage())
                    .build();
        }
    }

    @GET
    public Response getAllProducts() {
        List<ProductResponse> products = productService.getAllProducts().stream()
                .map(ProductResponse::new)
                .collect(Collectors.toList());
        return Response.ok(products).build();
    }

    @GET
    @Path("/{id}")
    public Response getProductById(@PathParam("id") Long id) {
        return productService.getProductById(id)
                .map(product -> Response.ok(new ProductResponse(product)).build())
                .orElse(Response.status(Response.Status.NOT_FOUND).build());
    }

    @PUT
    @Path("/{id}")
    public Response updateProduct(@PathParam("id") Long id, ProductRequest request) {
        try {
            Product product = new Product(
                    request.getName(),
                    request.getDescription(),
                    request.getPrice(),
                    request.getStock()
            );
            Product updated = productService.updateProduct(id, product);
            return Response.ok(new ProductResponse(updated)).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(e.getMessage())
                    .build();
        }
    }

    @DELETE
    @Path("/{id}")
    public Response deleteProduct(@PathParam("id") Long id) {
        try {
            productService.deleteProduct(id);
            return Response.noContent().build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(e.getMessage())
                    .build();
        }
    }
}
