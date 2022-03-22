
"""Sensor data processing functions
"""

def gps2location(gpsdata):
    """Takes GPS data as input and returns location of a person"""
    ...
    return location

def bluetooth2location(bledata):
    """Takes GPS data as input and returns location of a person"""
    ...
    return location

def wifi2location(wifidata):
    """Takes WiFi data as input and returns location of a person"""
    ...
    return location

"""function to find sensor types"""

def get_sensor_types():
    """return all sensor types"""
    ...
    return [Sensor Types]

"""function to get sensor of a particular types"""

def get_sensor(sensor_type, location):
    """get sensor of a particular type situated at a particular location"""
    ...
    return [Sensor]

def get_sensor(sensor_type, owner):
    """get sensor of a partiucal type owned by a particular person"""
    ...
    return [Sensor]

"""Functions to get data from sensors"""

def fetch_sensor_data(sensor_id, interval):
    """get data from a sensor in a particular interval"""
    ...
    return [(time, payload), ...]

def fetch_sensor_data(sensor_list, interval):
    """get data from all sensors in a list a particular interval"""
    ...
    return [(sensor, time, payload), ...]

"""Implementing query that finds out all the people who spent at least 15 mins with a particular person
in a particular time interval"""
def execute_query(person, interval):
    sensor_types = get_sensor_types()

    """remove all those sensor types that can not be used for localization"""
    for entry in sensor_types:
        if not check(entry, "localization"):
            del sensor_types[entry]

    """Fetch applicable sensors for each sensor type"""
    for entry in sensor_types:
        if entry == "GPS":
            person_gps = get_sensor("GPS", person)
        elif entry == "WiFi":
            wifi_aps = get_sensor("WiFi", "ALL")
        elif entry == "BLE":
            bles = get_sensor("BLE", "ALL")

    """Filter sensor who were down during the time interval"""
    for ap in wifi_aps:
        if not available(ap, interval):
            del wifi_aps[ap]

    """Fetch data from sensors"""
    gps_data =  fetch_sensor_data(person_gps, interval)
    wifi_data =  fetch_sensor_data(wifi_aps, interval)
    ble_data =  fetch_sensor_data(bles, interval)

    """Run sensor processing code to get location information"""
    gps_result = gps2location(gps_data)
    wifi_result = wifi2location(wifi_data)
    ble_result = ble2location(ble_data)

    """Merge the location information from multiple processing codes to get final set of locations of the person"""
    possible_locations = optimize(gps_result, wifi_result, ble_result)

    """After finding locations the particular person was in find all other people were there at the same location"""

    wifi_aps2 = get_sensor("Wifi", possible_locations)
    bles2 = get_sensor("BLE", possible_locations)

    wifi_data2 = fetch_sensor_data(wifi_aps, interval)
    ble_data2 = fetch_sensor_data(bles, interval)

    """Run sensor processing code to get location information"""
    wifi_result2 = wifi2location(wifi_data2)
    ble_result2 = ble2location(ble_data2)

    person_at_same_location = optimize(wifi_result2, ble_result2)


if __name__ == "__main__":
    execute_query("Mary", (10, 100))