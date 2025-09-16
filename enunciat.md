## Exercici 0: Llegir contingut d’un fitxer i mostrar-lo per pantalla amb números de línia
Crea un programa anomenat PR110ReadFile.java.
Des dins del programa, realitza les següents tasques:


Llegeix el contingut del fitxer GestioTasques.java, que es troba dins del directori data. La ruta del fitxer serà System.getProperty("user.dir") + "/data/GestioTasques.java".
Mostra per pantalla tot el contingut del fitxer, afegint el número de línia davant de cada línia de text.
El format del número de línia serà el següent: cada línia ha d'estar precedida per un número seguit de dos punts (:) i un espai abans del contingut de la línia. Ex.: 1: Aquesta és una línia de prova.
Si el fitxer no existeix o es produeix un error de lectura, el programa ha de gestionar l'error i mostrar un missatge adequat, evitant que l'execució es finalitzi de manera abrupta.
