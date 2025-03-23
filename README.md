# Kshirsa Money Backend

A comprehensive financial management application designed to help users track, categorize, and manage their budgets effectively.

## Features

- **User Management**
    - Secure user registration and authentication
    - Profile management with JWT-based security
    - Role-based access control

- **Budget Management**
    - Create hierarchical budget segments (parent-child relationships)
    - Set budget allocations for different categories
    - Configure spending alerts based on percentage thresholds
    - Track spending against allocated amounts

- **Transaction Tracking**
    - Categorize transactions
    - Associate transactions with budget segments
    - View spending patterns and history

- **Reporting & Analytics**
    - Budget utilization metrics
    - Historical budget comparisons
    - Category-based spending analysis
    - Monthly budget history tracking

- **Notifications**
    - Email alerts for budget thresholds
    - Account activity notifications
    - Regular spending reports

## Technologies

### Backend
- Java 21
- Spring Boot 3.3.7
- Spring Data JPA
- Spring Security with JWT Authentication
- PostgreSQL Database
- Gradle Build System 8.11

### API Services
- Email Service (Gmail SMTP)
- Email Validation API (RapidAPI MailCheck)
- GeoLite2 for location services

### DevOps & Deployment
- Docker (containerization)
- CI/CD Pipeline support

## Project Structure

The application follows a microservice architecture with the following components:
- `budgetingservice` - Manages budget segments and allocations
- `trackingservice` - Handles transaction tracking and categorization
- `userservice` - Manages user authentication and profiles