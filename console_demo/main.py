###################General functions
xo = [[" " for j in range(7)] for i in range(7)]


def print_table():
    print("  ", end="")
    print(" ".join(" ".join(str(i) for i in range(7))))
    for i in range(7):
        print(i, end=" ")
        print(" | ".join(xo[i]))

    print()


#### checking winner conditions
# TODO: improve lines make extracting faster


def check_win(p: str):
    for line in get_lines():
        if check_line(p, line):
            return True
    return False


def get_lines():
    lines = []
    for i in range(7):
        for j in range(4):
            lines.append([(i, j + k) for k in range(4)])
    for i in range(4):
        for j in range(7):
            lines.append([(i + k, j) for k in range(4)])
    for i in range(4):
        for j in range(4):
            lines.append([(i + k, j + k) for k in range(4)])
    for i in range(4):
        for j in range(3, 7):
            lines.append([(i + k, j - k) for k in range(4)])
    return lines


def check_line(p: str, coord: list):
    return all(xo[i][j] == p for i, j in coord)