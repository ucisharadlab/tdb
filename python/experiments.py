import pandas as pd
import duckdb
import timeit
from sqlalchemy import create_engine

import plan
from translation import Translation, TranslationExecutor


def translator(df, relation, property, id, value, st, et, context=None):
    emap = plan.extract(df, id, value, st, et)
    tra = Translation(emap, relation, property, context)
    tra_plan = tra.get_plan()
    executor = TranslationExecutor(tra_plan, relation, property)
    executor.execute()
    df = executor.merge(df, id, value, st, et)
    print(df)
    return df


class Query1:

    def __init__(self):

        self.conn = duckdb.connect()
        self.conn_cursor = self.conn.cursor()
        self.cursor = self.conn_cursor
        self.engine = create_engine('postgresql://sdb:sdb@localhost:5432/tdb')
        occupant_df = pd.read_sql_query("SELECT * FROM occupants", self.engine, index_col="index")
        occupant_location_df = pd.read_sql_query("SELECT * FROM occupants_location WHERE et > 9000 and st < 9400",
                                                 self.engine, index_col="index")
        self.conn.register("O", occupant_df)
        self.conn.register("OL", occupant_location_df)

    def plan1(self):
        df = self.conn.execute("SELECT O.id,O.name,OL.st, OL.et, OL.value FROM O,OL "
                               "WHERE O.id=OL.eid AND O.name='Occupant_11'").fetchdf()

        df = translator(df, "occupants", "location", "id", "value", "st", "et")
        print(df)

    def plan2(self):
        pass


class Query2:

    def __init__(self):
        self.conn = duckdb.connect()
        self.cursor = self.conn.cursor()
        self.engine = create_engine('postgresql://sdb:sdb@localhost:5432/tdb')
        occupant_df = pd.read_sql_query("SELECT * FROM occupants", self.engine, index_col="index")
        occupant_location_df = pd.read_sql_query("SELECT * FROM occupants_location WHERE et > 5000 and st < 5100",
                                                 self.engine, index_col="index")
        self.conn.register("O", occupant_df)
        self.conn.register("OL", occupant_location_df)

    def plan1(self):
        o_df = self.conn.execute("SELECT * FROM OL").fetchdf()
        o_df = translator(o_df, "occupants", "location", "id", "value", "st", "et", {"rooms": [1, 2, 3]})

        self.conn.register("OL", o_df)
        print(self.conn.execute("SELECT * FROM OL,O WHERE O.id=OL.eid").fetchdf())


class Query3:

    def __init__(self):
        self.conn = duckdb.connect()
        self.cursor = self.conn.cursor()
        self.engine = create_engine('postgresql://sdb:sdb@localhost:5432/tdb')

        room_df = pd.read_sql_query("SELECT * FROM rooms", self.engine, index_col="index")
        room_occcupancy_df = pd.read_sql_query("SELECT * FROM Rooms_Occupancy WHERE et > 5000 and st < 5100",
                                               self.engine, index_col="index")
        self.conn.register("R", room_df)
        self.conn.register("RO", room_occcupancy_df)

    def plan1(self):
        r_df = self.conn.execute("SELECT R.id,R.name,RO.st, RO.et, RO.value "
                                 "FROM R,RO WHERE R.id=RO.eid and R.name='Room 1'").fetchdf()
        r_df = translator(r_df, "rooms", "occupancy", "id", "value", "st", "et")

        self.conn.register("R", r_df)
        print(self.conn.execute("SELECT * FROM R").fetchdf())


class Query4:

    def __init__(self):
        self.conn = duckdb.connect()
        self.cursor = self.conn.cursor()
        self.engine = create_engine('postgresql://sdb:sdb@localhost:5432/tdb')

        room_df = pd.read_sql_query("SELECT * FROM rooms", self.engine, index_col="index")
        room_occcupancy_df = pd.read_sql_query("SELECT * FROM Rooms_Occupancy WHERE et > 5000 and st < 5100",
                                               self.engine, index_col="index")
        self.conn.register("R", room_df)
        self.conn.register("RO", room_occcupancy_df)

    def plan1(self):
        r_df = self.conn.execute("SELECT R.id,R.name,RO.st, RO.et, RO.value "
                                 "FROM R,RO WHERE R.id=RO.eid").fetchdf()
        r_df = translator(r_df, "rooms", "occupancy", "id", "value", "st", "et")

        self.conn.register("R", r_df)
        print(self.conn.execute("SELECT * FROM R Where occupancy > 50").fetchdf())


