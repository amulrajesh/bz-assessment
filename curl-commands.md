## Get Token
```
curl --location --request POST 'http://localhost:8080/token' \
--header 'Content-Type: application/json' \
--data-raw '{
    "username": "mithu",
    "password": "password"
}'
```

## Load All Recipe

```
curl --location --request GET 'http://localhost:8080/api/v1/recipe' \
--header 'Authorization: Bearer *********************' 
```

## Load Recipe By Id
```
curl --location --request GET 'http://localhost:8080/api/v1/recipe/1' \
--header 'Authorization: Bearer *********************' 
```

## Delete Recipe By Id
```
curl --location --request DELETE 'http://localhost:8080/api/v1/recipe/1' \
--header 'Authorization: Bearer *********************' 
```

## Save Recipe
```
curl --location --request POST 'http://localhost:8080/api/v1/recipe' \
--header 'Content-Type: application/json' \ \
--header 'Authorization: Bearer *********************' 
--data-raw '{    
    "recipeName": "Dosa",
    "noOfServings": 1,
    "instructions": "Add Ghee",
    "userId": "postman",
    "type": "veg",
    "ingredients": ["Rice Batter", "Ghee", "Oil"]
}'
```

## Update Recipe
```
curl --location --request PUT 'http://localhost:8080/api/v1/recipe/1' \
--header 'Content-Type: application/json' \ \
--header 'Authorization: Bearer *********************' 
--data-raw '{    
    "recipeName": "Pasta Updated Version",
    "noOfServings": 1,
    "instructions": "Add Ghee",
    "userId": "postman",
    "type": "veg",
    "ingredients": ["Rice Batter", "Ghee", "Oil"]
}'
```

## Search Recipe
```
curl --location --request GET 'http://localhost:8080/api/v1/recipe/search?type=non-veg&minServings=1&maxServings=1&queryInstructions=Medium%20Fry,%20Fully%20cooked&includedIngredients=Tomato&excludedIngredients=Chicken' \
--header 'Authorization: Bearer *********************' 
```