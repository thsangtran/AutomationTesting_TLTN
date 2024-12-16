import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.*;
import java.time.Duration;
import java.util.List;

public class SearchFunctionalityTest {
    WebDriver driver;
    WebDriverWait wait;
    @BeforeMethod
    public void setUp() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        driver.get("https://tiki.vn");
    }
    @Test
    // Tìm kiếm theo danh mục Thiết Bị Số - Phụ Kiện Số
     public void testSearchByCategory() {
        driver.get("https://tiki.vn");
        // Đóng quảng cáo nếu xuất hiện
        closePopupIfExists();
        // Tìm và nhấn vào danh mục "Thiết Bị Số - Phụ Kiện Số"
        WebElement categoryLink = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//a[@title='Thiết Bị Số - Phụ Kiện Số']")));
        categoryLink.click();
        // Kiểm tra danh mục đã được mở đúng cách
        WebElement categoryHeader = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//h2[contains(text(),'Thiết Bị Số - Phụ Kiện Số')]")));
        Assert.assertTrue(categoryHeader.isDisplayed(), "Không hiển thị tiêu đề danh mục!");
        // Kiểm tra sản phẩm hiển thị
        List<WebElement> products = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(
                By.xpath("//a[contains(@class,'product-item')]")));
        Assert.assertTrue(products.size() > 0, "Không có sản phẩm nào được hiển thị trong danh mục!");
        System.out.println("Test tìm kiếm theo danh mục: PASS");
    }
    @Test
    // Tìm kiếm với từ khóa hợp lệ
    public void testSearchWithCorrectKeyword() {
        driver.get("https://tiki.vn");
        // Đóng quảng cáo nếu xuất hiện
        closePopupIfExists();
        // Click vào thanh tìm kiếm trước khi nhập
        WebElement searchBox = wait.until(ExpectedConditions.elementToBeClickable(
                By.cssSelector("input[data-view-id='main_search_form_input']")));
        searchBox.click(); // Thao tác click trước
        // Nhập từ khóa và tìm kiếm
        searchBox.sendKeys("iPhone 15 Pro Max");
        searchBox.sendKeys(Keys.ENTER);
        // Kiểm tra kết quả tìm kiếm
        List<WebElement> products = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(
                By.xpath("//a[contains(@class,'product-item')]")));
        Assert.assertTrue(products.size() > 0, "Không có sản phẩm nào được hiển thị!");
        System.out.println("Test tìm kiếm từ khóa chính xác: PASS");
    }
    @Test
    // Tìm kiếm với từ khóa không hợp lệ
    public void testSearchWithPartialOrIncorrectKeyword() {
        driver.get("https://tiki.vn");
        // Đóng quảng cáo nếu xuất hiện
        closePopupIfExists();
        // Click vào thanh tìm kiếm trước khi nhập
        WebElement searchBox = wait.until(ExpectedConditions.elementToBeClickable(
                By.cssSelector("input[data-view-id='main_search_form_input']")));
        searchBox.click(); // Thao tác click trước
        // Nhập từ khóa không chính xác và tìm kiếm
        searchBox.sendKeys("xyz123%&@");
        searchBox.sendKeys(Keys.ENTER);
        // Kiểm tra không có sản phẩm nào hiển thị
        List<WebElement> products = driver.findElements(By.cssSelector(".product-item"));
        Assert.assertTrue(products.isEmpty(), "Có sản phẩm hiển thị khi không tìm thấy kết quả!");
        // Kiểm tra thông báo không tìm thấy
        WebElement noResultMessage = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//div[contains(text(),'Không tìm thấy sản phẩm phù hợp')]")));
        Assert.assertTrue(noResultMessage.isDisplayed(), "Không hiển thị thông báo không tìm thấy!");
        System.out.println("Test tìm kiếm từ khóa sai hoặc một phần: PASS");
    }
    @Test
    // Tìm kiếm và lọc theo Bán chạy
    public void testSearchWithFiltersAndSorting1() {
        driver.get("https://tiki.vn");
        // Đóng quảng cáo nếu xuất hiện
        closePopupIfExists();
        // Click vào thanh tìm kiếm trước khi nhập
        WebElement searchBox = wait.until(ExpectedConditions.elementToBeClickable(
                By.cssSelector("input[data-view-id='main_search_form_input']")));
        searchBox.click(); // Thao tác click trước
        // Nhập từ khóa và tìm kiếm
        searchBox.sendKeys("iPhone");
        searchBox.sendKeys(Keys.ENTER);
        // Click vào "Phổ biến"
        WebElement popularFilter = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//div[contains(text(),'Phổ biến')]")));
        popularFilter.click();
        // Click vào bộ lọc "Bán chạy"
        WebElement bestSellerFilter = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//div[contains(text(),'Bán chạy')]")));
        bestSellerFilter.click();
        // Kiểm tra danh sách sản phẩm hiển thị
        List<WebElement> products = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(
                By.xpath("//a[contains(@class,'product-item')]")));
        Assert.assertTrue(products.size() > 0, "Không có sản phẩm nào hiển thị trong bộ lọc 'Bán chạy'!");
        System.out.println("Test bộ lọc 'Bán chạy': PASS");
    }
    @Test
    // Tìm kiếm và lọc theo Hàng mới
    public void testSearchWithFiltersAndSorting2() {
        driver.get("https://tiki.vn");
        // Đóng quảng cáo nếu xuất hiện
        closePopupIfExists();
        // Click vào thanh tìm kiếm trước khi nhập
        WebElement searchBox = wait.until(ExpectedConditions.elementToBeClickable(
                By.cssSelector("input[data-view-id='main_search_form_input']")));
        searchBox.click(); // Thao tác click trước
        // Nhập từ khóa vào thanh tìm kiếm
        searchBox.sendKeys("iPhone");
        searchBox.sendKeys(Keys.ENTER);
        // Click vào "Phổ biến"
        WebElement popularFilter = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//div[contains(text(),'Phổ biến')]")));
        popularFilter.click();
        // Áp dụng bộ lọc "Hàng mới"
        WebElement newestFilter = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//div[contains(text(),'Hàng mới')]")));
        newestFilter.click();
        // Kiểm tra danh sách sản phẩm hiển thị
        List<WebElement> products = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(
                By.xpath("//a[contains(@class,'product-item')]")));
        Assert.assertTrue(products.size() > 0, "Không có sản phẩm nào hiển thị trong bộ lọc 'Hàng mới'!");
        System.out.println("Test bộ lọc 'Hàng mới': PASS");
    }
    @Test
    // Tìm kiếm và lọc theo Giá thấp đến cao
    public void testSearchWithFiltersAndSorting3() {
        driver.get("https://tiki.vn");
        // Đóng quảng cáo nếu có
        closePopupIfExists();
        // Click vào thanh tìm kiếm trước khi nhập
        WebElement searchBox = wait.until(ExpectedConditions.elementToBeClickable(
                By.cssSelector("input[data-view-id='main_search_form_input']")));
        searchBox.click();
        searchBox.sendKeys("iPhone");
        searchBox.sendKeys(Keys.ENTER);
        // Chờ danh sách sản phẩm tải xong
        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".product-item")));
        // Click vào "Phổ biến"
        WebElement popularFilter = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//div[contains(text(),'Phổ biến')]")));
        popularFilter.click();
        // Áp dụng bộ lọc "Giá thấp đến cao"
        WebElement lowToHighPriceFilter = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//div[contains(text(),'Giá thấp đến cao')]")));
        lowToHighPriceFilter.click();
        // Chờ danh sách sản phẩm được tải lại sau khi áp dụng bộ lọc
        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".product-item")));
        // Lấy giá của sản phẩm thứ nhất
        WebElement productPrice1 = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("(//div[contains(@class,'price-discount__price')])[1]")));
        String priceText1 = productPrice1.getText().replace("₫", "").replace(".", "").trim();
        int price1 = Integer.parseInt(priceText1);
        // Lấy giá của sản phẩm thứ hai
        WebElement productPrice2 = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("(//div[contains(@class,'price-discount__price')])[2]")));
        String priceText2 = productPrice2.getText().replace("₫", "").replace(".", "").trim();
        int price2 = Integer.parseInt(priceText2);
        // Kiểm tra thứ tự giá
        Assert.assertTrue(price1 <= price2, "Giá sản phẩm thứ nhất lớn hơn sản phẩm thứ hai, bộ lọc không đúng!");
        System.out.println("Bộ lọc 'Giá thấp đến cao' hoạt động chính xác!" + price1 + "<" + price2);
    }
    @Test
    // Tìm kiếm và lọc theo Giá cao đến thấp
    public void testSearchWithFiltersAndSorting4() {
        driver.get("https://tiki.vn");
        // Đóng quảng cáo nếu có
        closePopupIfExists();
        // Click vào thanh tìm kiếm trước khi nhập
        WebElement searchBox = wait.until(ExpectedConditions.elementToBeClickable(
                By.cssSelector("input[data-view-id='main_search_form_input']")));
        searchBox.click(); // Thao tác click trước
        // Nhập từ khóa vào thanh tìm kiếm
        searchBox.sendKeys("iPhone");
        searchBox.sendKeys(Keys.ENTER);
        // Chờ danh sách sản phẩm tải xong
        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".product-item")));
        // Click vào "Phổ biến"
        WebElement popularFilter = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//div[contains(text(),'Phổ biến')]")));
        popularFilter.click();
        // Áp dụng bộ lọc "Giá cao đến thấp"
        WebElement highToLowPriceFilter = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//div[contains(text(),'Giá cao đến thấp')]")));
        highToLowPriceFilter.click();
        // Chờ danh sách sản phẩm được tải lại sau khi áp dụng bộ lọc
        wait.until(ExpectedConditions.stalenessOf(driver.findElement(By.cssSelector(".product-item"))));
        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".product-item")));
        // Lấy giá của sản phẩm thứ nhất
        WebElement productPrice1 = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("(//div[contains(@class,'price-discount__price')])[1]")));
        String priceText1 = productPrice1.getText().replace("₫", "").replace(".", "").trim();
        int price1 = Integer.parseInt(priceText1);
        // Lấy giá của sản phẩm thứ hai
        WebElement productPrice2 = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("(//div[contains(@class,'price-discount__price')])[2]")));
        String priceText2 = productPrice2.getText().replace("₫", "").replace(".", "").trim();
        int price2 = Integer.parseInt(priceText2);
        // Kiểm tra thứ tự giá
        Assert.assertTrue(price1 >= price2, "Giá sản phẩm thứ nhất nhỏ hơn sản phẩm thứ hai, bộ lọc không đúng!");
        System.out.println("Bộ lọc 'Giá cao đến thấp' hoạt động chính xác!" + price1 + ">" + price2);
    }
    private void closePopupIfExists() {
        try {
            WebElement closeAdButton = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//img[@alt='close-icon']"))); // Xpath có thể điều chỉnh nếu cần
            closeAdButton.click();
            Thread.sleep(2000); // Chờ một chút để đảm bảo quảng cáo đã đóng
        } catch (Exception e) {
            System.out.println("Không tìm thấy quảng cáo nào, tiếp tục thực thi.");
        }
    }
    @AfterMethod
    public void tearDown() throws InterruptedException{
        if (driver != null) {
            Thread.sleep(5000);
            driver.quit();
        }
    }
}
