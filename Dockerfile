FROM fabric8/java-alpine-openjdk8-jdk

WORKDIR /deployments

RUN curl -o fitnesse-standalone.jar 'http://fitnesse.org/fitnesse-standalone.jar?responder=releaseDownload&release=20180127'

EXPOSE 8080

RUN addgroup -S fitnesse && adduser -S -G fitnesse fitnesse 

COPY target/guarabot-*.jar /deployments/guarabot.jar

COPY target/fitnesse/FitNesseRoot /deployments/FitNesseRoot

RUN chown fitnesse:fitnesse /deployments -R

USER fitnesse

CMD ["java", "-jar","fitnesse-standalone.jar","-p","8080"]