### Instructions to this project
I remained it as a maven structure. The main things I did:
1. Changed the structure of application (details below)
2. Migrated from H2 to mariadb (details below)
3. Fixed some bugs, covered with tests
4. Added civilized migrations control system

### What is yet to be done
(in order I would plan to do it)
1. Cover with integration tests. Probably usage of H2 should be fine in this case, since we don't use platform-specific features, like JSON fields and\or complicated stored procedures.
2. Cover with end-to-ent tests (something like Karate should work fine)
3. Do some dependencies cleanup
4. I saw some attempts to do logging. I changed it to normal java's logging, but I suppose some kind of audit is expected here. 
It can be something on a database level with stored procedure (storing all the changes as a JSON in a dedicated table - a standard solution). 
Or it also can be something on a service level with logging the user's details. Maybe even by sending events to kafka or whatever. 
That is why I didn't implement "soft delete" though I wanted. I didn't decide yet, how/where old data should be stored.
5. Authentication\Authorization can be added. Most probably we wish to secure endpoints and also introduce a role model.
I.e., 
- only authenticated users (i. e., managers?) can make changes (or even view?), 
- Managers are connected with their department
- Managers can be only created by Admin, but Admin can't create/edit employees
This role model is just an example. Anyway, I don't wand to implement it inside this microservice - it should be done by a separate one, this microservice should only receive security token and request its integrity and provided permissions.
Implementing separate security microservice should involve also configuration service (zookeeper/vault/whatever). 
And first I'd prefer to implement mock microservice (or just service class) to remain possible to test it all.
6. Currently, database credentials are stored in a property file. This is not good, since it means that we'll make different builds for different environments. They should be moved to configuration server (zookeeper/vault/whatever) and it's address should be execution parameter of the application. 
7. More documentation on swagger. It would be nice to other teams and our API's users.
8. Last but not least - currently used approach for "update" flow is a bit controversial - 
First, it is done in a repository level, but looks like a part of business logic - maybe it can be moved here
Second, I used the Exception flow here. It is fast and simple, but exceptions spoil the code's structure and can affect application's performance.
(Yes, I have read the Uncle Bob's "clean code", and that is why I call this point "controversial").


### Environment
Before running the application, use local_setup/start_environment.sh script. It downloads and creates docker with database. 
Further it should be extended for other apps, like aforementioned configuration server, other database(s), mock for security microservice etc.
local_setup/stop_environment.sh - stops the current environment
local_setup/stop_clear_environment.sh - stops database and cleans its contents. Use it in case of breaking local data/jumping between incompatible branches etc.


### Application structure
I have split application to separate modules. The general idea is to implement Dependency Inversion principle - now no code has any dependencies on any other layer's implementation.

controller-api's contents is not used by modules other than controller-impl, but it is separated since it can (should) be built as a separate jar and shared with other team(s) as a public API of our module

service-api and repository-api could be merged into core, but I separated them in order to follow interface segregation principle.


### Some remarks
Java 8 is obsolete at this moment, it is sad I couldn't use junit5 at least, not mentioning record classes.
Currently, I manually remove ID from newly created Employee, though I could restrict it on a language level, by creating some kind of "NewEmployeeDTO" without ID itself. Didn't do that thinking about record classes, which can be very useful for such cases. 
I could use spring validation, but I didn't. Just a personal dislike. One of the arguments - it forces you to use exception flow. 
Currently, I used component's auto-scan, though for a big legacy project it can be dangerous, and it can be better to use manual configurations. 

I also tried to avoid obvious comments, since personally I think they can be confusing. BTW, as uncle Bob said, comments can get stale, while your code can't. So I hope I created good enough documentation by my tests. 


### My experience in Java 
- I am java back-end developer since 2008. I use spring boot since 2018, though I used spring before. 
- Before spring boot I used JavaEE and JBoss/EAP.

