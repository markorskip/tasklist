"# tasklist" 

Postgres:
open cmd prompt
psql -U postgres
password:password

show all databases:
\l

Connect to database:
\c tasklistdb

show all schemas:
\dn;

Show all tables:
\dt;

Describe a table:
\d+ task_list;

DB size:
SELECT pg_size_pretty( pg_database_size('tasklistdb') );

drop table "task_list" cascade;
drop table "users" cascade;
drop table "users_task_list" cascade;

notes:
Can't use "user" as a table name, reserved


Create a new task and associate to user
https://stackoverflow.com/questions/51975413/post-request-with-many-to-many-relationship-in-spring-data-rest

First you have to create a new tasklist by posting to
api/taskLists/

Then get the uri
and
curl -v -X POST -H "Content-Type: text/uri-list" \
     -d "URI of new task created ie http://localhost:8080/api/taskLists/4
     http://localhost:8080/api/users/NAMEOFUSERTO_ASSOCIATE_WITH/tasklists/
     
     POST associate a new task in addition to others
     PUT removes all other associates and adds the new one
     
 https://stackoverflow.com/questions/26259474/how-to-add-elements-in-a-many-to-many-relationship-via-springs-repositoryrestr
 https://stackoverflow.com/questions/17981252/how-to-update-reference-object-in-spring-data-rest?rq=1