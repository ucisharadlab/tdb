import psycopg2
import pandas as pd
from sqlalchemy import create_engine


class DB:

    def __init__(self):
        self.con = psycopg2.connect(database='tdb', user='sdb', password='sdb')
        self.cur = self.con.cursor()
        self.engine = create_engine('postgresql://sdb:sdb@localhost:5432/tdb')

    def update(self, df, relation, eid, value, st, et):
        df = df.convert_dtypes()
        for index, row in df.iterrows():
            sql = 'delete from {} where eid = %s and st <= %s and et>=%s'.format(relation)
            self.cur.execute(sql, (row[eid], row[et], row[st]))
        self.con.commit()

        df.to_sql(relation, self.engine, if_exists='append')

    def explain(self, query):
        self.cur.execute("EXPLAIN (FORMAT JSON) " + query)
        return self.cur.fetchall()[0][0]