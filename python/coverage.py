import random


class InverseCoverage:

    def __init__(self):
        self.of_to_ic_functions = {
            1: 1,
            2: 2,
            3: 3,
            4: 2
        }
        self.ic_group_size = {
            1: 2,
            2: 1,
            3: 3
        }

    def get_sensors(self, entity, interval_list, o_function, context):
        ic_function = self.of_to_ic_functions[o_function]
        return self.run_ic_function(entity, interval_list, ic_function, context)

    def run_ic_function(self, entity, interval_list, ic_function, context):
        sensors = {}
        group_size = self.ic_group_size[ic_function]
        for interval in interval_list:
            sensors[interval] = tuple(random.sample(range(1, 1000), group_size))

        return sensors



