Spring Boot Microservices Project
(Eureka Serivce, User Service, Activity Service, AI Service to interface Google Gemini)

About the project
This project is based Spring Boot Microservice to get Google Gemini AI to generaet the Activity Recommendation.
Service invovled
  Eureka Service: Service Discovery  (Spring boot starter, Web, Eureka Server)
  User Service : To maintian the User info (Spring boot starter, Web, Eureka Client, Oracle DB)
  Activity Service: To maintina the Activity of each user then produce the message into Rabbit MQ (Spring boot starter, Web, AMQB - Rabbit MQ,  Eureka Client, Mongo DB)
  AI Service: To consume the Activity Message to interface to Google Gemini to generae the Remommendation (Spring boot starter, Web, AMQB - Rabbit MQ,  Eureka Client, Mongo DB)


