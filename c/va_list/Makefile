CC = gcc
OBJS = main.o va_list.o

ifeq ("$(VERBOSE)", "1")
	Q :=
	VECHO = @true
else
	Q := @
	VECHO = @printf
endif

all: out

out: $(OBJS)
	$(VECHO) '  LD\t $^\n'
	$(Q)$(CC) -o $@ $^
	
test: main_test.o va_list.o
	$(VECHO) '  LD\t $^\n'
	$(Q)$(CC) -o $@ $^
	@echo ''
	@echo 'Running automated tests...'
	@echo ''
	@./$@

%.o: %.c
	$(VECHO) '  CC\t $^\n'
	$(Q)$(CC) -o $@ -g -c $^

clean: 
	@-rm test out *.o