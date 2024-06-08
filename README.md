# redis-pojo


### Database
* Please configure application-local.properties for database credentials
* Here are the default db configurations
* database name = appdb
* username = appuser
* password = secret 
```shell
$ psql --host=localhost --username=appuser --dbname=appdb
```

### Format code
```shell
$ ./gradlew spotlessCheck
```

```shell
$ ./gradlew spotlessApply
```
### Run PMD

```shell
$ ./gradlew pmdMain
```
* Go to build/reports/pmd/main.html and see the result
### Run spotbugs

```shell
$ ./gradlew spotbugsMain
```
* Go to build/reports/spotbugs/main.html and see the result
### Run tests

```shell
$ ./gradlew clean build
```
* This will create a JACOCO code coverage report under build/reports/jacoco/test/html/index.html
* By default, jacoco_min_coverage_required property is reset to 0.0. please change it accordingly.
### Run SonarQube
* Configure SonarQube by updating the properties in the 'sonar-project.properties' file. Tailor the following settings to match your SonarQube instance.
  * sonar.login
  * sonar.host.url
```shell
$ ./gradlew sonar 
```
### Run locally

```shell
$ docker-compose -f docker/docker-compose.yml up -d
$ ./gradlew bootRun -Plocal
```

### Configure dependency version
* Open gradle.properties and customize specific versions

### Useful Links
* Swagger UI: http://localhost:8080/swagger-ui.html
* Actuator Endpoint: http://localhost:8080/actuator


