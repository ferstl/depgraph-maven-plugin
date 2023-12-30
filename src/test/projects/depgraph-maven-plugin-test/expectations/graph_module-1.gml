graph [
node [
id "com.github.ferstl:module-1:jar"
label "com.github.ferstl
module-1
1.0.0-SNAPSHOT"
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
3.14.0"
]

node [
id "org.junit.jupiter:junit-jupiter:jar"
label "org.junit.jupiter
junit-jupiter
5.10.1
(test)"
]

edge [
source "com.github.ferstl:module-1:jar"
target "commons-codec:commons-codec:jar"
]

edge [
source "com.github.ferstl:module-1:jar"
target "org.apache.commons:commons-lang3:jar"
]

edge [
source "com.github.ferstl:module-1:jar"
target "org.junit.jupiter:junit-jupiter:jar"
]

]
