
Q = 
CC = gcc
VERBOSE =

ifeq ($(VERBOSE), 1)
	VECHO = @true
else
	Q = @
	VECHO = @printf
endif

test: main_test.c va_list.c
	$(Q)$(CC) -o $@ -g $^
	$(VECHO) '  CC\t $^\n'
	@echo ''
	@echo 'Running automated tests...'
	@echo ''
	@./$@

clean: 
	@-rm test