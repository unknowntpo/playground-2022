package org.example.controller;

import org.example.dto.PolicyRequest;
import org.example.service.PolicyManagementService;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Path("/api/policies")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PolicyController {
    private final PolicyManagementService policyService;
    
    public PolicyController() {
        this.policyService = new PolicyManagementService();
    }
    
    @GET
    public Response getAllPolicies() {
        try {
            List<List<String>> policies = policyService.getAllPolicies();
            List<List<String>> roles = policyService.getAllRoles();
            
            Map<String, Object> result = new HashMap<>();
            result.put("policies", policies);
            result.put("roles", roles);
            
            return Response.ok(result).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity("Error retrieving policies: " + e.getMessage())
                .build();
        }
    }
    
    @POST
    public Response addPolicy(PolicyRequest request) {
        try {
            if (request.getSubject() == null || request.getObject() == null || request.getAction() == null) {
                return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Missing required fields: subject, object, action")
                    .build();
            }
            
            boolean added = policyService.addPolicy(
                request.getSubject(),
                request.getObject(),
                request.getAction()
            );
            
            if (added) {
                return Response.status(Response.Status.CREATED)
                    .entity("Policy added successfully")
                    .build();
            } else {
                return Response.status(Response.Status.CONFLICT)
                    .entity("Policy already exists")
                    .build();
            }
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity("Error adding policy: " + e.getMessage())
                .build();
        }
    }
    
    @DELETE
    public Response removePolicy(PolicyRequest request) {
        try {
            if (request.getSubject() == null || request.getObject() == null || request.getAction() == null) {
                return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Missing required fields: subject, object, action")
                    .build();
            }
            
            boolean removed = policyService.removePolicy(
                request.getSubject(),
                request.getObject(),
                request.getAction()
            );
            
            if (removed) {
                return Response.ok("Policy removed successfully").build();
            } else {
                return Response.status(Response.Status.NOT_FOUND)
                    .entity("Policy not found")
                    .build();
            }
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity("Error removing policy: " + e.getMessage())
                .build();
        }
    }
    
    @POST
    @Path("/roles")
    public Response addRole(@QueryParam("user") String user, @QueryParam("role") String role) {
        try {
            if (user == null || role == null) {
                return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Missing required parameters: user, role")
                    .build();
            }
            
            boolean added = policyService.addRole(user, role);
            
            if (added) {
                return Response.status(Response.Status.CREATED)
                    .entity("Role assigned successfully")
                    .build();
            } else {
                return Response.status(Response.Status.CONFLICT)
                    .entity("Role assignment already exists")
                    .build();
            }
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity("Error assigning role: " + e.getMessage())
                .build();
        }
    }
    
    @DELETE
    @Path("/roles")
    public Response removeRole(@QueryParam("user") String user, @QueryParam("role") String role) {
        try {
            if (user == null || role == null) {
                return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Missing required parameters: user, role")
                    .build();
            }
            
            boolean removed = policyService.removeRole(user, role);
            
            if (removed) {
                return Response.ok("Role removed successfully").build();
            } else {
                return Response.status(Response.Status.NOT_FOUND)
                    .entity("Role assignment not found")
                    .build();
            }
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity("Error removing role: " + e.getMessage())
                .build();
        }
    }
    
    @POST
    @Path("/reload")
    public Response reloadPolicies() {
        try {
            policyService.reloadPolicies();
            return Response.ok("Policies reloaded successfully").build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity("Error reloading policies: " + e.getMessage())
                .build();
        }
    }
}