Bike Rental Management Component

This ZIP contains only the Bike Rental Management component.

Component Name:
Bike Rental Management Component

Purpose:
This component handles the rental/booking part of the bike rental system.

3 UI in this component:

1. Add Rental UI
   URL: /rentals
   File: src/main/resources/templates/rentals.html
   Purpose: User can create a new bike rental booking.
   CRUD Operation: Create

2. View My Rentals UI
   URL: /rentals
   File: src/main/resources/templates/rentals.html
   Purpose: User can view their own rental bookings.
   CRUD Operation: Read

3. Admin Rental Records UI
   URL: /admin/rentals
   File: src/main/resources/templates/rentals.html
   Purpose: Admin can view all customer rental records.
   CRUD Operation: Read

Key Components:

1. Rental.java
   Location: src/main/java/com/example/bikerental/model/Rental.java
   Purpose: OOP model class that stores one rental record as an object.

2. RentalFileService.java
   Location: src/main/java/com/example/bikerental/service/RentalFileService.java
   Purpose: Handles file handling for rental records.

3. RentalViewController.java
   Location: src/main/java/com/example/bikerental/controller/RentalViewController.java
   Purpose: Controls /rentals and /admin/rentals routes.

4. rentals.html
   Location: src/main/resources/templates/rentals.html
   Purpose: UI page for Add Rental, View My Rentals, and Admin Rental Records.

5. rentals.css
   Location: src/main/resources/static/css/rentals.css
   Purpose: Styling for the rental UI.

6. rentals.txt
   Purpose: Text file used to store rental data.

CRUD Explanation:
Create: User creates a rental booking.
Read: User views own rentals and admin views all rentals.
Update: Rental details/status can be updated in the service/file handling layer.
Delete: Rental records can be deleted from rentals.txt using file handling.

Viva Explanation:
My component is the Bike Rental Management component. It has three UIs: Add Rental UI, View My Rentals UI, and Admin Rental Records UI. I used Java OOP through Rental.java, file handling through RentalFileService.java, and Spring Boot MVC through RentalViewController.java. Rental data is stored in rentals.txt.
