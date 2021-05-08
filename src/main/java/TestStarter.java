
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class TestStarter {
    private TestStarter() {}

    public static void start(Class aClass) {
        Object obj = initObject(aClass);
        List<Method> testMethods = findMethods(aClass, Test.class);

        Collections.sort(testMethods, new Comparator<Method>() {
            @Override
            public int compare(Method o1, Method o2) {
                Test an1 = o1.getAnnotation(Test.class);
                Test an2 = o2.getAnnotation(Test.class);
                return an1.order() - an2.order();
            }
        });

        if (testMethods.isEmpty()) {
            System.out.println(String.format("% has no any test methods", aClass.getName()));
            return;
        }

        List<Method> beforeSuiteMethods = findMethods(aClass, BeforeSuite.class);
        if (!beforeSuiteMethods.isEmpty() && beforeSuiteMethods.size() > 1) {
            throw new RuntimeException("BeforeSuite annotation must be only one");
        }

        List<Method> afterSuiteMethods = findMethods(aClass, AfterSuite.class);
        if (!afterSuiteMethods.isEmpty() && afterSuiteMethods.size() > 1) {
            throw new RuntimeException("AfterSuite annotation must be only one");
        }

        if (beforeSuiteMethods.size() == 1) {
            executeMethod(beforeSuiteMethods.get(0), obj);
        }

        for (Method testMethod : testMethods) {
            executeMethod(testMethod, obj);
        }

        if (afterSuiteMethods.size() == 1) {
            executeMethod(afterSuiteMethods.get(0), obj);
        }
    }

    private static void executeMethod(Method testMethod, Object obj, Object... args) {
        try {
            testMethod.setAccessible(true);
            testMethod.invoke(obj, args);
            testMethod.setAccessible(false);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    private static List<Method> findMethods(Class aClass, Class<? extends Annotation> annotationClass) {
        List<Method> testMethods = new ArrayList<>();
        for (Method method : aClass.getDeclaredMethods()) {
            if (method.isAnnotationPresent(annotationClass)) {
                testMethods.add(method);
            }
        }
        return testMethods;
    }

    public static void start(String className) {
        try {
            start(Class.forName(className));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }



    private static Object initObject(Class aClass) {
        try {
            return aClass.getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException("RE", e);
        }
    }
}