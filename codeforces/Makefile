CC = g++ 
STD = -std=c++17 
LDFLAGS = -fsanitize=undefined -fsanitize=address

TARGET = 1598a 400b 1862b
	
all: $(TARGET)

%: %.cc
	$(CC) $(STD) $(LDFLAGS) -o $@ $^


.PHONY: run
run/%: 
	cat $*.cmd | $*

.PHONY: clean
clean:
	-rm -f $(TARGET)
