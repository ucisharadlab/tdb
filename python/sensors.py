import duckdb
import pandas as pd
from sqlalchemy import create_engine

Q1 = "SELECT * FROM {} WHERE time >= {} AND time <= {}"
Q2 = "SELECT * FROM {} WHERE time >= {} AND time <= {} AND sid={}"
Q3 = "SELECT * FROM {} WHERE time >= {} AND time <= {} AND sid IN ({})"


class Sensor:

    def __init__(self):
        self.engine = create_engine('postgresql://sdb:sdb@localhost:5432/tdb')

    def fetch_data(self, type, sensors, start, end):
        if len(sensors) == 0:
            return pd.read_sql_query(Q1.format(type, start, end), self.engine, index_col="index")
        elif len(sensors) == 1:
            return pd.read_sql_query(Q2.format(type, start, end, sensors[0]), self.engine, index_col="index")
        else:
            return pd.read_sql_query(Q3.format(type, start, end, ','.join(map(str, sensors))),
                                     self.engine, index_col="index")


if __name__ == "__main__":
    sen = Sensor()
    df = sen.fetch_data("wifi", [21], 10, 50)
    print(df)