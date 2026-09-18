import sys


def test(a: int, b:int) -> int:
    return a+b

if __name__ == '__main__':
    if len(sys.argv) < 3:
        print('Usage: main.py <number> <number>')

    print(test(int(sys.argv[1]), int(sys.argv[2])))