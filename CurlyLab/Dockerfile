FROM thyrlian/android-sdk:latest

ENV ANDROID_HOME=/opt/android-sdk
ENV PATH=$ANDROID_HOME/cmdline-tools/latest/bin:$ANDROID_HOME/platform-tools:$PATH

ENV JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64
ENV PATH="$JAVA_HOME/bin:$PATH"

WORKDIR /app
COPY . .

RUN echo "sdk.dir=/opt/android-sdk" > local.properties

RUN yes | sdkmanager --licenses && ./gradlew assembleDebug