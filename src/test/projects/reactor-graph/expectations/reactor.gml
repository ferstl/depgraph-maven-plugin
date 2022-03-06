graph [
node [
id "com.github.ferstl:reactor-ui"
label "reactor-ui"
]

node [
id "com.github.ferstl:reactor-application"
label "reactor-application"
]

node [
id "com.github.ferstl:reactor-database"
label "reactor-database"
]

node [
id "com.github.ferstl:reactor-service"
label "reactor-service"
]

node [
id "com.github.ferstl:reactor-common"
label "reactor-common"
]

node [
id "com.github.ferstl:reactor-parent"
label "reactor-parent"
]

node [
id "com.github.ferstl:reactor-api"
label "reactor-api"
]

edge [
source "com.github.ferstl:reactor-ui"
target "com.github.ferstl:reactor-application"
]

edge [
source "com.github.ferstl:reactor-database"
target "com.github.ferstl:reactor-service"
]

edge [
source "com.github.ferstl:reactor-common"
target "com.github.ferstl:reactor-database"
]

edge [
source "com.github.ferstl:reactor-common"
target "com.github.ferstl:reactor-ui"
]

edge [
source "com.github.ferstl:reactor-parent"
target "com.github.ferstl:reactor-api"
]

edge [
source "com.github.ferstl:reactor-parent"
target "com.github.ferstl:reactor-common"
]

]