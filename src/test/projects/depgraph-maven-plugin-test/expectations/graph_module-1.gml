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
1.10"
]

node [
id "org.apache.commons:commons-lang3:jar"
label "org.apache.commons
commons-lang3
3.1"
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
source "com.github.ferstl:module-1:jar"
target "commons-codec:commons-codec:jar"
]

edge [
source "com.github.ferstl:module-1:jar"
target "org.apache.commons:commons-lang3:jar"
]

edge [
source "junit:junit:jar"
target "org.hamcrest:hamcrest-core:jar"
]

edge [
source "com.github.ferstl:module-1:jar"
target "junit:junit:jar"
]

]
