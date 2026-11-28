package com.example.agentdryrun.entity;

public enum ViolationType {
    PARKING_ON_FOOTPATH, // Vehicle parked on a pedestrian footpath
    NO_PARKING_ZONE, // Vehicle parked in a designated no-parking area
    BLOCKING_ENTRANCE, // Blocking a building or property entrance
    BLOCKING_DRIVEWAY, // Blocking vehicle access to a driveway
    BLOCKING_ROAD, // Obstructing traffic or road access
    WRONG_SIDE_PARKING, // Parked on the prohibited side
    DOUBLE_PARKING, // Parked alongside another parked vehicle, obstructing access
    DISABLED_PARKING_VIOLATION, // Improper use of a designated accessible parking space
    FIRE_LANE_VIOLATION, // Parked in a designated emergency access area
    OTHER // Violation not covered by the available categories
}
