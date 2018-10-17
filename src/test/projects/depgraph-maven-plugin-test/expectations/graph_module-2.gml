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
21.0"
]

node [
id "commons-codec:commons-codec:jar"
label "commons-codec
commons-codec
1.10"
]

node [
id "org.apache.commons:commons-lang3:jar"
label "org.apache.commons
commons-lang3
3.0"
]

node [
id "org.springframework:spring-core:jar"
label "<optional>
org.springframework
spring-core
5.0.7.RELEASE"
]

node [
id "junit:junit:jar"
label "junit
junit
4.12
(test)"
]

node [
id "org.hamcrest:hamcrest-core:jar"
label "org.hamcrest
hamcrest-core
1.3
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
source "junit:junit:jar"
target "org.hamcrest:hamcrest-core:jar"
]

edge [
source "com.github.ferstl:module-2:jar"
target "junit:junit:jar"
]

]
