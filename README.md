added Comment for Movie
usage:

# add comment:

POST /api/comments

body:
{
"message":"cool movie",
"user":{
"username":"william2",
"password":"12345"
},
"movie":{
"dbID":"99966"
}
}

# vote comment:

POST api/comments/{id}/vote

{
"username":"zerbra",
"password":"123321"
}

# delete comment:

DELETE api/comments/{id}

{
"username":"william2",
"password":"12345"
}

# login:

POST /api/customers/login

USES APPLICATION/JSON HEADER

tries to login using provided credentials

returns an id for that user, used for below customer apis

* username
* password

# PUT /api/customers/{id}

USE APPLICATION/JSON

# DELETE /api/customers/{id}

self explanatory

# GET /api/customers/{id}

get single customer

# GET /api/customers/watchlater

# GET /api/customers/history

self explanatory

USE APPLICATION/JSON

MUST HAVE USERNAME & PASSWORD IN BODY

# POST /api/customers/watchlater

# POST /api/customers/history

include attribute “dbID” in body to add to watchlater or history

HISTORY ONLY SAVES 10 LATEST MOVIES

# DELETE /api/customers/watchlater

# DELETE /api/customers/history

self explanatory, same params as above

# GET /api/movies/search params:
* title: search via regrex, case insensitive

* episodesLTE: search for shows with less or equal episodes

* episodesGT: search for shows with greater or equal episodes

* rateGT/rateLTE: see above, for rating
* 
* dateGT/dateLTE: see above, for date (yyyy-mm-dd)

* sort: in the form of [{attrib1,dir1}, {attrib2, dir2}, …]

* * attrib: attribute of a movie

* * dir: direction of sort (asc/desc)

* page: index of page to show

* size: size of each page

Example:
https://dramaholic.herokuapp.com/api/movies/search?sort=rating,desc&page=0&title=all of us&episodesLTE=12

will search for all shows on the first page, with the title “all of us”, that has 12 or less episodes, sorted by rating from highest to lowest