class Query5:

    def __init__(self):
        self.conn = duckdb.connect()
        self.cursor = self.conn.cursor()
        self.engine = create_engine('postgresql://sdb:sdb@localhost:5432/tdb')
        occupant_df = pd.read_sql_query("SELECT * FROM occupants", self.engine, index_col="index")
        occupant_location_df = pd.read_sql_query("SELECT * FROM occupants_location WHERE et > 5000 and st < 5100",
                                                 self.engine, index_col="index")
        self.conn.register("O", occupant_df)
        self.conn.register("OL", occupant_location_df)

    def plan1(self):
        df1 = self.conn.execute("SELECT O.id,O.name,OL.st, OL.et, OL.value FROM O,OL "
                                "WHERE O.id=OL.eid AND O.name='Occupant_11'").fetchdf()

        df1 = translator(df1, "occupants", "location", "id", "value", "st", "et")
        print(df1)

        self.conn.register("OL1", df1)
        print(self.conn.execute("SELECT * FROM OL,O WHERE O.id=OL.eid").fetchdf())

        df2 = self.conn.execute("SELECT O.id,O.name,OL.st, OL.et, OL.value FROM O,OL "
                                "WHERE O.id=OL.eid").fetchdf()
        df2 = translator(df2, "occupants", "location", "id", "value", "st", "et", {"rooms": [1, 2, 3]})
        print(df2)


class Query6:

    def __init__(self):
        self.conn = duckdb.connect()
        self.cursor = self.conn.cursor()
        self.engine = create_engine('postgresql://sdb:sdb@localhost:5432/tdb')
        occupant_df = pd.read_sql_query("SELECT * FROM occupants", self.engine, index_col="index")
        occupant_location_df = pd.read_sql_query("SELECT * FROM occupants_location WHERE et > 5000 and st < 5100",
                                                 self.engine, index_col="index")
        self.conn.register("O", occupant_df)
        self.conn.register("OL", occupant_location_df)

    def plan1(self):
        o_df = self.conn.execute("SELECT * FROM OL").fetchdf()
        o_df = translator(o_df, "occupants", "location", "id", "value", "st", "et", {"rooms": [1, 2]})

        self.conn.register("OL", o_df)
        print(self.conn.execute("SELECT * FROM OL as O1, OL as O2 WHERE O1.id!=OL.eid and O1.value=1 and O2.value=2").fetchdf())


class Query7:

    def __init__(self):

        self.conn = duckdb.connect()
        self.conn_cursor = self.conn.cursor()
        self.cursor = self.conn_cursor
        self.engine = create_engine('postgresql://sdb:sdb@localhost:5432/tdb')
        occupant_df = pd.read_sql_query("SELECT * FROM occupants", self.engine, index_col="index")
        occupant_location_df = pd.read_sql_query("SELECT * FROM occupants_location WHERE et > 9000 and st < 9400",
                                                 self.engine, index_col="index")
        self.conn.register("O", occupant_df)
        self.conn.register("OL", occupant_location_df)

    def plan1(self):
        df = self.conn.execute("SELECT O.id,O.name,OL.st, OL.et, OL.value FROM O,OL "
                               "WHERE O.id=OL.eid AND O.name='Occupant_11'").fetchdf()

        df = translator(df, "occupants", "location", "id", "value", "st", "et")
        print(df)

    def plan2(self):
        pass


