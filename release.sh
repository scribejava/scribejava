#!/bin/bash

mvn -DperformRelease=true clean deploy

cd ../gh-maven-repo
./update-repository-index.sh

