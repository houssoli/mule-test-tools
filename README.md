# mule-test-tools
Library that simplifies testing of [Mule ESB] application flows.

## License
[Apache 2.0]

## Obtain
The project is based on [Maven] and, since it has not been released yet, is only available from the Sonatype OSS [snapshot repository].

Example dependency config:

```xml
<dependency>
    <groupId>com.greenbird.mule</groupId>
    <artifactId>greenbird-mule-test-tools</artifactId>
    <version>1.0-SNAPSHOT</version>
    <scope>test</scope>
</dependency>
```

You can also [download] the jars directly if you need too.

Include the jar as a test scoped dependency for your project and you're ready to go.

## Usage
The main component of the library is the `com.greenbird.test.mule.GreenbirdMuleFunctionalTestCase` which 
extends and simplifies the standard Mule [functional test case] with a small [DSL]. 
Extend the `GreenbirdMuleFunctionalTestCase` instead of the standard `FunctionalTestCase` and get access to the 
following methods:

* `MuleClient client()`: Get an initialized client.
* `String load(String location)`: Load a resource using a Spring style [resource pattern].
* `void dispatch(String address, Object payload)`: Dispatch a message containing the given payload to the given address.
* `MuleMessage request(String address)`: Pop a message off the given address or get null if the request times out. Timeout can be changed from default by overriding `int getMessageRequestTimeout()`.
* `Flow flow(String name)`: Get the flow with the given name.
* `MuleEvent event(Object payload)`: Build a test event containing the given payload.
* `<T> T bean(String componentName)`: Get the component with the given name from the Mule registry.

Some usage examples:

```java
MuleEvent responseEvent = flow("testFlow").process(event("Test payload"));

dispatch("vm://test-queue", load("/large-payload.xml"));

MuleMessage messageFromQueue = request("vm://test-queue");
```
 
[Apache 2.0]:                 http://www.apache.org/licenses/LICENSE-2.0.html
[DSL]:                        http://en.wikipedia.org/wiki/Domain-specific_language
[functional test case]:       http://www.mulesoft.org/documentation/display/current/Functional+Testing
[issues]:                     https://github.com/greenbird/mule-test-tools/issues
[download]:                   https://oss.sonatype.org/content/repositories/snapshots/com/greenbird/mule/greenbird-mule-test-tools/1.0-SNAPSHOT/
[Maven]:                      http://maven.apache.org/
[Mule ESB]:                   http://www.mulesoft.org/
[resource pattern]:           http://static.springsource.org/spring/docs/current/javadoc-api/org/springframework/core/io/support/PathMatchingResourcePatternResolver.html
[snapshot repository]:        https://oss.sonatype.org/content/repositories/snapshots/com/greenbird/mule/greenbird-mule-test-tools

