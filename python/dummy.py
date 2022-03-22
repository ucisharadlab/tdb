import time
from sensors import Sensor

OB_FUNCS = {
    1: "gps_to_building",
    2: "wifi_to_region",
    3: "camera_to_room",
    4: "beacon_to_room",
    5: "gps_to_building_occupancy",
    6: "wifi_to_region_occupancy",
    7: "camera_to_room_occupancy",
    8: "wemo_to_energy",
    9: "hvac_to_energy",
    10: "fitbit_to_vitals"
}

FUNC_COST = {
    1: 10,
    2: 5,
    3: 9,
    4: 14,
    5: 10,
    6: 10,
    7: 5,
    8: 9,
    9: 14,
    10: 10,
}

SENSOR = Sensor()


def gps_to_building(sensors, start, end):
    sdata = SENSOR.fetch_data("gps", sensors, start, end)
    time.sleep(1/100)


def wifi_to_region(sensors, start, end):
    sdata = SENSOR.fetch_data("wifi", sensors, start, end)
    time.sleep(1/100)


def camera_to_room(sensors, start, end):
    sdata = SENSOR.fetch_data("camera", sensors, start, end)

    time.sleep(1/100)


def beacon_to_room(sensors, start, end):
    sdata = SENSOR.fetch_data("beacon", sensors, start, end)

    time.sleep(1/100)


def gps_to_building_occupancy(sensors, start, end):
    sdata = SENSOR.fetch_data("gps", sensors, start, end)

    pass


def wifi_to_region_occupancy(sensors, start, end):
    sdata = SENSOR.fetch_data("wifi", sensors, start, end)

    pass


def camera_to_room_occupancy(sensors, start, end):
    sdata = SENSOR.fetch_data("camera", sensors, start, end)

    pass


def wemo_to_energy(sensors, start, end):
    sdata = SENSOR.fetch_data("wemo", sensors, start, end)

    time.sleep(1/100)


def hvac_to_energy(sensors, start, end):
    sdata = SENSOR.fetch_data("hvac", sensors, start, end)
    time.sleep(1/100)


def num_plans():
    pass


