# mule-test-tools
Library that simplifies testing of [Mule ESB] application flows.

Part of the [greenbird] Open Source Java [projects].

Bugs, feature suggestions and help requests can be filed with the [issue-tracker].

[![Build Status][build-badge]][build-link]

## Table of contents
- [License](#license)
- [Obtain](#obtain)
- [Usage](#usage)
- [History](#history)

## License
[Apache 2.0]

## Obtain
The project is based on [Maven] and available on the central Maven repository.

Example dependency config:

```xml
<dependency>
    <groupId>com.greenbird.mule</groupId>
    <artifactId>greenbird-mule-test-tools</artifactId>
    <version>1.0.0</version>
    <scope>test</scope>
</dependency>
```

You can also [download] the jars directly if you need too.

Snapshot builds are available from the Sonatype OSS [snapshot repository].

Include the jar as a test scoped dependency for your project and you're ready to go.

## Usage
The main component of the library is the `com.greenbird.test.mule.GreenbirdMuleFunctionalTestCase` which 
extends and simplifies the standard Mule [functional test case] with a small [DSL]. 
Extend the `GreenbirdMuleFunctionalTestCase` instead of the standard `FunctionalTestCase` and get access to the 
following methods:

* `MuleClient client()`: Get an initialized client.
* `String load(String location)`: Load a resource using a Spring style [resource pattern].
* `void dispatch(String address, Object payload)`: Dispatch a message containing the given payload to the given address.
* `MuleMessage send(String address, Object payload)`: Send a message containing the given payload synchronously to the given address and return the response.
* `MuleMessage request(String address)`: Pop a message off the given address or get null if the request times out. Timeout can be changed from default by overriding `int getMessageRequestTimeout()`.
* `Flow flow(String name)`: Get the flow with the given name.
* `MuleEvent event(Object payload)`: Build a test event containing the given payload.
* `<T> T bean(String componentName)`: Get the component with the given name from the Mule registry.

Some usage examples:

```java
MuleMessage messageFromRequestResponseEndpoint = send("vm\://test-synchronous-endpoint", "Test payload");

MuleEvent responseEventFromDirectFlowCall = flow("testFlow").process(event("Test payload"));

dispatch("vm://test-queue", load("/large-payload.xml"));

MuleMessage messageFromQueue = request("vm\://test-queue");
```

## History
- [1.0.0]: Initial release.
- [1.1.0-SNAPSHOT]: Moved non-mule specific utilities in this project to the [greenbird-test-tools] project. Added support for synchronous sending. 

[1.0.0]:                https://github.com/greenbird/mule-test-tools/issues?milestone=2&state=closed
[1.1.0-SNAPSHOT]:       https://github.com/greenbird/mule-test-tools/issues?milestone=1&state=closed
[Apache 2.0]:           http://www.apache.org/licenses/LICENSE-2.0.html
[build-badge]:          https://build.greenbird.com/job/mule-test-tools/badge/icon
[build-link]:           https://build.greenbird.com/job/mule-test-tools/
[DSL]:                  http://en.wikipedia.org/wiki/Domain-specific_language
[functional test case]: http://www.mulesoft.org/documentation/display/current/Functional+Testing
[greenbird]:            http://greenbird.com/
[issue-tracker]:        https://github.com/greenbird/mule-test-tools/issues
[download]:             http://search.maven.org/#search|ga|1|greenbird-mule-test-tools
[greenbird-test-tools]: https://github.com/greenbird/greenbird-test-tools
[Maven]:                http://maven.apache.org/
[Mule ESB]:             http://www.mulesoft.org/
[projects]:             http://greenbird.github.io/
[resource pattern]:     http://static.springsource.org/spring/docs/current/javadoc-api/org/springframework/core/io/support/PathMatchingResourcePatternResolver.html
[snapshot repository]:  https://oss.sonatype.org/content/repositories/snapshots/com/greenbird/mule/greenbird-mule-test-tools
