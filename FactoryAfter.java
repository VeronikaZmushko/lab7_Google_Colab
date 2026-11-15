// Задача 1: Factory Method (Вариант 8: Пригородные перевозки - абонементы)
public class FactoryAfter {

    public static void main(String[] args) {

        // Вызывающий код (main) работает только с интерфейсами PassCreator и Pass
        // Никаких if/switch по типу абонемента здесь нет
        PassCreator[] creators = {
                new WeekendPassCreator(),
                new MonthlyPassCreator(),
                new StudentPassCreator(),
                new SchoolPassCreator() // 4-й тип добавлен новым классом
        };

        System.out.println("--- Информация об абонементах ---");
        for (PassCreator creator : creators) {
            Pass pass = creator.createPass();
            System.out.printf("%-25s | Цена: %6.2f руб.%n", pass.description(), pass.price());
        }

        System.out.println();
        System.out.println("--- Проверки ---");

        // 1. Описание не пустое и содержит цену (аналог грузоподъемности)
        check("Описание абонемента не пустое", !new WeekendPassCreator().createPass().description().isEmpty());
        check("Цена абонемента больше нуля", new WeekendPassCreator().createPass().price() > 0);
        
        // 2. Тип созданного объекта соответствует ожидаемому создателю
        check("WeekendPassCreator создает WeekendPass", 
                new WeekendPassCreator().createPass() instanceof WeekendPass);
        check("MonthlyPassCreator создает MonthlyPass", 
                new MonthlyPassCreator().createPass() instanceof MonthlyPass);
        check("StudentPassCreator создает StudentPass", 
                new StudentPassCreator().createPass() instanceof StudentPass);

        // 3. Четвертый тип работает наравне с остальными
        Pass schoolPass = new SchoolPassCreator().createPass();
        check("Четвертый тип (SchoolPass) успешно создан", schoolPass instanceof SchoolPass);
        check("Четвертый тип имеет описание", !schoolPass.description().isEmpty());

        System.out.println();
        System.out.println("Все проверки пройдены.");
    }

    static void check(String checkName, boolean passed) {
        System.out.println((passed ? "[OK]   " : "[FAIL] ") + checkName);
        if (!passed) {
            throw new AssertionError("Не пройдена проверка: " + checkName);
        }
    }
}

// --- участники паттерна Factory Method ---

/** Общий тип продукта: абонемент */
interface Pass {
    String description();
    double price();
}

/** Создатель с фабричным методом */
interface PassCreator {
    Pass createPass();
}

// Не менее трёх конкретных продуктов
class WeekendPass implements Pass {
    public String description() { return "Абонемент выходного дня"; }
    public double price() { return 500.0; }
}

class MonthlyPass implements Pass {
    public String description() { return "Единый месячный абонемент"; }
    public double price() { return 2500.0; }
}

class StudentPass implements Pass {
    public String description() { return "Студенческий абонемент"; }
    public double price() { return 1200.0; }
}

// Четвертый тип (добавлен новым классом)
class SchoolPass implements Pass {
    public String description() { return "Школьный абонемент"; }
    public double price() { return 800.0; }
}

// Конкретные создатели для каждого типа
class WeekendPassCreator implements PassCreator {
    public Pass createPass() { return new WeekendPass(); }
}

class MonthlyPassCreator implements PassCreator {
    public Pass createPass() { return new MonthlyPass(); }
}

class StudentPassCreator implements PassCreator {
    public Pass createPass() { return new StudentPass(); }
}

class SchoolPassCreator implements PassCreator {
    public Pass createPass() { return new SchoolPass(); }
}
