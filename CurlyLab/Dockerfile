FROM thyrlian/android-sdk:latest AS build

WORKDIR /app
COPY . .

RUN yes | sdkmanager --licenses && \
    ./gradlew assembleDebug

FROM budtmo/docker-android-x86-11.0

COPY --from=build /app/app/build/outputs/apk/debug/app-debug.apk /apk/CurlyLab.apk

CMD ["wait-for-emulator", "sh", "-c", "adb install /apk/CurlyLab.apk && am start -n your.package.name/.MainActivity"]
