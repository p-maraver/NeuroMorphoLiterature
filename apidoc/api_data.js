define({ "api": [
  {
    "version": "1.0.0",
    "type": "get",
    "url": "/literature/articles?page=&data.publishedDate=&data.dataUsage=",
    "title": "Publications data",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "integer",
            "optional": false,
            "field": "page",
            "description": "<p>Page number</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "data.publishedDate",
            "description": "<p>Publication year to filter by</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "data.dataUsage",
            "description": "<p>DataUsage: USING|CITING|SHARING|DESCRIBING_NEURONS|ABOUT to filter by</p>"
          }
        ]
      }
    },
    "description": "<p>Returns all the publications and it's data found for the query</p>",
    "name": "articles",
    "group": "NeuroMorpho.Org_Literature_API",
    "success": {
      "examples": [
        {
          "title": "Success-Response:",
          "content": "{\n     \"content\": [\n                {\n                    \"id\": \"5ead7db59314014452056e88\",\n                    \"pmid\": \"33494860\",\n                    \"pmcid\": \"7837682\",\n                    \"doi\": \"10.7554/eLife.60936\",\n                    \"link\": null,\n                    \"journal\": \"eLife\",\n                    \"title\": \"Data-driven reduction of dendritic morphologies with preserved dendro-somatic responses\",\n                    \"evaluatedDate\": 1589414400000,\n                    \"publishedDate\": 1611619200000,\n                    \"authorList\": [\n                                    \"Willem Am Wybo\",\n                                    \"Jakob Jordan\",\n                                    \"Benjamin Ellenberger\",\n                                    \"Ulisses Marti Mengual\",\n                                    \"Thomas Nevian\",\n                                    \"Walter Senn\"\n                                    ],\n                    \"dataUsage\": [\n                                    \"Describing\",\n                                    \"Using\",\n                                    \"Citing\"\n                                    ],\n                    \"metadata\": {\n                        \"tracingSystem\": [\"Neurolucida\"],\n                        \"brainRegion\": [\"anterior cingulate cortex\"],\n                        \"negativeIfNoAnswer\": false,\n                        \"cellType\": [\"pyramidal\"],\n                        \"nReconstructions\": 1,\n                        \"species\": [\"mice\"],\n                    }\n                    \"specificDetails\": \"[No response]\",\n                    \"nReconstructions\": 1,\n                    \"globalStatus\": \"Not available\",\n                    \"collection\": \"Positive\",\n                }, ...\n            ],\n        \"last\": false,\n        \"totalPages\": 7,\n        \"totalElements\": 329,\n         \"sort\": null,\n        \"first\": true,\n        \"numberOfElements\": 50,\n        \"size\": 50,\n        \"number\": 0\n    }",
          "type": "json"
        }
      ]
    },
    "filename": "./src/main/java/org/neuromorpho/literature/api/controller/LiteratureController.java",
    "groupTitle": "NeuroMorpho.Org_Literature_API"
  },
  {
    "version": "1.0.0",
    "type": "get",
    "url": "/literature/count",
    "title": "Count",
    "description": "<p>Returns the count summary for articles and reconstructions by status: Available, Not available, Determining availability, Negative</p>",
    "name": "count",
    "group": "NeuroMorpho.Org_Literature_API",
    "success": {
      "fields": {
        "Success 200": [
          {
            "group": "Success 200",
            "type": "String",
            "optional": false,
            "field": "label",
            "description": "<p>Status of the article or reconstructions label</p>"
          },
          {
            "group": "Success 200",
            "type": "Number",
            "optional": false,
            "field": "value",
            "description": "<p>#Reconstructions or #Articles</p>"
          }
        ]
      },
      "examples": [
        {
          "title": "Success-Response:",
          "content": "{\n\"Determining availability\": 357,\n\"Available\": 1779,\n\"Negatives\": 51525,\n\"Not available\": 3210,\n\"Not available.nReconstructions\": 158403,\n\"Available.nReconstructions\": 188566,\n\"Determining availability.nReconstructions\": 31589\n}",
          "type": "json"
        }
      ]
    },
    "filename": "./src/main/java/org/neuromorpho/literature/api/controller/LiteratureController.java",
    "groupTitle": "NeuroMorpho.Org_Literature_API"
  },
  {
    "version": "1.0.0",
    "type": "get",
    "url": "/literature/reports?type=",
    "title": "Reports",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "type",
            "description": "<p>Report type: FrozenEvolution|CompleteDetails</p>"
          }
        ]
      }
    },
    "description": "<p>Generates the Excel reports FrozenEvolution and CompleteDetails in folder pre-defined on application.properties</p>",
    "name": "reports",
    "group": "NeuroMorpho.Org_Literature_API",
    "filename": "./src/main/java/org/neuromorpho/literature/api/controller/LiteratureController.java",
    "groupTitle": "NeuroMorpho.Org_Literature_API"
  },
  {
    "version": "1.0.0",
    "type": "get",
    "url": "/version?type=literature",
    "title": "Version",
    "description": "<p>Returns the current version and date</p>",
    "name": "version",
    "group": "NeuroMorpho.Org_Literature_API",
    "success": {
      "fields": {
        "Success 200": [
          {
            "group": "Success 200",
            "type": "String",
            "optional": false,
            "field": "version",
            "description": "<p>Version number</p>"
          },
          {
            "group": "Success 200",
            "type": "String",
            "optional": false,
            "field": "type",
            "description": "<p>Literature</p>"
          },
          {
            "group": "Success 200",
            "type": "String",
            "optional": false,
            "field": "date",
            "description": "<p>Release date</p>"
          }
        ]
      },
      "examples": [
        {
          "title": "Success-Response:",
          "content": "{\n     \"version\": \"8.1.6\",\n     \"type\": \"literature\",\n     \"date\": 1635375600000\n}",
          "type": "json"
        }
      ]
    },
    "filename": "./src/main/java/org/neuromorpho/literature/api/controller/VersionController.java",
    "groupTitle": "NeuroMorpho.Org_Literature_API"
  },
  {
    "version": "1.0.0",
    "type": "get",
    "url": "/literature/years",
    "title": "Publication years",
    "description": "<p>Returns the years for which there are publications</p>",
    "name": "years",
    "group": "NeuroMorpho.Org_Literature_API",
    "success": {
      "fields": {
        "Success 200": [
          {
            "group": "Success 200",
            "type": "Number[]",
            "optional": false,
            "field": "value",
            "description": "<p>List of publication years</p>"
          }
        ]
      },
      "examples": [
        {
          "title": "Success-Response:",
          "content": "[2016, 1992, ..., 1997, 1996]",
          "type": "json"
        }
      ]
    },
    "filename": "./src/main/java/org/neuromorpho/literature/api/controller/LiteratureController.java",
    "groupTitle": "NeuroMorpho.Org_Literature_API"
  }
] });