package edu.uci.ics.perpetual.api.utilities;

public class ResultCodes {
    public static final int ENTRY_DOESNT_EXIST = 11;
    public static final int FOREIGN_KEY_ERROR = 12;
    public static final int UPDATE_SUCCESS = 20;
    public static final int UPDATE_FAIL = 21;
    public static final int DELETE_SUCCESS = 30;
    public static final int DELETE_FAIL = 31;

    public static final int INTERNAL_SERVER_ERROR = -1;
    public static final int JSON_MAPPING_EXCEPTION = -2;
    public static final int JSON_PARSE_EXCEPTION = -3;


    // entity
    public static final int ENTITIES_FOUND = 100;
    public static final int ENTITIES_NOT_FOUND = 101;

    public static final int ENTITY_TYPES_FOUND = 110;
    public static final int ENTITY_TYPES_NOT_FOUND = 111;

    public static final int ENTITY_ADDED = 120;
    public static final int ENTITY_NOT_ADDED = 121; // not used
    public static final int ENTITY_TYPE_INVALID = 122;

    public static final int ENTITY_CLASSES_FOUND = 130;
    public static final int ENTITY_CLASSES_NOT_FOUND = 131;

    public static final int ENTITY_TYPE_ADDED = 140;
    public static final int ENTITY_CLASS_INVALID = 141;
    public static final int ENTITY_CLASS_WRONG = 142;
    public static final int ENTITY_SUPERTYPE_DOESNT_EXIST = 143;

    public static final int ENTITY_SUPERTYPE_INVALID = 151;

    public static final int ENTITY_PROPERTIES_RETRIEVED = 160;

    public static final int ENTITY_PROPERTY_ADDED = 170;
    public static final int ENTITY_PROPERTY_NOT_ADDED = 171;

    public static final int ENTITY_TYPE_WRONG_CLASS = 181;

    public static final int ENTITY_GEO_FOUND = 191;


    // property
    public static final int PROPERTIES_FOUND = 200;
    public static final int PROPERTIES_NOT_FOUND = 201;

    public static final int PROPERTY_ADDED = 210;
    public static final int PROPERTY_CLASS_INVALID = 211;

    public static final int PROPERTY_CLASSES_FOUND = 220;


    // device
    public static final int DEVICES_FOUND = 300;
    public static final int DEVICES_NOT_FOUND = 301;

    public static final int DEVICE_ADDED = 320;
    public static final int DEVICE_TYPE_ID_INVALID = 321;

    public static final int DEVICE_TYPES_FOUND = 330;
    public static final int DEVICE_TYPES_NOT_FOUND = 331;

    public static final int DEVICE_TYPE_ADDED = 340;
    public static final int DEVICE_CLASS_ID_INVALID = 341;

    public static final int DEVICE_CLASSES_FOUND = 350;
    public static final int DEVICE_CLASSES_NOT_FOUND = 351;

    public static final int DEVICE_TYPE_WRONG_CLASS = 361;


    // observations
    public static final int VALUES_FOUND = 400;
    public static final int VALUES_NOT_FOUND = 401;
    public static final int VALUE_OBS_ID_INVALID = 402;
    public static final int VALUE_OBS_ID_MISSING = 403;

    public static final int VALUE_ADDED = 410;
    public static final int VALUE_PAYLOAD_BAD_FORMAT = 411;
    public static final int VALUE_DEVICE_INVALID = 412;

    public static final int VALUE_TYPE_ADDED = 420;
    public static final int VALUE_PAYLOAD_ATTRIBUTE_NAME_INVALID = 421;
    public static final int VALUE_PAYLOAD_ATTRIBUTE_TYPE_INVALID = 422;

    public static final int VALUE_TYPES_FOUND = 430;
    public static final int VALUE_TYPES_NOT_FOUND = 431;

    public static final int VALUE_TABLE_NOT_DELETED = 441;

    // actuations
    public static final int ACTUATION_TYPES_FOUND = 500;
    public static final int ACTUATION_TYPES_NOT_FOUND = 501;

    public static final int ACTUATION_TYPE_ADDED = 510;


