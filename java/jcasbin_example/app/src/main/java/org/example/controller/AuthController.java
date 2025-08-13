package org.example.controller;

import org.example.dto.AuthRequest;
import org.example.dto.AuthResponse;
import org.example.service.AuthorizationService;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/api/auth")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AuthController {
    private final AuthorizationService authService;
    
    public AuthController() {
        this.authService = AuthorizationService.getInstance();
    }
    
    @POST
    @Path("/check")
    public Response checkAuthorization(AuthRequest request) {
        try {
            if (request.getSubject() == null || request.getObject() == null || request.getAction() == null) {
                return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new AuthResponse(false, "Missing required fields: subject, object, action"))
                    .build();
            }
            
            boolean allowed = authService.checkPermission(
                request.getSubject(), 
                request.getObject(), 
                request.getAction()
            );
            
            String message = allowed ? "Access granted" : "Access denied";
            return Response.ok(new AuthResponse(allowed, message)).build();
            
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(new AuthResponse(false, "Internal server error: " + e.getMessage()))
                .build();
        }
    }
}