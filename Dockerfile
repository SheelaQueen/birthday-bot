FROM openjdk:10-jre

WORKDIR /etc/birthdaybot

# Copy dir to the image
COPY ./ ./

# Make gradle wrapper executable
RUN sudo chmod +x ./gradlew

# Build jar
RUN ./gradlew build

ENTRYPOINT java -jar ./Server/build/libs/BirthdayBot-all.jar