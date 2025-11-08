Hello!
This is payment integration of Esewa in spring boot

First of all go through the esewa documentation
https://developer.esewa.com.np/

You will have the knowledge regarding Transaction Flow, Integration, Token, Status Check and Credentials
For credentials:
https://developer.esewa.com.np/pages/Epay#credentials&urls


Guidance of my code

1. pom.xml file
    this includes all required dependencies like Web, Gson ...

2.resources/application.properties
    the fields of ESewaConfig.java class is initialized here.

3.resources/static folder
    index.html - ui for sending post request (to start payment)
    success.html - ui that is redirected after successful payment
    failure.html - ui that is redirected after payment is failed

4.classes
    ESewaIntegrationApplication.java - main class with bean for gson (creates object for reusablilty)
    ESewaConfig.java -  contains special field required to esewa while carrying out operation
                        contains methods for generating transactionuuid, signature , verifying signature
    GlobalExceptionHandler - for handling exception that occurs throughout the program
    ESewa - entity that contains fields required for sending request to esewa,
            that contains fields that is sent by esewa as a response
    ESewaController - contains end points , send form to esewa from here with auto submission
    ESewaService - methods that is used to parse and validate the response received from esewa

