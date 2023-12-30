graph [
node [
id "com.github.ferstl:module-2:jar"
label "module-2"
]

node [
id "com.google.guava:guava:jar"
label "guava"
]

node [
id "com.github.ferstl:module-3:jar"
label "module-3"
]

edge [
source "com.github.ferstl:module-2:jar"
target "com.google.guava:guava:jar"
]

edge [
source "com.github.ferstl:module-3:jar"
target "com.github.ferstl:module-2:jar"
]

]
