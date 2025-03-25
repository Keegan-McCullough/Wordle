### Wordle Assignment

The original version of the project was taken from GitHub

https://github.com/ggleblanc2/wordle 

* Author: Gilbert G. Le Blanc
* Date Created: 31 March 2022
* Version: 1.0

The current form of the project is a Gradle build with the "Java" plugin. The build file has been modified to build an executable jar from the command line using the command:
```
./gradlew clean build jar
```

and the project can be executed from command line with:
```
java -jar build/libs/Wordle-snapshot-1.0.jar
```
from top-level project folder.