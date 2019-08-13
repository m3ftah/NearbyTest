# NearbyTest [![License: CC BY-NC-SA 4.0](https://licensebuttons.net/l/by-nc-sa/4.0/80x15.png)](https://creativecommons.org/licenses/by-nc-sa/4.0/)

Testing Android apps using Google Nearby Connections API.

# Build the template app

```bash
./gradlew installDebug
```


# Using PEERFLEET library Gradle Plugin
For faster integration with your app, you can use our Gradle project.

In your root project folder you should checkout this project:
```bash
git checkout git@github.com:m3ftah/NearbyTest.git
```

In `YourProject/settings.gradle` you should add:
```gradle
include ':NearbyTest'
project(':NearbyTest').projectDir = new File('../NearbyTest')
```

And in `YourProject/settings.gradle` you should add:
```gradle
compile project(":NearbyTest")
```

# Using Fougere
If you are using `NearbyTest`, please cite the following research paper:
>Lakhdar Meftah, Romain Rouvoy, and Isabelle Chrisment. "Testing nearby peer-to-peer mobile apps at large."In Proceedings of the 6th International Conference onMobile Software Engineering and Systems, pp. 1-11. IEEE Press, 2019. ([pdf](https://hal.inria.fr/hal-02059088v1)).
