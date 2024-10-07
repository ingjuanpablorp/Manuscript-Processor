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