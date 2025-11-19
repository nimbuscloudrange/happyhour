# Happy Hour API

A Node.js microservice for the Happy Hour local discovery platform that helps users find bars, restaurants, and locations offering happy hour deals.

## Features

- RESTful API endpoint to search locations by latitude, longitude, and categories
- Runs locally for development
- Deployable to AWS Lambda using Serverless Framework
- Returns mock location data with full details including menus, hours, and contact information

## Prerequisites

- Node.js (v14 or higher)
- npm or yarn
- AWS CLI configured (for deployment)
- Serverless Framework (installed globally or via npx)

## Installation

1. Install dependencies:
```bash
npm install
```

2. For local development with auto-reload:
```bash
npm install -g nodemon
```

## Running Locally

### Option 1: Standard Node.js server
```bash
npm start
```

The server will start on `http://localhost:3000`

### Option 2: Development mode with auto-reload
```bash
npm run dev
```

### Option 3: Using Serverless Offline (simulates AWS Lambda locally)
```bash
npx serverless offline
```

## API Endpoints

### Get Locations
**GET** `/api/locations`

Query parameters:
- `latitude` (required): Latitude coordinate
- `longitude` (required): Longitude coordinate
- `categories` (optional): Comma-separated list of categories to filter by

**Example requests:**

```
GET /api/locations?latitude=40.7589&longitude=-73.9851
GET /api/locations?latitude=40.7589&longitude=-73.9851&categories=Rooftop,Date Night
```

**cURL examples:**

```bash
# Basic request with latitude and longitude
curl "http://localhost:3000/api/locations?latitude=40.7589&longitude=-73.9851"

# Request with categories filter
curl "http://localhost:3000/api/locations?latitude=40.7589&longitude=-73.9851&categories=Rooftop,Date%20Night"

# Request with single category
curl "http://localhost:3000/api/locations?latitude=40.7589&longitude=-73.9851&categories=Tacos"
```

**Example response:**
```json
{
  "locations": [
    {
      "id": "a7f3b2c9d4e1f8a6b5c3d2e9f1a8b7c6",
      "name": "The Rooftop Bar",
      "description": "Stunning rooftop views...",
      "latitude": 40.7589,
      "longitude": -73.9851,
      "distance": 0.3,
      "rating": 4.5,
      "reviews": 234,
      "categories": ["Rooftop", "Date Night"],
      "menu": [...],
      "hours": {...},
      ...
    }
  ],
  "count": 1,
  "query": {
    "latitude": 40.7589,
    "longitude": -73.9851,
    "categories": ["Rooftop", "Date Night"]
  }
}
```

### Health Check
**GET** `/health`

Returns server status and timestamp.

### Root
**GET** `/`

Returns API information and available endpoints.

## Deployment to AWS

1. Install Serverless Framework globally (if not already installed):
```bash
npm install -g serverless
```

2. Configure AWS credentials:
```bash
aws configure
```

3. Deploy to AWS:
```bash
npm run deploy
```

Or with specific stage/region:
```bash
serverless deploy --stage prod --region us-west-2
```

4. After deployment, you'll receive an API Gateway endpoint URL. Use this URL to access your API.

## Project Structure

```
.
├── handler.js              # AWS Lambda handler wrapper
├── server.js               # Express app and route handlers
├── data/
│   └── locations.json      # Location data
├── package.json            # Dependencies and scripts
├── serverless.yml          # Serverless Framework configuration
└── README.md               # This file
```

## Adding More Mock Data

Edit `data/locations.json` and add more location objects to the array. Each location should follow the same structure as the example provided.

## Environment Variables

- `PORT`: Port number for local server (default: 3000)
- `NODE_ENV`: Environment mode (development/production)

## License

ISC

