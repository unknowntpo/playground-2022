CC = g++ 
STD = -std=c++17 
LDFLAGS = -fsanitize=undefined -fsanitize=address

TARGET = 1598a

all: 1598a 400b 1862b

1862b: 1862b.cc
	$(CC) $(STD) $(LDFLAGS) -o $@ $^

1598a: 1598a.cc
	$(CC) $(STD) $(LDFLAGS) -o $@ $^

400b: 400b.cc
	$(CC) $(STD) $(LDFLAGS) -o $@ $^



.PHONY: run
run/%: 
	cat $*.cmd | $*

.PHONY: clean
clean:
	-rm -f $(TARGET)
