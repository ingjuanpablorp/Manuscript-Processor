# Manuscript-Processor

Una primera versión del Manuscript-Processor permite identificar
si existen pistas en un manuscrito y almacenar este manuscrito en 
una base de datos no relacional Dynamo.

Esta primera implementación es imperativa y esta sujeta a cambios ya que 
se tiene un diseño en curso donde se hace uso de las bondades de los schedulers 
de project reactor para realizar procesamiento en hilos paralelos.

## Requisitos

Para ejecutar Manuscript-Processor en local se debe aprovisionar una base de datos 
dynamo en el puerto 8010, y crear la tabla 'manuscripts'.

## Instrucciones para correr el programa

Una vez clonado el repositorio, nos ubicamos en la raíz del proyecto
y ejecutamos el comando

`docker build --platform linux/'your-architecture' -t manuscript-processor -f Doc
kerfile .`

y luego:

`docker run --rm -it --platform linux/'your-architecture' manuscript-processor /b
in/sh`

## API api/clue

Se pueden realizar registros de manuscritos con el siguiente curl de ejemplo:

`curl --location 'http://localhost:8080/api/clue' \
--header 'Content-Type: application/json' \
--data '{
    "manuscript": [
        "ABCDEF",
        "GHIJKL",
        "MNOPQR",
        "STUXWZ",
        "ABCDEF"
    ]
}'`

## API api/stats

Se pueden realizar consulta de estadisticas por cada manuscrito que se haya analizado:

Usando el siguiente curl,

`curl --location 'http://localhost:8080/api/stats' \
--header 'Content-Type: application/json' \
--data '{
    "manuscript": [
        "ABCDEF",
        "GHIJKL",
        "MNOPQR",
        "STUXWZ",
        "ABCDEF"
    ]
}'`

Se pueden obtener respuestas cómo:

`{
    "count_clue_found": 1,
    "count_no_clue": 29,
    "ratio": 0.03333333333333333
}`