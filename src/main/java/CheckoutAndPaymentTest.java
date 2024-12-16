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
public class CheckoutAndPaymentTest {
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
    @Test
    // Đặt hàng và thanh toán bằng phương thức thanh toán tiền mặt
    public void testCheckoutandPayment() {
        loginToTiki();
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
        // Nhấn vào nút Mua Hàng
        WebElement buyNowButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[contains(@class, 'styles__StyledButton-sc-rq5fjn-0') and contains(text(),'Mua Hàng')]")));
        buyNowButton.click();
        // Chọn phương thức thanh toán (ví dụ: Thanh toán khi nhận hàng)
        WebElement paymentOption = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//div[contains(@class,'method-content__title')]//span[text()='Thanh toán tiền mặt']")));
        paymentOption.click();
        // Nhấn vào nút Đặt hàng
        WebElement placeOrderButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[contains(@class, 'styles__StyledButton-sc-1kbdasz-3') and contains(text(),'Đặt hàng')]")));
        placeOrderButton.click();
        // Nhấn vào nút "Quay về trang chủ"
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement backToHomeButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[text()='Quay về trang chủ']")));
        backToHomeButton.click();
        // Đơi tắt popup nếu có
        closePopupIfExists();
        // Nhấn chọn vào Giỏ hàng
        WebElement cartIcon = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//img[@class='cart-icon' and @alt='header_header_img_Cart']")));
        cartIcon.click();
        // Đợi phần tử thông báo "Giỏ hàng trống" hiển thị
        WebElement emptyCartMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//p[@class='empty__message1' and text()='Giỏ hàng trống']")));
        // Kiểm tra rằng thông báo "Giỏ hàng trống" đã hiển thị
        Assert.assertTrue(emptyCartMessage.isDisplayed(), "Giỏ hàng không trống, đơn hàng chưa được hoàn tất!");
        System.out.println("Đặt hàng thành công!");
    }
    @AfterMethod
    public void tearDown() {
        // Đóng trình duyệt sau khi hoàn tất
        if (driver != null) {
            driver.quit();
        }
    }


}
