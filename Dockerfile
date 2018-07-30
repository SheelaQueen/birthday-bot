FROM openjdk:10-jre-slim

RUN apt-get update && apt-get install -y sudo // installs sudo
RUN useradd -m birthdaybot // creates the user
RUN adduser birthdaybot sudo // adds the user to the sudo group
USER birthdaybot // switches to the birthdaybot user

WORKDIR /etc/birthdaybot

# Copy dir to the image
COPY ./ ./

# Make gradle wrapper executable
RUN sudo chmod +x ./gradlew

# Build jar
RUN ./gradlew build

ENTRYPOINT java -jar ./Server/build/libs/BirthdayBot-all.jar