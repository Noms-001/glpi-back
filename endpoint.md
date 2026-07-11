# API Endpoints

## 1. GET /status

- Méthode : `GET`
- URL : `/status`
- Entrée : aucune
- Sortie : `200 OK`
- Corps JSON :
```json
{
  "success": true,
  "data": [
    {
      "id": 1,
      "couleur": "rouge",
      "langues": [
        {
          "langue": "fr",
          "valeur": "Ouvert"
        },
        {
          "langue": "en",
          "valeur": "Open"
        }
      ]
    }
  ],
  "error": null
}
```

## 2. POST /status

- Méthode : `POST`
- URL : `/status`
- Entrée : corps JSON
```json
{
  "id": 1,
  "langue": "fr",
  "valeur": "Réglé"
}
```
- Sortie : `200 OK`
- Corps JSON :
```json
{
  "success": true,
  "data": {
    "id": 1,
    "couleur": "rouge",
    "langues": [
      {
        "langue": "fr",
        "valeur": "Réglé"
      }
    ]
  },
  "error": null
}
```

## 3. POST /status/color

- Méthode : `POST`
- URL : `/status/color`
- Entrée : corps JSON
```json
{
  "id": 1,
  "couleur": "vert"
}
```
- Sortie : `200 OK`
- Corps JSON :
```json
{
  "success": true,
  "data": {
    "id": 1,
    "couleur": "vert",
    "langues": [
      {
        "langue": "fr",
        "valeur": "Réglé"
      }
    ]
  },
  "error": null
}
```

## 4. GET /langues

- Méthode : `GET`
- URL : `/langues`
- Entrée : aucune
- Sortie : `200 OK`
- Corps JSON :
```json
{
  "success": true,
  "data": [
    {
      "id": 1,
      "code": "fr"
    },
    {
      "id": 2,
      "code": "en"
    }
  ],
  "error": null
}
```