# Archived project

This project has been discontinued and is not being updated to latest standards anymore. There are several sample applications around that show the usage of Axon Framework that are kept up-to-date continuously.

A few examples (at the time of writing):
- https://github.com/AxonIQ/hotel-demo
- https://github.com/fraktalio/restaurant-demo
- https://github.com/abuijze/bike-rental-demo
- https://github.com/abuijze/bike-rental-extended

---

This is a sample application to demonstrate the possibilities of the Axon framework in a high load environment. We have chosen to create a Trader application. All you need to run it is java and maven.


Initial setup
-------------
- Make sure you have java installed
http://www.java.com/download/

Running the sample
------------------
- First you need to download the source code, if you are reading this file on your local machine you already have downloaded the source code. If you are on the main page of the Github project, you can easily find a url to clone the repository or to download a zip with all the sources.

* Maven
- Step into the main folder of the project
> ./mvnw tomcat7:run
- Browse to http://localhost:8080 and you should see the user accounts that you can use to login.
(You can always refresh the data by calling /data/init on the application)

More documentation
----------------------
We are documenting the sample on the wiki of the github project.
https://github.com/AxonFramework/Axon-trader/wiki
