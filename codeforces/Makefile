CC = g++ 
STD = -std=c++17 
LDFLAGS = -fsanitize=undefined -fsanitize=address

TARGET = 1598a

all: 1598a

1598a: 1598a.cc
	$(CC) $(STD) $(LDFLAGS) -o $@ $^

.PHONY: run
run: $(RUN)
	cat $(RUN).cmd | $(RUN)

.PHONY: clean
clean:
	-rm -f $(TARGET)