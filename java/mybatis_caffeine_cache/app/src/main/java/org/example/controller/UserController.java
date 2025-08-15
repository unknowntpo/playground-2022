package org.example.controller;

import org.example.entity.User;
import org.example.service.UserService;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

@Path("/api/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserController {

    private final UserService userService;

    public UserController() {
        this.userService = new UserService();
    }

    @GET
    @Path("/{id}")
    public Response getUserById(@PathParam("id") Long id) {
        try {
            User user = userService.getUserById(id);
            if (user == null) {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
            return Response.ok(user).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error retrieving user: " + e.getMessage())
                    .build();
        }
    }

    @GET
    @Path("/{id}/with-roles")
    public Response getUserByIdWithRoles(@PathParam("id") Long id) {
        try {
            User user = userService.getUserByIdWithRoles(id);
            if (user == null) {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
            return Response.ok(user).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error retrieving user with roles: " + e.getMessage())
                    .build();
        }
    }

    @GET
    public Response getAllUsers() {
        try {
            List<User> users = userService.getAllUsers();
            return Response.ok(users).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error retrieving users: " + e.getMessage())
                    .build();
        }
    }

    @GET
    @Path("/by-role/{roleId}")
    public Response getUsersByRoleId(@PathParam("roleId") Long roleId) {
        try {
            List<User> users = userService.getUsersByRoleId(roleId);
            return Response.ok(users).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error retrieving users by role: " + e.getMessage())
                    .build();
        }
    }

    @POST
    public Response createUser(User user) {
        try {
            userService.createUser(user);
            return Response.status(Response.Status.CREATED).entity(user).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error creating user: " + e.getMessage())
                    .build();
        }
    }

    @PUT
    @Path("/{id}")
    public Response updateUser(@PathParam("id") Long id, User user) {
        try {
            user.setId(id);
            userService.updateUser(user);
            return Response.ok(user).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error updating user: " + e.getMessage())
                    .build();
        }
    }

    @DELETE
    @Path("/{id}")
    public Response deleteUser(@PathParam("id") Long id) {
        try {
            userService.deleteUser(id);
            return Response.noContent().build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error deleting user: " + e.getMessage())
                    .build();
        }
    }

    @DELETE
    @Path("/{id}/roles")
    public Response removeUserFromAllRoles(@PathParam("id") Long id) {
        try {
            userService.removeUserFromAllRoles(id);
            return Response.noContent().build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error removing user from all roles: " + e.getMessage())
                    .build();
        }
    }
}
