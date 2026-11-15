import java.util.ArrayList;
import java.util.List;

// Задача 2: Observer (Вариант 8: Пригородные перевозки - изменение расписания)
public class ObserverAfter {

    private static int failedChecks = 0;

    public static void main(String[] args) {

        SuburbanStation station = new SuburbanStation("Минск-Пассажирский");

        // Создаем наблюдателей
        DisplayBoard board = new DisplayBoard();
        Journal journal = new Journal();
        Broadcast broadcast = new Broadcast();

        // Подписываем
        station.addListener(board);
        station.addListener(journal);
        station.addListener(broadcast);

        System.out.println("--- Событие 1: Изменение расписания (подписаны 3 наблюдателя) ---");
        station.scheduleChanged("Электричка 6001", "12:30");
        check("Журнал получил ровно одну запись", journal.getRecords().size() == 1);
        check("Все подписчики получили одно и то же событие", 
                journal.getRecords().get(0).contains("6001") && board.getLastMessage().contains("6001"));

        System.out.println("\n--- Событие 2: Отписываем табло ---");
        station.removeListener(board);
        station.scheduleChanged("Электричка 6002", "12:45");
        
        check("После отписки табло не получило новое событие", 
                !board.getLastMessage().contains("6002"));
        check("Журнал продолжает получать записи (их стало 2)", 
                journal.getRecords().size() == 2);

        System.out.println("\n--- Событие 3: Добавление 4-го наблюдателя без правки класса SuburbanStation ---");
        MobileApp mobileApp = new MobileApp();
        station.addListener(mobileApp);
        station.scheduleChanged("Электричка 6003", "13:00");
        
        check("Новый наблюдатель MobileApp получил событие", 
                mobileApp.getLastMessage().contains("6003"));
        check("Журнал накопил 3 записи", journal.getRecords().size() == 3);

        if (failedChecks > 0) {
            throw new AssertionError("не пройдено проверок: " + failedChecks);
        }
        System.out.println("\nВсе проверки пройдены.");
    }

    static void check(String checkName, boolean passed) {
        System.out.println((passed ? "[OK]   " : "[FAIL] ") + checkName);
        if (!passed) {
            failedChecks++;
        }
    }
}

// --- участники паттерна Observer ---

/** Наблюдатель */
interface ScheduleListener {
    void onScheduleChanged(String route, String newTime);
}

/** Субъект (Станция) */
class SuburbanStation {
    private final String name;
    private final List<ScheduleListener> listeners = new ArrayList<>();

    SuburbanStation(String name) {
        this.name = name;
    }

    void addListener(ScheduleListener listener) {
        listeners.add(listener);
    }

    void removeListener(ScheduleListener listener) {
        listeners.remove(listener);
    }

    void scheduleChanged(String route, String newTime) {
        System.out.println("[Станция " + name + "] Внимание! Изменено расписание: " + route + " -> " + newTime);
        for (ScheduleListener listener : listeners) {
            listener.onScheduleChanged(route, newTime);
        }
    }
}

// Конкретные наблюдатели
class DisplayBoard implements ScheduleListener {
    private String lastMessage = "";
    
    public void onScheduleChanged(String route, String newTime) {
        lastMessage = "Рейс " + route + " отправляется в " + newTime;
        System.out.println("  [Табло]     " + lastMessage);
    }
    String getLastMessage() { return lastMessage; }
}

class Journal implements ScheduleListener {
    private final List<String> records = new ArrayList<>();
    
    public void onScheduleChanged(String route, String newTime) {
        String record = "Зафиксировано изменение: " + route + " в " + newTime;
        records.add(record);
        System.out.println("  [Журнал]    " + record);
    }
    List<String> getRecords() { return records; }
}

class Broadcast implements ScheduleListener {
    public void onScheduleChanged(String route, String newTime) {
        System.out.println("  [Рассылка]  SMS/Email: Поезд " + route + " теперь в " + newTime);
    }
}

// 4-й наблюдатель
class MobileApp implements ScheduleListener {
    private String lastMessage = "";
    public void onScheduleChanged(String route, String newTime) {
        lastMessage = "Push: " + route + " в " + newTime;
        System.out.println("  [Моб. App]  " + lastMessage);
    }
    String getLastMessage() { return lastMessage; }
}
