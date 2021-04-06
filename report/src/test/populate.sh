curl -X POST localhost:8080/reports -H "Content-type:application/json" -d {\"message\":\"1st\ message\"\,\"status\":\"DRAFT\"} -u admin:password
echo
curl -X POST localhost:8080/reports -H "Content-type:application/json" -d {\"message\":\"2nd\ message\"\,\"status\":\"DRAFT\"} -u admin:password
echo
curl -X POST localhost:8080/reports -H "Content-type:application/json" -d {\"message\":\"3rd\ message\"\,\"status\":\"DRAFT\"} -u admin:password
echo
curl -X POST localhost:8080/reports -H "Content-type:application/json" -d {\"message\":\"4th\ message\"\,\"status\":\"DRAFT\"} -u admin:password
echo
