#!/bin/bash

mvn -DperformRelease=true clean deploy

cd ../maven-repo
./update-repository-index.sh

