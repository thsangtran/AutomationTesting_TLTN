import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;
import org.testng.Assert;
import java.time.Duration;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.Keys;
import org.openqa.selenium.JavascriptExecutor;

public class AccountManagementTest {
    private WebDriver driver;
    private WebDriverWait wait;
    @BeforeMethod
    public void setUp() {
        // Khởi động WebDriverManager và khởi tạo WebDriver
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        // Khởi tạo WebDriverWait
        wait = new WebDriverWait(driver, java.time.Duration.ofSeconds(10));
    }
    @Test
    // Đăng nhập đúng MK + SĐT
    public void testLoginWithCorrectPassword() {
        try {
            // Mở trang chủ Tiki
            driver.get("https://tiki.vn");
            // Đợi và tắt popup nếu có
            closePopupIfExists();
            // Nhấn vào "Tài khoản"
            WebElement accountButton = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//span[contains(text(),'Tài khoản')]")));
            accountButton.click();
            // Nhập số điện thoại
            WebElement phoneInput = wait.until(ExpectedConditions.presenceOfElementLocated(
                    By.xpath("//input[@name='tel']")));
            phoneInput.sendKeys("0362458928");
            // Nhấn nút "Tiếp tục"
            WebElement continueButton = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//button[contains(text(),'Tiếp Tục')]")));
            continueButton.click();
            // Đợi trang chuyển đến phần nhập mật khẩu
            WebElement passwordInput = wait.until(ExpectedConditions.presenceOfElementLocated(
                    By.xpath("//input[@type='password']")));
            passwordInput.sendKeys("sangtran301003");
            // Nhấn nút "Đăng nhập"
            WebElement loginButton = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//button[contains(text(),'Đăng Nhập')]")));
            loginButton.click();
            // Kiểm tra nếu đăng nhập thành công
            try {
                WebElement welcomeMessage = wait.until(ExpectedConditions.presenceOfElementLocated(
                        By.xpath("//*[contains(text(),'Chào mừng Thanh Sang')]")));
                System.out.println("Đăng nhập thành công: " + welcomeMessage.getText());
            } catch (Exception e) {
                System.out.println("Không tìm thấy thông báo chào mừng.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Test
    // Đăng nhập đúng SĐT + sai MK
    public void testLoginWithIncorrectPassword() {
        try {
            // Mở trang chủ Tiki
            driver.get("https://tiki.vn");
            // Đợi và tắt popup nếu có
            closePopupIfExists();
            // Nhấn vào "Tài khoản"
            WebElement accountButton = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//span[contains(text(),'Tài khoản')]")));
            accountButton.click();
            // Nhập số điện thoại
            WebElement phoneInput = wait.until(ExpectedConditions.presenceOfElementLocated(
                    By.xpath("//input[@name='tel']")));
            phoneInput.sendKeys("0362458928");
            // Nhấn nút "Tiếp tục"
            WebElement continueButton = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//button[contains(text(),'Tiếp Tục')]")));
            continueButton.click();
            // Đợi trang chuyển đến phần nhập mật khẩu
            WebElement passwordInput = wait.until(ExpectedConditions.presenceOfElementLocated(
                    By.xpath("//input[@type='password']")));
            passwordInput.sendKeys("301003sangtran"); // Mật khẩu sai
            // Nhấn nút "Đăng nhập"
            WebElement loginButton = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//button[contains(text(),'Đăng Nhập')]")));
            loginButton.click();
            // Kiểm tra nếu thông báo lỗi xuất hiện
            try {
                WebElement errorMessage = wait.until(ExpectedConditions.presenceOfElementLocated(
                        By.xpath("//*[contains(text(),'Thông tin đăng nhập không đúng')]")));
                System.out.println("Đăng nhập thất bại (đúng hành vi): " + errorMessage.getText());
            } catch (Exception e) {
                System.out.println("Không tìm thấy thông báo lỗi.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Test
    // Đăng nhập sai SĐT
    public void testLoginWithInvalidPhoneNumber() {
        try {
            // Mở trang chủ Tiki
            driver.get("https://tiki.vn");
            // Đợi và tắt popup nếu có
            closePopupIfExists();
            // Nhấn vào "Tài khoản"
            WebElement accountButton = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//span[contains(text(),'Tài khoản')]")));
            accountButton.click();
            // Nhập số điện thoại sai
            WebElement phoneInput = wait.until(ExpectedConditions.presenceOfElementLocated(
                    By.xpath("//input[@name='tel']")));
            phoneInput.sendKeys("0362458817"); // Số điện thoại không hợp lệ
            // Nhấn nút "Tiếp tục"
            WebElement continueButton = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//button[contains(text(),'Tiếp Tục')]")));
            continueButton.click();
            // Kiểm tra xem hệ thống chuyển tới màn hình nhập mã xác minh
            try {
                WebElement errorMessage = wait.until(ExpectedConditions.presenceOfElementLocated(
                        By.xpath("//*[contains(text(),'Số điện thoại không hợp lệ')]")));
                String actualMessage = errorMessage.getText();
                Assert.assertEquals(actualMessage, "Số điện thoại không hợp lệ", "Thông báo lỗi không đúng!");
            } catch (Exception e) {
                Assert.fail("Không tìm thấy thông báo lỗi: " + e.getMessage());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Test
    // Thay đổi Name đúng yêu cầu: Gồm 2 từ trở lên, không có kí tự đặc biệt và số
    public void testChangeName1() {
        try {
            // Mở trang chủ Tiki
            driver.get("https://tiki.vn");
            // Đợi và tắt popup nếu có
            closePopupIfExists();
            // Đăng nhập với thông tin hợp lệ
            loginWithValidCredentials();
            // Đợi và tắt popup nếu có lần nữa
            closePopupIfExists();
            // Chờ 6 giây để giao diện tải đầy đủ (nếu cần thiết)
            Thread.sleep(6000);
            // Chờ và di chuyển chuột đến mục Tài khoản
            WebElement accountButton = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//span[contains(text(),'Tài khoản')]")));
            Actions actions = new Actions(driver);
            actions.moveToElement(accountButton).perform();
            // Nhấn vào "Thông tin tài khoản"
            WebElement accountInfoLink = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//a[@href='/customer/account/edit?src=header_my_account']/p[contains(text(),'Thông tin tài khoản')]")));
            accountInfoLink.click();
            // Đợi đến trường Nickname
            WebElement nameField = wait.until(ExpectedConditions.presenceOfElementLocated(
                    By.xpath("//input[@name='fullName' and @placeholder='Thêm họ tên']")));
            // Xóa nội dung hiện tại
            nameField.click();
            nameField.sendKeys(Keys.CONTROL + "a");
            nameField.sendKeys(Keys.DELETE);
            // Đảm bảo trường đã được làm trống
            Assert.assertEquals(nameField.getAttribute("value"), "", "Trường nickname không được xóa thành công!");
            // Nhập nickname mới
            nameField.sendKeys("Thanh Sang Tran");
            // Nhấn nút "Lưu thay đổi" để cập nhật nickname
            WebElement saveButton = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//button[@type='submit' and contains(text(),'Lưu thay đổi')]")));
            saveButton.click();
            // Kiểm tra nếu thông báo lưu không thành công xuất hiện
            try {
                WebElement successMessage = wait.until(ExpectedConditions.presenceOfElementLocated(
                        By.xpath("//*[contains(text(),'Cập nhật thông tin thành công')]")));
                Assert.assertTrue(successMessage.isDisplayed(), "Không tìm thấy thông báo cập nhật thành công!");
                System.out.println("Đổi Name thành công!");
            } catch (Exception e) {
                Assert.fail("Không tìm thấy thông báo thành công: " + e.getMessage());
            }
        } catch (Exception e) {
            Assert.fail("Có lỗi xảy ra khi thực hiện kiểm tra thay đổi name: " + e.getMessage());
        }
    }
    @Test
    // Thay đổi Name sai yêu cầu: Họ tên chỉ 1 từ
    public void testChangeName2() {
        try {
            // Mở trang chủ Tiki
            driver.get("https://tiki.vn");
            // Đợi và tắt popup nếu có
            closePopupIfExists();
            // Đăng nhập với thông tin hợp lệ
            loginWithValidCredentials();
            // Đợi và tắt popup nếu có lần nữa
            closePopupIfExists();
            // Chờ 6 giây để giao diện tải đầy đủ (nếu cần thiết)
            Thread.sleep(6000);
            // Chờ và di chuyển chuột đến mục Tài khoản
            WebElement accountButton = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//span[contains(text(),'Tài khoản')]")));
            Actions actions = new Actions(driver);
            actions.moveToElement(accountButton).perform();
            // Nhấn vào "Thông tin tài khoản"
            WebElement accountInfoLink = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//a[@href='/customer/account/edit?src=header_my_account']/p[contains(text(),'Thông tin tài khoản')]")));
            accountInfoLink.click();
            // Đợi đến trường Nickname
            WebElement nameField = wait.until(ExpectedConditions.presenceOfElementLocated(
                    By.xpath("//input[@name='fullName' and @placeholder='Thêm họ tên']")));
            // Xóa nội dung hiện tại
            nameField.click();
            nameField.sendKeys(Keys.CONTROL + "a");
            nameField.sendKeys(Keys.DELETE);
            // Đảm bảo trường đã được làm trống
            Assert.assertEquals(nameField.getAttribute("value"), "", "Trường nickname không được xóa thành công!");
            // Nhập nickname mới
            nameField.sendKeys("Sang");
            // Nhấn nút "Lưu thay đổi" để cập nhật nickname
            WebElement saveButton = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//button[@type='submit' and contains(text(),'Lưu thay đổi')]")));
            saveButton.click();
            // Kiểm tra nếu thông báo lưu không thành công xuất hiện
            try {
                WebElement successMessage = wait.until(ExpectedConditions.presenceOfElementLocated(
                        By.xpath("//*[contains(text(),'Họ & Tên gồm 2 từ trở lên')]")));
                Assert.assertTrue(successMessage.isDisplayed(), "Không tìm thấy thông báo Họ & Tên gồm 2 từ trở lên!");
                System.out.println("Đổi Name không thành công!");
            } catch (Exception e) {
                Assert.fail("Không tìm thấy thông báo không thành công: " + e.getMessage());
            }
        } catch (Exception e) {
            Assert.fail("Có lỗi xảy ra khi thực hiện kiểm tra thay đổi name: " + e.getMessage());
        }
    }
    @Test
    // Thay đổi Name sai yêu cầu: Họ tên có kí tự đặc biệt
    public void testChangeName3() {
        try {
            // Mở trang chủ Tiki
            driver.get("https://tiki.vn");
            // Đợi và tắt popup nếu có
            closePopupIfExists();
            // Đăng nhập với thông tin hợp lệ
            loginWithValidCredentials();
            // Đợi và tắt popup nếu có lần nữa
            closePopupIfExists();
            // Chờ 6 giây để giao diện tải đầy đủ (nếu cần thiết)
            Thread.sleep(6000);
            // Chờ và di chuyển chuột đến mục Tài khoản
            WebElement accountButton = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//span[contains(text(),'Tài khoản')]")));
            Actions actions = new Actions(driver);
            actions.moveToElement(accountButton).perform();
            // Nhấn vào "Thông tin tài khoản"
            WebElement accountInfoLink = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//a[@href='/customer/account/edit?src=header_my_account']/p[contains(text(),'Thông tin tài khoản')]")));
            accountInfoLink.click();
            // Đợi đến trường Nickname
            WebElement nameField = wait.until(ExpectedConditions.presenceOfElementLocated(
                    By.xpath("//input[@name='fullName' and @placeholder='Thêm họ tên']")));
            // Xóa nội dung hiện tại
            nameField.click();
            nameField.sendKeys(Keys.CONTROL + "a");
            nameField.sendKeys(Keys.DELETE);
            // Đảm bảo trường đã được làm trống
            Assert.assertEquals(nameField.getAttribute("value"), "", "Trường nickname không được xóa thành công!");
            // Nhập nickname mới
            nameField.sendKeys("Thanh Sang@");
            // Nhấn nút "Lưu thay đổi" để cập nhật nickname
            WebElement saveButton = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//button[@type='submit' and contains(text(),'Lưu thay đổi')]")));
            saveButton.click();
            // Kiểm tra nếu thông báo lưu không thành công xuất hiện
            try {
                WebElement successMessage = wait.until(ExpectedConditions.presenceOfElementLocated(
                        By.xpath("//*[contains(text(),'Họ tên không bao gồm kí tự đặc biệt và số')]")));
                Assert.assertTrue(successMessage.isDisplayed(), "Không tìm thấy thông báo Họ tên không bao gồm kí tự đặc biệt và số!");
                System.out.println("Đổi Name không thành công!");
            } catch (Exception e) {
                Assert.fail("Không tìm thấy thông báo không thành công: " + e.getMessage());
            }
        } catch (Exception e) {
            Assert.fail("Có lỗi xảy ra khi thực hiện kiểm tra thay đổi name: " + e.getMessage());
        }
    }
    @Test
    // Thay đổi Name sai yêu cầu: Họ tên chỉ có số
    public void testChangeName4() {
        try {
            // Mở trang chủ Tiki
            driver.get("https://tiki.vn");
            // Đợi và tắt popup nếu có
            closePopupIfExists();
            // Đăng nhập với thông tin hợp lệ
            loginWithValidCredentials();
            // Đợi và tắt popup nếu có lần nữa
            closePopupIfExists();
            // Chờ 6 giây để giao diện tải đầy đủ (nếu cần thiết)
            Thread.sleep(6000);
            // Chờ và di chuyển chuột đến mục Tài khoản
            WebElement accountButton = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//span[contains(text(),'Tài khoản')]")));
            Actions actions = new Actions(driver);
            actions.moveToElement(accountButton).perform();
            // Nhấn vào "Thông tin tài khoản"
            WebElement accountInfoLink = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//a[@href='/customer/account/edit?src=header_my_account']/p[contains(text(),'Thông tin tài khoản')]")));
            accountInfoLink.click();
            // Đợi đến trường Nickname
            WebElement nameField = wait.until(ExpectedConditions.presenceOfElementLocated(
                    By.xpath("//input[@name='fullName' and @placeholder='Thêm họ tên']")));
            // Xóa nội dung hiện tại
            nameField.click();
            nameField.sendKeys(Keys.CONTROL + "a");
            nameField.sendKeys(Keys.DELETE);
            // Đảm bảo trường đã được làm trống
            Assert.assertEquals(nameField.getAttribute("value"), "", "Trường nickname không được xóa thành công!");
            // Nhập nickname mới
            nameField.sendKeys("Thanh Sang30");
            // Nhấn nút "Lưu thay đổi" để cập nhật nickname
            WebElement saveButton = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//button[@type='submit' and contains(text(),'Lưu thay đổi')]")));
            saveButton.click();
            // Kiểm tra nếu thông báo lưu không thành công xuất hiện
            try {
                WebElement successMessage = wait.until(ExpectedConditions.presenceOfElementLocated(
                        By.xpath("//*[contains(text(),'Họ tên không bao gồm kí tự đặc biệt và số')]")));
                Assert.assertTrue(successMessage.isDisplayed(), "Không tìm thấy thông báo Họ tên không bao gồm kí tự đặc biệt và số!");
                System.out.println("Đổi Name không thành công!");
            } catch (Exception e) {
                Assert.fail("Không tìm thấy thông báo không thành công: " + e.getMessage());
            }
        } catch (Exception e) {
            Assert.fail("Có lỗi xảy ra khi thực hiện kiểm tra thay đổi name: " + e.getMessage());
        }
    }
    @Test
    // Thay đổi Name sai yêu cầu: Họ tên có 2 chữ cái chứ không phải 2 từ "S T"
    public void testChangeName5() {
        try {
            // Mở trang chủ Tiki
            driver.get("https://tiki.vn");
            // Đợi và tắt popup nếu có
            closePopupIfExists();
            // Đăng nhập với thông tin hợp lệ
            loginWithValidCredentials();
            // Đợi và tắt popup nếu có lần nữa
            closePopupIfExists();
            // Chờ 6 giây để giao diện tải đầy đủ (nếu cần thiết)
            Thread.sleep(6000);
            // Chờ và di chuyển chuột đến mục Tài khoản
            WebElement accountButton = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//span[contains(text(),'Tài khoản')]")));
            Actions actions = new Actions(driver);
            actions.moveToElement(accountButton).perform();
            // Nhấn vào "Thông tin tài khoản"
            WebElement accountInfoLink = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//a[@href='/customer/account/edit?src=header_my_account']/p[contains(text(),'Thông tin tài khoản')]")));
            accountInfoLink.click();
            // Đợi đến trường Nickname
            WebElement nameField = wait.until(ExpectedConditions.presenceOfElementLocated(
                    By.xpath("//input[@name='fullName' and @placeholder='Thêm họ tên']")));
            // Xóa nội dung hiện tại
            nameField.click();
            nameField.sendKeys(Keys.CONTROL + "a");
            nameField.sendKeys(Keys.DELETE);
            // Đảm bảo trường đã được làm trống
            Assert.assertEquals(nameField.getAttribute("value"), "", "Trường nickname không được xóa thành công!");
            // Nhập nickname mới
            nameField.sendKeys("T S");
            // Nhấn nút "Lưu thay đổi" để cập nhật nickname
            WebElement saveButton = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//button[@type='submit' and contains(text(),'Lưu thay đổi')]")));
            saveButton.click();
            // Kiểm tra nếu thông báo lưu không thành công xuất hiện
            try {
                WebElement successMessage = wait.until(ExpectedConditions.presenceOfElementLocated(
                        By.xpath("//*[contains(text(),'Họ & Tên gồm 2 từ trở lên')]")));
                Assert.assertTrue(successMessage.isDisplayed(), "Không tìm thấy thông báo Họ & Tên gồm 2 từ trở lên!");
                System.out.println("Đổi Name không thành công!");
            } catch (Exception e) {
                Assert.fail("Không tìm thấy thông báo không thành công: " + e.getMessage());
            }
        } catch (Exception e) {
            Assert.fail("Có lỗi xảy ra khi thực hiện kiểm tra thay đổi name: " + e.getMessage());
        }
    }
    @Test
    // Thay đổi Nickname đúng yêu cầu: Gồm 3-20 kí tự, chỉ bao gồm chữ, số, “_” và “.”
    public void testChangeNickname1() {
        try {
            // Mở trang chủ Tiki
            driver.get("https://tiki.vn");
            // Đợi và tắt popup nếu có
            closePopupIfExists();
            // Đăng nhập với thông tin hợp lệ
            loginWithValidCredentials();
            // Đợi và tắt popup nếu có lần nữa
            closePopupIfExists();
            // Chờ 6 giây để giao diện tải đầy đủ (nếu cần thiết)
            Thread.sleep(6000);
            // Chờ và di chuyển chuột đến mục Tài khoản
            WebElement accountButton = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//span[contains(text(),'Tài khoản')]")));
            Actions actions = new Actions(driver);
            actions.moveToElement(accountButton).perform();
            // Nhấn vào "Thông tin tài khoản"
            WebElement accountInfoLink = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//a[@href='/customer/account/edit?src=header_my_account']/p[contains(text(),'Thông tin tài khoản')]")));
            accountInfoLink.click();
            // Đợi đến trường Nickname
            WebElement nicknameField = wait.until(ExpectedConditions.presenceOfElementLocated(
                    By.xpath("//input[@name='userName' and @placeholder='Thêm nickname']")));
            // Xóa nội dung hiện tại
            nicknameField.click();
            nicknameField.sendKeys(Keys.CONTROL + "a");
            nicknameField.sendKeys(Keys.DELETE);
            // Đảm bảo trường đã được làm trống
            Assert.assertEquals(nicknameField.getAttribute("value"), "", "Trường nickname không được xóa thành công!");
            // Nhập nickname mới
            nicknameField.sendKeys("sangtran30_.");
            // Nhấn nút "Lưu thay đổi" để cập nhật nickname
            WebElement saveButton = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//button[@type='submit' and contains(text(),'Lưu thay đổi')]")));
            saveButton.click();
            // Kiểm tra nếu thông báo lưu thành công xuất hiện
            try {
                WebElement successMessage = wait.until(ExpectedConditions.presenceOfElementLocated(
                        By.xpath("//*[contains(text(),'Cập nhật thông tin thành công')]")));
                Assert.assertTrue(successMessage.isDisplayed(), "Không tìm thấy thông báo cập nhật thành công!");
                System.out.println("Đổi Nickname thành công!");
            } catch (Exception e) {
                Assert.fail("Không tìm thấy thông báo thành công: " + e.getMessage());
            }
        } catch (Exception e) {
            Assert.fail("Có lỗi xảy ra khi thực hiện kiểm tra thay đổi nickname: " + e.getMessage());
        }
    }
    @Test
    // Thay đổi Nickname không đúng yêu cầu: <3 kí tự
    public void testChangeNickname2() {
        try {
            // Mở trang chủ Tiki
            driver.get("https://tiki.vn");
            // Đợi và tắt popup nếu có
            closePopupIfExists();
            // Đăng nhập với thông tin hợp lệ
            loginWithValidCredentials();
            // Đợi và tắt popup nếu có lần nữa
            closePopupIfExists();
            // Chờ 6 giây để giao diện tải đầy đủ (nếu cần thiết)
            Thread.sleep(6000);
            // Chờ và di chuyển chuột đến mục Tài khoản
            WebElement accountButton = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//span[contains(text(),'Tài khoản')]")));
            Actions actions = new Actions(driver);
            actions.moveToElement(accountButton).perform();
            // Nhấn vào "Thông tin tài khoản"
            WebElement accountInfoLink = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//a[@href='/customer/account/edit?src=header_my_account']/p[contains(text(),'Thông tin tài khoản')]")));
            accountInfoLink.click();
            // Đợi đến trường Nickname
            WebElement nicknameField = wait.until(ExpectedConditions.presenceOfElementLocated(
                    By.xpath("//input[@name='userName' and @placeholder='Thêm nickname']")));
            // Xóa nội dung hiện tại
            nicknameField.click();
            nicknameField.sendKeys(Keys.CONTROL + "a");
            nicknameField.sendKeys(Keys.DELETE);
            // Đảm bảo trường đã được làm trống
            Assert.assertEquals(nicknameField.getAttribute("value"), "", "Trường nickname không được xóa thành công!");
            // Nhập nickname mới
            nicknameField.sendKeys("sg");
            // Nhấn nút "Lưu thay đổi" để cập nhật nickname
            WebElement saveButton = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//button[@type='submit' and contains(text(),'Lưu thay đổi')]")));
            saveButton.click();
            // Kiểm tra nếu thông báo lưu không thành công xuất hiện
            try {
                WebElement successMessage = wait.until(ExpectedConditions.presenceOfElementLocated(
                        By.xpath("//*[contains(text(),'Nickname hợp lệ từ 3-20 kí tự, chỉ bao gồm chữ, số, “_” và “.”')]")));
                Assert.assertTrue(successMessage.isDisplayed(), "Không tìm thấy thông báo cập nhật không thành công!");
                System.out.println("Đổi Nickname không thành công!");
            } catch (Exception e) {
                Assert.fail("Không tìm thấy thông báo thành công: " + e.getMessage());
            }
        } catch (Exception e) {
            Assert.fail("Có lỗi xảy ra khi thực hiện kiểm tra thay đổi nickname: " + e.getMessage());
        }
    }
    @Test
    // Thay đổi Nickname không đúng yêu cầu: >20 kí tự
    public void testChangeNickname3() {
        try {
            // Mở trang chủ Tiki
            driver.get("https://tiki.vn");
            // Đợi và tắt popup nếu có
            closePopupIfExists();
            // Đăng nhập với thông tin hợp lệ
            loginWithValidCredentials();
            // Đợi và tắt popup nếu có lần nữa
            closePopupIfExists();
            // Chờ 6 giây để giao diện tải đầy đủ (nếu cần thiết)
            Thread.sleep(6000);
            // Chờ và di chuyển chuột đến mục Tài khoản
            WebElement accountButton = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//span[contains(text(),'Tài khoản')]")));
            Actions actions = new Actions(driver);
            actions.moveToElement(accountButton).perform();
            // Nhấn vào "Thông tin tài khoản"
            WebElement accountInfoLink = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//a[@href='/customer/account/edit?src=header_my_account']/p[contains(text(),'Thông tin tài khoản')]")));
            accountInfoLink.click();
            // Đợi đến trường Nickname
            WebElement nicknameField = wait.until(ExpectedConditions.presenceOfElementLocated(
                    By.xpath("//input[@name='userName' and @placeholder='Thêm nickname']")));
            // Xóa nội dung hiện tại
            nicknameField.click();
            nicknameField.sendKeys(Keys.CONTROL + "a");
            nicknameField.sendKeys(Keys.DELETE);
            // Đảm bảo trường đã được làm trống
            Assert.assertEquals(nicknameField.getAttribute("value"), "", "Trường nickname không được xóa thành công!");
            // Nhập nickname mới
            nicknameField.sendKeys("thsangtranthsangtranth");
            // Nhấn nút "Lưu thay đổi" để cập nhật nickname
            WebElement saveButton = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//button[@type='submit' and contains(text(),'Lưu thay đổi')]")));
            saveButton.click();
            // Kiểm tra nếu thông báo lưu không thành công xuất hiện
            try {
                WebElement successMessage = wait.until(ExpectedConditions.presenceOfElementLocated(
                        By.xpath("//*[contains(text(),'Nickname hợp lệ từ 3-20 kí tự, chỉ bao gồm chữ, số, “_” và “.”')]")));
                Assert.assertTrue(successMessage.isDisplayed(), "Không tìm thấy thông báo cập nhật không thành công!");
                System.out.println("Đổi Nickname không thành công!");
            } catch (Exception e) {
                Assert.fail("Không tìm thấy thông báo thành công: " + e.getMessage());
            }
        } catch (Exception e) {
            Assert.fail("Có lỗi xảy ra khi thực hiện kiểm tra thay đổi nickname: " + e.getMessage());
        }
    }
    @Test
    // Thay đổi Nickname không đúng yêu cầu: Có kí tự đặc biệt @
    public void testChangeNickname4() {
        try {
            // Mở trang chủ Tiki
            driver.get("https://tiki.vn");
            // Đợi và tắt popup nếu có
            closePopupIfExists();
            // Đăng nhập với thông tin hợp lệ
            loginWithValidCredentials();
            // Đợi và tắt popup nếu có lần nữa
            closePopupIfExists();
            // Chờ 6 giây để giao diện tải đầy đủ (nếu cần thiết)
            Thread.sleep(6000);
            // Chờ và di chuyển chuột đến mục Tài khoản
            WebElement accountButton = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//span[contains(text(),'Tài khoản')]")));
            Actions actions = new Actions(driver);
            actions.moveToElement(accountButton).perform();
            // Nhấn vào "Thông tin tài khoản"
            WebElement accountInfoLink = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//a[@href='/customer/account/edit?src=header_my_account']/p[contains(text(),'Thông tin tài khoản')]")));
            accountInfoLink.click();
            // Đợi đến trường Nickname
            WebElement nicknameField = wait.until(ExpectedConditions.presenceOfElementLocated(
                    By.xpath("//input[@name='userName' and @placeholder='Thêm nickname']")));
            // Xóa nội dung hiện tại
            nicknameField.click();
            nicknameField.sendKeys(Keys.CONTROL + "a");
            nicknameField.sendKeys(Keys.DELETE);
            // Đảm bảo trường đã được làm trống
            Assert.assertEquals(nicknameField.getAttribute("value"), "", "Trường nickname không được xóa thành công!");
            // Nhập nickname mới
            nicknameField.sendKeys("thsangtran@");
            // Nhấn nút "Lưu thay đổi" để cập nhật nickname
            WebElement saveButton = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//button[@type='submit' and contains(text(),'Lưu thay đổi')]")));
            saveButton.click();
            // Kiểm tra nếu thông báo lưu không thành công xuất hiện
            try {
                WebElement successMessage = wait.until(ExpectedConditions.presenceOfElementLocated(
                        By.xpath("//*[contains(text(),'Nickname hợp lệ từ 3-20 kí tự, chỉ bao gồm chữ, số, “_” và “.”')]")));
                Assert.assertTrue(successMessage.isDisplayed(), "Không tìm thấy thông báo cập nhật không thành công!");
                System.out.println("Đổi Nickname không thành công!");
            } catch (Exception e) {
                Assert.fail("Không tìm thấy thông báo thành công: " + e.getMessage());
            }
        } catch (Exception e) {
            Assert.fail("Có lỗi xảy ra khi thực hiện kiểm tra thay đổi nickname: " + e.getMessage());
        }
    }
    @Test
    // Thay đổi MK không đúng yêu cầu: MK < 8 kí tự
    public void testUpdatePassword1() {
        try {
            // Mở trang chủ Tiki
            driver.get("https://tiki.vn");
            // Đợi và tắt popup nếu có
            closePopupIfExists();
            // Đăng nhập với thông tin hợp lệ
            loginWithValidCredentials();
            // Đợi và tắt popup nếu có lần nữa
            closePopupIfExists();
            // Chờ 6 giây để giao diện tải đầy đủ (nếu cần thiết)
            Thread.sleep(6000);
            // Chờ và di chuyển chuột đến mục Tài khoản
            WebElement accountButton = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//span[contains(text(),'Tài khoản')]")));
            Actions actions = new Actions(driver);
            actions.moveToElement(accountButton).perform();
            // Nhấn vào "Thông tin tài khoản"
            WebElement accountInfoLink = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//a[@href='/customer/account/edit?src=header_my_account']/p[contains(text(),'Thông tin tài khoản')]")));
            accountInfoLink.click();
            // Nhấn chọn vào button "Cập nhật"
            WebElement updateButton = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//span[contains(text(),'Đổi mật khẩu')]/ancestor::div[@class='list-item']//button[contains(@class,'button active')]")));
            // Cuộn đến phần tử
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", updateButton);
            Thread.sleep(500); // Give time for the scroll
            updateButton.click();
            // Điền vào trường mật khẩu hiện tại
            WebElement nowPasswordField = wait.until(ExpectedConditions.presenceOfElementLocated(
                    By.xpath("//input[@name='oldPassword' and @placeholder='Nhập mật khẩu hiện tại']")));
            nowPasswordField.sendKeys("sangtran301003");
            // Điền vào trường "Mật khẩu mới"
            WebElement newPasswordField = wait.until(ExpectedConditions.presenceOfElementLocated(
                    By.xpath("//input[@name='password' and @placeholder='Nhập mật khẩu mới']")));
            newPasswordField.sendKeys("thsang"); // Mật khẩu không hợp lệ
            // Điền vào trường "Nhập lại mật khẩu mới"
            WebElement confirmPasswordField = wait.until(ExpectedConditions.presenceOfElementLocated(
                    By.xpath("//input[@name='confirmPassword' and @placeholder='Nhập lại mật khẩu mới']")));
            confirmPasswordField.sendKeys("thsang"); // Mật khẩu không hợp lệ
            // Nhấn nút "Lưu thay đổi" để cập nhật mật khẩu
            WebElement saveButton = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//button[@type='submit' and text()='Lưu thay đổi']")));
            saveButton.click();
            // Kiểm tra nếu thông báo "Mật khẩu không đúng định dạng" xuất hiện
            try {
                WebElement errorMessage = wait.until(ExpectedConditions.presenceOfElementLocated(
                        By.xpath("//*[contains(text(),'Mật khẩu không đúng định dạng')]")));
                Assert.assertTrue(errorMessage.isDisplayed(), "Không tìm thấy thông báo lỗi về mật khẩu!");
                System.out.println("Kiểm tra thay đổi mật khẩu không hợp lệ: PASS");
            } catch (Exception e) {
                Assert.fail("Không tìm thấy thông báo lỗi định dạng mật khẩu: " + e.getMessage());
            }
        } catch (Exception e) {
            Assert.fail("Có lỗi xảy ra khi thực hiện kiểm tra thay đổi mật khẩu: " + e.getMessage());
        }
    }
    @Test
    // Thay đổi MK không đúng yêu cầu: MK > 32 kí tự
    public void testUpdatePassword2() {
        try {
            // Mở trang chủ Tiki
            driver.get("https://tiki.vn");
            // Đợi và tắt popup nếu có
            closePopupIfExists();
            // Đăng nhập với thông tin hợp lệ
            loginWithValidCredentials();
            // Đợi và tắt popup nếu có lần nữa
            closePopupIfExists();
            // Chờ 6 giây để giao diện tải đầy đủ (nếu cần thiết)
            Thread.sleep(6000);
            // Chờ và di chuyển chuột đến mục Tài khoản
            WebElement accountButton = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//span[contains(text(),'Tài khoản')]")));
            Actions actions = new Actions(driver);
            actions.moveToElement(accountButton).perform();
            // Nhấn vào "Thông tin tài khoản"
            WebElement accountInfoLink = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//a[@href='/customer/account/edit?src=header_my_account']/p[contains(text(),'Thông tin tài khoản')]")));
            accountInfoLink.click();
            // Nhấn chọn vào button "Cập nhật"
            WebElement updateButton = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//span[contains(text(),'Đổi mật khẩu')]/ancestor::div[@class='list-item']//button[contains(@class,'button active')]")));
            // Cuộn đến phần tử
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", updateButton);
            Thread.sleep(500); // Give time for the scroll
            updateButton.click();
            // Điền vào trường mật khẩu hiện tại
            WebElement nowPasswordField = wait.until(ExpectedConditions.presenceOfElementLocated(
                    By.xpath("//input[@name='oldPassword' and @placeholder='Nhập mật khẩu hiện tại']")));
            nowPasswordField.sendKeys("sangtran301003");
            // Điền vào trường "Mật khẩu mới"
            WebElement newPasswordField = wait.until(ExpectedConditions.presenceOfElementLocated(
                    By.xpath("//input[@name='password' and @placeholder='Nhập mật khẩu mới']")));
            newPasswordField.sendKeys("thsangtranthsangtranthsangtranthsang"); // Mật khẩu không hợp lệ
            // Điền vào trường "Nhập lại mật khẩu mới"
            WebElement confirmPasswordField = wait.until(ExpectedConditions.presenceOfElementLocated(
                    By.xpath("//input[@name='confirmPassword' and @placeholder='Nhập lại mật khẩu mới']")));
            confirmPasswordField.sendKeys("thsangtranthsangtranthsangtranthsang"); // Mật khẩu không hợp lệ
            // Nhấn nút "Lưu thay đổi" để cập nhật mật khẩu
            WebElement saveButton = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//button[@type='submit' and text()='Lưu thay đổi']")));
            saveButton.click();
            // Kiểm tra nếu thông báo "Mật khẩu không đúng định dạng" xuất hiện
            try {
                WebElement errorMessage = wait.until(ExpectedConditions.presenceOfElementLocated(
                        By.xpath("//*[contains(text(),'Mật khẩu không đúng định dạng')]")));
                Assert.assertTrue(errorMessage.isDisplayed(), "Không tìm thấy thông báo lỗi về mật khẩu!");
                System.out.println("Kiểm tra thay đổi mật khẩu không hợp lệ: PASS");
            } catch (Exception e) {
                Assert.fail("Không tìm thấy thông báo lỗi định dạng mật khẩu: " + e.getMessage());
            }
        } catch (Exception e) {
            Assert.fail("Có lỗi xảy ra khi thực hiện kiểm tra thay đổi mật khẩu: " + e.getMessage());
        }
    }
    @Test
    // Thay đổi MK không đúng yêu cầu: MK có kí tự đặc biệt @
    public void testUpdatePassword3() {
        try {
            // Mở trang chủ Tiki
            driver.get("https://tiki.vn");
            // Đợi và tắt popup nếu có
            closePopupIfExists();
            // Đăng nhập với thông tin hợp lệ
            loginWithValidCredentials();
            // Đợi và tắt popup nếu có lần nữa
            closePopupIfExists();
            // Chờ 6 giây để giao diện tải đầy đủ (nếu cần thiết)
            Thread.sleep(6000);
            // Chờ và di chuyển chuột đến mục Tài khoản
            WebElement accountButton = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//span[contains(text(),'Tài khoản')]")));
            Actions actions = new Actions(driver);
            actions.moveToElement(accountButton).perform();
            // Nhấn vào "Thông tin tài khoản"
            WebElement accountInfoLink = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//a[@href='/customer/account/edit?src=header_my_account']/p[contains(text(),'Thông tin tài khoản')]")));
            accountInfoLink.click();
            //Nhấn chọn vào button "Cập nhật"
            WebElement updateButton = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//span[contains(text(),'Đổi mật khẩu')]/ancestor::div[@class='list-item']//button[contains(@class,'button active')]")));
            // Cuộn đến phần tử
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", updateButton);
            Thread.sleep(500); // Give time for the scroll
            updateButton.click();
            // Điền vào trường mật khẩu hiện tại
            WebElement nowPasswordField = wait.until(ExpectedConditions.presenceOfElementLocated(
                    By.xpath("//input[@name='oldPassword' and @placeholder='Nhập mật khẩu hiện tại']")));
            nowPasswordField.sendKeys("sangtran301003");
            // Điền vào trường "Mật khẩu mới"
            WebElement newPasswordField = wait.until(ExpectedConditions.presenceOfElementLocated(
                    By.xpath("//input[@name='password' and @placeholder='Nhập mật khẩu mới']")));
            newPasswordField.sendKeys("thsangtran@"); // Mật khẩu không hợp lệ
            // Điền vào trường "Nhập lại mật khẩu mới"
            WebElement confirmPasswordField = wait.until(ExpectedConditions.presenceOfElementLocated(
                    By.xpath("//input[@name='confirmPassword' and @placeholder='Nhập lại mật khẩu mới']")));
            confirmPasswordField.sendKeys("thsangtran@"); // Mật khẩu không hợp lệ
            // Nhấn nút "Lưu thay đổi" để cập nhật mật khẩu
            WebElement saveButton = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//button[@type='submit' and text()='Lưu thay đổi']")));
            saveButton.click();
            // Kiểm tra nếu thông báo "Mật khẩu không đúng định dạng" xuất hiện
            try {
                WebElement errorMessage = wait.until(ExpectedConditions.presenceOfElementLocated(
                        By.xpath("//*[contains(text(),'Mật khẩu không đúng định dạng')]")));
                Assert.assertTrue(errorMessage.isDisplayed(), "Không tìm thấy thông báo lỗi về mật khẩu!");
                System.out.println("Kiểm tra thay đổi mật khẩu không hợp lệ: PASS");
            } catch (Exception e) {
                Assert.fail("Không tìm thấy thông báo lỗi định dạng mật khẩu: " + e.getMessage());
            }
        } catch (Exception e) {
            Assert.fail("Có lỗi xảy ra khi thực hiện kiểm tra thay đổi mật khẩu: " + e.getMessage());
        }
    }
    @Test
    // Thay đổi MK không đúng yêu cầu: MK toàn chữ
    public void testUpdatePassword4() {
        try {
            // Mở trang chủ Tiki
            driver.get("https://tiki.vn");
            // Đợi và tắt popup nếu có
            closePopupIfExists();
            // Đăng nhập với thông tin hợp lệ
            loginWithValidCredentials();
            // Đợi và tắt popup nếu có lần nữa
            closePopupIfExists();
            // Chờ 6 giây để giao diện tải đầy đủ (nếu cần thiết)
            Thread.sleep(6000);
            // Chờ và di chuyển chuột đến mục Tài khoản
            WebElement accountButton = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//span[contains(text(),'Tài khoản')]")));
            Actions actions = new Actions(driver);
            actions.moveToElement(accountButton).perform();
            // Nhấn vào "Thông tin tài khoản"
            WebElement accountInfoLink = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//a[@href='/customer/account/edit?src=header_my_account']/p[contains(text(),'Thông tin tài khoản')]")));
            accountInfoLink.click();
            //Nhấn chọn vào button "Cập nhật"
            WebElement updateButton = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//span[contains(text(),'Đổi mật khẩu')]/ancestor::div[@class='list-item']//button[contains(@class,'button active')]")));
            // Cuộn đến phần tử
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", updateButton);
            Thread.sleep(500); // Give time for the scroll
            updateButton.click();
            // Điền vào trường mật khẩu hiện tại
            WebElement nowPasswordField = wait.until(ExpectedConditions.presenceOfElementLocated(
                    By.xpath("//input[@name='oldPassword' and @placeholder='Nhập mật khẩu hiện tại']")));
            nowPasswordField.sendKeys("sangtran301003");
            // Điền vào trường "Mật khẩu mới"
            WebElement newPasswordField = wait.until(ExpectedConditions.presenceOfElementLocated(
                    By.xpath("//input[@name='password' and @placeholder='Nhập mật khẩu mới']")));
            newPasswordField.sendKeys("thsangtran"); // Mật khẩu không hợp lệ
            // Điền vào trường "Nhập lại mật khẩu mới"
            WebElement confirmPasswordField = wait.until(ExpectedConditions.presenceOfElementLocated(
                    By.xpath("//input[@name='confirmPassword' and @placeholder='Nhập lại mật khẩu mới']")));
            confirmPasswordField.sendKeys("thsangtran"); // Mật khẩu không hợp lệ
            // Nhấn nút "Lưu thay đổi" để cập nhật mật khẩu
            WebElement saveButton = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//button[@type='submit' and text()='Lưu thay đổi']")));
            saveButton.click();
            // Kiểm tra nếu thông báo "Mật khẩu không đúng định dạng" xuất hiện
            try {
                WebElement errorMessage = wait.until(ExpectedConditions.presenceOfElementLocated(
                        By.xpath("//*[contains(text(),'Mật khẩu không đúng định dạng')]")));
                Assert.assertTrue(errorMessage.isDisplayed(), "Không tìm thấy thông báo lỗi về mật khẩu!");
                System.out.println("Kiểm tra thay đổi mật khẩu không hợp lệ: PASS");
            } catch (Exception e) {
                Assert.fail("Không tìm thấy thông báo lỗi định dạng mật khẩu: " + e.getMessage());
            }
        } catch (Exception e) {
            Assert.fail("Có lỗi xảy ra khi thực hiện kiểm tra thay đổi mật khẩu: " + e.getMessage());
        }
    }
    @Test
    // Thay đổi MK không đúng yêu cầu: MK toàn số
    public void testUpdatePassword5() {
        try {
            // Mở trang chủ Tiki
            driver.get("https://tiki.vn");
            // Đợi và tắt popup nếu có
            closePopupIfExists();
            // Đăng nhập với thông tin hợp lệ
            loginWithValidCredentials();
            // Đợi và tắt popup nếu có lần nữa
            closePopupIfExists();
            // Chờ 6 giây để giao diện tải đầy đủ (nếu cần thiết)
            Thread.sleep(6000);
            // Chờ và di chuyển chuột đến mục Tài khoản
            WebElement accountButton = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//span[contains(text(),'Tài khoản')]")));
            Actions actions = new Actions(driver);
            actions.moveToElement(accountButton).perform();
            // Nhấn vào "Thông tin tài khoản"
            WebElement accountInfoLink = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//a[@href='/customer/account/edit?src=header_my_account']/p[contains(text(),'Thông tin tài khoản')]")));
            accountInfoLink.click();
            //Nhấn chọn vào button "Cập nhật"
            WebElement updateButton = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//span[contains(text(),'Đổi mật khẩu')]/ancestor::div[@class='list-item']//button[contains(@class,'button active')]")));
            // Cuộn đến phần tử
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", updateButton);
            Thread.sleep(500); // Give time for the scroll
            updateButton.click();
            // Điền vào trường mật khẩu hiện tại
            WebElement nowPasswordField = wait.until(ExpectedConditions.presenceOfElementLocated(
                    By.xpath("//input[@name='oldPassword' and @placeholder='Nhập mật khẩu hiện tại']")));
            nowPasswordField.sendKeys("sangtran301003");
            // Điền vào trường "Mật khẩu mới"
            WebElement newPasswordField = wait.until(ExpectedConditions.presenceOfElementLocated(
                    By.xpath("//input[@name='password' and @placeholder='Nhập mật khẩu mới']")));
            newPasswordField.sendKeys("2003301003"); // Mật khẩu không hợp lệ
            // Điền vào trường "Nhập lại mật khẩu mới"
            WebElement confirmPasswordField = wait.until(ExpectedConditions.presenceOfElementLocated(
                    By.xpath("//input[@name='confirmPassword' and @placeholder='Nhập lại mật khẩu mới']")));
            confirmPasswordField.sendKeys("2003301003"); // Mật khẩu không hợp lệ
            // Nhấn nút "Lưu thay đổi" để cập nhật mật khẩu
            WebElement saveButton = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//button[@type='submit' and text()='Lưu thay đổi']")));
            saveButton.click();
            // Kiểm tra nếu thông báo "Mật khẩu không đúng định dạng" xuất hiện
            try {
                WebElement errorMessage = wait.until(ExpectedConditions.presenceOfElementLocated(
                        By.xpath("//*[contains(text(),'Mật khẩu không đúng định dạng')]")));
                Assert.assertTrue(errorMessage.isDisplayed(), "Không tìm thấy thông báo lỗi về mật khẩu!");
                System.out.println("Kiểm tra thay đổi mật khẩu không hợp lệ: PASS");
            } catch (Exception e) {
                Assert.fail("Không tìm thấy thông báo lỗi định dạng mật khẩu: " + e.getMessage());
            }
        } catch (Exception e) {
            Assert.fail("Có lỗi xảy ra khi thực hiện kiểm tra thay đổi mật khẩu: " + e.getMessage());
        }
    }
    @Test
    // MK mới khác Nhập lại MK mới
    public void testUpdatePassword6() {
        try {
            // Mở trang chủ Tiki
            driver.get("https://tiki.vn");
            // Đợi và tắt popup nếu có
            closePopupIfExists();
            // Đăng nhập với thông tin hợp lệ
            loginWithValidCredentials();
            // Đợi và tắt popup nếu có lần nữa
            closePopupIfExists();
            // Chờ 6 giây để giao diện tải đầy đủ (nếu cần thiết)
            Thread.sleep(6000);
            // Chờ và di chuyển chuột đến mục Tài khoản
            WebElement accountButton = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//span[contains(text(),'Tài khoản')]")));
            Actions actions = new Actions(driver);
            actions.moveToElement(accountButton).perform();
            // Nhấn vào "Thông tin tài khoản"
            WebElement accountInfoLink = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//a[@href='/customer/account/edit?src=header_my_account']/p[contains(text(),'Thông tin tài khoản')]")));
            accountInfoLink.click();
            // Nhấn chọn vào button "Cập nhật"
            WebElement updateButton = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//span[contains(text(),'Đổi mật khẩu')]/ancestor::div[@class='list-item']//button[contains(@class,'button active')]")));
            // Cuộn đến phần tử
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", updateButton);
            Thread.sleep(500); // Give time for the scroll
            updateButton.click();
            // Điền vào trường mật khẩu hiện tại
            WebElement nowPasswordField = wait.until(ExpectedConditions.presenceOfElementLocated(
                    By.xpath("//input[@name='oldPassword' and @placeholder='Nhập mật khẩu hiện tại']")));
            nowPasswordField.sendKeys("sangtran301003");
            // Điền vào trường "Mật khẩu mới"
            WebElement newPasswordField = wait.until(ExpectedConditions.presenceOfElementLocated(
                    By.xpath("//input[@name='password' and @placeholder='Nhập mật khẩu mới']")));
            newPasswordField.sendKeys("thsangtran30"); // Mật khẩu không hợp lệ
            // Điền vào trường "Nhập lại mật khẩu mới"
            WebElement confirmPasswordField = wait.until(ExpectedConditions.presenceOfElementLocated(
                    By.xpath("//input[@name='confirmPassword' and @placeholder='Nhập lại mật khẩu mới']")));
            confirmPasswordField.sendKeys("thsangtran3010"); // Mật khẩu không hợp lệ
            // Nhấn nút "Lưu thay đổi" để cập nhật mật khẩu
            WebElement saveButton = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//button[@type='submit' and text()='Lưu thay đổi']")));
            saveButton.click();
            // Kiểm tra nếu thông báo "Mật khẩu không đúng định dạng" xuất hiện
            try {
                WebElement errorMessage = wait.until(ExpectedConditions.presenceOfElementLocated(
                        By.xpath("//*[contains(text(),'Mật khẩu chưa trùng khớp')]")));
                Assert.assertTrue(errorMessage.isDisplayed(), "Không tìm thấy thông báo lỗi về mật khẩu!");
                System.out.println("Kiểm tra thay đổi mật khẩu không hợp lệ: PASS");
            } catch (Exception e) {
                Assert.fail("Không tìm thấy thông báo lỗi định dạng mật khẩu: " + e.getMessage());
            }
        } catch (Exception e) {
            Assert.fail("Có lỗi xảy ra khi thực hiện kiểm tra thay đổi mật khẩu: " + e.getMessage());
        }
    }
    @Test
    // Đăng xuất
    public void testLogout() {
        try {
            // Mở trang chủ Tiki
            driver.get("https://tiki.vn");
            // Đợi và tắt popup nếu có
            closePopupIfExists();
            // Đăng nhập với thông tin hợp lệ
            loginWithValidCredentials();
            // Đóng popup quảng cáo nếu có
            closePopupIfExists();
            Thread.sleep(6000);
            // Rơ chuột vào mục Tài khoản và click Đăng xuất
            WebElement accountMenu = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//span[contains(text(),'Tài khoản')]")));
            Actions actions = new Actions(driver);
            actions.moveToElement(accountMenu).perform();
            // Nhấn chọn vào button "Đăng xuất"
            WebElement logoutButton = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//a[@data-view-id='header_header_account_item']//p[@title='Đăng xuất']")));
            logoutButton.click();
            // Kiểm tra kết quả đăng xuất
            try {
                WebElement logoutMessage = wait.until(ExpectedConditions.presenceOfElementLocated(
                        By.xpath("//*[contains(text(),'Đăng xuất thành công')]")));
                // So sánh giá trị văn bản của thông báo với giá trị mong đợi
                Assert.assertEquals(logoutMessage.getText().trim(), "Đăng xuất thành công", "Thông báo đăng xuất không chính xác!");
                System.out.println("Test đăng xuất: PASS");
            } catch (Exception e) {
                Assert.fail("Thông báo đăng xuất không xuất hiện: " + e.getMessage());
            }
        } catch (Exception e) {
            Assert.fail("Có lỗi xảy ra khi thực hiện kiểm tra đăng xuất: " + e.getMessage());
        }
    }
    // Hàm hỗ trợ đăng nhập với thông tin hợp lệ
    private void loginWithValidCredentials() {
        try {
            // Chờ và click vào mục "Tài khoản"
            WebElement accountButton = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//span[contains(text(),'Tài khoản')]")));
            accountButton.click();

            // Nhập số điện thoại
            WebElement phoneInput = wait.until(ExpectedConditions.presenceOfElementLocated(
                    By.xpath("//input[@name='tel']")));
            phoneInput.sendKeys("0362458928");

            // Nhấn Tiếp tục
            WebElement continueButton = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//button[contains(text(),'Tiếp Tục')]")));
            continueButton.click();

            // Nhập mật khẩu
            WebElement passwordInput = wait.until(ExpectedConditions.presenceOfElementLocated(
                    By.xpath("//input[@type='password']")));
            passwordInput.sendKeys("sangtran301003");

            // Nhấn Đăng nhập
            WebElement loginButton = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//button[contains(text(),'Đăng Nhập')]")));
            loginButton.click();
        } catch (Exception e) {
            Assert.fail("Không thể đăng nhập với thông tin hợp lệ: " + e.getMessage());
        }
    }
    // Hàm hỗ trợ đăng xuất
    private void logout() {
        try {
            // Chờ và di chuyển chuột đến mục Tài khoản
            WebElement accountButton = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//span[contains(text(),'Tài khoản')]")));
            Actions actions = new Actions(driver);
            actions.moveToElement(accountButton).perform();

            // Nhấn vào "Đăng xuất"
            WebElement logoutButton = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//a[contains(text(),'Đăng xuất')]")));
            logoutButton.click();
        } catch (Exception e) {
            Assert.fail("Không thể đăng xuất: " + e.getMessage());
        }
    }
    //Tắt quảng cáo nếu có
    private void closePopupIfExists() {
        try {
            // Tìm và nhấn nút đóng popup (nếu có)
            WebElement closePopupButton = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//img[@alt='close-icon']"))); // Cập nhật XPath theo popup của trang
            closePopupButton.click();
            System.out.println("Popup đã được đóng.");
        } catch (Exception e) {
            // Không tìm thấy popup, không làm gì
            System.out.println("Không tìm thấy popup, tiếp tục thực thi.");
        }
    }
    @AfterMethod
    public void tearDown() {
        // Đóng trình duyệt
        if (driver != null) {
            driver.quit();
        }
    }
}
