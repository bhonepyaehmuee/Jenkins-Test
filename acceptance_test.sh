#!/bin/bash

echo "================================="
echo " Running Acceptance Test"
echo "================================="

# URL of the running app
APP_URL="http://localhost:9096"

echo "✅ Using app at $APP_URL"

# Run Maven acceptance tests
# Make sure your tests generate XML for Jenkins
mvn test -Dcalculator.url="$APP_URL"
