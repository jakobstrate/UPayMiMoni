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
