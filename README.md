# NeighborhoodApp

**Fast and Easy Information Retrieval**: The app quickly fetches the most essential information about a given location's surroundings.
**Utility Examples**:
  - Checking out what's around a potential rental property.
  - Exploring the amenities of a new neighborhood.
    
## How It Works:
  1. The main screen features an input field to enter the location you want to explore.
  2. Upon confirmation, you can select the type of facilities you're interested in, such as shops, gyms, or public transport stops.
  3. The location is converted to coordinates, and a call is made to retrieve a list of places.
  4. Subsequently, a call is made to calculate the distances between these places and the main location.
  5. The results are displayed in a list sorted by distance from the main location.

## Google APIs Used

- **Places API**: Returns basic information about places of a specific type within a certain radius. [Places API Overview](https://developers.google.com/maps/documentation/places/web-service/overview)
- **Distance Matrix API**: Provides a matrix of distances and times between starting and ending addresses. [Distance Matrix API Overview](https://developers.google.com/maps/documentation/distance-matrix/overview)

## Key Components

- **MainActivity**: Serves as the container for all application windows (fragments).
- **fragments Folder**: Manages the application's views.
- **MainViewModel**: Responsible for acquiring data and communicates with views using Flow.
- **PlacesRepo**: Enables API calls to be made.
- **di Folder**: Modules used by Dagger Hilt for dependency injection.
- **data Folder**: Data classes returned by the APIs.

## Getting Started

To run the LocalExplorer app, clone the repository, and insert your Google API keys into the appropriate configuration files.
