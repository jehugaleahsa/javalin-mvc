Push-Location
Set-Location ..

&mvn -Prelease -pl !javalin-mvc-test clean deploy

Pop-Location