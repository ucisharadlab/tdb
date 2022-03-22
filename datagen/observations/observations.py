import json
import uuid

import temperature
import wemo
import wifiap


def createObservations(dt, end, step, dataDir, outputDir):

    line = None
    finalObj = open(outputDir + 'observation.json', 'w')
    finalObj.write("[\n")

    print ("Merging Random WiFiData")

    wifiap.createWiFiObservations(dt, end, step, outputDir)
    wifiObj = open("data/wifiAPData.json")
    for line in wifiObj:
        finalObj.write(line + ",\n")
    wifiObj.close()

    print ("Merging Random WeMoData")

    wemo.createWemoObservations(dt, end, step, outputDir)
    wemoObj = open("data/wemoData.json")
    for line in wemoObj:
        finalObj.write(line + ",\n")
    wemoObj.close()

    print ("Merging Random Temperature Data")

    temperature.createTemperatureObservations(dt, end, step, outputDir)
    temperatureObj = open("data/temperatureData.json")
    for line in temperatureObj:
        finalObj.write(line + ",\n")
    temperatureObj.close()

    line = json.loads(line)
    line['id'] = str(uuid.uuid4())

    finalObj.write(json.dumps(line))
    finalObj.write("\n]")

    finalObj.close()


def createIntelligentObservations(startTime, origDays, extendDays, origSpeed, extendSpeed,
                                  origWemo, extendWemo, origTemp, extendTemp, speedScaleNoise,
                               timeScaleNoise, deviceScale, dataDir, outputDir):

    wifiap.createIntelligentWiFiObs(startTime, extendDays, extendSpeed, outputDir)
    wemo.createIntelligentWemoObs(origDays, extendDays, origSpeed, extendSpeed,
                                  origWemo, extendWemo, speedScaleNoise,
                                  timeScaleNoise, deviceScale, outputDir)
    temperature.createIntelligentTempObs(origDays, extendDays, origSpeed, extendSpeed,
                                         origTemp, extendTemp, speedScaleNoise,
                                         timeScaleNoise, deviceScale, outputDir)
    line = None
    finalObj = open(outputDir + 'observation.json', 'w')
    finalObj.write("[\n")


    wifiObj = open("data/wifiAPData.json")
    for line in wifiObj:
        finalObj.write(line + ",\n")
    wifiObj.close()


    wemoObj = open("data/wemoData.json")
    for line in wemoObj:
        finalObj.write(line + ",\n")
    wemoObj.close()

    temperatureObj = open("data/temperatureData.json")
    for line in temperatureObj:
        finalObj.write(line + ",\n")
    temperatureObj.close()

    line = json.loads(line)
    line['id'] = str(uuid.uuid4())

    finalObj.write(json.dumps(line))
    finalObj.write("\n]")

    finalObj.close()
