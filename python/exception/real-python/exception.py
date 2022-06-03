import sys

def linux_interaction():
    assert ('linux' in sys.platform), "Function can only run in linux platform."
    print('Doing something')

# try to open a file
try:
    linux_interaction()
    with open('file.log') as file:
        read_data = file.read()
except FileNotFoundError as fnf_error:
    print(fnf_error)
    print('Cound not open file.log')
except AssertionError as error:
    print(error)
    print('Linux linux_interaction() function is not executed')