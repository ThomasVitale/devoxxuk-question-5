# Devoxx UK - Question 5

This is question 5 of a game intended to demonstrate serverless and event-driven features on Kubernetes, using Knative and CloudEvents.

* For more information on the game, visit this [page](https://github.com/salaboy/from-monolith-to-k8s/tree/main/game).
* For instructions on deploying the entire system, visit this [page](https://github.com/ThomasVitale/eventing-game).

Question 3 is a Spring Boot project relying on Spring Cloud Function, Spring Native, and the CloudEvents Java SDK. The project
has been initialized using the Knative [func](https://github.com/knative-sandbox/kn-plugin-func) plugin.

## Usage

```shell
$ http <url> player="jon-snow" sessionId="game-blahblah" optionA=false optionB=true optionC=false optionD=false remainingTime=13

HTTP/1.1 200 OK
Content-Length: 98
Content-Type: application/json
accept-encoding: gzip, deflate
connection: keep-alive
user-agent: HTTPie/3.1.0

{
    "player": "jon-snow"
    "level": "devoxxuk-question-5",
    "levelScore": 18,
    "sessionId": "game-blahblah",
    "gameTime": "2022-04-19T11:40:46.04108"
}
```

## Local execution

Make sure you have a Java 17 distribution installed.

You can run the application locally as follows.

```shell
./gradlew bootRun
```

To run the tests run the following command.

```shell
./gradlew test
```

## The `func` CLI

It's recommended to set the `FUNC_REGISTRY` environment variable.

```shell script
export FUNC_REGISTRY=<registry>/<username>
echo "export FUNC_REGISTRY=<registry>/<username>" >> ~/.bashrc
```

where `<registry>` is a container registry URI (for example, `ghcr.io`) and `username` is your account name on
that registry.

### Building

This command builds an OCI image for the function. By default, this will build a GraalVM native image.

```shell
func build -v
```

**Note**: If you want to disable the native build, you need to edit the `func.yaml` file and
remove (or set to false) the following BuilderEnv variable:
```
buildEnvs:
  - name: BP_NATIVE_IMAGE
    value: "true"
```

### Running

This command runs the func locally in a container using the image created above.

```shell
func run
```

### Deploying

This command will build and deploy the function into cluster.

```shell
func deploy -v
```

## Function invocation

For the examples below, please be sure to set the `URL` variable to the route of your function.

You get the route by following command.

```shell script
func info
```

Note the value of **Routes:** from the output, set `$URL` to its value.

__TIP__:

If you use `kn` then you can set the url by:

```shell script
# kn service describe <function name> and show route url
export URL=$(kn service describe $(basename $PWD) -ourl)
```

Then, call the function as follows.

```shell script
http $URL player="jon-snow" sessionId="game-blahblah" optionA=false optionB=false optionC=true optionD=false remainingTime=13
```

## Cleanup

To clean the deployed function run:

```shell
func delete
```
