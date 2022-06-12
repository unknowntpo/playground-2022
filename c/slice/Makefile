all: test
test:
	g++ slice_test.cpp slice.c -std=c++11 -lgtest -lpthread -o test
	./test
	rm -f ./test
.PHONY: clean
clean:
	-rm -f test