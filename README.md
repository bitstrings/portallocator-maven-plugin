portallocator-maven-plugin
==========================

Example - Port assignment
-------------------------
```
<configuration>
    <ports>
        <port>
            <preferredPorts>8080</preferredPorts>

            <name>tomcat</name>

            <offsetBasePort>8080</offsetBasePort>

            <portNameSuffix>port</portNameSuffix>
            <offsetNameSuffix>offset</offsetNameSuffix>

            <nameLevelSeparator>-</nameLevelSeparator>

            <relativePort>
                <name></name>
                <offset>10</offset>
            </relativePort>
        </port>
    </ports>
</configuration>
```
