import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.Duration;

public class AddToCartTest {
    WebDriver driver;
    WebDriverWait wait;

    @BeforeMethod
    public void setUp() {
        // Cấu hình trình duyệt
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }
    public void closePopupIfExists() {
        try {
            WebElement closePopupButton = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//img[@alt='close-icon']"))); // Nút đóng popup
            closePopupButton.click();
        } catch (Exception e) {
            System.out.println("Không tìm thấy popup nào, tiếp tục thực thi.");
        }
    }
    public void loginToTiki() {
        // Bước 1: Truy cập vào trang web
        driver.get("https://tiki.vn");

        // Đóng quảng cáo nếu có
        closePopupIfExists();

        // Bước 2: Click vào "Tài khoản"
        WebElement accountButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//span[contains(text(),'Tài khoản')]")));
        accountButton.click();

        // Bước 3: Nhập số điện thoại và click "Tiếp tục"
        WebElement phoneInput = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//input[@type='tel']")));
        phoneInput.sendKeys("0362458928");
        WebElement continueButton = driver.findElement(By.xpath("//button[contains(text(),'Tiếp Tục')]"));
        continueButton.click();

        // Bước 4: Nhập mật khẩu và click "Đăng nhập"
        WebElement passwordInput = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//input[@type='password']")));
        passwordInput.sendKeys("sangtran301003");
        WebElement loginButton = driver.findElement(By.xpath("//button[contains(text(),'Đăng Nhập')]"));
        loginButton.click();

        // Chờ đăng nhập hoàn tất
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[contains(text(),'Chào mừng Thanh Sang')]")));
        System.out.println("Đăng nhập thành công!");
    }
    // Hàm để tăng số lượng sản phẩm lên n lần
    public void increaseQuantity(int times) {
        WebElement increaseQuantityButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//img[@alt='add-icon']/parent::button"))); // Nút tăng số lượng
        for (int i = 0; i < times; i++) {
            increaseQuantityButton.click();
        }
    }
    // Hàm để giảm số lượng sản phẩm xuống n lần
    public void decreaseQuantity(int times) {
        WebElement decreaseQuantityButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//img[@alt='remove-icon']/parent::button"))); // Nút giảm số lượng
        for (int i = 0; i < times; i++) {
            decreaseQuantityButton.click();
        }
    }
    @Test
    // Thêm 1 sản phẩm vào giỏ hàng
    public void testAddToCart() {
        // Gọi hàm đăng nhập
        loginToTiki();
        // Đóng quảng cáo nếu xuất hiện
        closePopupIfExists();
        // Đợi 10 giây để cửa sổ trợ lý AI tự động tắt
        try {
            Thread.sleep(6000);
            System.out.println("Đã đợi 10 giây để cửa sổ trợ lý AI tự động đóng.");
        } catch (InterruptedException e) {
            System.out.println("Có lỗi xảy ra khi đợi: " + e.getMessage());
        }
        // Tìm kiếm sản phẩm
        WebElement searchBox = wait.until(ExpectedConditions.elementToBeClickable(
                By.cssSelector("input[data-view-id='main_search_form_input']")));
        searchBox.click();
        searchBox.sendKeys("iPhone");
        searchBox.sendKeys(Keys.ENTER);
        // Chọn sản phẩm đầu tiên
        WebElement firstProduct = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("(//a[@class='style__ProductLink-sc-139nb47-2 cKoUly product-item'])[1]")));
                firstProduct.click();
        // Thêm vào giỏ hàng
        WebElement addToCartButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[contains(text(),'Thêm vào giỏ')]")));
        addToCartButton.click();
        // Kiểm tra thông báo "Thêm vào giỏ hàng thành công"
        WebElement successMessage = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//*[contains(text(),'Thêm vào giỏ hàng thành công')]")));
        Assert.assertTrue(successMessage.isDisplayed(), "Thông báo 'Thêm vào giỏ hàng thành công' không hiển thị!");
        System.out.println("Test thêm vào giỏ hàng sau khi đăng nhập: PASS");
    }
    @Test
    // Thêm 1 sản phẩm vào giỏ hàng với tăng/giảm số lượng
    public void testAddToCartWithQuantityAdjustment() {
        // Gọi hàm đăng nhập
        loginToTiki();
        // Đóng quảng cáo nếu xuất hiện
        closePopupIfExists();
        // Đợi 6 giây để cửa sổ trợ lý AI tự động tắt
        try {
            Thread.sleep(6000);
            System.out.println("Đã đợi 10 giây để cửa sổ trợ lý AI tự động đóng.");
        } catch (InterruptedException e) {
            System.out.println("Có lỗi xảy ra khi đợi: " + e.getMessage());
        }
        // Tìm kiếm sản phẩm
        WebElement searchBox = wait.until(ExpectedConditions.elementToBeClickable(
                By.cssSelector("input[data-view-id='main_search_form_input']")));
        searchBox.click();
        searchBox.sendKeys("iPhone");
        searchBox.sendKeys(Keys.ENTER);
        // Chọn sản phẩm đầu tiên
        WebElement firstProduct = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("(//a[@class='style__ProductLink-sc-139nb47-2 cKoUly product-item'])[1]")));
        firstProduct.click();
        // Tăng số lượng sản phẩm
        increaseQuantity(2);
        // Kiểm tra số lượng sản phẩm hiển thị là 3
        WebElement quantityDisplay = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//input[@class='input']"))); // Ô hiển thị số lượng
        Assert.assertEquals(quantityDisplay.getAttribute("value"), "3", "Số lượng sản phẩm không đúng!");
        // Giảm số lượng sản phẩm
        decreaseQuantity(1);
        // Kiểm tra số lượng sản phẩm hiển thị là 2
        Assert.assertEquals(quantityDisplay.getAttribute("value"), "2", "Số lượng sản phẩm sau khi giảm không đúng!");
        // Thêm vào giỏ hàng
        WebElement addToCartButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[contains(text(),'Thêm vào giỏ')]")));
        addToCartButton.click();
        // Kiểm tra thông báo "Thêm vào giỏ hàng thành công"
        WebElement successMessage = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//*[contains(text(),'Thêm vào giỏ hàng thành công')]")));
        Assert.assertTrue(successMessage.isDisplayed(), "Thông báo 'Thêm vào giỏ hàng thành công' không hiển thị!");
        System.out.println("Test thêm vào giỏ hàng với điều chỉnh số lượng: PASS");
    }
    @Test
    // Xóa 1 sản phẩm khỏi giỏ hàng
    public void testRemoveFromCart() {
        // Đăng nhập và thêm sản phẩm vào giỏ
        loginToTiki();
        // Tắt popup nếu có
        closePopupIfExists();
        // Đợi 6 giây để cửa sổ trợ lý AI tự động tắt
        try {
            Thread.sleep(6000);
            System.out.println("Đã đợi 10 giây để cửa sổ trợ lý AI tự động đóng.");
        } catch (InterruptedException e) {
            System.out.println("Có lỗi xảy ra khi đợi: " + e.getMessage());
        }
        // Điều hướng tới giỏ hàng
        WebElement cartButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//img[@class='cart-icon' and @alt='header_header_img_Cart']")));
        cartButton.click();
        // Xóa sản phẩm khỏi giỏ hàng
        WebElement removeButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//div[contains(@class,'styles__StyledItemAction-sc-baodwo-5')]//img[@alt='deleted']")));
        removeButton.click();
        // Xác nhận xóa nếu có popup
        try {
            WebElement confirmRemoveButton = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//div[contains(@class,'dialog-control__button--secondary') and contains(text(),'Xác Nhận')]")));
            confirmRemoveButton.click();
        } catch (Exception e) {
            System.out.println("Không có xác nhận xóa, tiếp tục kiểm tra.");
        }
        // Kiểm tra giỏ hàng trống
        WebElement emptyCartMessage = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//*[contains(text(),'Giỏ hàng trống')]")));
        Assert.assertTrue(emptyCartMessage.isDisplayed(), "Giỏ hàng không trống sau khi xóa sản phẩm!");
        System.out.println("Test xóa sản phẩm khỏi giỏ hàng: PASS");
    }
    @AfterMethod
    public void tearDown() {
        // Đóng trình duyệt sau khi hoàn tất
        if (driver != null) {
            driver.quit();
        }
    }
}
