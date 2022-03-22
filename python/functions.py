import random
import time


class ObservingFunction:

    def __init__(self):
        self.property_function_map = {
            "occupants": {
                "location": [1, 2]
            },
            "rooms": {
                "occupancy": [3, 4],
                "energy": [2, 3]
            }
        }

        self.function_cost = {
            1: 10,
            2: 5,
            3: 9,
            4: 14
        }

    def read_function_metadata(self):
        pass

    def execute(self, fid, interval, sensors):
        st = interval[0]
        et = interval[1]
        values = []
        low = 1
        high = 10000

        if fid in [1, 2]:
            low = 1
            high = 200
        elif fid in [3, 4]:
            low = 1
            high = 1000

        while st < et:
            if et-st < 50:
                ilen = et-st
            else: ilen = random.randint(50, et-st)
            values.append(((st, st+ilen), random.randint(low, high)))
            st += ilen+1

        # time.sleep((interval[1]-interval[0])*self.function_cost[fid]*0.0000001)
        return values

    def get_functions(self, relation, property):
        return self.property_function_map[relation][property]

    def cost(self, function, interval):
        return self.function_cost[function]*(interval[1]-interval[0])
