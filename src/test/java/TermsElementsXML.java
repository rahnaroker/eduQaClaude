/**
 * XML ПРЕДСТАВЛЕНИЕ ДЛЯ "Terms and Conditions":
 *
 * <!-- Сценарий проблемы: Inspector показывает конкретный элемент -->
 *
 * <!-- Элемент #1: Основной заголовок (тот, который нужен) -->
 * <XCUIElementTypeStaticText type="XCUIElementTypeStaticText" 
 *      name="Terms and Conditions" 
 *      value="Terms and Conditions" 
 *      label="Terms and Conditions" 
 *      enabled="true" 
 *      visible="true" 
 *      accessible="true" 
 *      x="100" y="150" 
 *      width="375" height="30" 
 *      index="0" />
 *
 * <!-- Элемент #2: Дубликат в скрытом контейнере -->
 * <XCUIElementTypeStaticText type="XCUIElementTypeStaticText" 
 *      name="Terms and Conditions" 
 *      value="Terms and Conditions" 
 *      label="Terms and Conditions" 
 *      enabled="false" 
 *      visible="false" 
 *      accessible="false" 
 *      x="0" y="-50" 
 *      width="375" height="30" 
 *      index="0" />

 */


/**
 * ЗАДАНИЯ ДЛЯ КАНДИДАТА:
 *
 * 1. На основе XML-представления
 * напишите полный надежный метод для
 * получения правильного текста "Terms and Conditions":
 *
 */
public class TermsElementsXML {

    static void main() {
//        String a = "java";              // пул
//        String b = "java";              // пул (тот же объект, что a)
//        String c = new String("java");
//        c = "java".intern();
//
//        System.out.println(a == b);
//        System.out.println(a == c);

        System.out.println(reverseString("boy"));

        System.out.println(facto(4));
    }

    static String reverseString(String s) {
        if (s == null) return null;
        if (s.length() < 2) return s;

        char[] chars = s.toCharArray();
        int left = 0;
        int right = s.length() - 1;

        while (left < right) {
            char temp = chars[left];
            chars[left] = chars[right];
            chars[right] = temp;
            left++;
            right--;
        }

        return new String(chars);
    }

    public static int facto(int i) {
        if (i <= 1) return i;
        return i * facto(i - 1);
    }

}

