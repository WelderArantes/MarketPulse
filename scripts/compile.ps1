mvn compile
if ($LASTEXITCODE -eq 0) { java -cp target/classes br.edu.marketpulse.Main }
