package org.example.controller;

import org.example.entity.Role;
import org.example.service.RoleService;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

@Path("/api/roles")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RoleController {

    private final RoleService roleService;

    public RoleController() {
        this.roleService = new RoleService();
    }

    @GET
    @Path("/{id}")
    public Response getRoleById(@PathParam("id") Long id) {
        try {
            Role role = roleService.getRoleById(id);
            if (role == null) {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
            return Response.ok(role).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error retrieving role: " + e.getMessage())
                    .build();
        }
    }

    @GET
    @Path("/{id}/with-users")
    public Response getRoleByIdWithUsers(@PathParam("id") Long id) {
        try {
            Role role = roleService.getRoleByIdWithUsers(id);
            if (role == null) {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
            return Response.ok(role).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error retrieving role with users: " + e.getMessage())
                    .build();
        }
    }

    @GET
    public Response getAllRoles() {
        try {
            List<Role> roles = roleService.getAllRoles();
            return Response.ok(roles).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error retrieving roles: " + e.getMessage())
                    .build();
        }
    }

    @GET
    @Path("/test")
    public Response test() {
        return Response.ok("Hello from RoleResource!").build();
    }

    @POST
    public Response createRole(Role role) {
        try {
            roleService.createRole(role);
            return Response.status(Response.Status.CREATED).entity(role).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error creating role: " + e.getMessage())
                    .build();
        }
    }

    @PUT
    @Path("/{id}")
    public Response updateRole(@PathParam("id") Long id, Role role) {
        try {
            role.setId(id);
            roleService.updateRole(role);
            return Response.ok(role).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error updating role: " + e.getMessage())
                    .build();
        }
    }

    @DELETE
    @Path("/{id}")
    public Response deleteRole(@PathParam("id") Long id) {
        try {
            roleService.deleteRole(id);
            return Response.noContent().build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error deleting role: " + e.getMessage())
                    .build();
        }
    }

    @DELETE
    @Path("/{roleId}/users/{userId}")
    public Response removeUserFromRole(@PathParam("roleId") Long roleId, @PathParam("userId") Long userId) {
        try {
            roleService.removeUserFromRole(userId, roleId);
            return Response.noContent().build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error removing user from role: " + e.getMessage())
                    .build();
        }
    }

    @POST
    @Path("/{roleId}/users/{userId}")
    public Response addUserToRole(@PathParam("roleId") Long roleId, @PathParam("userId") Long userId) {
        try {
            roleService.addUserToRole(userId, roleId);
            return Response.ok().build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error adding user to role: " + e.getMessage())
                    .build();
        }
    }
}
