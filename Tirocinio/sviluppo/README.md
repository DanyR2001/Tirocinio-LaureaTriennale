# Hospitalization-Analysis
In questa repository troviamo 3 cartelle nelle quali troviamo:
- ETL: Dove troviamo il modulo incaricato di effettuare la Data Ingestion e Data Progressing. Questo modulo espone un metodo rest con il quale caricare il file .cvs dal quale verrano prelevati i dati e salvati nel DB Postgress.
- OLAP: Dove troviamo il docker compose per avviare Cube.js, oltre a una cartella nella quale troviamo le definizioni dei cubi e delle view.
- SUPERSET: Nel quale troviamo un read.me in quanto dobbiamo scaricare il sorgente clonando dalla repo di Apache.

Inoltre troviamo 2 file bash per far partire e fermare docker. Questi file avviano/spengono i 3 container e creano la rete bridge che consente loro di comunicare.
