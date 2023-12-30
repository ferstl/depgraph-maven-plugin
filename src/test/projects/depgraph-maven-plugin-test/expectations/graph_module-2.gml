graph [
node [
id "com.github.ferstl:module-2:jar"
label "com.github.ferstl
module-2
1.0.0-SNAPSHOT"
]

node [
id "com.github.ferstl:module-1:jar"
label "com.github.ferstl
module-1
1.0.0-SNAPSHOT"
]

node [
id "com.google.guava:guava:jar"
label "com.google.guava
guava
33.0.0-jre"
]

node [
id "commons-codec:commons-codec:jar"
label "commons-codec
commons-codec
1.16.0"
]

node [
id "org.apache.commons:commons-lang3:jar"
label "org.apache.commons
commons-lang3
3.12.0"
]

node [
id "org.springframework:spring-core:jar"
label "<optional>
org.springframework
spring-core
6.1.2"
]

node [
id "org.junit.jupiter:junit-jupiter:jar"
label "org.junit.jupiter
junit-jupiter
5.10.1
(test)"
]

edge [
source "com.github.ferstl:module-2:jar"
target "com.github.ferstl:module-1:jar"
]

edge [
source "com.github.ferstl:module-2:jar"
target "com.google.guava:guava:jar"
]

edge [
source "com.github.ferstl:module-2:jar"
target "commons-codec:commons-codec:jar"
]

edge [
source "com.github.ferstl:module-2:jar"
target "org.apache.commons:commons-lang3:jar"
]

edge [
source "com.github.ferstl:module-2:jar"
target "org.springframework:spring-core:jar"
]

edge [
source "com.github.ferstl:module-2:jar"
target "org.junit.jupiter:junit-jupiter:jar"
]

]
