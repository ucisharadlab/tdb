import itertools
import random
import time


class Rectangle:

    def __init__(self, lb, rt):
        self.lb = lb
        self.rt = rt
        self.w = rt[0] - lb[0]
        self.h = rt[1] - lb[1]
        self.area = self.h*self.w

    def __eq__(self, other):
        return isinstance(other, Rectangle) and tuple(self.lb) == tuple(other.lb) and tuple(self.rt) == tuple(other.rt)

    def __repr__(self):
        return type(self).__name__ + '(' + str(self.lb) + ',' + str(self.rt) + ')'


def intersect(R1, R2):
    lb0 = max(R1.lb[0], R2.lb[0])
    lb1 = max(R1.lb[1], R2.lb[1])

    rt0 = min(R1.rt[0], R2.rt[0])
    rt1 = min(R1.rt[1], R2.rt[1])

    # no intersection
    if (lb0 > rt0 or lb1 > rt1):
        return None

    else:
        return Rectangle([lb0, lb1], [rt0, rt1])


def pairwise(iterable):
    # //docs.python.org/dev/library/itertools.html#recipes
    a, b = itertools.tee(iterable)
    next(b, None)
    return zip(a, b)


def difference(R1, R2):
    inter = intersect(R1, R2)
    if inter is None:
        return [R1]

    xs = {R1.lb[0], R1.rt[0]}
    ys = {R1.lb[1], R1.rt[1]}
    if R1.lb[0] < R2.lb[0] < R1.rt[0]: xs.add(R2.lb[0])
    if R1.lb[0] < R2.rt[0] < R1.rt[0]: xs.add(R2.rt[0])
    if R1.lb[1] < R2.lb[1] < R1.rt[1]: ys.add(R2.lb[1])
    if R1.lb[1] < R2.rt[1] < R1.rt[1]: ys.add(R2.rt[1])

    rects = []
    for (x1, x2), (y1, y2) in itertools.product(
            pairwise(sorted(xs)), pairwise(sorted(ys))
    ):
        rect = Rectangle([x1, y1], [x2, y2])
        if rect != inter:
            rects.append(rect)

    return rects


def plot_rectangles(rects, main=None, fill=False, stop=True):
    import matplotlib.pyplot as plt
    from matplotlib.patches import Rectangle
    from matplotlib.pyplot import rcParams
    rcParams['figure.figsize'] = 12, 6

    fig, ax = plt.subplots()
    if main is not None:
        ax.add_patch(Rectangle(main.lb, main.w, main.h, fill=False, edgecolor='black', linewidth=4.4))
    for rect in rects:
        r = random.random()
        b = random.random()
        g = random.random()
        color = (r, g, b)
        ax.add_patch(Rectangle(rect.lb, rect.w, rect.h, fill=fill, edgecolor=color, linewidth=1.4))
    # plt.xlabel("X-AXIS")
    # plt.ylabel("Y-AXIS")
    plt.xlim(0, 15)
    plt.ylim(0, 15)
    if stop:
        plt.show(block=False)
        plt.pause(1)
        plt.close('all')
    else:
        plt.show()


def random_rect(rect):
    lb = [random.randint(rect.lb[0]-1, rect.rt[0]-1), random.randint(rect.lb[1]-1, rect.rt[1])]
    rt = [random.randint(lb[0]+1, rect.rt[0]+1), random.randint(lb[1]+1, rect.rt[1]+1)]

    return Rectangle(lb, rt)


def inverese_coverage(main):
    entries = []
    rects = []
    for i in range(24):
        rect = intersect(random_rect(main), main)
        if rect.area == 0:
            continue
        entry = [i, rect, random.randint(10, 100), rect.area]
        entries.append(entry)
        rects.append(rect)
    plot_rectangles(rects, main, False, False)
    plot_rectangles(rects, main, True, False)
    return entries


def manual_entry():
    return [[0, Rectangle([7, 1],[12, 2]), 15, 5], [1, Rectangle([8, 10],[12, 11]), 94, 4]]


def adjust(entry, entry1, rects):

    inter = intersect(entry[1], entry1[1])
    if inter is None:
        return entry1[3]

    area = 0
    for rect in rects:
        inter1 = intersect(inter, rect)
        if inter1 is not None:
            area = area + inter1.area

    entry1[3] -= area
    if entry1[3] <= 0:
        entry1[2] = 100000000
        entry1[3] = 1

    return entry1[3]


def plan(rect):
    plan_space = inverese_coverage(rect)
    # plan_space = manual_entry()
    rects = [rect]
    ans = []
    ids = []
    total_cost = 0

    while rects:
        # print(sorted(plan_space, key=lambda x: x[2] / x[3]))
        entry = sorted(plan_space, key=lambda x:x[2]/x[3])[0]
        # print ("E", entry)
        total_cost += entry[2]

        # ans2 = ans[:]
        # for i in range(len(ans)):
        #     if intersect(entry[1], ans[i]) != ans[i]:
        #         ans2.append(ans[i])
        # ans = ans2

        ans.append(entry[1])
        ids.append(entry[0])
        entry[2] = 100000000
        plot_rectangles(rects, rect, True)
        # plot_rectangles(ans, rect, False)

        for entry1 in plan_space:
            if entry1[0] != entry[0] and entry1[0] not in ids:
                entry1[3] = adjust(entry, entry1, rects)

        rects2 = []
        for r in rects:
            res = difference(r, entry[1])
            if res:
                rects2.extend(res)
        rects = rects2
        # print(ans)

    print("Total Cost:", total_cost)
    return ans


if __name__ == "__main__":

    R1 = Rectangle([4, 3], [10, 8])
    R2 = Rectangle([1, 1], [4, 6])

    rects = plan(R1)
    plot_rectangles(rects, None, False, False)
