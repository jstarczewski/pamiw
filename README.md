## PAMIW
PAMIW is a simple project created to pass Web Applications Development course on Warsaw University of Technology. The general "use-case" for the whole project was to create two clients apps, that can manage "PDF related resources" shared via HTTP API application.

## Code
This was my first real Ktor project, so it is highly influenced by various [Ktor samples](https://github.com/ktorio/ktor-samples). The code is **not production ready, because I work full time and all features in the projects were written nearly the same day the deadline for each part was :(**. Though, it was a great experience to learn something new and whole Ktor framework is really insteresting. Project can be run in local or in **Docker** enviroment. 

## Content
PAMIW contains three small projects that work together.
1. HTTP API app written in **Kotlin** with **Ktor** framework.
2. HTTPI Client app written in **Kotlin** with **Ktor** framework.
3. Android application written in **Kotlin**.

## Polish description

Uruchomienie aplikacji cz4.
1. Uruchamiamy tak jak w cz2. ewentualnie puszczamy `./Deploy`
2. W pliku `/etc/hosts` by skojarzyc adres `127.0.0.1` z domena js.pamiw.com
3. Uruchamiamy aplikacje i wpisujemy adres `js.pamiw.com:8081`

Uruchomienie aplikacji cz3.  
Klient Web
1. Przechodzimy do katalogu `log/` i wykonujemy skrypt `./grdlew build`
2. Przechodzimy do katalogu `updf/` i wykonujemy skrypt `./gradlew build`
3. Wykonujemy polecenie `docker-compose build`
4. Wykonujemy polecenie `docker-compose up`
5. Aplikacja jest dostępna pod adresem http://localhost:8081
6. Logujemy się na dostepne konto z loginem `bchaber` i hasłem `123456789`  
Klient Mobile
1. Zmieniamy branch w repozytorium na `feature/localhost`
2. Za pomocą Android Studio otwieramy projekt `MLog`
3. Otwieramy `updf` w IntelliJ IDEAD i uruchamiamy przyciskiem `run`
4. Otwieramy emulator telefoun np. Pixel 2 i uruchamiamy aplikacje MLog przyciskiem `run`

Zdecydowałem się zostawić klienta mobilnego na localhost, ponieważ nie zdążyłem postawić api w chmurze. W razie problemów z prezentację moge z chęcia uruchomić projekt na swoim komputerze i zaprezentować całość.

Uruchomienie aplikacji cz2.
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
