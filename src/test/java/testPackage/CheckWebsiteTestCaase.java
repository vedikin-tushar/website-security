package testPackage;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

public class CheckWebsiteTestCaase {

    WebDriver driver;

    @BeforeClass
    public void setup() {
        // Set up ChromeDriver path
        System.setProperty("webdriver.chrome.driver", "src\\test\\resources\\driver\\chromedriver.exe");
        driver = new ChromeDriver();
        driver.manage().window().maximize();
    }

    @Test
    public void verifyWebsiteSecurity() throws IOException, InterruptedException {
        // Path to your CSV file
        String csvFilePath = "src/test/resources/csvFile/urls.csv";
        
        // Parse the CSV file
        FileReader reader = new FileReader(Paths.get(csvFilePath).toFile());
        CSVParser csvParser = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(reader);
        List<CSVRecord> records = csvParser.getRecords();

        boolean hasInsecureSite = false;

        for (CSVRecord record : records) {
            String url = record.get("URL");
            System.out.println("Checking URL: " + url);

            // Open the website
            driver.get(url);
            Thread.sleep(3000); // Wait for page to load

            // Check if "Your connection is not private" text is present
            boolean isNotPrivate = driver.getPageSource().contains("Your connection is not private");

            // Print and track security status
            if (isNotPrivate) {
                System.out.println("Checking URL: " + url + " - Website is not secure.");
                hasInsecureSite = true;
            } else {
                System.out.println("Checking URL: " + url + " - Website is secure.");
            }
        }

        reader.close();

        // Fail the test if any website was not secure
        if (hasInsecureSite) {
            Assert.fail("One or more websites are not secure.");
        }
    }

    @AfterClass
    public void teardown() {
        if (driver != null) {
            driver.quit(); // Close the browser after tests
        }
    }
}
