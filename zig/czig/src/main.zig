const std = @import("std");

const fib_zig = @import("fib.zig");

const sum_c = @cImport(@cInclude("sum.h"));

const timeit_cpp = @cImport(@cInclude("timeit.h"));

fn fib20() callconv(.C) void {
    _ = fib_zig.fib(20);
}

fn sum_test() callconv(.C) void {
    _ = sum_c.sum(10, 20);
}

pub fn main() !void {
    // Prints to stderr (it's a shortcut based on `std.io.getStdErr()`)
    std.debug.print("All your {s} are belong to us.\n", .{"codebase"});

    // stdout is for the actual output of your application, for example if you
    // are implementing gzip, then only the compressed bytes should be sent to
    // stdout, not any debugging messages.
    const stdout_file = std.io.getStdOut().writer();
    var bw = std.io.bufferedWriter(stdout_file);
    const stdout = bw.writer();

    try stdout.print("Run `zig build test` to run the tests.\n", .{});

    try stdout.print("{} + {} = {}, \t\t{d:>12.3} ms\n", .{ 20, 10, sum_c.sum(10, 20), timeit_cpp.time_it(sum_test) });
    try stdout.print("fib({}) = {}\t\t{d:>12.3} ms\n", .{ 20, fib_zig.fib(20), timeit_cpp.time_it(fib20) });

    try bw.flush(); // don't forget to flush!
}

test "simple test" {
    var list = std.ArrayList(i32).init(std.testing.allocator);
    defer list.deinit(); // try commenting this out and see if zig detects the memory leak!
    try list.append(42);
    try std.testing.expectEqual(@as(i32, 42), list.pop());
}
