[![Hex.pm](https://img.shields.io/hexpm/l/plug.svg)](http://www.apache.org/licenses/LICENSE-2.0)
[![Platform](https://img.shields.io/badge/platform-android-green.svg)](http://developer.android.com/index.html)
[![Download](https://jitpack.io/v/tperraut/FlowFirebase.svg)](https://jitpack.io/#tperraut/FlowFirebase)
![example workflow](https://github.com/tperraut/FlowFirebase/actions/workflows/workflow.yml/badge.svg)

# FlowFirebase
[Kotlin Flow](https://kotlin.github.io/kotlinx.coroutines/kotlinx-coroutines-core/kotlinx.coroutines.flow/-flow/)
wrapper on Google's [Firebase for Android](https://www.firebase.com/docs/android/) library.

## Usage
TODO

### Authentication
### Database
##### Collecting list
##### Collecting map
### Storage

# Download
### Gradle
Add this to your root build.gradle
```groovy
allprojects {
	repositories {
		...
		maven { url 'https://jitpack.io' }
	}
}
```
Add the dependency
```groovy
dependencies {
    implementation 'com.github.tperraut:FlowFirebase:Tag'
}
```
### Maven
Repo
```xml
<repositories>
	<repository>
	    <id>jitpack.io</id>
	    <url>https://jitpack.io</url>
	</repository>
</repositories>
```
Add the dependency
```xml
<dependency>
    <groupId>com.github.tperraut</groupId>
    <artifactId>FlowFirebase</artifactId>
    <version>Tag</version>
</dependency>
```

# Credits
A big thanks to nmoskalenko for [RxFirebase](https://github.com/nmoskalenko/RxFirebase) that I literally copy and adapt
to [Kotlin Flow](https://kotlin.github.io/kotlinx.coroutines/kotlinx-coroutines-core/kotlinx.coroutines.flow/-flow/)

# License
   Copyright 2019 Thomas Perraut

   Licensed under the Apache License, Version 2.0 (the "License");  
   you may not use this file except in compliance with the License.  
   You may obtain a copy of the License at  

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software  
   distributed under the License is distributed on an "AS IS" BASIS,  
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  
   See the License for the specific language governing permissions and  
   limitations under the License.
