Q = @
CC = gcc
VERBOSE =

all: out

ifeq ("$(VERBOSE)", "1")
	$(warning executing VERBOSE)
	Q :=
	VECHO := @true
else
	Q := @
	VECHO := @printf
endif

out: main.o va_list.o 
	$(warning executing debug target 'out')
	echo '$(VERBOSE)'
	echo '$(VECHO)'
	echo '$(Q)'

	$(VECHO) '  LD\t $^\n'
	$(Q)$(CC) -o $@ $^
	
test: main_test.o va_list.o
	$(VECHO) '  LD\t $^\n'
	$(Q)$(CC) -o $@ $^
	@echo ''
	@echo 'Running automated tests...'
	@echo ''
	@./$@

SUFFIXES: .o .c
.c.o:
	$(VECHO) '  CC\t $^\n'
	$(Q)$(CC) -o $@ -g -c $^

clean: 
	@-rm test out *.o