#!/bin/bash
echo "Starting finaglezk in development mode..."
java -server -Xmx1024m -Dstage=development -jar ./dist/finaglezk/@DIST_NAME@-@VERSION@.jar
