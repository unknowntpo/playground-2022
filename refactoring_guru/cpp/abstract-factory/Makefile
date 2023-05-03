CXX=g++
CXXFLAGS=-std=c++11

TARGETS=main ccUI

.PHONY: all

all: $(TARGETS)

$(TARGETS):
	$(CXX) $(CXXFLAGS) $@.cc -o $@

clean:
	rm -f $(TARGETS)
