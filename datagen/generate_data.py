import random
import pandas as pd
from sqlalchemy import create_engine

# Metadata
NUM_OCCUPANTS = 100
NUM_OCCUPANT_TYPES = 5
NUM_BUILDINGS = 10
NUM_REGIONS = 100
NUM_ROOMS = 200
NUM_ROOM_TYPES = 20
TIME = 100000
TIME_GAP = 10

# Sensors
NUM_WIFI = 100
NUM_WEMO = 150
NUM_HVAC = 100
NUM_CAMERA = 50
NUM_GPS = NUM_OCCUPANTS
NUM_BLE = NUM_OCCUPANTS


def sensor_type():
    file = open("data/sensor_type.csv", "w")
    header = ["stid", "name"]
    data = [
        (1, "WiFi"),
        (2, "Wemo"),
        (3, "Hvac"),
        (4, "Camera"),
        (5, "GPS"),
        (6, "BLE")
    ]
    file.write(",".join(header)+"\n")
    for line in data:
        file.write(",".join(map(str, line)) + "\n")


def observation_type():
    file = open("data/observation_type.csv", "w")
    header = ["otid", "name"]
    data = [
        (1, "WiFi Connection"),
        (2, "Energy Usage"),
        (3, "Temperature"),
        (4, "Images"),
        (5, "GPS Reading"),
        (6, "Contact")
    ]
    file.write(",".join(header)+"\n")
    for line in data:
        file.write(",".join(map(str, line)) + "\n")


def sensor():
    file = open("data/sensor.csv", "w")
    header = ["sid", "stid", "otid", "name", "movement_type", "location_id", "owner_id"]
    file.write(",".join(header)+"\n")

    regions = list(range(NUM_BUILDINGS+1, NUM_BUILDINGS+NUM_REGIONS+1))
    rooms = list(range(NUM_BUILDINGS+NUM_REGIONS+1, NUM_BUILDINGS+NUM_REGIONS+NUM_ROOMS+1))

    index = 1
    for i in range(1, NUM_WIFI+1):
        line = (index, 1, 1, "wifi-{}".format(index), "static", regions[i-1], 0)
        file.write(",".join(map(str, line)) + "\n")
        index += 1

    for i in range(1, NUM_WEMO+1):
        line = (index, 2, 2, "wemo-{}".format(index), "static", rooms[i-1], 0)
        file.write(",".join(map(str, line)) + "\n")
        index += 1

    for i in range(1, NUM_HVAC+1):
        line = (index, 3, 3, "hvac-{}".format(index), "static", regions[i-1], 0)
        file.write(",".join(map(str, line)) + "\n")
        index += 1

    for i in range(1, NUM_CAMERA+1):
        line = (index, 4, 4, "camera-{}".format(index), "static", rooms[i-1], 0)
        file.write(",".join(map(str, line)) + "\n")
        index += 1

    for i in range(1, NUM_GPS+1):
        line = (index, 5, 5, "GPS-{}".format(index), "dynamic", 0, random.randint(1, NUM_OCCUPANTS))
        file.write(",".join(map(str, line)) + "\n")
        index += 1

    for i in range(1, NUM_BLE+1):
        line = (index, 6, 6, "ble-{}".format(index), "dynamic", 0, random.randint(1, NUM_OCCUPANTS))
        file.write(",".join(map(str, line)) + "\n")
        index += 1


def WiFi():
    wifi = open("data/wifi.csv", "w")
    header = ["oid", "sid", "time", "mac"]
    wifi.write(",".join(header) + "\n")

    index = 1
    for i in range(1, TIME, 10):
        users = random.sample(list(range(1, NUM_OCCUPANTS)), int(0.05*NUM_OCCUPANTS))
        for j in users:
            line = [index, str(random.randint(1, NUM_WIFI)), i, j]
            wifi.write(",".join(map(str, line)) + "\n")
            index += 1


