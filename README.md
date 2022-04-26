openapi: "3.0.3"
info:
title: "EEET2580_Final API"
description: "EEET2580_Final API"
version: "1.0.0"
servers:
- url: "https://EEET2580_Final"
  paths:
  /api/customers:
  get:
  summary: "GET api/customers"
  operationId: "getCustomers"
  parameters:
  - name: "page"
  in: "query"
  required: true
  schema:
  type: "number"
  format: "int32"
  - name: "size"
  in: "query"
  required: true
  schema:
  type: "number"
  format: "int32"
  - name: "sort"
  in: "query"
  required: true
  schema:
  type: "array"
  responses:
  "200":
  description: "OK"
  post:
  summary: "POST api/customers"
  operationId: "addCustomer"
  responses:
  "200":
  description: "OK"
  /api/customers/history:
  get:
  summary: "GET api/customers/history"
  operationId: "getHistory"
  responses:
  "200":
  description: "OK"
  post:
  summary: "POST api/customers/history"
  operationId: "addHistory"
  responses:
  "200":
  description: "OK"
  delete:
  summary: "DELETE api/customers/history"
  operationId: "deleteHistory"
  responses:
  "200":
  description: "OK"
  /api/customers/login:
  post:
  summary: "POST api/customers/login"
  operationId: "login"
  responses:
  "200":
  description: "OK"
  /api/customers/watchlater:
  get:
  summary: "GET api/customers/watchlater"
  operationId: "getWatchlater"
  responses:
  "200":
  description: "OK"
  post:
  summary: "POST api/customers/watchlater"
  operationId: "addWatchlater"
  responses:
  "200":
  description: "OK"
  delete:
  summary: "DELETE api/customers/watchlater"
  operationId: "deleteWatchlater"
  responses:
  "200":
  description: "OK"
  /api/customers/{id}:
  get:
  summary: "GET api/customers/{id}"
  operationId: "getCustomer"
  parameters:
  - name: "id"
  in: "path"
  required: true
  schema:
  type: "number"
  format: "int64"
  responses:
  "200":
  description: "OK"
  put:
  summary: "PUT api/customers/{id}"
  operationId: "updateCustomer"
  parameters:
  - name: "id"
  in: "path"
  required: true
  schema:
  type: "number"
  format: "int64"
  responses:
  "200":
  description: "OK"
  delete:
  summary: "DELETE api/customers/{id}"
  operationId: "deleteCustomer"
  parameters:
  - name: "id"
  in: "path"
  required: true
  schema:
  type: "number"
  format: "int64"
  responses:
  "200":
  description: "OK"
  /api/movies:
  get:
  summary: "GET api/movies"
  operationId: "getAllMovies"
  parameters:
  - name: "page"
  in: "query"
  required: true
  schema:
  type: "number"
  format: "int32"
  - name: "size"
  in: "query"
  required: true
  schema:
  type: "number"
  format: "int32"
  - name: "sort"
  in: "query"
  required: true
  schema:
  type: "array"
  responses:
  "200":
  description: "OK"
  post:
  summary: "POST api/movies"
  operationId: "addCustomer"
  responses:
  "200":
  description: "OK"
  /api/movies/loadDatabase:
  post:
  summary: "POST api/movies/loadDatabase"
  operationId: "reloadDatabase"
  parameters:
  - name: "g"
  in: "query"
  required: true
  schema:
  type: "number"
  format: "int32"
  - name: "ko"
  in: "query"
  required: true
  schema:
  type: "number"
  format: "int32"
  responses:
  "200":
  description: "OK"
  /api/movies/loadDatabase/{id}:
  post:
  summary: "POST api/movies/loadDatabase/{id}"
  operationId: "reloadDatabase"
  parameters:
  - name: "id"
  in: "path"
  required: true
  schema:
  type: "number"
  format: "int64"
  responses:
  "200":
  description: "OK"
  /api/movies/search:
  get:
  summary: "GET api/movies/search"
  operationId: "getMovies"
  parameters:
  - name: "page"
  in: "query"
  required: true
  schema:
  type: "number"
  format: "int32"
  - name: "size"
  in: "query"
  required: true
  schema:
  type: "number"
  format: "int32"
  - name: "sort"
  in: "query"
  required: true
  schema:
  type: "array"
  - name: "title"
  in: "query"
  required: true
  schema:
  type: "string"
  - name: "rateGT"
  in: "query"
  required: true
  schema:
  type: "number"
  format: "double"
  - name: "rateLTE"
  in: "query"
  required: true
  schema:
  type: "number"
  format: "double"
  - name: "episodesGT"
  in: "query"
  required: true
  schema:
  type: "number"
  format: "int64"
  - name: "episodesLTE"
  in: "query"
  required: true
  schema:
  type: "number"
  format: "int64"
  - name: "country"
  in: "query"
  required: true
  schema:
  type: "array"
  responses:
  "200":
  description: "OK"
  /api/movies/{id}:
  get:
  summary: "GET api/movies/{id}"
  operationId: "getMovie"
  parameters:
  - name: "id"
  in: "path"
  required: true
  schema:
  type: "number"
  format: "int64"
  responses:
  "200":
  description: "OK"
  delete:
  summary: "DELETE api/movies/{id}"
  operationId: "deleteMovie"
  parameters:
  - name: "id"
  in: "path"
  required: true
  schema:
  type: "number"
  format: "int64"
  responses:
  "200":
  description: "OK"