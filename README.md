# TekVideo

## Contents

<!-- MarkdownTOC -->

- [Setting up Local Development Environment](#setting-up-local-development-environment)
    - [Requirements](#requirements)
    - [Getting the Code](#getting-the-code)
    - [Setting up the Database](#setting-up-the-database)
    - [Configuration](#configuration)
    - [Running the Development Server](#running-the-development-server)

<!-- /MarkdownTOC -->

## Setting up Local Development Environment

### Requirements

  - Grails 2.5.2
  - PostgreSQL 9.4.5

This guide will assume a Linux system, but setup should be possible on both
Windows and Mac.

### Getting the Code

Once the dependencies mentioned above are installed you should be able to
continue.

First clone and pull in sub-modules the project using:

```
git clone https://github.com/DanThrane/tekvideo.sdu.dk.git
git submodule init
git submodule update
```

This will download the code for `tekvideo.sdu.dk` and download additional
dependencies stored as submodules.

### Setting up the Database

The configuration for the development database is stored in `grails-
app/conf/DataSource.groovy`. By default the server will require a user, with
the username `devuser` and the password `devpassword`. This user should own the
two database required for development, which are `tekvideo-dev` and `tekvideo-
test`. This setup can be created from the command-line using the following:

Start postgres from the CLI:

```
sudo -u postgres psql
```

Create user and associated databases.

```
CREATE USER devuser PASSWORD 'devpassword';
CREATE DATABASE "tekvideo-dev" OWNER devuser;
CREATE DATABASE "tekvideo-test" OWNER devuser;
```

At this point the database schema needs to be created. This project uses the
Grails plugin [database-migration](http://grails-plugins.github.io/grails-
database-migration/docs/manual/index.html) to handle database migrations. This
can also be used to create the schema.

```
cd tekvideo.sdu.dk
grails dbm-update
```

### Configuration

Some additional configuration is required for things which cannot be stored
inside of the repository (such as API tokens and passwords). The file is
loaded from `~/.grails/tekvideo-config.properties`. It should contain the
following values:

```
grails.mail.username=<USERNAME>
grails.mail.password=<PASSWORD>
apis.youtube.key=<YOUTUBE_API_KEY>
apis.vimeo.token=<VIMEO_API_KEY>
```

The e-mail used is assumed to be a GMail. To change this look at the
configuration in `grails-app/conf/Config.groovy`.

### Running the Development Server

```
cd tekvideo.sdu.dk
grails run-app
```

The first time you run the server, it will download any additional missing
dependencies, and create additional test-data for the server. Once this
process is done, you should receive the following message:

```
| Server running. Browse to http://localhost:8080/tekvideo
```

Simply point your browser to http://localhost:8080/tekvideo and you should be
presented with the application.

This process will create the following users which can be used for testing:

| Username |  Password  |      Role      |
|----------|------------|----------------|
| Teacher  | `password` | `ROLE_TEACHER` |
| Student  | `password` | `ROLE_STUDENT` |

These users are only created once, and only if run in development mode. The
behavior of the data bootstrap can be changed in 
`grails-app/conf/Bootstrap.groovy`.
