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
id "com.github.ferstl:module-3:jar"
label "com.github.ferstl
module-3
1.0.0-SNAPSHOT"
]

node [
id "com.github.ferstl:module-2:jar"
label "com.github.ferstl
module-2
1.0.0-SNAPSHOT"
]

node [
id "com.google.guava:guava:jar"
label "com.google.guava
guava
21.0"
]

node [
id "com.mysema.querydsl:querydsl-core:jar"
label "com.mysema.querydsl
querydsl-core
3.7.4"
]

node [
id "com.google.code.findbugs:jsr305:jar"
label "com.google.code.findbugs
jsr305
1.3.9"
]

node [
id "com.mysema.commons:mysema-commons-lang:jar"
label "com.mysema.commons
mysema-commons-lang
0.2.4"
]

node [
id "com.infradna.tool:bridge-method-annotation:jar"
label "com.infradna.tool
bridge-method-annotation
1.13"
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
source "com.github.ferstl:module-3:jar"
target "com.github.ferstl:module-1:jar"
]

edge [
source "com.github.ferstl:module-2:jar"
target "com.google.guava:guava:jar"
]

edge [
source "com.github.ferstl:module-3:jar"
target "com.github.ferstl:module-2:jar"
]

edge [
source "com.mysema.querydsl:querydsl-core:jar"
target "com.google.code.findbugs:jsr305:jar"
]

edge [
source "com.mysema.querydsl:querydsl-core:jar"
target "com.mysema.commons:mysema-commons-lang:jar"
]

edge [
source "com.mysema.querydsl:querydsl-core:jar"
target "com.infradna.tool:bridge-method-annotation:jar"
]

edge [
source "com.github.ferstl:module-3:jar"
target "com.mysema.querydsl:querydsl-core:jar"
]

]
