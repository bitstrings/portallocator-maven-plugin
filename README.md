portallocator-maven-plugin
==========================

Example - Port selectors
------------------------
```
<groupId>org.bitstrings.maven.plugins</groupId>
<artifactId>portallocator-maven-plugin</artifactId>
<executions>
    <execution>
        <id>selectors</id>
        <goals>
            <goal>allocate</goal>
        </goals>
        <configuration>

            <portSelectors>

                <portSelector>

                    <id>selector-1</id>

                    <preferredPorts>8090</preferredPorts>

                    <depletionAction>continue|fail</depletionAction>

                    <validateAvailability>0,10,20,30</validateAvailability>

                </portSelector>

            </portSelectors>

        </configuration>
    </execution>
</executions>
```

Example - Port assignment
-------------------------
```
<configuration>
    <ports>
        <port>
            <portSelectorRef>selector-1</portSelectorRef>
            <name>tomcat</name>
            <portNameSuffix>port</portNameSuffix>
            <offsetNameSuffix>offset</offsetNameSuffix>
        </port>
    </ports>
</configuration>
```