def Wemo():
    wemo = open("data/wemo.csv", "w")
    header = ["oid", "sid", "time", "volt"]
    wemo.write(",".join(header) + "\n")

    index = 1
    ind =  NUM_WIFI
    for i in range(1, TIME, 10):
        wemos = random.sample(list(range(1, NUM_WEMO)), int(0.5*NUM_WEMO))
        for j in wemos:
            line = [index, ind+j, i, random.randint(1, 100)]
            wemo.write(",".join(map(str, line)) + "\n")
            index += 1


def Hvac():
    hvac = open("data/hvac.csv", "w")
    header = ["oid", "sid", "time", "temperature"]
    hvac.write(",".join(header) + "\n")
    index = 1

    ind =  NUM_WIFI + NUM_WEMO
    for i in range(1, TIME, 10):
        hvacs = random.sample(list(range(1, NUM_HVAC)), int(0.5*NUM_HVAC))
        for j in hvacs:
            line = [index, ind+j, i, random.randint(1, 100)]
            hvac.write(",".join(map(str, line)) + "\n")
            index += 1


def Camera():
    camera = open("data/camera.csv", "w")
    header = ["oid", "sid", "time", "image"]
    camera.write(",".join(header) + "\n")
    index = 1

    ind =  NUM_WIFI + NUM_WEMO + NUM_HVAC
    for i in range(1, TIME, 10):
        cameras = random.sample(list(range(1, NUM_CAMERA)), int(0.5*NUM_CAMERA))
        for j in cameras:
            line = [index, ind+j, i, random.randint(1, 100)]
            camera.write(",".join(map(str, line)) + "\n")
            index += 1


def GPS():
    gps = open("data/gps.csv", "w")
    header = ["oid", "sid", "time", "lat", "long"]
    gps.write(",".join(header) + "\n")
    index = 1

    ind =  NUM_WIFI + NUM_WEMO + NUM_HVAC + NUM_CAMERA
    for i in range(1, TIME, 10):
        users = random.sample(list(range(1, NUM_OCCUPANTS)), int(0.5*NUM_OCCUPANTS))
        for j in users:
            line = [index, ind+j, i, random.randint(1, 100), random.randint(1, 100)]
            gps.write(",".join(map(str, line)) + "\n")
            index += 1


def BLE():
    ble = open("data/ble.csv", "w")
    header = ["oid", "sid", "time", "contact_mac"]
    ble.write(",".join(header) + "\n")
    index = 1

    ind =  NUM_WIFI + NUM_WEMO + NUM_HVAC + NUM_CAMERA + NUM_GPS
    for i in range(1, TIME, 10):
        users = random.sample(list(range(1, NUM_OCCUPANTS)), int(0.05*NUM_OCCUPANTS))
        for j in users:
            line = [index, str(ind+random.randint(1, NUM_OCCUPANTS)), i, j]
            ble.write(",".join(map(str, line)) + "\n")
            index += 1


