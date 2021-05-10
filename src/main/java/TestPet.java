public class TestPet {

//    	@BeforeSuite
//	void setUp() {
//		System.out.println("Settings up...");
//	}

//	@BeforeSuite
//	void setUp2() {
//		System.out.println("Settings-2 up...");
//	}

//	@AfterSuite
//	void tearDown() {
//		System.out.println("Finalizing test class...");
//	}

//	@AfterSuite
//	void tearDown2() {
//		System.out.println("Finalizing-2 test class...");
//	}

    @Test
    void shouldTestSomethingDefault() {
        System.out.println("Method-default with test annotation...");
    }


    @Test(order = 1)
    void shouldTestSomethingOne() {
        System.out.println("Method-1 with test annotation...");
    }

    @Test(order = 3)
    void shouldTestSomethingThree() {
        System.out.println("Method-3 with test annotation...");
    }

    @Test(order = 2)
    void shouldTestSomethingTwo() {
        System.out.println("Method-2 with test annotation...");
    }

    void shouldTestSomething2() {
        System.out.println("Method without test annotation...");
    }

}
