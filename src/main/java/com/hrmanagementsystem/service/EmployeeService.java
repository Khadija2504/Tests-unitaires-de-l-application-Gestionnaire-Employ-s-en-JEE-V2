package com.hrmanagementsystem.service;

import com.hrmanagementsystem.dao.interfaces.EmployeeInterface;
import com.hrmanagementsystem.entity.User;
import com.hrmanagementsystem.enums.Role;
import org.mindrot.jbcrypt.BCrypt;

import javax.persistence.NoResultException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class EmployeeService {
    protected EmployeeInterface employeeInterface;
    public EmployeeService(EmployeeInterface employeeInterface) {
        this.employeeInterface = employeeInterface;
    }

    public User getById(int id) {
        return employeeInterface.getById(id);
    }


    public boolean save(String firstName, String lastName, String phoneNumber, int salary,
                               String birthdayStr, String hireDateStr, String position, int kidsNum,
                               String situation, String department, String email, String password, String nssu) throws ParseException, ParseException {

        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date birthday = dateFormat.parse(birthdayStr);
        Date hireDate = dateFormat.parse(hireDateStr);

        int familyAllowance = calculateFamilyAllowance(salary, kidsNum);
        int totalSalary = salary + familyAllowance;

        User user = new User(firstName, lastName, phoneNumber, salary, birthday, hireDate, position, kidsNum,
                totalSalary, situation, department, email, hashedPassword, nssu, Role.Employee);

        return employeeInterface.save(user);
    }

    public int calculateFamilyAllowance(int salary, int kidsNum) {
        int allowancePerChild;
        int maxChildren = Math.min(kidsNum, 6);

        if (salary < 6000) {
            allowancePerChild = 300;
        } else if (salary > 8000) {
            allowancePerChild = 200;
        } else {
            double factor = (salary - 6000.0) / 2000.0;
            allowancePerChild = (int) (300 - (factor * 100));
        }

        int totalAllowance = 0;
        for (int i = 0; i < maxChildren; i++) {
            if (i < 3) {
                totalAllowance += allowancePerChild;
            } else {
                totalAllowance += (salary < 6000) ? 150 : 110;
            }
        }

        return totalAllowance;
    }

    public void delete(int id) {
        employeeInterface.delete(id);
    }

    public boolean update(int id, String firstName, String lastName, String phoneNumber, int salary,
                                  String birthdayStr, String hireDateStr, String position, int kidsNum,
                                  String situation, String department, String email, String password, String nssu) throws ParseException {

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date birthday = dateFormat.parse(birthdayStr);
        Date hireDate = dateFormat.parse(hireDateStr);

        int familyAllowance = calculateFamilyAllowance(salary, kidsNum);
        int totalSalary = salary + familyAllowance;

        User user = new User(firstName, lastName, phoneNumber, salary, birthday, hireDate, position, kidsNum,
                totalSalary, situation, department, email, password, nssu, Role.Employee);
        user.setId(id);

        return employeeInterface.update(user);
    }

    public double calculateFamilyAllowanceReport(User employee, int salary, int kidsNum) {
        int allowancePerChild;
        int maxChildren = Math.min(kidsNum, 6);

        if (salary < 6000) {
            allowancePerChild = 300;
        } else if (salary > 8000) {
            allowancePerChild = 200;
        } else {
            double factor = (salary - 6000.0) / 2000.0;
            allowancePerChild = (int) (300 - (factor * 100));
        }

        double totalAllowance = 0;
        for (int i = 0; i < maxChildren; i++) {
            if (i < 3) {
                totalAllowance += allowancePerChild;
            } else {
                totalAllowance += (salary < 6000) ? 150 : 110;
            }
        }

        return totalAllowance;
    }

    public Map<String, Object> generateFamilyAllowanceStats() {
        List<User> employees = employeeInterface.getAll();
        Map<String, Map<String, Double>> monthlyStats = new HashMap<>();
        Map<Integer, Double> employeeStats = new HashMap<>();

        LocalDate currentDate = LocalDate.now();
        int currentYear = currentDate.getYear();

        for (User employee : employees) {
            double totalAllowance = 0;
            for (int month = 1; month <= 12; month++) {
                double monthlyAllowance = calculateFamilyAllowanceReport(employee, employee.getSalary(), employee.getKidsNum());
                totalAllowance += monthlyAllowance;

                String monthKey = String.format("%04d-%02d", currentYear, month);
                monthlyStats.computeIfAbsent(monthKey, k -> new HashMap<>())
                        .merge("total", monthlyAllowance, Double::sum);
                monthlyStats.get(monthKey).merge("count", 1.0, Double::sum);
            }
            employeeStats.put(employee.getId(), totalAllowance);
        }

        Map<String, Object> result = new HashMap<>();
        result.put("monthlyStats", monthlyStats);
        result.put("employeeStats", employeeStats);
        result.put("employees", employees);

        return result;
    }

    public List<User> getAll() {
        return employeeInterface.getAll();
    }

    public void initializeAdminUser() {
        try {
            User adminUser = employeeInterface.getByUsername("admin");
            System.out.println("Admin user already exists.");
        } catch (NoResultException e) {
            User adminUser = new User();
            adminUser.setFirstName("admin");
            String hashedPassword = BCrypt.hashpw("admin123", BCrypt.gensalt());
            adminUser.setPassword(hashedPassword);
            adminUser.setEmail("admin@example.com");
            adminUser.setRole(Role.Admin);

            employeeInterface.save(adminUser);
            System.out.println("Default admin user created.");
        }
    }

}
