# Ktor Server Template
This template features an [Ktor](https://ktor.io/) Http server with MongoDB Integration.
As this Template is specified for my personal needs, this may contain to much code for your Project. Read below to see what is included.

## MongoDB
This template evovles kotlinx.coroutines. This is why we have an coroutine based MongoDB Client integrated.  
For development purposes, there is an `docker-compose.yml` file. You can spin up an sample Database Server to test your Application.  
To run the database, you require [**Docker**](https://www.docker.com/) with [**Docker-Compose**](https://docs.docker.com/compose/). Then, open up an new Terminal Window and navigate into the project directory.
Execute by running:
```bash
docker-compose up
```
The database only runs locally on your machine, thats why there is not really security measurements build in. The Root User is auto. set up. **DO NOT USE THIS IN PRODUCTION**.  

## Gradle
When taking a look into our `buildSrc` directory, you may find an task called `runWithEnv`. To run the Application without needing to set System-Wide Environment variables, use this Task. 
By executing, an JavaExec task will spawn and load the `.env` file into the freshly crafted thread.

This template also features an BuildConfig to use Build Variables later in the Code. By default this features three diffrent Fields.  
- GIT_SHA: The hash of where the current HEAD is
- GIT_BRANCH: Which branch is currently active
- VERSION: The App Version field

## Misc Route
There is already an basic `/misc` route to provide basic Information.  
In case you do not want this info to be public, just delete this part of the Code. 
By default this will provide the three BuildConfig fields as well as the state of the **debug** mode.  

## Testing
This is also equipped with JUnit 5 testing Framework. Some tests are already written for stuff like e.g. Database Connection or the `/misc` route.  
In case you also want to write tests for your app, you can start where this template left off.

## Environment
Using [envconf](https://github.com/DRSchlaubi/stdx.kt/tree/main/envconf), there is easy access to our environment. This is used for easy Dockerization. The only one required is `MONGO_URL`.
Optional there are `HOST` and `PORT` for our Web Server and `DEBUG` to configure our Log Level.
