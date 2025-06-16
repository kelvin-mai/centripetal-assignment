# Centripetal assignment

This repo contains the solution to the Centripetal [assignment](./resources/assets/hs-assignment.pdf).

## Installation

This project uses the [Clojure CLI](https://clojure.org/guides/deps_and_cli) as the package manager as well as [docker](https://www.docker.com/) to run a local database. [Shadow CLJS](https://shadow-cljs.github.io/docs/UsersGuide.html) and [npm](https://nodejs.org/en) is used as the build tools for the frontend, the clojurescript dependencies are still contained in the deps.edn file.

## Tecnologies

- [Pedestal 0.8.0-beta](https://pedestal.io/pedestal/0.8/index.html) - Latest version of pedestal http api framework
- [cheshire](https://github.com/dakrone/cheshire) - Clojure JSON encode and decode library
- [aero](https://github.com/juxt/aero) - Clojure configuration library
- [deps.edn](https://clojure.org/guides/deps_and_cli) - Clojure CLI build framework for managing dependencies
- [tools.build](https://clojure.org/guides/tools_build) - Clojure build library for deps.edn based projects
- [test-runner](https://github.com/cognitect-labs/test-runner) - A test runner for clojure.test

## Routes

- `/api/` - api
  - GET `/health-check` - check to see if server is alive
  - `/indicators`
    - GET `/` - returns all documents, optional query param of `type` to return all documents that includes an indicator with the provided type
    - GET `/:id` - returns document with provided id parameter or a `404` with null response
    - POST `/search` - search endpoint that returns documents that fufill the given search parameters in a json body

### Search Parameters

Only some parameters are implemented based on the document type. I decided on the parameters that I believed made the most sense and this can easily be expanded in the future.

| parameter     | type       | description                                                                                                                                                                                    |
| ------------- | ---------- | ---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| `tlp`         | `string`   | Checks if substring matches the `tlp` property of the document.                                                                                                                                |
| `author_name` | `string`   | Checks if substring matches the `author_name` property of the document.                                                                                                                        |
| `name`        | `string`   | Checks if substring matches the `name` property of the document.                                                                                                                               |
| `description` | `string`   | Checks if substring matches the `description` property of the document.                                                                                                                        |
| `tags`        | `string[]` | Checks if the document tags includes all of the given tags parameter. The tags are using an "AND" comparison and the document returned must include all tags provided in the search json body. |

## Usage

### Command Line

You can invoke main function using the `:server` alias and access the http server on the default port of 8000.

```bash
$ clj -M:server
```

You can customize the port by changing the environment variable of `PORT` or updating the `:port` property in `./resources/config.edn`

To run the interactive terminal use the `:dev` alias and evaluate the `start!` and `stop!` functions.

```bash
$ clj -M:dev
Clojure 1.11.1
user=> (start!)
```

To run all the unit tests use the `:test` alias.

```bash
$ clj -X:test
```

### Docker

To build and run the `Dockerfile` and subsequent docker image run the following commands in the terminal

```bash
$ docker build --tag indicators-api .
$ docker run -p 8000:8000 indicators-api
```

The api should now be running on port 8000
