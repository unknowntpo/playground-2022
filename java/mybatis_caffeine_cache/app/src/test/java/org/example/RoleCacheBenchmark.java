package org.example;

import org.example.entity.Role;
import org.example.service.RoleService;
import org.openjdk.jmh.annotations.*;

import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@State(Scope.Benchmark)
@Fork(1)
@Warmup(iterations = 3, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 5, time = 2, timeUnit = TimeUnit.SECONDS)
public class RoleCacheBenchmark {

    private RoleService roleService;

    @Setup(Level.Trial)
    public void setup() {
        roleService = new RoleService();
        roleService.getRoleByIdWithUsers(1L);
    }

    @Benchmark
    public Role benchmarkGetRoleByIdCached() {
        return roleService.getRoleById(1L);
    }

    @Benchmark
    public Role benchmarkGetRoleByIdWithUsersCached() {
        return roleService.getRoleByIdWithUsers(1L);
    }

    @Benchmark
    public Role benchmarkGetRoleByIdDifferentIds() {
        long id = (System.nanoTime() % 4) + 1;
        return roleService.getRoleById(id);
    }

    @Benchmark
    public Role benchmarkGetRoleByIdWithUsersDifferentIds() {
        long id = (System.nanoTime() % 4) + 1;
        return roleService.getRoleByIdWithUsers(id);
    }

    public static void main(String[] args) throws Exception {
        org.openjdk.jmh.Main.main(args);
    }
}