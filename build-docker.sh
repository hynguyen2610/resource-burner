#!/bin/bash

# Extract the version from build.gradle.kts
VERSION=$(grep "version = " build.gradle.kts | sed -E 's/version = "(.*)"/\1/')

# Output the version
echo "Building version " + "$VERSION"
docker build -t bluestorm1288/resource-burner:$VERSION -t bluestorm1288/resource-burner:latest .