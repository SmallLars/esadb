# esadb

Programm zur Steuerung von Häring-Anlagen mit ESA 2002.  
Eine Alternative zu HAWEV 2002.

## Download

[Version 1.0.2](https://github.com/SmallLars/esadb/raw/v1/pub/esadb.jar)

[Version 2.0.3](https://github.com/SmallLars/esadb/raw/v2/pub/esadb.jar)

## Installation

### Vorraussetzungen:

Microsoft Office Access 2007-Runtime:  
https://www.microsoft.com/de-de/download/details.aspx?id=4438

Office Microsoft Office Access Runtime und Data Connectivity 2007 Service Pack 3 (SP3):  
https://www.microsoft.com/de-de/download/details.aspx?id=27835

Java 8:  
http://java.com/de/download/

### Anleitung

Im Allgemeinen ist HAWEV unter "C:\esa_dat\HAWEV" bereits installiert.  
"C:\esa_dat" ist dabei der freigegebene Ordner der auf den Linien unter  
"Z:" als Netzlaufwerk eingebunden ist.

Um keine Änderung an den Linien vornehmen zu müssen, kann der Ordner  
"HAWEV" umbenannt werden, um für esadb einen neuen Ordner "HAWEV"  
zu erstellen, in den dann die esadb.jar kopiert wird.

Danach sind folgende Schritte empfohlen:

1. esadb.jar per Doppelklick starten
2. Unter "Datei -> Einstellungen... -> Allgemein" die gewünschten Linien einfügen
3. esadb beenden
4. Für die Nutzung der Wildscheiben müssen die Dateien HZ_7775 und *.bmp  
auf die Linien in das Installationsverzeichnis von ESA 2002 kopiert werden.
5. Eine Verknüpfung zu esadb.jar auf dem Desktop anlegen
6. Eine Verknüpfung zu Stammdaten.mdb auf dem Desktop anlegen
7. Vereine, Schützen und Disziplinen in den Stammdaten hinterlegen

## Screenshots:

[![Hautprogramm](https://github.com/SmallLars/esadb/raw/master/pub/main_tn.png)](https://github.com/SmallLars/esadb/raw/master/pub/main.png)
[![Treffereditor](https://github.com/SmallLars/esadb/raw/master/pub/hits_tn.png)](https://github.com/SmallLars/esadb/raw/master/pub/hits.png)

[![Scheibeneditor](https://github.com/SmallLars/esadb/raw/master/pub/ringtarget_tn.png)](https://github.com/SmallLars/esadb/raw/master/pub/ringtarget.png)
[![Scheibeneditor](https://github.com/SmallLars/esadb/raw/master/pub/deertarget_tn.png)](https://github.com/SmallLars/esadb/raw/master/pub/deertarget.png)

[![Einzelergebnis - Wertung](https://github.com/SmallLars/esadb/raw/master/pub/result_match_tn.png)](https://github.com/SmallLars/esadb/raw/master/pub/result_match.png)
[![Einzelergebnis - Probe und Wertung](https://github.com/SmallLars/esadb/raw/master/pub/result_both_tn.png)](https://github.com/SmallLars/esadb/raw/master/pub/result_both.png)

## Dokumentation:

###Eclipse Plugin

ObjectAid UML Explorer - http://www.objectaid.net/update:  
ObjectAid UML Explorer -> ObjectAid Class Diagram 1.19