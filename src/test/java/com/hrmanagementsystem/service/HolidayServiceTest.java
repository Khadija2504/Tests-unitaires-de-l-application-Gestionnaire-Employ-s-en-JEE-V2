package com.hrmanagementsystem.service;

import com.hrmanagementsystem.dao.interfaces.HolidayInterface;
import com.hrmanagementsystem.entity.Holiday;
import com.hrmanagementsystem.entity.User;
import com.hrmanagementsystem.enums.HolidayStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.servlet.http.Part;
import java.io.*;
        import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

        import static org.junit.jupiter.api.Assertions.*;
        import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HolidayServiceTest {

    @Mock
    private HolidayInterface holidayInterface;

    @Mock
    private Part filePart;

    private HolidayService holidayService;

    @BeforeEach
    void setUp() {
        holidayService = new HolidayService(holidayInterface);
    }

    @Test
    void testAddHoliday() throws ParseException, IOException {
        User employee = new User();
        String startDateStr = "2023-07-01";
        String endDateStr = "2023-07-05";
        String reason = "Vacation";
        String uploadFilePath = "test_uploads";
        String fileName = "test.txt";

        when(filePart.getHeader("content-disposition")).thenReturn("filename=\"" + fileName + "\"");
        when(holidayInterface.getAcceptedHolidaysForEmployee(employee)).thenReturn(new ArrayList<>());

        holidayService.addHoliday(startDateStr, endDateStr, reason, uploadFilePath, filePart, employee);

        verify(holidayInterface).save(any(Holiday.class));
        verify(filePart).write(anyString());
    }

    @Test
    void testAddHolidayExceedingLimit() throws ParseException, IOException {
        User employee = new User();
        String startDateStr = "2023-07-01";
        String endDateStr = "2023-08-01";
        String reason = "Long Vacation";
        String uploadFilePath = "test_uploads";
        String fileName = "test.txt";

        when(filePart.getHeader("content-disposition")).thenReturn("filename=\"" + fileName + "\"");

        List<Holiday> existingHolidays = new ArrayList<>();
        existingHolidays.add(new Holiday(new SimpleDateFormat("yyyy-MM-dd").parse("2023-06-01"),
                new SimpleDateFormat("yyyy-MM-dd").parse("2023-06-15"), "Previous Vacation", "path", employee));

        when(holidayInterface.getAcceptedHolidaysForEmployee(employee)).thenReturn(existingHolidays);

        assertThrows(IllegalArgumentException.class, () ->
                holidayService.addHoliday(startDateStr, endDateStr, reason, uploadFilePath, filePart, employee));

        verify(holidayInterface).getAcceptedHolidaysForEmployee(employee);
        verify(filePart).getHeader("content-disposition");
        verify(filePart).write(anyString());
    }

    @Test
    void testGetAcceptedHolidaysForEmployee() {
        User employee = new User();
        List<Holiday> expectedHolidays = new ArrayList<>();
        when(holidayInterface.getAcceptedHolidaysForEmployee(employee)).thenReturn(expectedHolidays);

        List<Holiday> actualHolidays = holidayService.getAcceptedHolidaysForEmployee(employee);

        assertEquals(expectedHolidays, actualHolidays);
        verify(holidayInterface).getAcceptedHolidaysForEmployee(employee);
    }

    @Test
    void testGenerateAbsenceReport() {
        User employee1 = new User();
        User employee2 = new User();
        List<User> employees = Arrays.asList(employee1, employee2);

        List<Holiday> holidays1 = new ArrayList<>();
        holidays1.add(new Holiday());
        List<Holiday> holidays2 = new ArrayList<>();

        when(holidayInterface.getAcceptedHolidaysForEmployee(employee1)).thenReturn(holidays1);
        when(holidayInterface.getAcceptedHolidaysForEmployee(employee2)).thenReturn(holidays2);

        Map<User, List<Holiday>> report = holidayService.generateAbsenceReport(employees);

        assertEquals(1, report.size());
        assertTrue(report.containsKey(employee1));
        assertFalse(report.containsKey(employee2));
        assertEquals(holidays1, report.get(employee1));
    }

    @Test
    void testGetAllHolidays() {
        List<Holiday> expectedHolidays = new ArrayList<>();
        when(holidayInterface.getAllHolidays()).thenReturn(expectedHolidays);

        List<Holiday> actualHolidays = holidayService.getAllHolidays();

        assertEquals(expectedHolidays, actualHolidays);
        verify(holidayInterface).getAllHolidays();
    }

    @Test
    void testGetById() {
        int id = 1;
        Holiday expectedHoliday = new Holiday();
        when(holidayInterface.getById(id)).thenReturn(expectedHoliday);

        Holiday actualHoliday = holidayService.getById(id);

        assertEquals(expectedHoliday, actualHoliday);
        verify(holidayInterface).getById(id);
    }

    @Test
    void testUpdate() {
        int holidayId = 1;
        String newStatus = "Approved";
        Holiday holiday = new Holiday();
        when(holidayInterface.getById(holidayId)).thenReturn(holiday);

        holidayService.update(holidayId, newStatus);

        assertEquals(HolidayStatus.Approved, holiday.getStatus());
        verify(holidayInterface).update(holiday);
    }

    @Test
    void testDownloadJustification() throws IOException {
        Holiday holiday = new Holiday();
        String justificationPath = "test_justification.txt";
        holiday.setJustification(justificationPath);

        File tempFile = File.createTempFile("test_justification", ".txt");
        tempFile.deleteOnExit();
        try (FileWriter writer = new FileWriter(tempFile)) {
            writer.write("Test justification content");
        }

        holiday.setJustification(tempFile.getAbsolutePath());

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        holidayService.downloadJustification(holiday, outputStream);

        String downloadedContent = outputStream.toString();
        assertEquals("Test justification content", downloadedContent);
    }

    @Test
    void testGetJustificationFileName() {
        Holiday holiday = new Holiday();
        String justificationPath = "/path/to/justification.pdf";
        holiday.setJustification(justificationPath);

        String fileName = holidayService.getJustificationFileName(holiday);

        assertEquals("justification.pdf", fileName);
    }
}
