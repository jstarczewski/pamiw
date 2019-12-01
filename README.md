Uruchomienie aplikajci cz2.
1. Przechodzimy do katalogu `log/` i wykonujemy skrypt `./grdlew build`
2. Przechodzimy do katalogu `updf/` i wykonujemy skrypt `./gradlew build`
3. Wykonujemy polecenie `docker-compose build`
4. Wykonujemy polecenie `docker-compose up`
5. Aplikacja jest dostępna pod adresem http://localhost:8081
6. Logujemy się na dostepne konto z loginem `bchaber` i hasłem `123456789`
  
Uruchomienie aplikacji
1. docker build -t jstar .
2. sudo docker run -p5000:5000 jstar
3. Formularz dostepny pod adresem http://localhost:5000/register
