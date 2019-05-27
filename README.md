Ad Enricher
===========
Steps to build and run:

* cd *project_directory*
* mvn clean install -Dspring.config.location=*config_location_directory* -Dspring.profiles.active=dev. This will run the unit tests and integration tests. Please note that you should have application.properties and application-dev.properties under same folder.
* java -jar target/app-0.0.1-SNAPSHOT.jar -Dspring.config.location=*config_location_directory* -Dspring.profiles.active=dev

Properties file contents should be like below.

1. application.properties should contain the below entries.

    spring.application.name = Ad Enricher Dev<br/>
    spring.profiles = dev, prod<br/>
    logging.level.org.springframework.web=INFO<br/>
    logging.file=logs/ad-enricher-logging.log<br/>
    management.endpoints.web.exposure.include=httptrace,info,health,monitoring<br/>
    server.tomcat.max-threads = 0<br/>
    javamelody.enabled = true<br/>
    javamelody.spring-monitoring-enabled = true<br/>
    management-endpoint-monitoring-enabled = true<br/>
    management.server.port = 8081<br/>

2. application-dev.properties should contain the following entries with valid values.

    adApp.maxMind.userId = 432432423<br/>
    adApp.maxMind.key = 432434234<br/>
    adApp.resourceWithPort = %s:%s%s<br/>
    adApp.allowedCountry = US<br/>
    adApp.siteServer.host = http://somehost<br/>
    adApp.siteServer.port = 6500<br/>
    adApp.siteServer.publisherPath = /api/publishers/find<br/>
    adApp.siteServer.demographicsPath = /api/sites/%s/demographics<br/>

Below are some helpful endpoints.

* Check the status of the service: http://localhost:8081/actuator/health
* Check details of last 100 requests: http://localhost:8081/actuator/httptrace
* Helpful information about http requests, database hits, etc. : http://localhost:8080/monitoring

Resource Endpoints
1. Hit http://localhost:8080/ads/enrich/v1/ to enrich an ad request. This will return an enriched request with details. It will return HTTP 403 with some detail message if the input IP address is not from USA or if it can't be resolved. It will return HTTP 404 response if publisher information can't be retrieved. All other failures except validation errors(HTTP 400) will be ignored.

2. If a publisher information is updated, hit http://localhost:8080/sites/publisher/cache/remove/v1/ to delete the cache data of relevant site. The payload accepts site id as the input.
