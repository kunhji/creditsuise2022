DROP TABLE IF EXISTS LOGEVENT;

CREATE TABLE LOGEVENT (
    logid	VARCHAR(50)	NOT NULL,
    state	VARCHAR(50),
    logtimestamp DECIMAL(30,0) ,
    logtype VARCHAR(30) ,
    hostname VARCHAR(300),
    alert VARCHAR(10),
    duration INT,
    
    PRIMARY KEY (logid)
);