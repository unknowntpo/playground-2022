
CC = gcc
OBJS := main.o

ifeq ($(VERBOSE),"1")
	Q :=
	VECHO = @true
else
	Q := @
	VECHO = @printf
endif

all: main

main: $(OBJS)  
	$(warning we are in target 'main')
	$(warning VECHO is $(VECHO))
	$(warning Q is $(Q))
	$(VECHO) "  LD\t $^\n"
	$(Q)$(CC) -o $@ $^

%.o: %.c
	$(warning we are in target 'main')
	$(warning VECHO is $(VECHO))
	$(warning Q is $(Q))
	$(VECHO) "  CC\t $^\n"
	$(Q)$(CC) -o $@ -c $^

.PNOHY: clean
clean:
	@-rm -rf $(OBJS)