echo
echo "-- ADMIN FUNCTIONS --"
# assign operator role to user
curl -X POST localhost:8080/tooperator -H "Content-type:application/json" -d {\"username\":\"user2\"} -u admin:password

# list users
curl localhost:8080/userlist -H "Content-type:application/json" -u admin:password

echo
echo "-- user functions --"

# list user's reports
curl localhost:8080/reports/ -u admin:password


curl -X POST localhost:8080/edit -H "Content-type:application/json" -d {\"id\":1\,\"text\":\"newtext\"} -u admin:password


# send to operator
curl -X POST localhost:8080/sendtooperator -H "Content-type:application/json" -d {\"id\":1\,\"operator\":\"user2\"} -u admin:password
curl -X POST localhost:8080/sendtooperator -H "Content-type:application/json" -d {\"id\":2\,\"operator\":\"user2\"} -u admin:password

echo
echo "operator functions"
# list operators's reports
curl localhost:8080/reports/ -u user2:password

#accept
curl localhost:8080/accept -H "Content-type:application/json" -d {\"id\":\"1\"} -u user2:password

#reject
curl localhost:8080/reject -H "Content-type:application/json" -d {\"id\":\"2\"} -u user2:password



