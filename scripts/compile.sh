#!/usr/bin/env bash
set -e
mvn compile
java -cp target/classes br.edu.marketpulse.Main
