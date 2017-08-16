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
id "com.github.ferstl:sub-parent:pom"
label "com.github.ferstl
sub-parent
1.0.0-SNAPSHOT"
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
source "com.github.ferstl:sub-parent:pom"
target "com.github.ferstl:module-2:jar"
]

]
