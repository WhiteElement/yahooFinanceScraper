import org.openqa.selenium.By;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.awt.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;
import java.util.List;
import java.util.Properties;


public class ShortCanScraper {

    private static String YAHOOSCREENERURL;
    private static String EDGEDRIVERPATH;
    private EdgeDriver driver;

    public ShortCanScraper() {

        readPropertiesfromFile();

        driver = createEdgeDriver();

        moveDrivertotheLeft(driver);

        scrape();
    }

    private void readPropertiesfromFile() {
        try{
            InputStream input = new FileInputStream("src/main/config.properties");

            Properties properties = new Properties();
            properties.load(input);

            YAHOOSCREENERURL = properties.getProperty("yahooscreenerurl");
            EDGEDRIVERPATH = properties.getProperty("driverlocation");

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void moveDrivertotheLeft(EdgeDriver driver) {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        double width = screenSize.getWidth();
        double height = screenSize.getHeight();
        driver.manage().window().setSize(new org.openqa.selenium.Dimension((int) width / 2, (int) height));
    }

    private EdgeDriver createEdgeDriver() {
        System.setProperty("webdriver.edge.driver", EDGEDRIVERPATH);

        EdgeOptions options = new EdgeOptions();

        EdgeDriver driver = new EdgeDriver(options);
        driver.manage().window().setPosition(new Point(0, 0));
        return driver;
    }

    public void scrape() {
        driver.get(YAHOOSCREENERURL);

        //Warten bis Element da ist
        WebElement firstResult = new WebDriverWait(driver, Duration.ofSeconds(5))
                .until(ExpectedConditions.elementToBeClickable(By.name("agree")));

        //Cookies annehmen
        WebElement cookiesAccept = driver.findElement(By.name("agree"));
        cookiesAccept.click();

        //ticker localisieren
        List<WebElement> tickers = driver.findElements(By.cssSelector("[aria-label=Symbol]"));

        for (WebElement elem : tickers) {
            System.out.println(elem.getText());
        }
    }
}

