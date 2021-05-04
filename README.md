# Classroom Application

## Technologies
* Java
* Primefaces 10.0
* JSF 2.3
* WebSockets
* TomCat 9.0.41

## Description
#### In the Application you can:
* login to the class
* logout from the class
* raise their hands up
#### The WebSockets technology can send notification when:
* new student signs in to the class
* the student signs out of the class
* the student raises his hand up or down

## Steps to run this project:
1. Clone this Git repository
2. Navigate to the folder `classroom`
3. Run Apache Tomcat v.9+
4. Change `properties` in `pom.xml` for TomCat and write command to the terminal `mvn package tomcat:deploy`
   
   OR
   
   Write command `mvn package` go to http://localhost:8080/ choose `Manager App` and deploy classroom.war
5. Visit http://localhost:8080/classroom/ in your browser and try it
