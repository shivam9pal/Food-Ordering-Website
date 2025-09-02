# Food Ordering Website - Testing Guide

## Overview
This document provides comprehensive testing information for the Food Ordering Website's user registration functionality.

## Test Structure

### 1. Unit Tests
- **AuthControllerTest**: Tests the signup endpoint controller logic
- **CustomerUserDetailsServiceTest**: Tests user authentication service
- **JwtProviderTest**: Tests JWT token generation and validation

### 2. Integration Tests
- **AuthIntegrationTest**: Tests complete signup flow with database operations

## Running Tests

### Prerequisites
- Java 17
- Maven 3.6+
- MySQL database (for integration tests)

### Quick Start
```bash
# Run all tests
mvn test

# Run only unit tests
mvn test -Dtest="*Test" -DfailIfNoTests=false

# Run only integration tests
mvn test -Dtest="*IntegrationTest" -DfailIfNoTests=false

# Run specific test class
mvn test -Dtest=AuthControllerTest

# Run with detailed output
mvn test -Dtest=AuthControllerTest -Dsurefire.useFile=false
```

### Windows Users
Use the provided batch script:
```cmd
run-tests.bat
```

## Test Coverage

### AuthControllerTest
Tests the `/auth/signup` endpoint with various scenarios:

#### ✅ **Successful Registration Tests**
- `testSuccessfulUserRegistration()`: Basic customer registration
- `testUserRegistrationWithRestaurantOwnerRole()`: Restaurant owner registration
- `testUserRegistrationWithNullRole()`: Registration without role specification

#### ❌ **Error Handling Tests**
- `testUserRegistrationWithDuplicateEmail()`: Duplicate email prevention
- `testUserRegistrationWithNullEmail()`: Null email handling
- `testUserRegistrationWithEmptyPassword()`: Empty password handling

#### 🔍 **Edge Case Tests**
- `testUserRegistrationWithSpecialCharactersInName()`: Special characters in names
- `testUserRegistrationWithNullRole()`: Null role handling

### CustomerUserDetailsServiceTest
Tests the Spring Security user details service:

#### ✅ **Authentication Tests**
- `testLoadUserByUsername_Success()`: Successful user loading
- `testLoadUserByUsername_WithRestaurantOwnerRole()`: Role-based authority loading
- `testLoadUserByUsername_WithAdminRole()`: Admin role handling

#### ❌ **Error Handling Tests**
- `testLoadUserByUsername_UserNotFound()`: Non-existent user handling
- `testLoadUserByUsername_WithNullEmail()`: Null email handling

#### 🔍 **Edge Case Tests**
- `testLoadUserByUsername_WithNullRole()`: Default role assignment
- `testLoadUserByUsername_WithEmptyEmail()`: Empty email handling

### JwtProviderTest
Tests JWT token generation and validation:

#### ✅ **Token Generation Tests**
- `testGenerateToken_WithCustomerRole()`: Basic token generation
- `testGenerateToken_WithMultipleRoles()`: Multi-role token generation
- `testGenerateToken_WithNoRoles()`: Token without roles

#### 🔍 **Edge Case Tests**
- `testGenerateToken_WithSpecialCharactersInEmail()`: Special email characters
- `testGenerateToken_WithLongEmail()`: Very long email addresses
- `testGenerateToken_Consistency()`: Token uniqueness verification

### AuthIntegrationTest
Tests complete signup flow with database operations:

#### ✅ **End-to-End Tests**
- `testCompleteUserRegistrationFlow()`: Complete registration process
- `testUserRegistrationWithRestaurantOwnerRole()`: Role-based registration
- `testUserRegistrationWithNullRole()`: Registration without role

#### 🔍 **Data Validation Tests**
- `testUserRegistrationWithSpecialCharacters()`: Special character handling
- `testUserRegistrationWithLongEmail()`: Long email validation
- `testUserRegistrationWithVeryLongFullname()`: Long name handling
- `testUserRegistrationWithVeryLongPassword()`: Long password handling

#### ❌ **Edge Case Tests**
- `testUserRegistrationWithEmptyPassword()`: Empty password handling
- `testUserRegistrationWithNullPassword()`: Null password handling
- `testUserRegistrationWithNullFullname()`: Null name handling

