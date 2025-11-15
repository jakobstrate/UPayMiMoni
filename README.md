# 1. firebase emulator setup

for this project to function locally a firebase auth and firestore emulator must be configured and running.

---

## 2. install firebase cli

follow the instructions for your operating system at [https://firebase.google.com/docs/cli](https://firebase.google.com/docs/cli).

### Install using npm

first check if npm and node are installed

```bash
node -v
npm -v
```

if this displays a version they are installed.
with npm firebase can be installed by running:

```bash
npm install -g firebase-tools
```

the install can be verified with:

```bash
firebase --version
```

## 3. login and initialize the emulator

1. authenticate with firebase (this only has to be done once, if you have authenticated previously skip this step)

```bash
firebase login
```

2. inside this project directory, initialize the firebase emulator

```bash
firebase init emulator
```
when prompted:
- select emulators and authentication.
- when asked which emulators to set up, select auth and firestore.
- choose ports (e.g 9099 and 8080).
- allow "enable emulator ui".
- install emulator now when prompted.
- save the config to firebase.json when prompted.

## 4. start the emulator

run the emulator locally with:

```bash
firebase emulators:start
```

you'll get an output showing where the auth emulator and emulator ui is running.
once up and running you can connect to the ui on the provided url.

## 5. configure your android app

in `app/src/main/java/com/example/upaymimoni/mainactivity.kt`
change the constants emulator_host and emulator_port to the values you have configured.
for emulator_host it should be "10.0.2.2" as this allows the android emulator to access localhost.
if you're running on a real device, replace it with your machine's local ip (e.g. "192.168.1.129").
the emulator_port should be the auth emualtor port and not the ui port.

## 6. get your google-services.json

your firebase project must be linked to the android app so that firebase knows the app's id.

steps:
1. go to the [firebase console](https://console.firebase.google.com/)
2. either create a new project or select an existing one.
3. click add app -> choose android, if the app has already been added simply select it.
4. enter the app's package name: com.example.upaymimoni.
5. download the generated google-services.json.
6. place it in the android project at: `app/src/google-services.json`.
7. sync gradle.

## 7. Go Backend Setup (For push notifications)

The backend service is responsible for sending Firebase Cloud Messagees (FCM) using your Firebase Service Account credentials.

**Prerequisites**

You must have Docker and Docker Compose installed.
This can be verified by running:

```bash
docker -v
docker-compose -v
```

### 7.1 Get Firebase Service Account Credentials
1. Go to the Firebase Console and select your project.
2. Navigate to Porject Settings -> Service account tab.
3. Click Generate new private key and download the resulting JSON file.
4. Create a directory named config at `backend/config`

```bash
mkdir -p backend/config
```

5. Rename the downloaded file to `serviceAccountKey.json` and place it inside the newly created config folder.
**DO NOT PUSH THIS FILE TO GITHUB, IT IS IN .GITIGNORE BUT PLEASE ENSURE THAT YOU DO NOT PUSH IT**

### 7.2 Spin Up the Backend

There is a docker-compose file at  `backend/docker-compose.yml` configured to host on port 3000, and load the private key.
Run the service with:

```bash
docker compose up --build -d
```

### 7.3

Verify the server started correctly with:

```bash
docker logs upaymimoni-backend
```

### 7.4 Android API Configuration
Update the API in client in the app to point towards the server.
This file is found at `app/src/main/java/com/example/upaymimoni/di/ApiModule.kt

- Change baseUrl to where the server is running.
