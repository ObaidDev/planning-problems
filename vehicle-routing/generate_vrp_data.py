import json
import random
from datetime import datetime, timedelta

def generate_dummy_vrp_data(
    name="demo",
    num_vehicles=3,
    num_visits=15,
    vehicle_capacity=30,
    min_demand=1,
    max_demand=5,
    service_duration=360,
    south_west_corner=None,
    north_east_corner=None,
    start_datetime="2025-01-18T07:30:00",
    end_datetime="2025-01-19T00:00:00"
):
    # Set default geographical boundaries (New York area coordinates)
    if south_west_corner is None:
        south_west_corner = [33.031741, -7.695507]
    if north_east_corner is None:
        north_east_corner = [32.397405, -5.870404]

    def generate_random_location():
        lat = random.uniform(south_west_corner[0], north_east_corner[0])
        lon = random.uniform(south_west_corner[1], north_east_corner[1])
        return [round(lat, 6), round(lon, 6)]

    # Generate vehicles
    vehicles = []
    for i in range(1, num_vehicles + 1):
        vehicles.append({
            "id": str(i),
            "capacity": vehicle_capacity,
            "homeLocation": generate_random_location(),
            "departureTime": start_datetime,
            "visits": [],
            "totalDrivingTimeSeconds": 0,
            "totalDemand": 0,
            "arrivalTime": start_datetime
        })

    # Generate visits with sequential IDs similar to example
    visits = []
    start_visit_id = 13232221
    for j in range(num_visits):
        visits.append({
            "id": str(start_visit_id + j),
            "location": generate_random_location(),
            "demand": random.randint(min_demand, max_demand),
            "minStartTime": start_datetime,
            "maxEndTime": end_datetime,
            "serviceDuration": service_duration
        })

    return {
        "name": name,
        "southWestCorner": south_west_corner,
        "northEastCorner": north_east_corner,
        "startDateTime": start_datetime,
        "endDateTime": end_datetime,
        "vehicles": vehicles,
        "visits": visits,
        "totalDrivingTimeSeconds": 0
    }

# Generate and save the data
if __name__ == "__main__":
    data = generate_dummy_vrp_data(
        num_vehicles=100,
        num_visits=1000,
        vehicle_capacity=70,
        min_demand=4,
        max_demand=8
    )

    with open('vrp_data.json', 'w') as f:
        json.dump(data, f, indent=2)

    print("VRP dummy data generated successfully!")