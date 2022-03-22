import pandas as pd
import duckdb
import timeit

from coverage import InverseCoverage
from functions import ObservingFunction
from database import DB

DELTA = 10


class Translation:

    def __init__(self, entities_map, relation, property, interval):
        self.relation = relation
        self.property = property
        self.interval = interval
        self.ic = InverseCoverage()
        self.of = ObservingFunction()
        self.entities_map = entities_map

    def estimate_cost(self, property, num_entites, avg_interval):
        return num_entites*avg_interval*0.1

    def get_plan(self):

        plan_space = self.generate_plan_space()
        print(self.num_plans(plan_space))

        plan = self.select_plan(plan_space)
        print(plan)
        return plan

    def num_plans(self, plan_space):
        c = 1
        for k, v in plan_space.items():
            c *= len(v)
        return c

    def generate_plan_space(self):
        plan_space = {}
        functions = self.of.get_functions(self.relation, self.property)
        for entity in self.entities_map.keys():
            plan_space[entity] = {}
            for function in functions:
                interval_sensors = self.ic.get_sensors(entity, self.entities_map[entity], function, self.context)
                for interval, sensors in interval_sensors.items():
                    try:
                        plan_space[entity][interval].append((function, sensors))
                    except KeyError:
                        plan_space[entity][interval] = [(function, sensors)]
        return plan_space

    def select_plan(self, plan_space):
        plan = {}
        for entity, imap in plan_space.items():
            plan[entity] = []
            cost = 1000000
            best = None
            for interval, sensors_list in imap.items():
                for sensors in sensors_list:
                    if self.of.cost(sensors[0], interval) < cost:
                        best = sensors
                plan[entity].append((interval, best))
        return plan


class TranslationExecutor:

    def __init__(self, plan, relation, property):
        self.of = ObservingFunction()
        self.plan = plan
        self.values_df = None
        self.db = DB()
        self.relation = relation
        self.property = property

    def execute(self):
        results = []
        for entity, values in self.plan.items():
            for value in values:
                interval = value[0]
                function = value[1][0]
                sensors = value[1][1]

                answers = self.of.execute(function, interval, sensors)
                for answer in answers:
                    results.append([entity, answer[0][0], answer[0][1], answer[1]])

        self.values_df = pd.DataFrame(results, columns=["eid", "st", "et", "value"])

    def merge(self, df, entity_col, value_col, st_col, et_col):
        a = timeit.default_timer()
        self.db.update(self.values_df, "{}_{}".format(self.relation, self.property), "eid", "value", "st", "et", )
        print(timeit.default_timer() - a)

        conn = duckdb.connect()
        conn.register("R", df)
        conn.register("S", self.values_df)
        df_cols = []
        for col in df.columns:
            if col == entity_col:
                df_cols.append("S.eid as {}".format(entity_col))
            elif col == value_col:
                df_cols.append("S.value as {}".format(value_col))
            elif col == st_col:
                df_cols.append("S.st as {}".format(st_col))
            elif col == et_col:
                df_cols.append("S.et as {}".format(et_col))
            else:
                df_cols.append("R."+col)
        df_cols = ", ".join(df_cols)

        df2 = conn.execute("SELECT {4} "
                           "FROM R, S  WHERE R.{0} = S.eid AND R.{2}<=S.et AND R.{3}>=S.st AND R.{1}=-1"
                           "UNION "
                           "(SELECT * FROM R WHERE R.{1} <> -1)".format(entity_col, value_col, st_col, et_col, df_cols)).fetchdf()

        return df2

    def update_table(self):
        pass