class Query8:

    def __init__(self):
        self.conn = duckdb.connect()
        self.cursor = self.conn.cursor()
        self.engine = create_engine('postgresql://sdb:sdb@localhost:5432/tdb')
        occupant_df = pd.read_sql_query("SELECT * FROM occupants", self.engine, index_col="index")
        occupant_location_df = pd.read_sql_query("SELECT * FROM occupants_location WHERE et > 5000 and st < 5100",
                                                 self.engine, index_col="index")
        self.conn.register("O", occupant_df)
        self.conn.register("OL", occupant_location_df)

        room_df = pd.read_sql_query("SELECT * FROM rooms", self.engine, index_col="index")
        room_occcupancy_df = pd.read_sql_query("SELECT * FROM Rooms_Occupancy WHERE et > 5000 and st < 5100",
                                               self.engine, index_col="index")
        self.conn.register("R", room_df)
        self.conn.register("RO", room_occcupancy_df)

    def plan1(self):
        o_df = self.conn.execute("SELECT O.id,O.name,OL.st, OL.et, OL.value "
                                 "FROM O,OL WHERE O.id=OL.eid and et > 10 and st < 50").fetchdf()
        r_df = self.conn.execute("SELECT R.id,R.name,RO.st, RO.et, RO.value "
                                 "FROM R,RO WHERE R.id=RO.eid and et > 10 and st < 50").fetchdf()

        o_df = translator(o_df, "occupants", "location", "id", "value", "st", "et")
        r_df = translator(r_df, "rooms", "occupancy", "id", "value", "st", "et")

        self.conn.register("R", r_df)
        self.conn.register("O", o_df)

        print(self.conn.execute("SELECT * FROM R,O WHERE R.id=O.value and R.value> 10").fetchdf())

    def plan2(self):
        o_df = self.conn.execute("SELECT O.id,O.name,OL.st, OL.et, OL.value "
                                 "FROM O,OL WHERE O.id=OL.eid and et > 10 and st < 50").fetchdf()
        r_df = self.conn.execute("SELECT R.id,R.name,RO.st, RO.et, RO.value "
                                 "FROM R,RO WHERE R.id=RO.eid and et > 10 and st < 50").fetchdf()

        o_df = translator(o_df, "occupants", "location", "id", "value", "st", "et", )
        self.conn.register("R", r_df)
        self.conn.register("O", o_df)

        r_df = self.conn.execute("SELECT * FROM O,R WHERE R.id=O.value").fetchdf()
        r_df = translator(r_df, "rooms", "occupancy", "id_2", "value_2", "st_2", "et_2")
        self.conn.register("RXS", r_df)

        r_df = self.conn.execute("SELECT * FROM RXS WHERE RXS.value_2 > 10").fetchdf()
        print(r_df)


class Query9:

    def __init__(self):
        self.conn = duckdb.connect()
        self.conn_cursor = self.conn.cursor()
        self.cursor = self.conn_cursor
        self.engine = create_engine('postgresql://sdb:sdb@localhost:5432/tdb')
        self.room_df = pd.read_sql_query("SELECT * FROM Rooms_Energy WHERE et > 9000 and st < 9100",
                                         self.engine, index_col="index")
        self.conn.register("R", self.room_df)

    def plan1(self):
        r_df = translator(self.room_df, "rooms", "energy", "eid", "value", "st", "et")
        print(r_df)

    def plan2(self):
        pass


class Query10:

    def __init__(self):
        self.conn = duckdb.connect()
        self.conn_cursor = self.conn.cursor()
        self.cursor = self.conn_cursor
        self.engine = create_engine('postgresql://sdb:sdb@localhost:5432/tdb')
        self.occupant_df = pd.read_sql_query("SELECT * FROM Occupants_Vitals WHERE et > 9000 and st < 9100",
                                         self.engine, index_col="index")
        self.conn.register("R", self.room_df)

    def plan1(self):
        r_df = translator(self.room_df, "rooms", "energy", "eid", "value", "st", "et")
        print(r_df)

    def plan2(self):
        pass


def run_queries():
    pass


if __name__ == "__main__":
    start = timeit.default_timer()
    # q1 = Query1()
    # q1.plan1()

    # q2 = Query2()
    # q2.plan1()

    # q3 = Query3()
    # q3.plan1()

    # q4 = Query4()
    # q4.plan1()

    # q5 = Query5()
    # q5.plan1()

    # q6 = Query1()
    # q6.plan1()

    # q7 = Query2()
    # q7.plan1()

    # q8 = Query3()
    # q8.plan1()

    # q9 = Query4()
    # q9.plan1()

    q10 = Query5()
    q10.plan1()

    stop = timeit.default_timer()

    print(stop-start)
