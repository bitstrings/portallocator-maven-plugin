portallocator-maven-plugin
==========================

Example - Port assignment
-------------------------
```
<configuration>
    <portAllocations>
        <portAllocation>
            <preferredPorts>8080</preferredPorts>
            <depletionAction>continue|fail</depletionAction>

            <name>tomcat</name>

            <offsetBasePort>8080</offsetBasePort>

            <portNameSuffix>port</portNameSuffix>
            <offsetNameSuffix>offset</offsetNameSuffix>

            <nameLevelSeparator>-</nameLevelSeparator>

            <relativePort>
                <name></name>
                <offset>10</offset>
            </relativePort>
        </portAllocation>
    </portAllocations>
</configuration>
```
