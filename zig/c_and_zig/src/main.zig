const adder = @cImport({
    @cInclude("add.h");
});

const std = @import("std");
const testing = std.testing;

test "basic add functionality" {
    try testing.expectEqual(@as(i32, 10), adder.add(3, 7));
}

pub fn main() !void {
    const stdout = std.io.getStdOut().writer();
    _ = stdout;
    const res = adder.add(1, 2);
    _ = res;
    try testing.expectEqual(@as(i32, 10), adder.add(3, 7));
}
