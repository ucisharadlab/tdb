package edu.uci.ics.perpetual.api.formula;

public class Interface {

    /*public static List<Obs> searchObservations (Integer sensorId, Long startTimestamp, Long endTimestamp,
                                                int observationTypeId) {
        MultivaluedHashMap<String, Object> queryParams = new MultivaluedHashMap<>();
        queryParams.add("maxTimestamp", endTimestamp);
        queryParams.add("minTimestamp", startTimestamp);
        queryParams.add("sensorId", sensorId);
        Response res = RevisedObservationLogic.get(Main.getCon(), observationTypeId, "id", "asc", "id", "asc", 10000000, 0, queryParams);
        obs_SearchRes searchRes;
        try {
            searchRes = (obs_SearchRes) res.getEntity();
        } catch (ClassCastException e) {
            return null;
        }
        return searchRes.getValues();
    }

    public static int addObservation (int observationTypeId, Integer deviceId, Long timestamp, Map<String, Object> payload) {
        Response res = ObservationLogic.add(Main.getCon(), observationTypeId, timestamp, payload, deviceId);
        obs_AddRes addRes;
        try {
            addRes = (obs_AddRes) res.getEntity();
        } catch (ClassCastException e) {
            return -1;
        }
        return addRes.getId();
    }

    public static List<obs_ObsType> getObservationTypes () {
        Response res = ObservationLogic.getTypes(Main.getCon());
        ArrayList<obs_ObsType> result = null;
        obs_GetTypeRes getTypeRes;
        try {
            getTypeRes = (obs_GetTypeRes) res.getEntity();
        } catch (ClassCastException e) {
            return result;
        }
        result = getTypeRes.getTypes();
        return result;
    }*/

}
