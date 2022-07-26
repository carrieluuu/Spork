Meta University Engineering Project

Original App Design Project [Spork] - README 
===

# Spork - Share & Find Food You Love

## Table of Contents
1. [Overview](#Overview)
1. [Product Spec](#Product-Spec)
1. [Wireframes](#Wireframes)
2. [Schema](#Schema)

## Overview
### Description
A social media application that connects users to recommended restaurants/cafes based on their recent reviews/experiences. 

### App Evaluation
- **Category:** Food & Drink
- **Mobile:** This app would be primarily developed for mobile but would perhaps be just as viable on the web due to the camera and location permissions. 
- **Story:** Recommends food spots for users based on their recent visits (e.g. positive reviews, cuisine, price range, location proximity) and also offers a social media feed composed of their friends' posts of recent food & drinks visits. As well, restaurants are also able to create pages on the app to showcase their business (similar to Yelp)
- **Market:** Any individual could choose to use this app as long as they have a passion for trying new foods :)
- **Habit:** This app will mainly be used for lifestyle purposes, so depending on the frequency of the user in which they eat/drink out, it may vary. However, even for users who do not often go to restaurants, it also functions as a social media platform for them to see what their friends are trying out.
- **Scope:** First, we would start with the functionality of recommending restaurants to a user based on the preferences they set during registration. Based on location services/permissions and recent activity on the app, Spork will also suggest more restaurants/cafes that will be of interest to the user. Additionally, users can further explore information about the restaurant on their respective pages with live user-curated data. 

## Product Spec

### 1. User Stories (Required and Optional)

**Required Must-have Stories**

* User logs in to access their previously visited restaurants, posted reviews, photos shared to their profile feed 
* User builds their profile based on the ranking for their preferences (e.g. cuisine, price-range, dine-in/take-out, etc)
* Social media feed of food spots that the user's friends have posted about
* After enough data has been curated on the user based on their interests/preferences, the app will then show restaurants on a map based on a recommedation algorithm (weighting factors that may influence the choice)
    * E.g. If a friend has visited the restaurant recently, the weighting for it to appears as a recommendation is 100%
    * If the user ranks price as a #1 choosing factor for where to eat, then a less expensive place will take precedence over a more expensive restaurant
* Camera feature to allow the user to take pictures to share to their news feed or to post reviews on the restaurant page
    * Only allow users to use the in-app camera feature to share pictures/reviews to improve the legitimacy of the review 
* Page for users to share real-time comments about the restaurant -> user-built data 
    * E.g. "Parking is full here, come back later; line is 20 min long"
* Settings (Accesibility, Notification, General, etc.)

**Optional Nice-to-have Stories**
* Search screen that allows the user to manually search for a restaurant's page 
   * Users can also scroll through a list of featured, top-rated restaurants in their area (based on their current location)
* Users can collect special coupons/points by spinning a spinner when their location matches the location of the restaurant which can be redeemed later 
    * The user can also earn additional points based on the $ amount that they have spent (e.g. use OCR to scan the receipt and identify the amount)
    * If the above does not work out, simplify the process to validate that the user has actually purchased a meal at the restaurant they have to take a picture of the receipt 
* Option to bookmark/save restaurants of interest and these can be accessed in a bookmark library 
    
### 2. Screen Archetypes

* Login 
* Register - User signs up or logs into their account
   * Upon download the application, the user is prompted to log in to gain access to their profile information for preferences, saved reviews, recommended restaurants 
* Profile Screen 
   * Recently visited restaurants, preferences, friend list, total points 
* Map Screen
   * Allows user to see where they are and nearby restaurants that match their preferences 
       * Horizontal scroll bar to adjust location proximity radius
* Restaurant Screen
    * Individual restaurant pages showcasing the restaurants contact info, menu info, reviews, and live user updates 
* Settings Screen
   * Lets people change language, preferences, and app notification settings
  
### 3. Navigation

**Tab Navigation** (Tab to Screen)

* Map Layout
* Search
* Social Feed 
* Profile

**Flow Navigation** (Screen to Screen)

* Restaurant Page
   * Map layout (By clicking on pinpoint on map)
   * Search 
* Camera feature
    * Restaurant page (By clicking post review button)
    * Social Feed (By clicking compose post button)
* Setting
   * Menu option from Profile

## Wireframes

### [BONUS] Digital Wireframes & Mockups
https://www.figma.com/file/a7K0nomcTwHmeO7wQDASZV/Spork-Wireframing?node-id=0%3A1

## Schema 

### Models
#### User

   | Property          | Type           | Description |
   | ----------------- | -------------- | ----------- |
   | userId            | String         | unique id for the user (default field) |
   | password          | String         | password of user |
   | name              | String         | name of user |
   | preferences       | Array (String) | ranking of preferences for the user when recommending restaurants |
   | friendsList       | Array (String) | list of friends that the user has |
   | friendsCount      | Integer        | number of friends the user has |
   | previouslyVisited | Array (String) | list of previously visited restaurants

#### Post
   | Property      | Type     | Description |
   | ------------- | -------- | ------------|
   | objectId      | String   | unique id for the user post (default field) |
   | author        | Pointer to User| user that created the post |
   | image         | File     | image that user posts |
   | createdAt     | DateTime | date when post is created (default field) |
   | location      | String   | location/restaurant at which the post is taken at |
   | placeId       | String   | id related to Places API |
   
   
### Networking
- Social Feed Screen
      - (Read/GET) Query all posts where user is name
      - (Create/POST) Create a new like on a post
      - (Delete) Delete existing like
      - (Create/POST) Create a new comment on a post
      - (Delete) Delete existing comment
- Create Post Screen
      - (Create/POST) Create a new post object
- Profile Screen
      - (Read/GET) Query logged in user object
      - (Update/PUT) Update user profile image
 
#### [OPTIONAL:] Existing API Endpoints
##### Yelp API
- Base URL - https://www.yelp.com/dataset/

HTTP Verb | Endpoint | Description
   ----------|----------|------------
    `GET`    | /business_id | get business id
    `GET`    | /name        | get business's name
    `GET`    | /address     | get full address of the business 
    `GET`    | /city        | get city of the business
    `GET`    | /state       | get 2 character state code
    `GET`    | /postal code  | get postal code
    `GET`    | /latitude    | get latitude
    `GET`    | /longitude   | get longitude
    `GET`    | /stars       | get star rating
    `GET`    | /longitude   | get longitude
    
