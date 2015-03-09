portallocator-maven-plugin
==========================

Example - Port allocators
-------------------------
```
<groupId>org.bitstrings.maven.plugins</groupId>
<artifactId>portallocator-maven-plugin</artifactId>
<executions>
    <execution>
        <id>pools</id>
        <goals>
            <goal>allocate</goal>
        </goals>
        <configuration>

            <portSelectors>

                <portSelector>

                    <id>allocator-1</id>

                    <preferredPorts>8090</preferredPorts>

                    <strategy>
                        <order>ascending|descending|random</order>
                        <depletionAction>continue|fail</depletionAction>
                    </strategy>

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
            <allocatorRef>allocator-1</allocatorRef>
            <name>tomcat</name>
            <portNameSuffix>port</portNameSuffix>
            <offsetNameSuffix>offset</offsetNameSuffix>
        </port>
    </ports>
</configuration>
```
