for i in {1..100}; do
  # echo "Creating user $i..."

  curl -s -X POST \
    'http://localhost:8080/users' \
    -H 'accept: */*' \
    -H 'Content-Type: application/json' \
    -d "{
      \"username\": \"kamyab$i\",
      \"mobile\": \"09127899241\",
      \"address\": \"tehroon$i\",
      \"postalCode\": \"1234567890\"
    }"

  echo "Request Sended"
done

echo "Done. Created 100 users."
