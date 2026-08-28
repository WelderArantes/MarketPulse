#!/usr/bin/env bash
set -e
mvn test-compile
java -cp target/test-classes:target/classes br.edu.marketpulse.DiagnosticChecks