def location():
    file = open("data/level.csv", "w")
    header = ["lvid", "name", "parent_lvid"]
    file.write(",".join(header) + "\n")
    data = [
        (0, "Campus", -1),
        (1, "Building", 0),
        (2, "Region", 1),
        (3, "Room", 2)
    ]
    for line in data:
        file.write(",".join(map(str, line)) + "\n")

    file = open("data/location.csv", "w")
    header = ["lid", "name", "parent_lid", "capacity", "type"]
    file.write(",".join(header) + "\n")

    file.write(",".join(['0', 'UCI', '-1', '10000', 'Campus']) + "\n")

    index = 1
    for i in range(1, NUM_BUILDINGS+1):
        line = (index, "Building_{}".format(i), 0, random.randint(100, 1000), "Building")
        file.write(",".join(map(str, line)) + "\n")
        index += 1

    regions = []
    for i in range(1, NUM_BUILDINGS+1):
        for j in range(1, NUM_REGIONS//NUM_BUILDINGS + 1):
            line = (index, "Region_{}_{}".format(i, j), i, random.randint(10, 100), "Region")
            file.write(",".join(map(str, line)) + "\n")
            regions.append(index)
            index += 1

    for i in regions:
        for j in range(1, NUM_ROOMS // NUM_REGIONS + 1):
            line = (index, "Room_{}_{}".format(i, j), i, random.randint(10, 100),
                    "Room")
            file.write(",".join(map(str, line)) + "\n")
            index += 1


def occupants():
    occupants = open("data/occupants.csv", "w")
    header = ["id", "name", "type"]
    occupants.write(",".join(header) + "\n")
    for i in range(1, NUM_OCCUPANTS + 1):
        line = (i, "Occupant_{}".format(i),
                "Type_{}".format(random.randint(1, NUM_OCCUPANT_TYPES)))
        occupants.write(",".join(map(str, line)) + "\n")

    occupants_location = open("data/occupants_location.csv", "w")
    header = ["eid", "st", "et", "value"]
    occupants_location.write(",".join(header) + "\n")
    for i in range(1, NUM_OCCUPANTS + 1):
        for j in range(1, TIME, TIME_GAP):
            line = (i, j, j+TIME_GAP-1, -1)
            occupants_location.write(",".join(map(str, line)) + "\n")

    occupants_vitals = open("data/occupants_vitals.csv", "w")
    header = ["eid", "st", "et", "value"]
    occupants_vitals.write(",".join(header) + "\n")
    for i in range(1, NUM_OCCUPANTS + 1):
        for j in range(1, TIME, TIME_GAP):
            line = (i, j, j + TIME_GAP - 1, -1)
            occupants_vitals.write(",".join(map(str, line)) + "\n")

    occupants_contact = open("data/occupants_contact.csv", "w")
    header = ["eid", "st", "et", "value"]
    occupants_contact.write(",".join(header) + "\n")
    for i in range(1, NUM_OCCUPANTS + 1):
        for j in range(1, TIME, TIME_GAP):
            line = (i, j, j + TIME_GAP - 1, -1)
            occupants_contact.write(",".join(map(str, line)) + "\n")


def rooms():
    rooms = open("data/rooms.csv", "w")
    header = ["id", "name", "type"]
    rooms.write(",".join(header) + "\n")

    regions = list(range(NUM_BUILDINGS+1, NUM_BUILDINGS+NUM_REGIONS+1))

    index = 1
    for i in regions:
        for j in range(1, NUM_ROOMS // NUM_REGIONS + 1):
            line = (index, "Room_{}_{}".format(i, j),
                    "Room_{}".format(random.randint(1, NUM_ROOM_TYPES)))
            rooms.write(",".join(map(str, line)) + "\n")
            index += 1

    rooms_occupancy = open("data/rooms_occupancy.csv", "w")
    header = ["eid", "st", "et", "value"]
    rooms_occupancy.write(",".join(header) + "\n")
    for i in range(1, NUM_ROOMS + 1):
        for j in range(1, TIME, TIME_GAP):
            line = (i, j, j+TIME_GAP-1, -1)
            rooms_occupancy.write(",".join(map(str, line)) + "\n")

    rooms_energy = open("data/rooms_energy.csv", "w")
    header = ["eid", "st", "et", "value"]
    rooms_energy.write(",".join(header) + "\n")
    for i in range(1, NUM_ROOMS + 1):
        for j in range(1, TIME, TIME_GAP):
            line = (i, j, j + TIME_GAP - 1, -1)
            rooms_energy.write(",".join(map(str, line)) + "\n")


def coverage_functions():
    file = open("data/coverage_function.csv", "w")
    header = ["cfid", "name", "stid", "otid"]
    file.write(",".join(header) + "\n")
    data = [
        (1, "wifi_coverage", 1, 1),
        (2, "wemo_coverage", 2, 2),
        (3, "hvac_coverage", 3, 3),
        (4, "camera_coverage", 4, 4),
        (5, "gps_coverage", 5, 5),
        (6, "ble_coverage", 6, 6),
    ]
    for line in data:
        file.write(",".join(map(str, line)) + "\n")


def observing_functions():
    file = open("data/property.csv", "w")
    header = ["eid", "pid", "entity_name", "property_name"]
    file.write(",".join(header) + "\n")
    data = [
        (1, 1, "occupants", "location"),
        (1, 2, "occupants", "contact "),
        (1, 3, "occupants", "vitals"),
        (2, 3, "rooms", "occupancy"),
        (2, 4, "rooms", "energy"),
    ]
    for line in data:
        file.write(",".join(map(str, line)) + "\n")

    file = open("data/observing_function.csv", "w")
    header = ["fid", "name", "cfid", "property"]
    file.write(",".join(header) + "\n")
    data = [
        (1, "wifi_to_region_loc", 1, 1),
        (2, "wemo_to_energy", 2, 3),
        (3, "hvac_to_energy", 3, 3),
        (4, "camera_to_room_location", 4, 1),
        (5, "gps_to_building_location", 5, 1),
        (6, "ble_to_contact", 6, 4),
    ]
    for line in data:
        file.write(",".join(map(str, line)) + "\n")


def load_data():
    engine = create_engine('postgresql://sdb:sdb@localhost:5432/tdb')

    # df = pd.read_csv('data/sensor_type.csv')
    # df.to_sql("sensor_type", engine)
    #
    # df = pd.read_csv('data/observation_type.csv')
    # df.to_sql("observation_type", engine)
    #
    # df = pd.read_csv('data/sensor.csv')
    # df.to_sql("sensor", engine)
    #
    # df = pd.read_csv('data/wifi.csv')
    # df.to_sql("wifi", engine)
    #
    # df = pd.read_csv('data/wemo.csv')
    # df.to_sql("wemo", engine)
    #
    # df = pd.read_csv('data/hvac.csv')
    # df.to_sql("hvac", engine)
    #
    # df = pd.read_csv('data/camera.csv')
    # df.to_sql("camera", engine)
    #
    # df = pd.read_csv('data/gps.csv')
    # df.to_sql("gps", engine)
    #
    # df = pd.read_csv('data/ble.csv')
    # df.to_sql("ble", engine)

    #
    # df = pd.read_csv('data/level.csv')
    # df.to_sql("level", engine)
    #
    # df = pd.read_csv('data/location.csv')
    # df.to_sql("location", engine)
    #
    #
    # df = pd.read_csv('data/occupants.csv')
    # df.to_sql("occupants", engine)

    # df = pd.read_csv('data/occupants_location.csv')
    # df.to_sql("occupants_location", engine)

    df = pd.read_csv('data/occupants_vitals.csv')
    df.to_sql("occupants_vitals", engine)

    df = pd.read_csv('data/occupants_contact.csv')
    df.to_sql("occupants_contact", engine)


    # df = pd.read_csv('data/rooms.csv')
    # df.to_sql("rooms", engine)

    # df = pd.read_csv('data/rooms_occupancy.csv')
    # df.to_sql("rooms_occupancy", engine)

    # df = pd.read_csv('data/rooms_energy.csv')
    # df.to_sql("rooms_energy", engine)
    #
    #
    # df = pd.read_csv('data/coverage_function.csv')
    # df.to_sql("coverage_function", engine)
    #
    # df = pd.read_csv('data/property.csv')
    # df.to_sql("property", engine)
    #
    # df = pd.read_csv('data/observing_function.csv')
    # df.to_sql("observing_function", engine)


if __name__ == "__main__":
    # sensor_type()
    # observation_type()
    # sensor()
    # WiFi()
    # Wemo()
    # Hvac()
    # Camera()
    # GPS()
    # BLE()
    #
    # location()
    #
    # occupants()
    occupants()
    #
    # coverage_functions()
    # observing_functions()
    load_data()