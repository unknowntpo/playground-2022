package org.example;

import org.example.entity.Role;
import org.example.service.RoleService;
import org.example.service.RoleServiceNonCached;
import org.openjdk.jmh.annotations.*;

import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@State(Scope.Benchmark)
@Fork(1)
@Warmup(iterations = 3, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 5, time = 2, timeUnit = TimeUnit.SECONDS)
public class RoleCacheBenchmark {

    private RoleService cachedService;
    private RoleServiceNonCached nonCachedService;

    @Setup(Level.Trial)
    public void setup() {
        cachedService = new RoleService();
        nonCachedService = new RoleServiceNonCached();
        // Warm up cache
        cachedService.getRoleById(1L);
        cachedService.getRoleByIdWithUsers(1L);
    }

    // Cached benchmarks
    @Benchmark
    public Role cachedGetRoleByIdSame() {
        return cachedService.getRoleById(1L);
    }

    @Benchmark
    public Role cachedGetRoleByIdWithUsersSame() {
        return cachedService.getRoleByIdWithUsers(1L);
    }

    @Benchmark
    public Role cachedGetRoleByIdDifferent() {
        long id = (System.nanoTime() % 4) + 1;
        return cachedService.getRoleById(id);
    }

    @Benchmark
    public Role cachedGetRoleByIdWithUsersDifferent() {
        long id = (System.nanoTime() % 4) + 1;
        return cachedService.getRoleByIdWithUsers(id);
    }

    // Non-cached benchmarks
    @Benchmark
    public Role nonCachedGetRoleByIdSame() {
        return nonCachedService.getRoleById(1L);
    }

    @Benchmark
    public Role nonCachedGetRoleByIdWithUsersSame() {
        return nonCachedService.getRoleByIdWithUsers(1L);
    }

    @Benchmark
    public Role nonCachedGetRoleByIdDifferent() {
        long id = (System.nanoTime() % 4) + 1;
        return nonCachedService.getRoleById(id);
    }

    @Benchmark
    public Role nonCachedGetRoleByIdWithUsersDifferent() {
        long id = (System.nanoTime() % 4) + 1;
        return nonCachedService.getRoleByIdWithUsers(id);
    }

    public static void main(String[] args) throws Exception {
        org.openjdk.jmh.Main.main(args);
    }
}