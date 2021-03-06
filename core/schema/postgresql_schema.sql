

CREATE TABLE MetadataType (
    name VARCHAR(40) NOT NULL,
    attributes TEXT NOT NULL,

    PRIMARY KEY (name)
);

CREATE TABLE RawType (
	name VARCHAR(40) NOT NULL,
	attributes TEXT NOT NULL,

	PRIMARY KEY (name)
);

CREATE TABLE DataSourceType (
	name VARCHAR(40) NOT NULL,
	paramList TEXT NOT NULL,
	returnType TEXT NOT NULL,
	sourceFunctions TEXT NOT NULL,

	PRIMARY KEY (name)
);

CREATE TABLE DataSource (
    id SERIAL,
	sourceDescription TEXT NOT NULL,
	typeName TEXT NOT NULL,
	functionPath TEXT NOT NULL,
	functionParams TEXT NOT NULL,

	PRIMARY KEY (id)
);

CREATE TABLE EnrichmentTag (
	name VARCHAR(40) NOT NULL,
	type VARCHAR(40) NOT NULL,
    rawType VARCHAR(40) NOT NULL,
	PRIMARY KEY (name)
);

CREATE TABLE TaggingFunction (
	functionName VARCHAR(40) NOT NULL,
	sourceType TEXT NOT NULL,
	paramList TEXT NOT NULL,
	returnTag TEXT NOT NULL,
	cost INT NOT NULL,
	quality FLOAT NOT NULL,
    path VARCHAR(300),

	PRIMARY KEY (functionName)
);

CREATE TABLE Relation (
    parent VARCHAR(40) NOT NULL,
 	child VARCHAR(40) NOT NULL,

 	PRIMARY KEY (parent, child)
);

CREATE TABLE CachingRules (
    rule TEXT,
    version INT
);

CREATE TABLE CachingVersions (
    version INT,
    creationTime TIMESTAMP
);

CREATE TABLE AcquisitionRequests (
    requestId INT,
    acquisitionName VARCHAR(60),
    dataSourceId INT,
    acquisitionFunctionPath VARCHAR(300),
    acquisitionFunctionParameters TEXT,
    rawTypeScheme TEXT,
    creationTime TIMESTAMP,
    updateTime TIMESTAMP,
    startTime TIMESTAMP,
    endTime TIMESTAMP,
    frequency INT,
    status VARCHAR(30),

    PRIMARY KEY(requestId)
);


CREATE TABLE Tables (
    name VARCHAR(40) NOT NULL,
    attributes TEXT NOT NULL,
    observables TEXT NOT NULL,
    PRIMARY KEY (name)
);