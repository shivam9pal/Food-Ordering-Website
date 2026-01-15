# Food Ordering Website 🍕

A full-stack food ordering web application built with Spring Boot, featuring user authentication, role-based access control, and comprehensive testing.

## 🚀 Features

- **User Authentication**: Secure JWT-based authentication system
- **Role-Based Access Control**: Support for multiple user roles (Customer, Restaurant Owner, Admin)
- **Spring Security**: Protected endpoints with Spring Security integration
- **RESTful API**: Well-structured REST APIs for food ordering operations
- **Database Integration**: MySQL for production, H2 for testing
- **Comprehensive Testing**: 25+ unit and integration tests with 100% pass rate
- **Lombok Integration**: Clean and concise code with reduced boilerplate

## 🛠️ Technology Stack

### Backend
- **Framework**: Spring Boot 3.5.3
- **Language**: Java 17
- **Build Tool**: Maven
- **Security**: Spring Security + JWT (JSON Web Tokens)
- **Database**: MySQL (Production) / H2 (Testing)
- **ORM**: Spring Data JPA with Hibernate
- **Testing**: JUnit 5, Mockito, Spring Boot Test

### Key Dependencies
- `spring-boot-starter-web` - RESTful web services
- `spring-boot-starter-data-jpa` - Database operations
- `spring-boot-starter-security` - Authentication & authorization
- `mysql-connector-j` - MySQL database driver
- `jjwt` - JWT token generation and validation
- `lombok` - Code generation and boilerplate reduction
- `h2` - In-memory database for testing

## 📋 Prerequisites

Before running this application, make sure you have:

- **Java Development Kit (JDK) 17** or higher
- **Maven 3.6+** for building the project
- **MySQL Database** (for production environment)
- **IDE** (IntelliJ IDEA, Eclipse, or VS Code recommended)

## 🔧 Installation & Setup

### 1. Clone the Repository
```bash
git clone https://github.com/shivam9pal/Food-Ordering-Website.git
cd Food-Ordering-Website
```

### 2. Configure Database
Create a MySQL database and update `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/online_food
spring.datasource.username=your_username
spring.datasource.password=your_password
spring.jpa.hibernate.ddl-auto=update
```

### 3. Build the Project
```bash
# Clean and build
mvn clean install

# Skip tests if needed
mvn clean install -DskipTests
```

### 4. Run the Application
```bash
# Using Maven
mvn spring-boot:run

# Or using the Maven wrapper
./mvnw spring-boot:run

# On Windows
mvnw.cmd spring-boot:run
```

The application will start on `http://localhost:8080`

## 🧪 Testing

This project includes comprehensive unit and integration tests covering authentication, user management, and JWT token operations.

### Run All Tests
```bash
mvn test
```

### Run Specific Test Class
```bash
mvn test -Dtest=AuthControllerTest
```

### Run with Detailed Output
```bash
mvn test -Dsurefire.useFile=false
```

### Windows Users
```cmd
run-tests.bat
```

### Test Coverage
- **Unit Tests**: AuthController, CustomerUserDetailsService, JwtProvider
- **Integration Tests**: Complete signup flow with database operations
- **Total Tests**: 25+ tests
- **Pass Rate**: 100%

For detailed testing information, see [TESTING.md](TESTING.md)

## 📁 Project Structure

```
Food-Ordering-Website/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── Food/
│   │   │           ├── controller/      # REST API controllers
│   │   │           ├── service/         # Business logic
│   │   │           ├── repository/      # Database operations
│   │   │           ├── model/           # Entity classes
│   │   │           ├── config/          # Configuration classes
���   │   │           └── security/        # JWT & Security config
│   │   └── resources/
│   │       ├── application.properties   # App configuration
│   │       └── application-test.properties
│   └── test/
│       └── java/
│           └── com/
│               └── Food/
│                   ├── controller/      # Controller tests
│                   ├── service/         # Service tests
│                   └── integration/     # Integration tests
├── pom.xml                              # Maven dependencies
├── README.md                            # This file
├── TESTING.md                           # Testing documentation
└── run-tests.bat                        # Windows test script
```

## 🔐 API Endpoints

### Authentication
```http
POST /auth/signup
Content-Type: application/json

{
  "email": "user@example.com",
  "password": "password123",
  "fullName": "John Doe",
  "role": "CUSTOMER"  // or "RESTAURANT_OWNER"
}
```

More endpoints will be documented as they are implemented.

## 🎯 User Roles

The application supports multiple user roles:

1. **CUSTOMER**: Regular users who can browse and order food
2. **RESTAURANT_OWNER**: Users who can manage restaurants and menus
3. **ADMIN**: Administrative users with full system access

## 🔑 JWT Authentication

The application uses JWT (JSON Web Tokens) for stateless authentication:

1. User registers/logs in
2. Server generates JWT token
3. Client includes token in Authorization header
4. Server validates token for protected endpoints

```http
Authorization: Bearer <your-jwt-token>
```

## 🚧 Development

### Running in Development Mode
```bash
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

### Building for Production
```bash
mvn clean package -DskipTests
java -jar target/Food-0.0.1-SNAPSHOT.jar
```

## 📊 Database Schema

The application uses the following main entities:
- **User**: User account information
- **Restaurant**: Restaurant details (coming soon)
- **Food**: Food items (coming soon)
- **Order**: Order information (coming soon)

## 🤝 Contributing

Contributions are welcome! Please follow these steps:

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

### Code Style
- Follow Java naming conventions
- Write meaningful commit messages
- Add tests for new functionality
- Update documentation as needed

## 📝 License

This project is open source and available under the [MIT License](LICENSE).

## 👤 Author

**Shivam Pal**
- GitHub: [@shivam9pal](https://github.com/shivam9pal)

## 🙏 Acknowledgments

- Spring Boot team for the excellent framework
- Spring Security for robust authentication
- All contributors and supporters

## 📞 Support

For support, email or open an issue in the GitHub repository.

---

**Note**: This project is currently in active development. Features and documentation will be updated regularly.

**Last Updated**: January 2026