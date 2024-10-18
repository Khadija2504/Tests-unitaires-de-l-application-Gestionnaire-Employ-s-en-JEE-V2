package com.hrmanagementsystem.service;

import com.hrmanagementsystem.dao.interfaces.EmployeeInterface;
import com.hrmanagementsystem.entity.User;
import com.hrmanagementsystem.enums.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.text.ParseException;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import static org.junit.jupiter.api.Assertions.assertEquals;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EmployeeServiceTest {

    @Mock
    private EmployeeInterface employeeInterface;

    private EmployeeService employeeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        employeeService = new EmployeeService(employeeInterface);
    }

    @Test
    void testSave() throws ParseException {
        // Arrange
        String firstName = "John";
        String lastName = "Doe";
        String phoneNumber = "1234567890";
        int salary = 5000;
        String birthdayStr = "1990-01-01";
        String hireDateStr = "2023-01-01";
        String position = "Developer";
        int kidsNum = 2;
        String situation = "Married";
        String department = "IT";
        String email = "john.doe@example.com";
        String password = "password123";
        String nssu = "123456";

        when(employeeInterface.save(any(User.class))).thenReturn(true);

        // Act
        boolean result = employeeService.save(firstName, lastName, phoneNumber, salary, birthdayStr, hireDateStr,
                position, kidsNum, situation, department, email, password, nssu);

        // Assert
        assertTrue(result);
        verify(employeeInterface).save(argThat(user ->
                user.getFirstName().equals(firstName) &&
                        user.getLastName().equals(lastName) &&
                        user.getPhoneNumber().equals(phoneNumber) &&
                        user.getSalary() == salary &&
                        user.getPosition().equals(position) &&
                        user.getKidsNum() == kidsNum &&
                        user.getSituation().equals(situation) &&
                        user.getDepartment().equals(department) &&
                        user.getEmail().equals(email) &&
                        user.getNssu().equals(nssu) &&
                        user.getRole() == Role.Employee &&
                        user.getTotalSalary() == 5600 // 5000 + (2 * 300)
        ));
    }

    @Test
    void testDelete() {
        // Arrange
        int id = 1;

        // Act
        employeeService.delete(id);

        // Assert
        verify(employeeInterface).delete(id);
    }

    @Test
    void testUpdate() throws ParseException {
        // Arrange
        int id = 1;
        String firstName = "Jane";
        String lastName = "Doe";
        String phoneNumber = "9876543210";
        int salary = 7000;
        String birthdayStr = "1985-05-05";
        String hireDateStr = "2022-01-01";
        String position = "Manager";
        int kidsNum = 3;
        String situation = "Married";
        String department = "HR";
        String email = "jane.doe@example.com";
        String password = "newpassword123";
        String nssu = "654321";

        when(employeeInterface.update(any(User.class))).thenReturn(true);

        boolean result = employeeService.update(id, firstName, lastName, phoneNumber, salary, birthdayStr, hireDateStr,
                position, kidsNum, situation, department, email, password, nssu);

        assertTrue(result);
        verify(employeeInterface).update(argThat(user ->
                user.getId() == id &&
                        user.getFirstName().equals(firstName) &&
                        user.getLastName().equals(lastName) &&
                        user.getPhoneNumber().equals(phoneNumber) &&
                        user.getSalary() == salary &&
                        user.getPosition().equals(position) &&
                        user.getKidsNum() == kidsNum &&
                        user.getSituation().equals(situation) &&
                        user.getDepartment().equals(department) &&
                        user.getEmail().equals(email) &&
                        user.getPassword().equals(password) &&
                        user.getNssu().equals(nssu) &&
                        user.getRole() == Role.Employee &&
                        user.getTotalSalary() == 7750
        ));
    }

    @ParameterizedTest
    @CsvSource({
            "5000, 2, 600",
            "6000, 2, 600",
            "7000, 3, 750",
            "8000, 1, 250",
            "8001, 1, 200",
            "9000, 4, 710",
            "6000, 6, 1350",
            "8000, 6, 1500",
            "10000, 0, 0",
            "8000, 8, 1500"
    })
    void testCalculateFamilyAllowanceDirectly(int salary, int kidsNum, int expectedAllowance) {
        EmployeeService service = new EmployeeService(null);
        int actualAllowance = service.calculateFamilyAllowance(salary, kidsNum);
        assertEquals(expectedAllowance, actualAllowance,
                String.format("For salary %d and %d kids, expected %d but got %d",
                        salary, kidsNum, expectedAllowance, actualAllowance));
    }

    @Test
    void testGetByEmail() {
        // Arrange
        String email = "test@example.com";
        when(employeeInterface.getByEmail(email)).thenReturn(true);

        // Act
        boolean result = employeeService.getByEmail(email);

        // Assert
        assertTrue(result);
        verify(employeeInterface).getByEmail(email);
    }

    @Test
    void testGetByUsername() {
        // Arrange
        String username = "johndoe";
        User expectedUser = new User();
        expectedUser.setFirstName("John");
        expectedUser.setLastName("Doe");
        when(employeeInterface.getByUsername(username)).thenReturn(expectedUser);

        // Act
        User result = employeeService.getByUsername(username);

        // Assert
        assertNotNull(result);
        assertEquals("John", result.getFirstName());
        assertEquals("Doe", result.getLastName());
        verify(employeeInterface).getByUsername(username);
    }

    @Test
    void testGetAll() {
        List<User> expectedUsers = Arrays.asList(
                new User("John", "Doe", "1234567890", 5000, null, null, "Developer", 2, 5600, "Married", "IT", "john@example.com", "password", "123456", Role.Employee, 0),
                new User("Jane", "Smith", "0987654321", 6000, null, null, "Manager", 1, 6300, "Single", "HR", "jane@example.com", "password", "654321", Role.Employee, 0)
        );
        when(employeeInterface.getAll()).thenReturn(expectedUsers);

        // Act
        List<User> result = employeeService.getAll();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("John", result.get(0).getFirstName());
        assertEquals("Jane", result.get(1).getFirstName());
        verify(employeeInterface).getAll();
    }

    @Test
    void testFindByNssu() {
        // Arrange
        String nssu = "123456";
        User expectedUser = new User();
        expectedUser.setFirstName("John");
        expectedUser.setLastName("Doe");
        expectedUser.setNssu(nssu);
        when(employeeInterface.findByNssu(nssu)).thenReturn(expectedUser);

        // Act
        User result = employeeService.findByNssu(nssu);

        // Assert
        assertNotNull(result);
        assertEquals("John", result.getFirstName());
        assertEquals("Doe", result.getLastName());
        assertEquals(nssu, result.getNssu());
        verify(employeeInterface).findByNssu(nssu);
    }
}