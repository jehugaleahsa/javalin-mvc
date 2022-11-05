Push-Location
Set-Location ..

&mvn11 -Prelease -pl !javalin-mvc-test clean deploy

Pop-Location