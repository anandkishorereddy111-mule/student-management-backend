Student Management System — Backend
Spring Boot backend for a course registration and payment system, built for PJN Technologies. Handles student registration, Google Sheets as a lightweight database, Razorpay payment processing, and automated email notifications.
Tech Stack
	•	Java 17, Spring Boot
	•	Google Sheets API (data storage)
	•	Razorpay (payments)
	•	Spring Mail (Gmail SMTP notifications)
	•	Maven
Features
	•	Student registration synced to Google Sheets
	•	Course catalog management (CRUD via admin portal)
	•	Razorpay order creation + payment signature verification
	•	Automatic payment status updates in Sheets
	•	Email confirmations on registration and payment
	•	Image upload for course thumbnails
Project Structure
src/main/java/com/students/
├── controller/   # REST endpoints
├── service/      # Business logic (Sheets, Email, Razorpay)
├── dto/          # Request/response models
└── config/       # CORS, Sheets client config
admin-portal/     # React admin dashboard for course management
Setup Prerequisites
	•	Java 17+
	•	Maven
	•	A Google Cloud service account with Sheets API access
	•	Razorpay test/live API keys
	•	Gmail account with an App Password
Configuration
This project keeps secrets out of version control. Create src/main/resources/application-secrets.properties (not tracked by git) with:
spring.mail.username=your-email@gmail.com
spring.mail.password=your-gmail-app-password
razorpay.key.id=your-razorpay-key-id
razorpay.key.secret=your-razorpay-key-secret
Also place your Google service account credentials at:src/main/resources/service-account-key.json
Run the backend: ./mvnw spring-boot:run  --runs on http://localhost:8080.
Run the admin portal  :  cd admin-portal
                         npm install
                         npm run dev --runs on http://localhost:5173.
API Overview:
|Endpoint                   |Method  |Purpose                              |
|/api/students/register   |POST    |Register a new student                 |
|/api/payment/create-order|POST    |Create Razorpay order                  |
|/api/payment/verify      |POST    |Verify payment signature, update status|
|/api/courses             |GET/POST|Manage course catalog |