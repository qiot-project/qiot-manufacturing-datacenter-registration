./mvnw --batch-mode release:prepare
./mvnw --batch-mode release:perform
if [[ "$?" -ne 0 ]] ; then
  ./mvnw --batch-mode release:rollback
fi