### Note to user
We from the AxonFramework Team are currently undertaking a rigorous adjustment to this application to ensure that you will have an as up-to-date set of standards on how to set up an Axon Framework application. 
Due too some circumstances, these are live changes for everybody to see. This does mean that the master branch might be broken at times. 
We are extremely sorry for any inconvenience caused through this and are remediating the issue as soon as possible. Kind regards, the AxonFramework Team.

*Note Update:* 
Any issues which caused the application to not be able to run/build/start-up should currently be resolved.
If any are still present, please add an issue to the issues list so that we can prioritize and resolve the problem at hand.

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