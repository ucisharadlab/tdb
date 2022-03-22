import random

from flask import Blueprint
from flask import flash
from flask import g
from flask import redirect
from flask import render_template
from flask import request
from flask import url_for
from flask import json, Response
from werkzeug.exceptions import abort

import threading
import time

bp = Blueprint("blog", __name__)

query_thread = {}
query_response = {}
query_plan = {}

approx_query = None


ERROR = {
    "error": 1
}

SUCCESS = {
    "success": 1
}


@bp.route("/execute", methods=("GET",))
def execute():
    """Create a new post for the current user."""

    time_from = int(request.args['from'])
    time_to = int(request.args['to'])
    # entity = request.args['entity']
    # prop = request.args['property']
    # sensor_type = request.args['sensor_type']

    result = {
        'data': [],
    }
    cover = []
    time = time_from
    while time < time_to:
        time += random.randint(10000, 1000000)
        result['data'].append({'time':time, 'value': random.randint(0, 1)})
        # result['value'].append(random.randint(0, 1))
        # cover.append({'time'time, random.randint(0, 1)])

    return Response(json.dumps(result), status=200, mimetype='application/json')


@bp.route("/coverage", methods=("GET",))
def npexecute():
    """Create a new post for the current user."""

    if not request.headers['Content-Type'] == 'application/json':
        return Response(json.dumps(ERROR), status=415, mimetype='application/json')

    payload = request.json
    query = payload['query']

    query_exec = Baseline1Query(query)

    return Response(json.dumps(query_exec.run()), status=200, mimetype='application/json')


@bp.route("/stop", methods=("POST",))
def stop():
    """Create a new post for the current user."""
    global query_thread
    global query_response
    global query_plan

    payload = request.json
    token = payload['token']

    if token not in query_thread:
        return Response(json.dumps(ERROR), status=415, mimetype='application/json')
    else:
        if query_thread[token].is_alive():
            query_thread[token].close()

        del query_thread[token]
        del query_response[token]
        del query_plan[token]

    return Response(json.dumps(SUCCESS), status=200, mimetype='application/json')


@bp.route("/reload", methods=("GET", "POST"))
def reload_results():
    """Update a post if the current user is the author."""
    global query_thread
    global query_response

    payload = request.json
    token = payload['token']

    if token not in query_thread:
        if approx_query is not None and token in approx_query.queries:
            return Response(json.dumps([approx_query.results[token].get_columns(), approx_query.results[token].fetch()]), status=200, mimetype='application/json')
        else:
            return Response(json.dumps(ERROR), status=415, mimetype='application/json')
    else:
        return Response(json.dumps([query_response[token].get_columns(), query_response[token].fetch()]), status=200, mimetype='application/json')

