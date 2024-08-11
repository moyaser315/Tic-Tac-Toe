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

#######implementing minmax


def minmaxopt(p: str, alpha: float, beta: float, depth: int):
    #print(f"opti called with player ={p}, alpha={alpha}, beta={beta}, depth={depth}")

    if check_win("o"):
        score = 1
        return score, None, None
    elif check_win("x"):
        score = -1
        return score, None, None
    elif depth == 0:
        score = check_b()
        return score, None, None

    ans_r = ans_col = None

    if p == "o":
        cur_score, ans_r, ans_col = maximmize_computer(alpha, beta, depth)

    else:
        cur_score, ans_r, ans_col = minimize_player(alpha, beta, depth)

    return cur_score, ans_r, ans_col


# getting probapility of O to win after looking ahead
# TODO: handle condition of 3 O and 1 X to be ignored
def check_b():
    Xwin = Owin = 0
    for line in get_lines():
        xcnt, ocnt, empcnt = check_l(line)
        if xcnt == 3 and empcnt == 1:
            Xwin += 1
        elif ocnt == 3 and empcnt == 1:
            Owin += 1

    if Xwin < Owin:
        return 1
    elif Owin < Xwin:
        return -1
    else:
        return 0


def check_l(line: list):
    xcnt = ocnt = empcnt = 0
    for i, j in line:
        if xo[i][j] == "x":
            xcnt += 1
        elif xo[i][j] == "o":
            ocnt += 1
        else:
            empcnt += 1
    return xcnt, ocnt, empcnt


def maximmize_computer(alpha: float, beta: float, depth: int):
    cur_score = -float("inf")
    for i in range(7):
        for j in range(7):
            if xo[i][j] == " ":
                xo[i][j] = "o"
                score, _, _ = minmaxopt("x", alpha, beta, depth - 1)
                xo[i][j] = " "
                if score > cur_score:
                    cur_score = score
                    ans_r, ans_col = i, j
                alpha = max(alpha, cur_score)
                if beta <= alpha:
                    break
    return cur_score, ans_r, ans_col


def minimize_player(alpha: float, beta: float, depth: int):
    cur_score = float("inf")
    for i in range(7):
        for j in range(7):
            if xo[i][j] == " ":
                xo[i][j] = "x"
                score, _, _ = minmaxopt("o", alpha, beta, depth - 1)
                xo[i][j] = " "
                if score < cur_score:
                    cur_score = score
                    ans_r, ans_col = i, j
                beta = min(beta, cur_score)
                if beta <= alpha:
                    break
    return cur_score, ans_r, ans_col


### start and test game


def start():
    p = "x"
    turns, dep = 0, 3
    print("please note your choice effect your ram and CPU usage directly")
    print("please choose difficulty 1 is easy 4 is extreme")
    dep = int(input())
    dep = dep if dep > 0 and dep < 5 else 3
    print(dep)
    while turns < 49:
        print_table()
        if p == "o":
            _, row, col = minmaxopt(p, -float("inf"), float("inf"), dep)
            print(f"player O move: ({row}, {col})")
        else:

            row, col = map(int, input("player X, (r,c): ").split())
        if xo[row][col] == " ":
            xo[row][col] = p
            if check_win(p):
                print_table()
                print("p " + p + " wins!")
                return
            p = "x" if p == "o" else "o"
            turns += 1
        else:
            print("occupied")

    print_table()
    print("tie")


if __name__ == "__main__":
    start()