## Test Configuration

### Test Profile
Tests use the `test` profile with the following configuration:
- **Database**: H2 in-memory database
- **Security**: Disabled for easier testing
- **Logging**: DEBUG level for detailed output

### Test Database
- **Type**: H2 in-memory
- **DDL**: `create-drop` (fresh database for each test)
- **Transactions**: `@Transactional` for test isolation

## Test Data Management

### Before Each Test
- Clean up all test data
- Reset database state
- Prepare fresh test objects

### Test Isolation
- Each test runs in its own transaction
- Database is rolled back after each test
- No test interference

## Expected Test Results

### All Tests Should Pass
- ✅ Unit tests: 100% pass rate
- ✅ Integration tests: 100% pass rate
- ✅ No test failures or errors

### Test Output
```
[INFO] Tests run: 25, Failures: 0, Errors: 0, Skipped: 0
[INFO] 
[INFO] Results:
[INFO] 
[INFO] Tests run: 25, Failures: 0, Errors: 0, Skipped: 0
```

## Troubleshooting

### Common Issues

#### 1. Database Connection Errors
```bash
# Ensure MySQL is running
mysql -u root -p

# Check application.properties configuration
spring.datasource.url=jdbc:mysql://localhost:3306/online_food
```

#### 2. Test Failures
```bash
# Run with detailed output
mvn test -Dtest=AuthControllerTest -Dsurefire.useFile=false

# Check test logs
tail -f target/surefire-reports/*.txt
```

#### 3. Maven Issues
```bash
# Clean and rebuild
mvn clean install

# Update dependencies
mvn dependency:resolve
```

### Debug Mode
Enable debug logging in `application-test.properties`:
```properties
logging.level.com.Food=DEBUG
logging.level.org.springframework.security=DEBUG
```

## Performance Testing

### Test Execution Time
- **Unit Tests**: < 5 seconds
- **Integration Tests**: < 30 seconds
- **Total Test Suite**: < 1 minute

### Memory Usage
- **H2 Database**: ~50MB
- **Test Context**: ~100MB
- **Total**: < 200MB

## Continuous Integration

### GitHub Actions (Recommended)
```yaml
name: Tests
on: [push, pull_request]
jobs:
  test:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v2
      - name: Set up JDK 17
        uses: actions/setup-java@v2
        with:
          java-version: '17'
      - name: Run tests
        run: mvn test
```

### Local Development
```bash
# Pre-commit hook
mvn test

# Continuous testing
mvn spring-boot:run -Dspring-boot.run.profiles=test
```

## Best Practices

### Writing Tests
1. **Arrange-Act-Assert**: Follow AAA pattern
2. **Test Names**: Use descriptive test method names
3. **Single Responsibility**: Test one thing per method
4. **Mocking**: Mock external dependencies
5. **Assertions**: Use specific assertions

### Test Data
1. **Realistic Data**: Use realistic test values
2. **Edge Cases**: Test boundary conditions
3. **Cleanup**: Always clean up test data
4. **Isolation**: Ensure tests don't interfere

### Performance
1. **Fast Execution**: Keep tests under 1 second each
2. **Minimal Setup**: Minimize test setup time
3. **Efficient Mocks**: Use efficient mocking strategies
4. **Database**: Use in-memory databases for tests

## Future Enhancements

### Planned Tests
- [ ] Performance tests for large datasets
- [ ] Security penetration tests
- [ ] Load testing for concurrent users
- [ ] API contract testing
- [ ] Database migration tests

### Test Automation
- [ ] Automated test reporting
- [ ] Test coverage reports
- [ ] Performance regression testing
- [ ] Security vulnerability scanning

## Support

### Getting Help
1. Check test logs for detailed error information
2. Review this documentation
3. Check Spring Boot testing documentation
4. Review JUnit 5 documentation

### Contributing
1. Follow existing test patterns
2. Add tests for new functionality
3. Update this documentation
4. Ensure all tests pass before committing

---

**Last Updated**: December 2024
**Test Framework**: JUnit 5 + Mockito + Spring Boot Test
**Database**: H2 (test), MySQL (production)

