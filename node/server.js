const express = require('express');
const fs = require('fs');
const path = require('path');
const app = express();

// Load locations from JSON file
const locations = JSON.parse(
  fs.readFileSync(path.join(__dirname, 'data', 'locations.json'), 'utf8')
);

// Middleware to parse JSON bodies
app.use(express.json());

// CORS middleware for local development
app.use((req, res, next) => {
  res.header('Access-Control-Allow-Origin', '*');
  res.header('Access-Control-Allow-Methods', 'GET, POST, OPTIONS');
  res.header('Access-Control-Allow-Headers', 'Content-Type');
  if (req.method === 'OPTIONS') {
    return res.sendStatus(200);
  }
  next();
});

// Endpoint to get locations based on latitude, longitude, and categories
app.get('/api/locations', (req, res) => {
  try {
    const { latitude, longitude, categories } = req.query;

    // Validate required parameters
    if (!latitude || !longitude) {
      return res.status(400).json({
        error: 'Missing required parameters',
        message: 'latitude and longitude are required'
      });
    }

    // Parse latitude and longitude
    const lat = parseFloat(latitude);
    const lon = parseFloat(longitude);

    if (isNaN(lat) || isNaN(lon)) {
      return res.status(400).json({
        error: 'Invalid parameters',
        message: 'latitude and longitude must be valid numbers'
      });
    }

    // Filter by categories if provided
    let filteredLocations = locations;
    if (categories) {
      const categoryArray = Array.isArray(categories) 
        ? categories 
        : categories.split(',').map(c => c.trim());
      
      filteredLocations = locations.filter(location => {
        return categoryArray.some(category => 
          location.categories.some(locationCategory => 
            locationCategory.toLowerCase() === category.toLowerCase()
          )
        );
      });
    }

    // Return the filtered locations
    res.json({
      locations: filteredLocations,
      count: filteredLocations.length,
      query: {
        latitude: lat,
        longitude: lon,
        categories: categories ? (Array.isArray(categories) ? categories : categories.split(',').map(c => c.trim())) : null
      }
    });
  } catch (error) {
    console.error('Error processing request:', error);
    res.status(500).json({
      error: 'Internal server error',
      message: error.message
    });
  }
});

// Health check endpoint
app.get('/health', (req, res) => {
  res.json({ status: 'ok', timestamp: new Date().toISOString() });
});

// Root endpoint
app.get('/', (req, res) => {
  res.json({
    message: 'Happy Hour API',
    version: '1.0.0',
    endpoints: {
      locations: '/api/locations?latitude=<lat>&longitude=<lon>&categories=<cat1,cat2>',
      health: '/health'
    }
  });
});

const PORT = process.env.PORT || 3000;

if (require.main === module) {
  app.listen(PORT, () => {
    console.log(`Happy Hour API server running on port ${PORT}`);
    console.log(`Health check: http://localhost:${PORT}/health`);
    console.log(`API endpoint: http://localhost:${PORT}/api/locations`);
  });
}

module.exports = app;

