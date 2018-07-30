FROM 4.9.0-jre8-slim

WORKDIR /etc/birthdaybot

# Copy dir to the image
COPY ./ ./

# Build jar
RUN gradle build

ENTRYPOINT java -jar ./Server/build/libs/BirthdayBot-all.jar