class MyQueue:
    def __init__(self):
        self.s0 = []
        self.s1 = []

    def push(self, x: int) -> None:
        self.s0.append(x)

    def pop(self) -> int:
        oldLength = len(self.s0)
        for i in range(0, oldLength-1):
            self.s1.append(self.s0.pop())
        out = self.s0.pop()

        # push element from s1 back to s0
        oldLength = len(self.s1)
        for e in range (0, oldLength):
            self.s0.append(self.s1.pop()) 
        return out
    def peek(self) -> int:
        return self.s0[0]

    def empty(self) -> bool:
        return len(self.s0) == 0

myQueue = MyQueue();
myQueue.push(1); # queue is: [1]
myQueue.push(2); # queue is: [1, 2] (leftmost is front of the queue)
assert(myQueue.peek() == 1) # return 1
assert(myQueue.pop() == 1) # return 1, queue is [2]
assert(myQueue.empty() == False) # return false
