Push-Location
Set-Location ..

&mvn -Prelease -pl !javalin-mvc-test deploy

Pop-Location