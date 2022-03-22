import pandas
import duckdb


def extract(df, entity_col, value_col, st_col, et_col):
    """ From the dataframe generate {e: [(st, et)]} map
    where the value is not filled i.e. is -1
    """
    conn = duckdb.connect()
    conn.register("T", df)

    df2 = conn.execute("SELECT distinct {0} as eid, {2} as st, {3} as et "
                        "FROM T where {1} = -1 "
                        "ORDER BY {0}, {2}, {3}".format(entity_col, value_col, st_col, et_col)).fetchdf()

    emap = {}
    for row in df2.itertuples():
        try:
            emap[row[1]].append((row[2], row[3]))
        except KeyError:
            emap[row[1]] = [(row[2], row[3])]
    return emap


