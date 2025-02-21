package at.ac.fhcampuswien.fhmdb;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

class HomeControllerTest {
    private HomeController homeController;

    @BeforeEach
    void setUp() {
        homeController = new HomeController();
    }

    @AfterEach
    void tearDown() {
        homeController = null;
    }
}