    public static String getMessage (int code) {
        switch (code) {
            case ACTUATION_TYPES_FOUND:
                return "Actuation types found.";
            case ACTUATION_TYPES_NOT_FOUND:
                return "No actuation types found.";
            case ACTUATION_TYPE_ADDED:
                return "Actuation type successfully added.";

            case VALUES_FOUND:
                return "Values found with search parameters.";
            case VALUES_NOT_FOUND:
                return "No values found with search parameters.";
            case VALUE_OBS_ID_INVALID:
                return "Invalid observation type ID.";
            case VALUE_OBS_ID_MISSING:
                return "Observation type ID not provided.";
            case VALUE_ADDED:
                return "Observation value successfully added.";
            case VALUE_PAYLOAD_BAD_FORMAT:
                return "Observation payload is incorrectly formatted.";
            case VALUE_DEVICE_INVALID:
                return "Observation device invalid.";
            case VALUE_TYPE_ADDED:
                return "obs_Value type successfully added.";
            case VALUE_PAYLOAD_ATTRIBUTE_NAME_INVALID:
                return "Payload attribute name invalid.";
            case VALUE_PAYLOAD_ATTRIBUTE_TYPE_INVALID:
                return "Payload attribute type invalid.";
            case VALUE_TYPES_FOUND:
                return "Observation types found.";
            case VALUE_TYPES_NOT_FOUND:
                return "No observation types found.";
            case VALUE_TABLE_NOT_DELETED:
                return "Cannot delete observation type when observations of this type exist.";


            case DEVICES_FOUND:
                return "Devices found with search parameters.";
            case DEVICES_NOT_FOUND:
                return "No devices found with search parameters.";
            case DEVICE_ADDED:
                return "dev_Device successfully added.";
            case DEVICE_TYPE_ID_INVALID:
                return "dev_Device type id invalid.";
            case DEVICE_TYPES_FOUND:
                return "dev_Device types found.";
            case DEVICE_TYPES_NOT_FOUND:
                return "No device types found.";
            case DEVICE_TYPE_ADDED:
                return "dev_Device type successfully added.";
            case DEVICE_CLASS_ID_INVALID:
                return "dev_Device class id invalid.";
            case DEVICE_CLASSES_FOUND:
                return "dev_Device classes found.";
            case DEVICE_CLASSES_NOT_FOUND:
                return "No device classes found.";
            case DEVICE_TYPE_WRONG_CLASS:
                return "Cannot change device type to one of a different class.";

            case PROPERTIES_FOUND:
                return "Properties found with search parameters.";
            case PROPERTIES_NOT_FOUND:
                return "No properties found with search parameters.";
            case PROPERTY_ADDED:
                return "ent_Property successfully added.";
            case PROPERTY_CLASS_INVALID:
                return "Invalid property class.";
            case PROPERTY_CLASSES_FOUND:
                return "ent_Property classes found.";

            case ENTITIES_FOUND:
                return "Entities found with search parameters.";
            case ENTITIES_NOT_FOUND:
                return "No entities found with search parameters.";
            case ENTITY_TYPES_FOUND:
                return "ent_Entity types found.";
            case ENTITY_TYPES_NOT_FOUND:
                return "No entity types found.";
            case ENTITY_ADDED:
                return "ent_Entity successfully added.";
            case ENTITY_NOT_ADDED:
                return "Could not add entity.";
            case ENTITY_TYPE_INVALID:
                return "Invalid entity type.";
            case ENTITY_TYPE_WRONG_CLASS:
                return "Entities cannot be changed to a type of different class from its original type.";
            case ENTITY_CLASSES_FOUND:
                return "ent_Entity classes found.";
            case ENTITY_CLASSES_NOT_FOUND:
                return "No entity classes found.";
            case ENTITY_TYPE_ADDED:
                return "ent_Entity type successfully added.";
            case ENTITY_CLASS_INVALID:
                return "Invalid entity class id.";
            case ENTITY_CLASS_WRONG:
                return "Cannot add entity type that is a subtype of an entity type of a different entity class.";
            case ENTITY_SUPERTYPE_DOESNT_EXIST:
                return "Provided subtypeOf is not an ID of an existing entity type.";
            case ENTITY_SUPERTYPE_INVALID:
                return "Provided subtypeOf must reference an entityType of the same class as the one being updated.";
            case ENTITY_PROPERTIES_RETRIEVED:
                return "ent_Entity type properties found.";
            case ENTITY_PROPERTY_ADDED:
                return "ent_Property successfully added to entity type.";
            case ENTITY_PROPERTY_NOT_ADDED:
                return "Could not add property to entity type.";
            case ENTITY_GEO_FOUND:
                return "Found space geo object";

            case ENTRY_DOESNT_EXIST:
                return "Entry does not exist.";
            case FOREIGN_KEY_ERROR:
                return "Foreign key error; deleting this entry will invalidate other entries. Please delete those first.";
            case UPDATE_SUCCESS:
                return "Entry updated successfully.";
            case UPDATE_FAIL:
                return "No entries updated.";
            case DELETE_SUCCESS:
                return "Entry deleted successfully.";
            case DELETE_FAIL:
                return "No entries deleted.";
            case JSON_MAPPING_EXCEPTION:
                return "JSON mapping exception.";
            case JSON_PARSE_EXCEPTION:
                return "JSON parse exception.";
            case INTERNAL_SERVER_ERROR:
            default:
                return "Internal server error.";
        }
    }
}
