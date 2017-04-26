# WebFLAT

This application was generated using JHipster, you can find documentation and help at https://jhipster.github.io.

## Prerequisites

Before you can build this project, you must install and configure Node.js. Node can be installed from https://nodejs.org/en/ (choose the LTS version).

After installing Node, install the Gulp command-line tool with:

    npm install -g gulp

## Docker

WebFLAT uses Docker to interface with JHipster's registry component.

Install Docker from https://www.docker.com/ according to your platform.
Ensure Docker is running correctly by typing

    docker ps

If no errors occur, and an empty table is shown, Docker is running correctly.

While inside the moctool-gateway folder, run the command

    docker-compose -f src/main/docker/jhipster-registry.yml up

to start the JHipster registry. Do not exit this terminal window as this will exit the registry.
Append -d to this command to run the registry outside of terminal control.

## Database

WebFLAT uses MySQL for its database connectivity. Install MySQL on your computer.

Create a database called "moctool".
Ensure the MySQL root user's password is "password" so a connection can be made (this can be changed through JHipster's configuration
if a more secure password is desired. Utilise JHipster's documentation for this).

## Building for production

To build WebFLAT for deployment, run:

    ./mvnw -Pprod -DskipTests clean package

This produces a target folder in the moctool-gateway folder that includes a .war file.

This file should be called "moctool-0.0.1-SNAPSHOT.war".

The tool can be run by typing

    ./moctool-0.0.1-SNAPSHOT.war

Any errors encountered should be generic errors relating to missing packages, incorrect database users or other things. 
These errors can be easily searched up to find solutions, as they are too extensive to list here.

The war file can be executed in the background as per normal Unix applications.