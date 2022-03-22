import json
from database import DB

SKIP = ["Materialize", "Hash", "Gather Merge", "Sort"]
STOP = ["Bitmap Heap Scan"]


class Node:
    def __init__(self, type, cost, rows):
        self.parent = None
        self.left = None
        self.right = None
        self.type = type
        self.cost = cost
        self.rows = rows
        self.join_condition = ""
        self.relation = None
        self.alias = None
        self.filter = ""


def fill_other_params(node, data):
    type = data["Node Type"]
    if type == "Hash Join":
        node.join_condition += data["Hash Cond"]
    if type in ("Hash Join", "Nested Loop"):
        try:
            node.join_condition += "AND" + data["Join Filter"]
        except KeyError:
            pass

    if type in ("Seq Scan", "Index Scan", "Bitmap Heap Scan"):
        node.relation = data["Relation Name"]
        node.alias = data["Alias"]
    if type == "Seq Scan":
        try:
            node.filter = data["Filter"]
        except KeyError:
            pass
    if type == "Index Scan":
        node.filter += data["Index Cond"]
        try:
            node.filter += " AND " + data["Filter"]
        except KeyError:
            pass
    if type == "Bitmap heap Scan":
        node.filter += data["Recheck Cond"]


def create_tree_helper(data):
    type = data["Node Type"]
    cost = data["Total Cost"]
    rows = data["Plan Rows"]
    if type in SKIP:
        return create_tree_helper(data["Plans"][0])
    elif type in STOP:
        return Node(type, cost, rows)

    node = Node(type, cost, rows)
    fill_other_params(node, data)

    if "Plans" not in data:
        return node
    plans = data["Plans"]
    lnode = create_tree_helper(plans[0])
    if lnode is not None:
        lnode.parent = node
    node.left = lnode
    if len(plans) > 1:
        rnode = create_tree_helper(plans[1])
        if rnode is not None:
            rnode.parent = node
        node.right = rnode

    return node


def create_tree(data):
    parent = create_tree_helper(data[0]['Plan'])
    return parent


def plan_to_procedure(node):

    if node.type == 'Translator':
        print("df = Translator(df)")
        plan_to_procedure(node.left)

    elif node.right is None:
        if node.left.left is None:
            if node.filter:
                print("df = self.conn.execute(SELECT * FROM {} WHERE {})".format(node.relation, node.filter))
            else:
                print("df = self.conn.execute(SELECT * FROM {})".format(node.relation))
        else:
            pass

    if node.left is not None and node.right is not None:
        ltext = plan_to_procedure(node.left)
        rtext = plan_to_procedure(node.right)

        if node.join_condition is not None:
            clause = "FROM {}, {} ON {}".format(ltext, rtext, node.join_condition)


if __name__ == "__main__":
    db = DB()
    data = db.explain("select ol.value, count(*) from occupants_location ol, occupants o where st >0 and o.id=ol.eid group by ol.value ")

    tree = create_tree(data)
    print(tree)

