FROM openjdk:10-jre-slim

# installs sudo
RUN apt-get update && apt-get install -y sudo
# creates the user
RUN useradd -m birthdaybot
# adds the user to the sudo group
RUN adduser birthdaybot sudo
# switches to the birthdaybot user
USER birthdaybot

WORKDIR /etc/birthdaybot

# Copy dir to the image
COPY ./ ./

# Make gradle wrapper executable
RUN sudo chmod +x ./gradlew

# Build jar
RUN ./gradlew build

ENTRYPOINT java -jar ./Server/build/libs/BirthdayBot-all.jar